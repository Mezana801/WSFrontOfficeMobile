package vae.vae.service;

import vae.vae.db.ConnectionPostgresSQL;
import vae.vae.model.CompteUtilisateurs;
import vae.vae.model.Enchere;
import vae.vae.model.MiseEnchere;
import vae.vae.model.Utilisateurs;
import vae.vae.repository.MiseEnchereRepository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

public class MiseEnchereService  {

    public boolean isAccountBlocked(int utilisateursID, MiseEnchereRepository miseEnchereRepository, double montant) throws Exception{
        Connection conn = ConnectionPostgresSQL.getconnect();
        Enchere[] encheres = new Enchere().findAll(conn);

        double sumDeductible = 0.0;
        for( Enchere enchere: encheres ){
            MiseEnchere maxMise = miseEnchereRepository.getmontantMax(enchere.getId());
            if( maxMise != null ){
                if( maxMise.getUtilisateursID() == utilisateursID ){
                    sumDeductible += maxMise.getMontantMax();
                }
            }
        }
        sumDeductible += montant;
        CompteUtilisateurs compteUtilisateurs = new CompteUtilisateurs();
        compteUtilisateurs.setUtilisateursid(utilisateursID);

        compteUtilisateurs = compteUtilisateurs.find(conn);
        conn.close();

        if( compteUtilisateurs.getSolde() < sumDeductible ){
            return true;
        }
        return false;
    }

    public boolean isInsufficientSolde(int utilisateursID, double mise) throws Exception{
        Connection conn = ConnectionPostgresSQL.getconnect();

        CompteUtilisateurs compteUtilisateurs = new CompteUtilisateurs();
        compteUtilisateurs.setUtilisateursid(utilisateursID);
        compteUtilisateurs = compteUtilisateurs.find(conn);
        if( compteUtilisateurs.getSolde() < mise ){
            return true;
        }
        return false;
    }

    public Timestamp addTimeToTimestamp(Timestamp debut, Time dureeEnchere){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(debut.getTime());

        calendar.add(Calendar.HOUR, dureeEnchere.getHours());
        calendar.add(Calendar.MINUTE, dureeEnchere.getMinutes());
        return new Timestamp(calendar.getTime().getTime());
    }

    public boolean isSelf(Enchere enchere, MiseEnchere miseEnchere){
        if( enchere.getUtilisateursid() == miseEnchere.getUtilisateursID() ){
            return true;
        }
        return false;
    }

    public void newMiseEnchere(Connection conn, Enchere enchere, MiseEnchere miseEnchere, MiseEnchereRepository miseEnchereRepository) throws Exception {
        MiseEnchere maxMiseEnchere = miseEnchereRepository.getmontantMax(miseEnchere.getEnchereID());
        if( maxMiseEnchere == null ){
            maxMiseEnchere = new MiseEnchere();
        }

        // check si c'est lui même
        if( isSelf(enchere, miseEnchere) ){
            throw new Exception("Erreur! Vous ne pouvez vous enchérir vous même");
        }

        // check date now et date enchère si correspondant
        Date now = new Date(System.currentTimeMillis());
        Timestamp finish = addTimeToTimestamp(enchere.getDateetheure(), enchere.getDureeenchere());

        if( now.after(finish) ){
            throw new Exception("Erreur! La date et heure de l'enchère est déjà expiré");
        } else if( now.before(enchere.getDateetheure()) ){
            throw new Exception("Erreur! L'enchère n'est pas encore à miser. Veuillez patienter " +
                    "jusqu'à la date et heure de celle ci");
        }
        if( isInsufficientSolde(miseEnchere.getUtilisateursID(), miseEnchere.getMontantMax()) ){
            throw new Exception("Solde insuffisant! Veuillez recharger votre compte");
        }

        // check if the bid price is lower or equals than the last max bid
        if( miseEnchere.getMontantMax() <= maxMiseEnchere.getMontantMax() || miseEnchere.getMontantMax() < enchere.getPrixdemise() ){
            throw new Exception("Montant invalide!");
        }
        // check if user account is blocked
        if( isAccountBlocked(miseEnchere.getUtilisateursID(), miseEnchereRepository, miseEnchere.getMontantMax()) ){
            throw new Exception("Erreur! Votre solde est déjà réservé à d'autres mises que vous avez effectué(s)");
        }
        Utilisateurs current_mise = new Utilisateurs(miseEnchere.getUtilisateursID());
        current_mise = (Utilisateurs)current_mise.find(conn);
        miseEnchere.setEnchere(enchere);  miseEnchere.setUtilisateurs(current_mise);
        miseEnchereRepository.save(miseEnchere);
    }
}
