package distributed_sota.notification_service.infrastructure;

import distributed_sota.notification_service.application.port.EmailSenderPort;
import distributed_sota.notification_service.application.port.NotificationService;
import distributed_sota.notification_service.application.service.NotificationServiceImpl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NotificationServiceMain {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(NotificationServiceMain.class);
        app.setAdditionalProfiles("notification");
        app.run(args);
    }

    // ========================= CORE BEANS =========================

    @Bean
    public EmailSenderPort emailSenderPort() {
        return new EmailSenderSimulator();
    }

    @Bean
    public NotificationService notificationService(EmailSenderPort emailSenderPort) {
        return new NotificationServiceImpl(emailSenderPort);
    }
}
