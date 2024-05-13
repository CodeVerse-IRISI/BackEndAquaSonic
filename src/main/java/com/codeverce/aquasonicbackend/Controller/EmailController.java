package com.codeverce.aquasonicbackend.Controller;

import com.codeverce.aquasonicbackend.Model.EmailDetails;
import com.codeverce.aquasonicbackend.Service.EmailService;
import com.codeverce.aquasonicbackend.Service.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


// Annotation
@RestController
// Class
public class EmailController {

    @Autowired
    private EmailServiceImpl emailService;

    // Sending a simple Email
    @PostMapping("/sendMail")
    public String
    sendMail(@RequestBody EmailDetails details)
    {
        return emailService.sendSimpleMail(details);
        
    }

    // Sending email with attachment
    @PostMapping("/sendMailWithAttachment")
    public String sendMailWithAttachment(@RequestBody EmailDetails details)
    {
        String status = emailService.sendMailWithAttachment(details);
        return status;
    }


}
