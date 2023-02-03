package vae.vae.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import vae.vae.db.ConnectionPostgresSQL;
import vae.vae.model.CompteUtilisateurs;
import vae.vae.model.DemandeRechargement;
import vae.vae.model.Utilisateurs;
import vae.vae.repository.TokenUsersRepository;
import vae.vae.service.DataResponse;
import vae.vae.service.SafeNavigation;
import vae.vae.service.TokenUtils;

import java.sql.Connection;

@Controller
@CrossOrigin("*")
@RequestMapping("/user")
public class UtilisateurController {
    @Autowired
    private TokenUsersRepository tokenUsersRepository;

    @PostMapping("/login")
    public ResponseEntity<DataResponse> getLogin (@RequestBody Utilisateurs utilisateurs) throws Exception {
        DataResponse dr = new DataResponse();
        Utilisateurs user= utilisateurs.login(utilisateurs.getEmail(),utilisateurs.getPassword());
        if(user != null){
            final TokenUtils token = SafeNavigation.encryptThisString(user);
            tokenUsersRepository.saveAdminToken(token);
            dr.setData(token.getToken());
            dr.setStatus("200");
        }
        else {
            dr.setStatus("500");
            dr.setData("Login incorrect");
        }
        return ResponseEntity.accepted().body(dr);
    }

    @PostMapping("/save")
    public ResponseEntity<DataResponse> createInscription(@RequestBody Utilisateurs user) throws Exception {
        Connection conn = ConnectionPostgresSQL.getconnect();
        DataResponse dr = new DataResponse();
        int iduser = user.inscription(user.getNom(), user.getPrenom(),user.getDatenaissance(),user.getAdresse(),user.getEmail(),user.getPassword());
        if(iduser != 0){
            user.setId(iduser);
            user = user.findOne(conn);
            dr.setData(user);
            dr.setStatus("200");
        }
        conn.close();
        return ResponseEntity.accepted().body(dr);
    }

    @PostMapping("/solde")
    public ResponseEntity<DataResponse> soldeCompte(@RequestBody Utilisateurs utilisateurs) throws Exception {
        Connection conn = ConnectionPostgresSQL.getconnect();
        Utilisateurs user = TokenUtils.identifyUserThroughToken(utilisateurs.getToken(), tokenUsersRepository, conn);
        CompteUtilisateurs compteUtilisateurs = new CompteUtilisateurs();
        compteUtilisateurs.setUtilisateursid(user.getId());
        compteUtilisateurs = compteUtilisateurs.find(conn);

        DataResponse dr = new DataResponse();
        dr.setStatus("200");
        dr.setData(compteUtilisateurs.getSolde());
        return ResponseEntity.accepted().body(dr);
    }
}
