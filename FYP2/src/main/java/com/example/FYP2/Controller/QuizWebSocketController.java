package com.example.FYP2.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class QuizWebSocketController {


    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void broadcastQuizUpdate(String roomCode, Object data) {
        messagingTemplate.convertAndSend("/topic/quiz/" + roomCode, data);
    }

}
