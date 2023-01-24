package vae.vae.service;

import vae.vae.model.ContrainteDuree;
import vae.vae.model.Enchere;
import vae.vae.model.PhotoEnchere;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public void addPhotoEnchere(PhotoEnchere photoEnchere, Connection conn) throws SQLException {

        preparedStatement = conn.prepareStatement("Insert into PhotosEnchere values (?,?)");

        int sizephoto = photoEnchere.getPhotourl().length;
        String[] urlphoto = photoEnchere.getPhotourl();

        for (int i=0;i<sizephoto;i++){
            preparedStatement.setInt(1,photoEnchere.getEnchereID());
            preparedStatement.setString(2, urlphoto[i]);

            preparedStatement.executeUpdate();
        }

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

}
