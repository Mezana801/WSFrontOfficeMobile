package vae.vae.model;
import vae.vae.annotation.FieldDisable;
import vae.vae.general.ObjetBDD;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vae.vae.db.ConnectionPostgresSQL;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.*;

@Getter
@Setter
@AllArgsConstructor
// @Entity
// @Table(name = "Utilisateurs")
public class Utilisateurs extends ObjetBDD {
  //  @Id
    int id;
    String nom;
    String prenom;
    Date datenaissance;
    String adresse;
    String email;
    String password;

    public Utilisateurs() {
    }

    public Utilisateurs(int id) {
        this.id = id;
    }

    public static int getNextval(String sequenceName, Connection conn){
        int result = 0;
        String sql = "select nextval('"+sequenceName+"') as id";
        System.out.println(sql);
        Statement stat = null;
        ResultSet res = null;
        try{
            stat = conn.createStatement();
            res = stat.executeQuery(sql);
            while( res.next() ){
                result = res.getInt("id");
            }
            stat.close();    res.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public int inscription(  String nom,String prenom, Date datenaissance, String adresse,String email, String password) throws Exception {
        Connection con =null;
        ResultSet res = null;
        int result = 0;
        try {
            con =new ConnectionPostgresSQL().getconnect();
            String sql = "insert into Utilisateurs(nom,prenom,datenaissance,adresse,email,password)values('"+ nom +"','"+ prenom +"','"+ datenaissance +"','"+ adresse +"','"+ email +"','"+password +"') returning id";
            res = con.createStatement().executeQuery(sql);
            while( res.next() ){
                result = res.getInt("id");
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }finally{
            if(res != null) res.close();
            if(con !=null) con.close();
        }
        return  result;
    }

    public Utilisateurs login(String user, String password) throws Exception {
        Utilisateurs response = null;
        Connection con =null;
        PreparedStatement stmt = null;
        try{
            con =new ConnectionPostgresSQL().getconnect();
            String sql = "Select * from utilisateurs where email='"+user+"'"+" AND password='"+password+"'";
            System.out.println(sql);
            ResultSet res = con.createStatement().executeQuery(sql);
            while(res.next()){
                response = new Utilisateurs();
                response.setId(res.getInt(1));
                response.setNom(res.getString(2));
                response.setPrenom(res.getString(3));
                response.setDatenaissance(res.getDate(4));
                response.setAdresse(res.getString(5));
                response.setEmail(res.getString(6));
                response.setPassword(res.getString(6));
            }
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }finally{
            if(stmt!=null) stmt.close();
            if(con !=null) con.close();
        }
        return response;
    }

    public Utilisateurs findOne(Connection conn) throws  Exception{
        return (Utilisateurs)this.find(conn);
    }
}
