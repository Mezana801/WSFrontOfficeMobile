package vae.vae.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Date;

@Document(collection = "DemandeRechargement")
@Getter
@Setter
public class DemandeRechargement {
    int id;
    int CompteUtilisateursid;
    CompteUtilisateurs compteUtilisateurs;
    double montant;
    Date dateDemande;
    int statut;

    public DemandeRechargement() {
    }

    public DemandeRechargement(int id, int compteUtilisateursid, CompteUtilisateurs compteUtilisateurs, double montant, Date dateDemande, int statut) {
        this.id = id;
        CompteUtilisateursid = compteUtilisateursid;
        this.compteUtilisateurs = compteUtilisateurs;
        this.montant = montant;
        this.dateDemande = dateDemande;
        this.statut = statut;
    }

    public void setMontant(double montant) throws Exception {
        if( montant < 0 ){
            throw new Exception("Solde montant invalide");
        }
        this.montant = montant;
    }

}
