package com.boot.ordercraft.service.MailService;
 
import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
 
import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
 
@Service
public class MailService {
 
    @Autowired
    private JavaMailSender mailSender;
 
    public void sendRegistrationEmail(String toEmail, String username, String rawPassword) {
        String subject = "Welcome to OrderCraft!";
        String message = String.format(
                "Hi %s,\n\nYour account has been successfully created.\n\nUsername: %s\nPassword: %s\n\nPlease log in and change your password as soon as possible.\n\nRegards,\nOrderCraft Team",
                username, username, rawPassword
        );
 
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(toEmail);
        mail.setSubject(subject);
        mail.setText(message);
        mailSender.send(mail);
    }
    
    
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
    
    
    public void OtpService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
 
    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();
 
    public String generateAndStoreOtp(String email) {
        String otp = String.valueOf(100000 + new SecureRandom().nextInt(900000));
        otpStorage.put(email, otp);
        sendOtpEmail(email, otp);
        return otp;
    }
 
    public boolean verifyOtp(String email, String otp) {
        String expectedOtp = otpStorage.get(email);
        if (expectedOtp == null || otp == null) {
            return false;
        }
        return expectedOtp.equals(otp);
    }
 
    private void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: " + otp + "\n\nPlease use this to reset your password. The code will expire shortly.");
        mailSender.send(message);
    }
    
    
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@yourdomain.com"); // or your configured email
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
    
    
    public void sendInvoiceEmail(String to, byte[] pdfBytes, String orderId) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
 
            // ✅ true = multipart
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("noreply@ordercraft.com");
            helper.setTo(to);
            helper.setSubject("Invoice for Order #" + orderId);
 
            // ✅ Plain text or HTML body
            String htmlBody = "<p>Dear customer,</p>" +
                    "<p>Please find attached your invoice for Order <strong>#" + orderId + "</strong>.</p>" +
                    "<p>Regards,<br>OrderCraft Team</p>";
            helper.setText(htmlBody,true);
 
            // ✅ Attachment
            ByteArrayResource pdfResource = new ByteArrayResource(pdfBytes);
            helper.addAttachment("Invoice_" + orderId + ".pdf", pdfResource);
 
            // ✅ Send mail
            mailSender.send(message);
            System.out.println("Email sent successfully");
 
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    
    public void sendLowStockEmail(String to, byte[] pdfBytes) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("noreply@ordercraft.com");
            helper.setTo(to);
            helper.setSubject("⚠️ Low Stock Alert - OrderCraft");

            String htmlBody = "<p>Dear Admin,</p>" +
                    "<p>The following products are below the minimum stock threshold. " +
                    "Please restock them as soon as possible.</p>" +
                    "<p>Attached is the detailed report in PDF format.</p>" +
                    "<p>Regards,<br>OrderCraft System</p>";

            helper.setText(htmlBody, true);

            // Attach PDF
            ByteArrayResource pdfResource = new ByteArrayResource(pdfBytes);
            helper.addAttachment("LowStockReport.pdf", pdfResource);

            mailSender.send(message);
            System.out.println("✅ Low stock email sent with PDF report");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
 
 