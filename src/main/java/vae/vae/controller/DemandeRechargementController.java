package vae.vae.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vae.vae.db.ConnectionPostgresSQL;
import vae.vae.model.CompteUtilisateurs;
import vae.vae.model.DemandeRechargement;
import vae.vae.model.Enchere;
import vae.vae.model.Utilisateurs;
import vae.vae.repository.DemandeRechargementRepository;
import vae.vae.service.DataResponse;

import java.sql.Connection;
import java.sql.Date;

@RestController
@CrossOrigin("*")
@RequestMapping("/erecharge")
public class DemandeRechargementController {

    @Autowired
    DemandeRechargementRepository demandeRechargementRepository;

    @PostMapping
    public ResponseEntity<DataResponse> demanderRechargement(@RequestBody DemandeRechargement demande) throws Exception {
        System.out.println("montant "+demande.getMontant());

        Connection conn = ConnectionPostgresSQL.getconnect();
        Date now = new Date(System.currentTimeMillis());
        CompteUtilisateurs compteUtilisateurs = new CompteUtilisateurs(demande.getCompteUtilisateursid());
        compteUtilisateurs = compteUtilisateurs.findByID(conn);

        demande.setId(Utilisateurs.getNextval("sDemande", conn));
        demande.setCompteUtilisateurs(compteUtilisateurs);
        demande.setDateDemande(now);    demande.setStatut(1);
        demandeRechargementRepository.save(demande);
        DataResponse dr = new DataResponse();
        dr.setData("ok");
        dr.setStatus("200");

        conn.close();

        return ResponseEntity.accepted().body(dr);
    }
}
