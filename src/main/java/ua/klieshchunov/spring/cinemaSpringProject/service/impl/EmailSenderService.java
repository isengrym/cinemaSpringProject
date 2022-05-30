package ua.klieshchunov.spring.cinemaSpringProject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Showtime;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Ticket;
import ua.klieshchunov.spring.cinemaSpringProject.service.TicketService;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmailSenderService {
    private final TicketService ticketService;

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;

    @Autowired
    public EmailSenderService(JavaMailSender mailSender,
                              TicketService ticketService) {
        this.mailSender = mailSender;
        this.ticketService = ticketService;
    }

    @Async
    public void send(String emailTo, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(username);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }

    @Async
    public void sendEmailsForShowtime(Showtime showtime) {
        List<Ticket> tickets = ticketService.findAllTicketsForShowtime(showtime);
        List<String> emails = new ArrayList<>();
        String email;
        for (Ticket ticket : tickets) {
            email = ticket.getUser().getEmail();

            if (!emails.contains(email))
                emails.add(email);
        }

        for (String userEmail : emails) {
            send(userEmail, "Showtime you bought ticket for has been updated",
                    String.format("Hello! It's the administration of Golden Globe Cinema." +
                            "\nWe want to inform you, that the showtime (http://localhost:8081/showtimes/%d ) " +
                            "you have bought ticket for has been updated.", showtime.getId()));
        }
    }
}
