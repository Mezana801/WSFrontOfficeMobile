package vae.vae.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vae.vae.db.ConnectionPostgresSQL;
import vae.vae.model.Categorie;
import vae.vae.service.DataResponse;

import java.sql.Connection;

@RestController
@CrossOrigin("*")
@RequestMapping("/categorie")
public class CategorieController {

    @GetMapping
    public ResponseEntity<DataResponse> getAllCategorie() throws Exception {
        Connection conn = ConnectionPostgresSQL.getconnect();
        DataResponse dr = new DataResponse();
        dr.setData(new Categorie().findAll(conn));

        conn.close();
        return ResponseEntity.accepted().body(dr);
    }
}
