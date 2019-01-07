package de.burandt.artists.contact.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class ContactService {

    private final ContactServiceProperties properties;

    private final JavaMailSenderImpl mailSender;

    private static final Logger LOG = Logger.getLogger(ContactService.class.getName());

    @Autowired
    public ContactService(ContactServiceProperties contactServiceProperties, JavaMailSenderImpl mailSender) {
        this.properties = contactServiceProperties;
        this.mailSender = mailSender;
    }

    public void sendContactEmail(String message, String subject){

        LOG.info("Email is being prepared...");

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mailSender.getUsername());
        mailMessage.setTo(properties.getRecipient());
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);

        LOG.info("Email has been sent.");
    }
}
