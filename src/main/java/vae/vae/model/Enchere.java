package vae.vae.model;

import vae.vae.annotation.FieldDisable;
import vae.vae.general.ObjetBDD;
import lombok.Getter;
import lombok.Setter;
import vae.vae.repository.EnchereTerminerRepository;
import vae.vae.repository.MiseEnchereRepository;
import vae.vae.service.MiseEnchereService;

import javax.persistence.*;
import java.sql.*;
import java.util.ArrayList;

@Entity
@Getter
@Setter
@Table(name = "Enchere")
public class Enchere extends ObjetBDD {
    @Id
   // @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    Timestamp dateetheure;
    String description;
    double prixdemise;
    Time dureeenchere;
    int utilisateursid;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "utilisateursID",referencedColumnName = "id",insertable = false, updatable = false)
    @FieldDisable
    Utilisateurs utilisateurs;
    int categorieid;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "categorieid",referencedColumnName = "id",insertable = false, updatable = false)
    @FieldDisable
    Categorie categorie;

    @FieldDisable
    @Transient
    int status;
    @FieldDisable
    @Transient
    String designationstatus;
    @FieldDisable
    @Transient
    String coverphoto;
    @FieldDisable
    @Transient
    String[] photos;
    @FieldDisable
    @Transient
    double prixmin;
    @FieldDisable
    @Transient
    double prixmax;
    @FieldDisable
    @Transient
    Enchereterminer maxMise;

    public Enchere(int id, Timestamp dateEtHeure, String description, double prixDeMise, Time dureeEnchere, int utilisateursID, int categorieID) {
        this.id = id;
        this.dateetheure = dateEtHeure;
        this.description = description;
        this.prixdemise = prixDeMise;
        this.dureeenchere = dureeEnchere;
        this.utilisateursid = utilisateursID;
        this.categorieid = categorieID;
    }

    public Enchere(int id) {
        this.id = id;
    }

    public Enchere() { }

    @Override
    public Enchere[] findAll(Connection conn) throws Exception{
        ArrayList<Enchere> array = new ArrayList<>();
        String sql = "select * from view_enchere_status order by id desc";
        System.out.println(sql);

        Statement stat = null;
        ResultSet res = null;

        try{
            stat = conn.createStatement();
            res = stat.executeQuery(sql);

            while( res.next() ){
                Enchere e = new Enchere(res.getInt("id"), res.getTimestamp("dateetheure"), res.getString("description"), res.getDouble("prixDeMise"), res.getTime("dureeEnchere"), res.getInt("utilisateursID"), res.getInt("categorieID"));
                e.setStatus(res.getInt("status"));   e.setDesignationstatus(res.getString("designationstatus"));
                Utilisateurs utilisateurs = (Utilisateurs) new Utilisateurs(e.getUtilisateursid()).find(conn);
                Categorie categorie = (Categorie) new Categorie(e.getCategorieid()).find(conn);
                e.setUtilisateurs(utilisateurs);
                e.setCategorie(categorie);
                e.setCoverphoto(PhotoEnchere.getcoverPhoto(e.getId(), conn));
                array.add(e);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            if (res !=null) res.close();
            if (stat !=null) stat.close();
        }
        Enchere[] result = new Enchere[array.size()];
        array.toArray(result);
        return result;
    }

    public Enchere[] findHomeEnchere(Connection conn) throws Exception{
        ArrayList<Enchere> array = new ArrayList<>();
        String sql = "select * from view_enchere_status where status != 1 order by id desc limit 12";
        System.out.println(sql);

        Statement stat = null;
        ResultSet res = null;

        try{
            stat = conn.createStatement();
            res = stat.executeQuery(sql);

            while( res.next() ){
                Enchere e = new Enchere(res.getInt("id"), res.getTimestamp("dateetheure"), res.getString("description"), res.getDouble("prixDeMise"), res.getTime("dureeEnchere"), res.getInt("utilisateursID"), res.getInt("categorieID"));
                e.setStatus(res.getInt("status"));   e.setDesignationstatus(res.getString("designationstatus"));
                Utilisateurs utilisateurs = (Utilisateurs) new Utilisateurs(e.getUtilisateursid()).find(conn);
                Categorie categorie = (Categorie) new Categorie(e.getCategorieid()).find(conn);
                e.setUtilisateurs(utilisateurs);
                e.setCategorie(categorie);
                e.setCoverphoto(PhotoEnchere.getcoverPhoto(e.getId(), conn));
                array.add(e);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            if (res !=null) res.close();
            if (stat !=null) stat.close();
        }
        Enchere[] result = new Enchere[array.size()];
        array.toArray(result);
        return result;
    }

    public Enchere[] findByUSer(Connection conn) throws Exception{
        ArrayList<Enchere> array = new ArrayList<>();
        String sql = "select * from view_enchere_status where utilisateursid = "+this.getUtilisateursid()+" order by dateEtHeure desc";
        System.out.println(sql);

        Statement stat = null;
        ResultSet res = null;

        try{
            stat = conn.createStatement();
            res = stat.executeQuery(sql);

            while( res.next() ){
                Enchere e = new Enchere(res.getInt("id"), res.getTimestamp("dateetheure"), res.getString("description"), res.getDouble("prixDeMise"), res.getTime("dureeEnchere"), res.getInt("utilisateursID"), res.getInt("categorieID"));
                e.setStatus(res.getInt("status"));   e.setDesignationstatus(res.getString("designationstatus"));
                Utilisateurs utilisateurs = (Utilisateurs) new Utilisateurs(e.getUtilisateursid()).find(conn);
                Categorie categorie = (Categorie) new Categorie(e.getCategorieid()).find(conn);
                e.setUtilisateurs(utilisateurs);
                e.setCategorie(categorie);
                e.setCoverphoto(PhotoEnchere.getcoverPhoto(e.getId(), conn));

                Enchereterminer enchereterminer = new Enchereterminer(e.getId());
                enchereterminer = enchereterminer.find(conn);
                e.setMaxMise(enchereterminer);
                array.add(e);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            if (res !=null) res.close();
            if (stat !=null) stat.close();
        }
        Enchere[] result = new Enchere[array.size()];
        array.toArray(result);
        return result;
    }

    @Override
    public Enchere find(Connection conn) throws Exception{
        Enchere result = null;
        String sql = "select * from view_enchere_status where id = "+this.getId();
        System.out.println(sql);

        Statement stat = null;
        ResultSet res = null;

        try{
            stat = conn.createStatement();
            res = stat.executeQuery(sql);

            while( res.next() ){
                result = new Enchere(res.getInt("id"), res.getTimestamp("dateetheure"), res.getString("description"), res.getDouble("prixDeMise"), res.getTime("dureeEnchere"), res.getInt("utilisateursID"), res.getInt("categorieID"));
                result.setStatus(res.getInt("status"));   result.setDesignationstatus(res.getString("designationstatus"));
                Utilisateurs utilisateurs = (Utilisateurs) new Utilisateurs(result.getUtilisateursid()).find(conn);
                Categorie categorie = (Categorie) new Categorie(result.getCategorieid()).find(conn);
                result.setUtilisateurs(utilisateurs);
                result.setCategorie(categorie);
                result.setCoverphoto(PhotoEnchere.getcoverPhoto(result.getId(), conn));
                result.setPhotos(PhotoEnchere.getPhotos(result.getId(), conn));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            if (res !=null) res.close();
            if (stat !=null) stat.close();
        }
        return result;
    }

    public Enchere[] search(Connection conn, String description, Timestamp dateheure, int idcategorie, double prixmin, double prixmax, int status) throws Exception{
        ArrayList<Enchere> array = new ArrayList<>();
        String sql = "select * from view_enchere_status where 1=1 ";
        if ( description != null ){
            if ( description.trim().length() > 0 ) {
                sql += "and lower(description) like lower('%" + description + "%')";
            }
        } if ( dateheure !=null ) {
            sql+=" and (dateetheure = '"+ String.valueOf(dateheure) +"')";
        } if (idcategorie != 0) {
            sql +=" and (categorieid = "+ idcategorie +")";
        } if (status != 10 ){
            sql += " and (status = "+status+")";
        } if (prixmin != 0  && prixmax != 0) {
            sql += " and ("+ prixmin +" <= prixdemise) and (prixdemise <= "+prixmax+")";
        } else if(prixmin != 0  && prixmax == 0){
            sql += " and ("+ prixmin +" <= prixdemise)";
        } else if(prixmin == 0  && prixmax != 0){
            sql += " and ("+ prixmax +" >= prixdemise)";
        }
        System.out.println(sql);

        Statement stat = null;
        ResultSet res = null;

        try{
            stat = conn.createStatement();
            res = stat.executeQuery(sql);

            while( res.next() ){
                Enchere e = new Enchere(res.getInt("id"), res.getTimestamp("dateetheure"), res.getString("description"), res.getDouble("prixDeMise"), res.getTime("dureeEnchere"), res.getInt("utilisateursID"), res.getInt("categorieID"));
                e.setStatus(res.getInt("status"));   e.setDesignationstatus(res.getString("designationstatus"));
                Utilisateurs utilisateurs = (Utilisateurs) new Utilisateurs(e.getUtilisateursid()).find(conn);
                Categorie categorie = (Categorie) new Categorie(e.getCategorieid()).find(conn);
                e.setUtilisateurs(utilisateurs);
                e.setCategorie(categorie);
                e.setCoverphoto(PhotoEnchere.getcoverPhoto(e.getId(), conn));
                array.add(e);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            if (res !=null) res.close();
            if (stat !=null) stat.close();
        }
        Enchere[] result = new Enchere[array.size()];
        array.toArray(result);
        return result;
    }

    public boolean checkFini(EnchereTerminerRepository enchereTerminerRepository, MiseEnchereRepository miseEnchereRepository){
        Date now = new Date(System.currentTimeMillis());
        Timestamp finish = new MiseEnchereService().addTimeToTimestamp(this.getDateetheure(), this.getDureeenchere());

        if( now.equals(finish) || now.after(finish)){
            finishEnchere(now, enchereTerminerRepository, miseEnchereRepository);
            return true;
        }
        return false;
    }

    public void finishEnchere(Date finission, EnchereTerminerRepository enchereTerminerRepository, MiseEnchereRepository miseEnchereRepository){
        MiseEnchere maxMise = miseEnchereRepository.getmontantMax(this.getId());
        if( maxMise == null ){
           enchereTerminerRepository.saveNullUserID(this.getId(), finission, 0, null);
        } else{
            Enchereterminer terminer = new Enchereterminer(this.getId(), finission, maxMise.getMontantMax(), maxMise.getUtilisateursID());
            enchereTerminerRepository.save(terminer);
        }
    }
}
