package vae.vae.model;

import vae.vae.general.ObjetBDD;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Getter
@Setter
@Table(name = "Enchereterminer")
public class Enchereterminer extends ObjetBDD {
    @Id
    int enchereid;
    Date datefinissions;
    double montantmax;
    int utilisateursid;

    public Enchereterminer(int enchereid, Date dateFinissions, double montantMax, int utilisateursid){
        this.setEnchereid(enchereid);
        this.setDatefinissions(dateFinissions);
        this.setMontantmax(montantMax);
        this.setUtilisateursid(utilisateursid);
    }

    public Enchereterminer() {

    }
}
