package com.example.ChatApplication.repository;

import com.example.ChatApplication.entity.ChatMessage;
import com.example.ChatApplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {
	void deleteBySender(User sender);
	void deleteByReceiver(User receiver);
}
