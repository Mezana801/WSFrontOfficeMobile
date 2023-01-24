package vae.vae.model;

import vae.vae.annotation.FieldDisable;
import vae.vae.general.ObjetBDD;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Connection;

@Getter
@Setter
@AllArgsConstructor
// @Entity
// @Table(name = "categorie")
public class Categorie extends ObjetBDD {
    // @Id
    int id;
    String nom;

    public Categorie() {
    }

    public Categorie(int id) {
        this.id = id;
    }

    public Categorie findOne(Connection conn) throws Exception{
        return (Categorie)this.find(conn);
    }

}
