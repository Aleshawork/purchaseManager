package com.manager.purchaseManager;

import com.manager.purchaseManager.mail.MailManager;
import com.manager.purchaseManager.model.Purchase;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;



@SpringBootApplication
public class PurchaseManagerApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(PurchaseManagerApplication.class, args);
//		MailManager mailManager = context.getBean(MailManager.class);
//		ArrayList<Purchase> list  = mailManager.readPurchase(
//				Date.from(LocalDate.of(2021, Month.MARCH,1).atStartOfDay(ZoneId.systemDefault()).toInstant())
//		);
//		System.out.println("Количество писем :"+list.size());
//		list.forEach(el-> System.out.println(el));


	}

}
