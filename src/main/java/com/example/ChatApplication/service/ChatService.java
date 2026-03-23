package com.example.ChatApplication.service;

import com.example.ChatApplication.dto.ChatMessageDTO;
import com.example.ChatApplication.entity.ChatMessage;
import com.example.ChatApplication.entity.User;
import com.example.ChatApplication.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChatService {

	@Autowired
	private ChatMessageRepository chatMessageRepository;


	@Autowired
	private UserService userService;

	public ChatMessageDTO processMessage(ChatMessageDTO message) {

		// Basic Validation
		if (message.getContent() == null || message.getContent().trim().isEmpty()) {
			throw new IllegalArgumentException("Message cannot be Empty");
		}

		String content = message.getContent().trim();

		if (content.length() > 500) {
			throw new IllegalArgumentException("Message too long");
		}

		message.setContent(content);

		// Add timestamp
		message.setTimeStamp(LocalDateTime.now());

		// Get sender from message
		String senderUsername = message.getSender();
		User sender = userService.getUserByUsername(senderUsername);

		 if(sender.isBlocked()){
			 throw new IllegalArgumentException("You are blocked and cannot send message");
		 }

		//  Handle broadcast safely
		User receiver = null;
		if (!"broadcast".equals(message.getReceiver())) {
			receiver = userService.getUserByUsername(message.getReceiver());
		}

		// Convert DTO → Entity
		ChatMessage entity = new ChatMessage();
		entity.setSender(sender);
		entity.setReceiver(receiver);
		entity.setContent(content);
		entity.setTimeStamp(message.getTimeStamp());

		chatMessageRepository.save(entity);

		return message;
	}
}
