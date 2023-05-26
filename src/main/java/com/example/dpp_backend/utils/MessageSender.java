package com.example.dpp_backend.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MessageSender {

    private String ACCOUNT_SID;
    private String AUTH_TOKEN;


    public MessageSender(@Value("${twilio.account_sid}") String ACCOUNT_SID, @Value("${twilio.auth_token}") String AUTH_TOKEN) {
        this.ACCOUNT_SID = ACCOUNT_SID;
        this.AUTH_TOKEN = AUTH_TOKEN;
    }


    public boolean send(String message, String phoneNumber) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        try {
            
            Message msg = Message
            .creator(
                new PhoneNumber(phoneNumber),
                new PhoneNumber("+12705177673"),
                message
            )
            .create();

            log.info("Message sent to {} with sid {}", phoneNumber, msg.getSid());
            return true;

        } catch (Exception e) {
            log.error("Error sending message to {}", phoneNumber);
            e.printStackTrace();
            return false;
        }
    }
    
}
