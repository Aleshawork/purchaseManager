package com.manager.purchaseManager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Purchase {
    private Date date;
    private Double totalCost;
    private ArrayList<String> products;
    private ArrayList<Double> prices;

}
