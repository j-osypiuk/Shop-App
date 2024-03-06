package com.shopapp.service;

import com.shopapp.model.MailDetails;

public interface EmailService {

    void sendMail(String toEmail, MailDetails mailDetails);
}
