package com.manager.purchaseManager.service;

import com.manager.purchaseManager.jpa.JpaRepository;
import com.manager.purchaseManager.model.Product;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;

@Service
public class ProductService {

    private final JpaRepository jpR;

    public ProductService(JpaRepository jpR) {
        this.jpR = jpR;
    }

    public Product findByName(String name){
        try {
            return  jpR.executeRequest(em ->
                    em.createQuery("select p from Product p where p.name=:name", Product.class)
                            .setParameter("name", name)
            ).getSingleResult();
        }catch (NoResultException ex){
            return null;
        }
    }
}
