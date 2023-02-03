package vae.vae.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import vae.vae.model.Utilisateurs;
import vae.vae.repository.TokenUsersRepository;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Connection;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "tokenUsers")

public class TokenUtils {
    @Id
    long id;
    long idadmin;
    String token;
    Timestamp expirationdate;


    public TokenUtils(String token, Timestamp EXPIRATION_DATE, Utilisateurs utilisateur){
        this.token = token;
        this.expirationdate = EXPIRATION_DATE;
        this.idadmin = utilisateur.getId();
    }

    public TokenUtils(){}

    public void setId(Long id) {
        this.id = id;
    }

    @javax.persistence.Id
    public Long getId() {
        return id;
    }

    public static Utilisateurs identifyUserThroughToken(String token, TokenUsersRepository tokenUsersRepository, Connection conn) throws Exception{
        int utilisateursID = (int)tokenUsersRepository.findToken(token).get(0).getIdadmin();
        Utilisateurs utilisateurs = new Utilisateurs(utilisateursID).findOne(conn);
        return utilisateurs;
    }
}
