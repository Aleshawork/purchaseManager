package com.manager.purchaseManager.mail;

import com.manager.purchaseManager.model.Purchase;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;
import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.SearchTerm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@Slf4j
@PropertySources({
        @PropertySource("classpath:application.properties"),
        @PropertySource("classpath:htmlparse.properties")
})
@Component
public class MailManager {

    @Value("${mail.host}")
    private String host;
    @Value("${mail.user}")
    private String user;
    @Value("${mail.password}")
    private String password;
    @Value("${mail.folder}")
    private String folderName;


    @Value("${html.select_product}")
    private String select_product;
    @Value("${html.select_price}")
    private String select_price;

    private final  Session session;

    public MailManager(Session session, Session session1) {
        this.session = session1;

    }


    /**
     * Метод для получения сообщений с почтового ящика
     * @param startDate - дата начала поиска
     * @return массив записей о покупках
     */
    public ArrayList<Purchase> readPurchase(Date startDate) {
        Message[] messages = null;
        try {
            Store store = session.getStore();
            store.connect(host, user, password);
            Folder folder = store.getFolder(folderName);
            folder.open(Folder.READ_ONLY);
            messages = folder.search(new SearchTerm() {
                @Override
                public boolean match(Message msg) {
                    try {
                        return msg.getSentDate()
                                .after(startDate);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            });
        } catch (MessagingException e) {
            log.error("Password or login incorrect ! Please try again!");
            e.printStackTrace();
        }
        if(messages.length>0) {
            log.info("Find {} messages", messages.length);
            return parseMessages(messages);
        }else return new ArrayList<>();
    }

    private ArrayList<Purchase> parseMessages(Message[] messages) {
        ArrayList<Purchase> list = new ArrayList<>();
        for (Message el : messages) {
            try {
                MimeMultipart content = (MimeMultipart) el.getContent();
                BodyPart bodyPart = content.getBodyPart(0);
                MimeMultipart content2 = (MimeMultipart) bodyPart.getContent();
                BodyPart bodyPart2 = content2.getBodyPart(0);

                String html = (String) bodyPart2.getContent();
                Document doc = Jsoup.parse(html);

                ArrayList<String> products = new ArrayList<>();
                ArrayList<Double> prices = new ArrayList<>();
                int index = 19;

                select_product = upIndex(select_product,index);
                select_price = upIndex(select_price,index);

                String product = doc.select(select_product).text();
                String price = doc.select(select_price).text();

                while (!product.equals("")) {
                    products.add(product.replace("*",""));
                    prices.add(Double.parseDouble(price));

                    index += 2;
                    select_product = upIndex(select_product,index);
                    select_price = upIndex(select_price,index);
                    product = doc.select(select_product).text();
                    price = doc.select(select_price).text();
                }
                log.info("The purchase reading is completed. Read {} products. Date of purchase {}",products.size(),el.getSentDate().toString());

                list.add(
                        new Purchase(
                            new java.sql.Date(el.getSentDate().getTime()),
                            prices.stream()
                                        .reduce((l,r)->l+r).get(),
                            products,
                            prices
                        )
                );
                select_product=setStartIndex(select_product,index);
                select_price=setStartIndex(select_price,index);

            } catch (IOException | MessagingException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private String setStartIndex(String stroka,int lastIndex){
        return stroka.replace(Integer.toString(lastIndex),"?");
    }

    private String upIndex(String stroka, int index){
        if(stroka.contains("?")) return stroka.replace("?", Integer.toString(index));
        return stroka.replace(Integer.toString(index-2), Integer.toString(index));
    }
}

