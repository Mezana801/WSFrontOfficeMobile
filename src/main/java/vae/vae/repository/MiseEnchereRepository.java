package vae.vae.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import vae.vae.model.MiseEnchere;

public interface MiseEnchereRepository extends MongoRepository<MiseEnchere,Integer>, CustomMiseEnchereRepository {

}
