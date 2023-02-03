package vae.vae.service;

import vae.vae.model.Categorie;
import vae.vae.model.ContrainteDuree;
import vae.vae.model.Enchere;
import vae.vae.model.PhotoEnchere;

import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class EnchereService {

    PreparedStatement preparedStatement;
    ResultSet resultSet;

    public int createEnchere(Enchere e,Connection conn) throws Exception {
        ContrainteDuree contrainteDuree = getContrainteDuree(conn);

//        Check if Duree Enchere in Intervalle Contrainte Duree
        if ((e.getDureeenchere().before(contrainteDuree.getDureemin())) || (e.getDureeenchere().after(contrainteDuree.getDureemax()))){
            throw new Exception("Duree Enchere Hors Limite");
        }

//        Check if Prixdemise Enchere is not negative
        if (e.getPrixdemise() < 0){
            throw new Exception("Prix de Mise negatif");
        }

        int result = 0;
        preparedStatement = conn.prepareStatement("Insert into Enchere values (default,?,?,?,?,?,?) returning id");
        preparedStatement.setTimestamp(1,e.getDateetheure());
        preparedStatement.setString(2,e.getDescription());
        preparedStatement.setDouble(3,e.getPrixdemise());
        preparedStatement.setTime(4,e.getDureeenchere());
        preparedStatement.setInt(5,e.getUtilisateursid());
        preparedStatement.setInt(6,e.getCategorieid());
        resultSet = preparedStatement.executeQuery();

        while( resultSet.next() ){
            result = resultSet.getInt("id");
        }

        closeStatement();
        return result;
    }

    private void closeStatement() throws SQLException {
        if (preparedStatement != null){
            preparedStatement.close();
        }
    }

    private void closeResultSet() throws SQLException {
        if (resultSet != null){
            resultSet.close();
        }
    }

    public List<Categorie> getAllCategorie(Connection conn) throws SQLException {
        preparedStatement = conn.prepareStatement("Select * From Categorie");
        resultSet = preparedStatement.executeQuery();
        List<Categorie> listecategorie = new ArrayList<>();
        while (resultSet.next()){
            Categorie c = new Categorie();
            c.setId(resultSet.getInt("id"));
            c.setNom(resultSet.getString("nom"));
            listecategorie.add(c);
        }

        closeStatement();
        return listecategorie;
    }

    public PhotoEnchere getPhotosEnchere(int idEnchere,Connection conn) throws SQLException {
        preparedStatement = conn.prepareStatement("Select * from photosenchere where enchereid=?");
        preparedStatement.setInt(1,idEnchere);
        resultSet = preparedStatement.executeQuery();
        List<String> listephoto = new ArrayList<>();
        while (resultSet.next()){
            String photourl = resultSet.getString("photourl");
            listephoto.add(photourl);
        }
        String[] photosenchere = listephoto.toArray(new String[0]);
        PhotoEnchere pe = new PhotoEnchere();
        pe.setEnchereID(idEnchere);
        pe.setPhotourl(photosenchere);
        return pe;
    }

    public void addPhotoEnchere(PhotoEnchere photoEnchere, Connection conn) throws Exception {
        photoToFile(photoEnchere, conn);
    }

    public void addPhotos(String fileName, int enchereID, Connection conn) throws Exception {
        preparedStatement = conn.prepareStatement("Insert into PhotosEnchere values (?,?)");
        preparedStatement.setInt(1,enchereID);
        preparedStatement.setString(2, fileName);

        preparedStatement.executeUpdate();
        closeStatement();
    }

    public ContrainteDuree getContrainteDuree(Connection conn) throws SQLException {
        preparedStatement = conn.prepareStatement("Select * from contrainteduree order by dates desc limit 1");
        resultSet = preparedStatement.executeQuery();
        ContrainteDuree contrainteDuree = null;

        while (resultSet.next()){
            contrainteDuree = new ContrainteDuree(resultSet.getTime("dureemin"),resultSet.getTime("dureemax"));
        }

        closeStatement();

        return contrainteDuree;
    }

    public String generateUniqueName() throws Exception{
        Date dateNow = new Date(System.currentTimeMillis());
        Timestamp expiration = new Timestamp(dateNow.getTime());

        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] messageDigest = md.digest(expiration.toString().getBytes());
        BigInteger no = new BigInteger(1, messageDigest);
        String hashtext = no.toString(16);
        return hashtext;
    }

    public void photoToFile(PhotoEnchere photoEnchere, Connection conn) throws Exception{
        String[] base64Syntax = photoEnchere.getPhotourl();

        for( String s: base64Syntax ){
            String sfinal = "IMG_"+generateUniqueName()+".jpeg";
            String path = "public/images/"+sfinal;
            byte[] bytes = Base64.getDecoder().decode(s);
            FileOutputStream stream = new FileOutputStream(path);
            stream.write(bytes);
            this.addPhotos(sfinal, photoEnchere.getEnchereID(), conn);
        }

    }

}
