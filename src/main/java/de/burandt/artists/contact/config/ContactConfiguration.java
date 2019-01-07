package de.burandt.artists.contact.config;

import de.burandt.artists.contact.service.ContactServiceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class ContactConfiguration {

    @Bean
    public JavaMailSenderImpl javaMailSenderImpl(ContactServiceProperties properties) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("send.one.com");
        mailSender.setPort(587);
        mailSender.setUsername(properties.getSender());
        mailSender.setPassword(properties.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
