package back.backend;
import org.springframework.mail.SimpleMailMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    private final JavaMailSender emailSender;

    public EmailController(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @PostMapping("/api/sendmail")
    public String sendEmail(@RequestBody EmailForm emailForm) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailForm.getOwnerEmail());
        message.setTo(emailForm.getUserEmail());
        message.setSubject("New form response for " + emailForm.getFormTitle());
        message.setText(emailForm.getFormDataAsString());
        emailSender.send(message);
        return "Email sent successfully";
    }

}
