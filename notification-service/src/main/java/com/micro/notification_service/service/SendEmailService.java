package com.micro.notification_service.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.micro.common.models.OrderEvent;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class SendEmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    
    @Autowired
    private ResourceLoader resourceLoader;

    @Value("$(spring.email.username)")
    private String fromEmailId;

    public void sendEmail(String recipient, String body, String subject){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setFrom(fromEmailId);
        simpleMailMessage.setTo(recipient);
        simpleMailMessage.setText(body);
        simpleMailMessage.setSubject(subject);

        javaMailSender.send(simpleMailMessage);
    }

    public void sendHtmlEmail(String to, String subject, OrderEvent orderEvent) throws MessagingException, IOException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);

        String htmlContent = loadHtmlFromResource("email.html");
        htmlContent = populateTemplate(htmlContent, orderEvent);

        helper.setText(htmlContent, true);

        javaMailSender.send(message);
    }

    public void sendMailRefund(String to, OrderEvent event) throws MessagingException, IOException{
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject("<T-SHOP> Thông Báo Hoàn Tiền");

        String htmlContent = loadHtmlFromResource("refund.html");
        htmlContent = populateTemplate(htmlContent, event);

        helper.setText(htmlContent, true);

        javaMailSender.send(message);
    }

    private String loadHtmlFromResource(String htmlFilePath) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:templates/" + htmlFilePath);
        byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private String populateTemplate(String template, OrderEvent orderEvent) {
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        return template
                .replace("{name}", "Customer") 
                .replace("{orderId}", orderEvent.getOrderId())
                .replace("{totalPrice}", String.valueOf(orderEvent.getPrice()))
                .replace("{paymentTime}", formattedDate);
    }
}
