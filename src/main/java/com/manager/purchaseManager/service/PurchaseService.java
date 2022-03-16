package com.manager.purchaseManager.service;

import com.manager.purchaseManager.jpa.JpaRepository;
import com.manager.purchaseManager.model.Item;
import com.manager.purchaseManager.model.Product;
import com.manager.purchaseManager.model.Purchase;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.ArrayList;

@Service
public class PurchaseService {

    private  final JpaRepository jpR;
    private final ProductService productService;
    private final ItemService itemService;

    public PurchaseService(JpaRepository jpR, ProductService productService, ItemService itemService) {
        this.jpR = jpR;
        this.productService = productService;
        this.itemService = itemService;
    }

    /**
     * Метод для поиска Item с самой поздней датой записи
     * @return последняя запись
     */
    public Item findItemWithLastDate(){
        try {
            return jpR.executeRequest(
                    em -> em.createQuery(
                                    "select i from Item i where i.date_of_buy in (select max(b.date_of_buy) from Item b)", Item.class)
                            .getSingleResult()
            );
        }catch(NoResultException ex){
            return  null;
        }
    }


    /**
     * Сохранение Списка покупок
     * @param list  массив покупок по датам
     */
    public void savePurchaseList(ArrayList<Purchase> list){
        EntityManager em = jpR.getEntityManager();
        for(Purchase p: list){
            Item itemExists = itemService.findByDateAndTotalPrice(p.getDate(),p.getTotalCost());
            if(itemExists==null) {
                ArrayList<Product> products = new ArrayList<>();
                for (String el : p.getProducts()) {
                    Product product = productService.findByName(el);
                    if (product == null) {
                        em.persist(
                                Product.builder()
                                        .name(el)
                                        .price(p.getPrices().get(p.getProducts().indexOf(el)))
                                        .build()
                        );
                    }
                    products.add(product);
                }
                Item item = Item.builder()
                        .date_of_buy(p.getDate())
                        .totalCost(p.getTotalCost())
                        .listOfPurchase(products).build();
                products.clear();
                em.persist(item);
            }
        }

    }

}
