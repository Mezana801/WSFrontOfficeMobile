package vae.vae.repository;

import org.springframework.stereotype.Repository;
import vae.vae.service.TokenUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class TokenUsersRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void saveAdminToken(TokenUtils tokenUtils){

        entityManager.createNativeQuery("insert into tokenUsers values (default, ?, ?, ?)")
                .setParameter(1, tokenUtils.getIdadmin())
                .setParameter(2, tokenUtils.getToken())
                .setParameter(3, tokenUtils.getExpirationdate())
                .executeUpdate();
    }

    @Transactional
    public List<TokenUtils> findToken(String token){
        System.out.println("select * from tokenUsers where token = '"+token+"'");
        List<TokenUtils> result = entityManager.createNativeQuery("select * from tokenUsers where token = ?", TokenUtils.class)
                .setParameter(1, token)
                .getResultList();

        return result;
    }

}
