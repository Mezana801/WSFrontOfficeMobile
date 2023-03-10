package vae.vae.controller;

import org.springframework.beans.factory.annotation.Autowired;
import vae.vae.general.ObjetBDD;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vae.vae.db.ConnectionPostgresSQL;
import vae.vae.model.*;
import vae.vae.repository.MiseEnchereRepository;
import vae.vae.repository.TokenUsersRepository;
import vae.vae.service.DataResponse;
import vae.vae.service.EnchereService;
import vae.vae.service.MiseEnchereService;
import vae.vae.service.TokenUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/enchere")
public class EnchereController {

    @Autowired
    private TokenUsersRepository tokenUsersRepository;

    @Autowired
    private MiseEnchereRepository miseEnchereRepository;

    EnchereService enchereService;

    @GetMapping
    public ResponseEntity<DataResponse> getAllEnchere() throws Exception {
        Connection conn = ConnectionPostgresSQL.getconnect();
        DataResponse dr = new DataResponse();
        dr.setData(new Enchere().findAll(conn));

        conn.close();
        return ResponseEntity.accepted().body(dr);
    }

    @GetMapping("/home")
    public ResponseEntity<DataResponse> getHomeEnchere() throws Exception {
        Connection conn = ConnectionPostgresSQL.getconnect();
        DataResponse dr = new DataResponse();
        dr.setData(new Enchere().findHomeEnchere(conn));

        conn.close();
        return ResponseEntity.accepted().body(dr);
    }

/*    @GetMapping("enchereTerminer")
    public ResponseEntity<DataResponse> enchereTerminer() throws Exception {
        Connection conn = ConnectionPostgresSQL.getconnect();

        ObjetBDD[] enchereterminers = new Enchereterminer().findAll(conn);
        Enchereterminer[] enchereterminers1 = Arrays.copyOf(enchereterminers, enchereterminers.length, Enchereterminer[].class);
        DataResponse dr = new DataResponse();
        dr.setData(enchereterminers1);

        conn.close();
        return ResponseEntity.accepted().body(dr);
    } */

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse> getByIdEnchere(@PathVariable int id) throws Exception {
        Connection conn = ConnectionPostgresSQL.getconnect();
        DataResponse dr = new DataResponse();
        dr.setData(new Enchere(id).find(conn));

        conn.close();
        return ResponseEntity.accepted().body(dr);
    }

    @GetMapping("/photos/{idenchere}")
    public ResponseEntity<DataResponse> getPhotosEnchere(@PathVariable int idenchere) throws SQLException, ClassNotFoundException {
        DataResponse dataResponse = new DataResponse();
        enchereService = getService();
        Connection conn = ConnectionPostgresSQL.getconnect();

        PhotoEnchere pe = enchereService.getPhotosEnchere(idenchere,conn);
        dataResponse.setStatus("200");
        dataResponse.setData(pe);
        return ResponseEntity.accepted().body(dataResponse);
    }

    @GetMapping("/categories")
    public ResponseEntity<DataResponse> getAllCategorie() throws SQLException, ClassNotFoundException {
        DataResponse dr = new DataResponse();

        Connection conn = ConnectionPostgresSQL.getconnect();

        enchereService = this.getService();
        List<Categorie> listecategorie = enchereService.getAllCategorie(conn);

        dr.setData(listecategorie);
        conn.close();

        return ResponseEntity.accepted().body(dr);
    }

    @PostMapping("/search")
    @ResponseBody
    public  ResponseEntity<DataResponse> search(@RequestBody Enchere enchere) throws Exception {
        Connection conn = ConnectionPostgresSQL.getconnect();
        DataResponse dr = new DataResponse();

        Enchere[] searchResponse = enchere.search(conn, enchere.getDescription(),enchere.getDateetheure(),enchere.getCategorieid(), enchere.getPrixmin(), enchere.getPrixmax(), enchere.getStatus());
        dr.setData(searchResponse);

        conn.close();
        return ResponseEntity.accepted().body(dr);
    }

    @PostMapping
    public ResponseEntity<DataResponse> createEnchere(@RequestBody Enchere e) {
        DataResponse dr = new DataResponse();
        enchereService = getService();
        try{
            Connection conn = ConnectionPostgresSQL.getconnect();

            int idEnchere = enchereService.createEnchere(e,conn);
            e.setId(idEnchere);
            e = (Enchere) e.find(conn);
            dr.setData(e);
            dr.setStatus("200");

            conn.close();
        }
        catch (Exception ex){
            dr.setStatus("500");
            dr.setData(ex.getMessage());
        }
        return ResponseEntity.accepted().body(dr);
    }

    @PostMapping("/photo")
    public ResponseEntity<DataResponse> createPhotoEnchere(@RequestBody PhotoEnchere photoEnchere) throws Exception, ClassNotFoundException {
        DataResponse dr = new DataResponse();
        enchereService = getService();
        Connection conn = ConnectionPostgresSQL.getconnect();

       try{
           enchereService.addPhotoEnchere(photoEnchere,conn);
           dr.setStatus("200");
           dr.setData(photoEnchere);
       }
       catch (Exception e){
           dr.setStatus("500");
           dr.setData(e.getMessage());
       }

        conn.close();
        return ResponseEntity.accepted().body(dr);
    }

    @PostMapping("/listeDeMesEncheres")
    public ResponseEntity<DataResponse> listeEnchereByIdUser(@RequestBody Utilisateurs utilisateurs) throws Exception {;
        Connection conn = ConnectionPostgresSQL.getconnect();
        Enchere enchere = new Enchere();
        utilisateurs = TokenUtils.identifyUserThroughToken(utilisateurs.getToken(), tokenUsersRepository, conn);
        enchere.setUtilisateursid(utilisateurs.getId());
        Enchere[] encheres = enchere.findByUSer(conn);

        DataResponse dr = new DataResponse();
        dr.setStatus("200");
        dr.setData(encheres);

        conn.close();
        return ResponseEntity.accepted().body(dr);
    }

    @PostMapping("/getTheAuctionWinner")
    public ResponseEntity<DataResponse> getTheAuctionWinner(@RequestBody Enchere enchere) throws Exception {;
        System.out.println("passing here "+enchere.getId());
        Connection conn = ConnectionPostgresSQL.getconnect();
        MiseEnchere maxMiseEnchere = miseEnchereRepository.getmontantMax(enchere.getId());

        DataResponse dr = new DataResponse();
        dr.setStatus("200");
        dr.setData(maxMiseEnchere);

        conn.close();
        return ResponseEntity.accepted().body(dr);
    }

    private EnchereService getService(){
        if (this.enchereService == null){
            return new EnchereService();
        }
        return this.enchereService;
    }
}