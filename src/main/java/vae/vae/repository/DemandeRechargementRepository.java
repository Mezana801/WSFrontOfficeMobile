package vae.vae.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import vae.vae.model.DemandeRechargement;

public interface DemandeRechargementRepository extends MongoRepository<DemandeRechargement,Integer>, CustomMiseEnchereRepository {

}
