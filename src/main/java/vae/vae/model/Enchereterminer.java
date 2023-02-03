package vae.vae.model;

import vae.vae.annotation.FieldDisable;
import vae.vae.general.ObjetBDD;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

@Entity
@Getter
@Setter
@Table(name = "Enchereterminer")
public class Enchereterminer extends ObjetBDD {
    @Id
    int enchereid;
    Date datefinissions;
    double montantmax;
    int utilisateursid;
    @FieldDisable
    @Transient
    Utilisateurs utilisateurs;

    public Enchereterminer(int enchereid, Date dateFinissions, double montantMax, int utilisateursid){
        this.setEnchereid(enchereid);
        this.setDatefinissions(dateFinissions);
        this.setMontantmax(montantMax);
        this.setUtilisateursid(utilisateursid);
    }

    public Enchereterminer() {

    }

    public Enchereterminer(int enchereid) {
        this.enchereid = enchereid;
    }

    @Override
    public Enchereterminer find(Connection conn) throws Exception{
        Enchereterminer result = new Enchereterminer();
        String sql = "select * from enchereterminer where enchereid = "+this.getEnchereid();
        System.out.println(sql);

        Statement stat = null;
        ResultSet res = null;

        try{
            stat = conn.createStatement();
            res = stat.executeQuery(sql);

            while( res.next() ){
               result = new Enchereterminer(res.getInt("enchereid"), res.getDate("dateFinissions"), res.getDouble("montantmax"), res.getInt("utilisateursid"));
               Utilisateurs utilisateurs = (Utilisateurs) new Utilisateurs(result.getUtilisateursid()).find(conn);
               result.setUtilisateurs(utilisateurs);
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
}
