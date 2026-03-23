package com.example.ChatApplication.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageDTO {
	private String sender;
	private String receiver;
	private String content;
	private String type;
	private LocalDateTime timeStamp;
}
