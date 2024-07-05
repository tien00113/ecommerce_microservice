package com.micro.notification_service.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.micro.common.models.OrderEvent;
import com.micro.notification_service.service.SendEmailService;

import jakarta.mail.MessagingException;



@RestController
public class EmailController {
    @Autowired
    private SendEmailService sendEmailService;

    @GetMapping("/public/notification/email")
    public String sendEmail() {
        sendEmailService.sendEmail("tiennguyenhienvx@gmail.com", "", "<T-SHOP> Xác Nhận Đơn Hàng");

        return "sent email successfully";
    }

    @GetMapping("/public/notification/email/html")
    public String getMethodName() {
        String to = "tiennguyenhienvx@gmail.com";
        String subject = "Xác Nhận Đơn Hàng - Cảm ơn bạn đã mua hàng!";
        String htmlFilePath = "email.html";

        // try {
        //     sendEmailService.sendHtmlEmail(to, subject, htmlFilePath);
        // } catch (MessagingException | IOException e) {
        //     e.printStackTrace();
        // }

        return "đã send về mail theo html";
    }

    @GetMapping("/public/notification/email/test")
    public String test() throws MessagingException, IOException{
        OrderEvent event = new OrderEvent();

        event.setOrderId("23654276347");
        event.setPrice(1999999);

        sendEmailService.sendHtmlEmail("tiennguyenhienvx@gmail.com", "<T-SHOP>", event);

        return "xong";
    }
    
    
}
