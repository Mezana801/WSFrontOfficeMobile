package vae.vae.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import vae.vae.db.ConnectionPostgresSQL;
import vae.vae.model.Categorie;
import vae.vae.service.DataResponse;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

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
