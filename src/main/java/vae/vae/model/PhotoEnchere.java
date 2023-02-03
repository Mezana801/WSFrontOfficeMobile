package vae.vae.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

@Getter
@Setter
public class PhotoEnchere {
    int enchereID;
    String[] photourl;
    Enchere enchere;

    public PhotoEnchere() {
    }

    public PhotoEnchere(int enchereID, String[] photourl) {
        this.enchereID = enchereID;
        this.photourl = photourl;
    }

    public static String getcoverPhoto(int enchereID, Connection conn) throws  Exception{
        String sql = "select * from PhotosEnchere where enchereID = "+enchereID+" limit 1";
        System.out.println(sql);
        String result = "";
        Statement stat = null;
        ResultSet res = null;

        try{
            stat = conn.createStatement();
            res = stat.executeQuery(sql);

            while( res.next() ){
               result = res.getString("photourl");
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

    public static String[] getPhotos(int enchereID, Connection conn) throws  Exception{
        String sql = "select * from PhotosEnchere where enchereID = "+enchereID;
        System.out.println(sql);
        Statement stat = null;
        ResultSet res = null;
        ArrayList<String> result = new ArrayList<String>();

        try{
            stat = conn.createStatement();
            res = stat.executeQuery(sql);
            while( res.next() ){
                result.add(res.getString("photourl"));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            if (res !=null) res.close();
            if (stat !=null) stat.close();
        }
        String[] response = new String[result.size()];
        result.toArray(response);
        return response;
    }

}
