package vae.vae.controller;

import jdk.nashorn.internal.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vae.vae.db.ConnectionPostgresSQL;
import vae.vae.model.CompteUtilisateurs;
import vae.vae.model.DemandeRechargement;
import vae.vae.model.Enchere;
import vae.vae.model.Utilisateurs;
import vae.vae.repository.DemandeRechargementRepository;
import vae.vae.repository.TokenUsersRepository;
import vae.vae.service.DataResponse;
import vae.vae.service.TokenUtils;

import java.sql.Connection;
import java.sql.Date;

@RestController
@CrossOrigin("*")
@RequestMapping("/erecharge")
public class DemandeRechargementController {

    @Autowired
    DemandeRechargementRepository demandeRechargementRepository;

    @Autowired
    TokenUsersRepository tokenUsersRepository;


    @PostMapping
    public ResponseEntity<DataResponse> demanderRechargement(@RequestBody DemandeRechargement demande) throws Exception {
        Connection conn = ConnectionPostgresSQL.getconnect();
        Date now = new Date(System.currentTimeMillis());

        Utilisateurs utilisateurs = TokenUtils.identifyUserThroughToken(demande.getToken(), tokenUsersRepository, conn);
        CompteUtilisateurs compteUtilisateurs = new CompteUtilisateurs();
        compteUtilisateurs.setUtilisateursid(utilisateurs.getId());
        compteUtilisateurs = compteUtilisateurs.find(conn);

        demande.setId(Utilisateurs.getNextval("sDemande", conn));
        demande.setCompteUtilisateursid(compteUtilisateurs.getId());
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
