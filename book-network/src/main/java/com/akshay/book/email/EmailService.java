package com.akshay.book.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static com.fasterxml.jackson.core.JsonEncoding.UTF8;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;
    @Value("${send.mail.from}")
    private String mailSenderEmailId;

    @Async
    public void sendEmail(String to , String username , EmailTemplateName emailTemplateName , String confirmationUrl , String activationCode , String subject) throws MessagingException {
        String templateName;
        if(emailTemplateName == null) {
            templateName = "confirm_email";
        } else {
            templateName = emailTemplateName.getName();
        }
        MimeMessage  mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage , MimeMessageHelper.MULTIPART_MODE_MIXED , UTF8.name());

        Map<String , Object> properties = new HashMap<>();
        properties.put("username", username);
        properties.put("confirmationUrl", confirmationUrl);
        properties.put("activation_code", activationCode);

        Context context = new Context();
        context.setVariables(properties);

        mimeMessageHelper.setFrom(mailSenderEmailId);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);


//        need to process template
        String template = springTemplateEngine.process(templateName , context);

        mimeMessageHelper.setText(template , true);

        javaMailSender.send(mimeMessage);

    }
}
