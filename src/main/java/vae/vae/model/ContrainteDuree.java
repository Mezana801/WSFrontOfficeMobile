package vae.vae.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
public class ContrainteDuree {
    Time dureemin;
    Time dureemax;

    public ContrainteDuree(Time dureemin,Time dureemax){
        setDureemin(dureemin);
        setDureemax(dureemax);
    }


}
