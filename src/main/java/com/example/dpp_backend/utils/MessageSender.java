package com.example.dpp_backend.utils;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageSender {

    public final String ACCOUNT_SID = "AC2ecd1cfde952d7c2e8de29b49951fdb1";
    public final String AUTH_TOKEN = "76926673be90591c3ef7a2543b4db617";


    public void send(String message, String phoneNumber) {
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

        } catch (Exception e) {
            log.error("Error sending message to {}", phoneNumber);
        }
    }
    
}
