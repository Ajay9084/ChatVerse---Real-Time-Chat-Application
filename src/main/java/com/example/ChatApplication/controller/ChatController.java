//package com.example.ChatApplication.controller;
//
//import com.example.ChatApplication.dto.ChatMessageDTO;
//import com.example.ChatApplication.service.ChatService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import java.security.Principal;
//import java.time.LocalDateTime;
//
//@Controller
//public class ChatController {
//
//
//	@Autowired
//	private SimpMessagingTemplate messagingTemplate;
//
//	@Autowired
//	private ChatService chatService;
//
//	@MessageMapping("/sendMessage")
//
//	public void sendMessage(ChatMessageDTO message, Principal principal) {
//		try {
//			ChatMessageDTO processed = chatService.processMessage(message);
//			messagingTemplate.convertAndSend("/topic/messages", processed);     // ✅ broadcast on success
//
//		} catch (IllegalArgumentException e) {
//			ChatMessageDTO error = new ChatMessageDTO();
//			error.setSender("SYSTEM");
//			error.setReceiver(principal.getName());
//			error.setContent("⚠ " + e.getMessage());
//			error.setTimeStamp(LocalDateTime.now());
//
//			messagingTemplate.convertAndSendToUser(
//					principal.getName(), "/queue/errors", error
//			);
//		}
//	}
//
//	@GetMapping("chat")
//	public String chat(){
//		return "chat";
//	}
//}



package com.example.ChatApplication.controller;

import com.example.ChatApplication.dto.ChatMessageDTO;
import com.example.ChatApplication.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
public class ChatController {

	@Autowired
	private ChatService chatService;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@MessageMapping("/sendMessage")
	public void sendMessage(ChatMessageDTO message) {
		try {
			ChatMessageDTO processed = chatService.processMessage(message);
			// Success → broadcast to everyone
			messagingTemplate.convertAndSend("/topic/messages", processed);

		} catch (IllegalArgumentException e) {
			// Failure → route error privately back to the sender using username from the DTO
			ChatMessageDTO error = new ChatMessageDTO();
			error.setSender("SYSTEM");
			error.setReceiver(message.getSender());
			error.setContent("⚠ " + e.getMessage());
			error.setTimeStamp(LocalDateTime.now());

			messagingTemplate.convertAndSendToUser(
					message.getSender(), "/queue/errors", error
			);
		}
	}

	@GetMapping("chat")
	public String chat() {
		return "chat";
	}
}
