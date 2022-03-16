package com.manager.purchaseManager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Purchase {
    private Date date;
    private Double totalCost;
    private ArrayList<String> products;
    private ArrayList<Double> prices;


    @Override
    public String toString() {
        return "Purchase{" +
                "date=" + date +
                ", totalCost=" + totalCost +
                ", products:" + Arrays.toString(products.toArray()) +
                ", prices:" + Arrays.toString(prices.toArray()) +
                '}';
    }
}
