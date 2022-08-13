package uz.in_trade_map.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequiredArgsConstructor
public class ChatController {
    @Autowired
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/message/chat")
    public void send(Message msg) throws Exception {
        simpMessagingTemplate.convertAndSend("/chat","Hello world!");
    }
}