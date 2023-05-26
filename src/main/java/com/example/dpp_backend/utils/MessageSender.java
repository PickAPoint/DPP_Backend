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

    private String accountSid;
    private String authToken;


    public MessageSender(@Value("${twilio.account_sid}") String accountSid, @Value("${twilio.auth_token}") String authToken) {
        this.accountSid = accountSid;
        this.authToken = authToken;
    }


    public boolean send(String message, String phoneNumber) {
        Twilio.init(accountSid, authToken);

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
            return false;
        }
    }
    
}
