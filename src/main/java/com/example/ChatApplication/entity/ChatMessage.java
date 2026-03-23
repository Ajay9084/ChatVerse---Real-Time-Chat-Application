package com.example.ChatApplication.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_message")
@Data
public class ChatMessage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String content;
	private LocalDateTime timeStamp;
	private boolean seen;

	@ManyToOne
	@JoinColumn(name = "sender_id")
	private User sender;

	@ManyToOne
	@JoinColumn(name = "receiver_id")
	private User receiver;

	@PrePersist
	public void setTimeStamp() {
		this.timeStamp = LocalDateTime.now();
	}
}
