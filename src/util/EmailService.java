package util; // Or your preferred package



import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {

    private static final String SENDER_EMAIL = "your-email@gmail.com";
    private static final String SENDER_PASSWORD = "your-16-character-app-password";
    public static boolean sendPasswordResetEmail(String recipientEmail, String resetCode) {
        // 1. Setup Mail Server Properties for Gmail SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // 2. Create a Session object with an Authenticator
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });

        try {
            // 3. Create a MimeMessage object to compose the email
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Your Password Reset Code for NextStepUni");

            // 4. Set the email's content
            String emailBody = "Hello,\n\n"
                    + "Your password reset code is: " + resetCode + "\n\n"
                    + "This code will expire in 15 minutes.\n\n"
                    + "If you did not request this, please ignore this email.\n\n"
                    + "Thanks,\nThe NextStepUni Team";

            message.setText(emailBody);

            // 5. Send the email
            Transport.send(message);

            System.out.println("Reset email sent successfully to " + recipientEmail);
            return true;

        } catch (MessagingException e) {
            System.err.println("Failed to send email.");
            e.printStackTrace();
            return false;
        }
    }
}
