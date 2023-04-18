package back.backend.controller.forms.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailSenderService emailSenderService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest emailRequest) {
        emailSenderService.sendSimpleEmail(emailRequest.getToEmail(), emailRequest.getSubject(), emailRequest.getBody());
        return ResponseEntity.ok("Email sent successfully");
    }
}
