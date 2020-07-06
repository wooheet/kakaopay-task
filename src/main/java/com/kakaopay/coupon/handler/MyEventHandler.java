package com.kakaopay.coupon.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyEventHandler {

    @EventListener
    public void handle(SignupEvent event){
        log.info("Event received {}", event.getData());
    }
}