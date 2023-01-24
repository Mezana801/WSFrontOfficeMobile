package vae.vae.repository;

import org.springframework.data.mongodb.repository.Aggregation;
import vae.vae.model.MiseEnchere;

import java.util.List;

public interface CustomMiseEnchereRepository {
    @Aggregation(pipeline = {
            "{'$match':  {'enchereID':  ?0}}",
            "{'$sort': {montantMax: -1   }}",
            "{ '$limit': 1 }"
    })
    MiseEnchere getmontantMax(int idEnchere);

    @Aggregation(pipeline = {
            "{'$match':  {'enchereID':  ?0}}",
            "{'$sort': {montantMax: -1   }}"
    })
    List<MiseEnchere> findAllMiseEnchereByEnchereID(int idEnchere);
}
