package com.projectct.notificationservice.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailUtil {
    final JavaMailSender mailSender;
    public String getEmailTemplate(String templateName, Object... args) throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/Email/"+templateName);
        String template = new String(Files.readAllBytes(Paths.get(resource.getURI())));
        return String.format(template, args);
    }

    public void sendEmail(String receiver, String subject, String templateName, Object... args) throws IOException, MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        String htmlBody = getEmailTemplate(templateName, args);

        helper.setTo(receiver);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        mailSender.send(mimeMessage);
    }
}
