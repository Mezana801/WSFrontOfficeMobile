package vae.vae.controller;

import vae.vae.general.ObjetBDD;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vae.vae.db.ConnectionPostgresSQL;
import vae.vae.model.Enchere;
import vae.vae.model.Enchereterminer;
import vae.vae.model.PhotoEnchere;
import vae.vae.model.Utilisateurs;
import vae.vae.service.DataResponse;
import vae.vae.service.EnchereService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

@RestController
@CrossOrigin("*")
@RequestMapping("/enchere")
public class EnchereController {

    EnchereService enchereService;

    @GetMapping
    public ResponseEntity<DataResponse> getAllEnchere() throws Exception {
        Connection conn = ConnectionPostgresSQL.getconnect();
        DataResponse dr = new DataResponse();
        dr.setData(new Enchere().findAll(conn));

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

    @GetMapping("/search")
    @ResponseBody
    public  ResponseEntity<DataResponse> search(@RequestBody Enchere enchere) throws Exception {
        Connection conn = ConnectionPostgresSQL.getconnect();
        DataResponse dr = new DataResponse();

        System.out.println("enchere "+enchere.getDescription());
        Enchere[] searchResponse = enchere.search(conn, enchere.getDescription(),enchere.getDateetheure(),enchere.getCategorieid(), enchere.getPrixdemise(), enchere.getStatus());

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
    public ResponseEntity<DataResponse> createPhotoEnchere(@RequestBody PhotoEnchere photoEnchere) throws SQLException, ClassNotFoundException {
        DataResponse dr = new DataResponse();
        enchereService = getService();
        Connection conn = ConnectionPostgresSQL.getconnect();

        enchereService.addPhotoEnchere(photoEnchere,conn);
        dr.setStatus("200");
        dr.setData(photoEnchere);

        conn.close();
        return ResponseEntity.accepted().body(dr);
    }

    @PostMapping("/listeDeMesEncheres")
    public ResponseEntity<DataResponse> listeEnchereByIdUser(@RequestBody Utilisateurs utilisateurs) throws Exception {;
        Connection conn = ConnectionPostgresSQL.getconnect();
        Enchere enchere = new Enchere();
        enchere.setUtilisateursid(utilisateurs.getId());
        Enchere[] encheres = enchere.findByUSer(conn);

        DataResponse dr = new DataResponse();
        dr.setStatus("200");
        dr.setData(encheres);

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