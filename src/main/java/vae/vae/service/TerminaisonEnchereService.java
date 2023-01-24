package vae.vae.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vae.vae.model.Enchere;
import vae.vae.repository.EnchereRepository;
import vae.vae.repository.EnchereTerminerRepository;
import vae.vae.repository.MiseEnchereRepository;

import java.util.ArrayList;

@Service
public class TerminaisonEnchereService {

   // @Autowired
  //  EnchereRepository enchereRepository;

    @Autowired
    EnchereTerminerRepository enchereTerminerRepository;

    @Autowired
    MiseEnchereRepository miseEnchereRepository;

    @Scheduled(fixedRate = 1000)
    public void terminaisonEnchere(){
/*        ArrayList<Enchere> loading = enchereRepository.findLoading();
        for( Enchere e: loading ){
                e.checkFini(enchereTerminerRepository, miseEnchereRepository);
        } */
    }
}
