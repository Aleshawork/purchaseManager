package com.manager.purchaseManager.service;

import com.manager.purchaseManager.jpa.JpaRepository;
import com.manager.purchaseManager.model.Item;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.Date;

@Service
public class ItemService {
    private final JpaRepository jpR;

    public ItemService(JpaRepository jpR) {
        this.jpR = jpR;
    }

    public Item findByDateAndTotalPrice(Date date,Double totalPrice){
        try{
            return jpR.executeRequest(em->
                    em.createQuery("select i from Item i where i.date_of_buy=:date and i.totalCost=:cost", Item.class)
                            .setParameter("date",date)
                            .setParameter("cost",totalPrice)
                    ).getSingleResult();
        } catch (NoResultException ex){
            return  null;
        }
    }
}
