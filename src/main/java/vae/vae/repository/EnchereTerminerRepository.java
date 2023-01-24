package vae.vae.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vae.vae.model.Enchereterminer;

import java.sql.Date;

public interface EnchereTerminerRepository  extends JpaRepository<Enchereterminer, Integer> {

    @Query(value = "insert into enchereterminer values(?1, ?2, ?3, ?4) returning enchereid", nativeQuery = true)
    public void saveNullUserID(int enchereId, Date dateFin,double montantMax, Integer userId);
}
