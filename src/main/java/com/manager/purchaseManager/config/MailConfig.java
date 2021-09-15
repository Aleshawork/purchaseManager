package com.manager.purchaseManager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.mail.Session;
import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public Session getSession(){
        Properties properties = new Properties();
        properties.setProperty("mail.store.protocol", "imaps");
        return  Session.getInstance(properties, null);
    }
}
