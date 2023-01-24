package vae.vae.model;

// import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "miseenchere")
@Getter
@Setter
public class MiseEnchere {
    @Id
    String id;
    int enchereID;
    Enchere enchere;
    int utilisateursID;
    Utilisateurs utilisateurs;
    double montantMax;
}
