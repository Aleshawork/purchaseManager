package com.manager.purchaseManager.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Builder
@Data
@Entity
@NoArgsConstructor
@Table(name = "item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private Date date_of_buy;

    @Column(name="total_cost")
    private Double totalCost;

    @OneToMany
    private List<Product> listOfPurchase;

    public Item(Date date_of_buy, Double totalCost, List<Product> listOfPurchase) {
        this.date_of_buy = date_of_buy;
        this.totalCost = totalCost;
        this.listOfPurchase = listOfPurchase;
    }

    public Item(Long id, Date date_of_buy, Double totalCost, List<Product> listOfPurchase) {
        this.id = id;
        this.date_of_buy = date_of_buy;
        this.totalCost = totalCost;
        this.listOfPurchase = listOfPurchase;
    }
}
