package com.ecommerce.notification_service.service;

public interface MailService {
	void sendSimpleEmail(String to, String subject, String body);
}
