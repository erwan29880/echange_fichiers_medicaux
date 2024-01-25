package fr.erwan.med.med.mel;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.logging.Logger;
import java.util.logging.Level;

import fr.erwan.med.med.Config;

/**
 * classe d'envoi de mail
 */
public class Mailer {

    private static final Logger LOGGER = Logger.getLogger(Mailer.class.getPackage().getName() );


    /**
     * instance de la classe des constantes
     */
    private Config config = Config.getInstance();

    /**
     * adresse gmail du compte de messagerie
     */
    private final String GMAIL_ADDRESS;

    /**
     * mot de passe tierce partie du compte gmail
     */
    private final String GMAIL_PWD;

    /**
     * constructeur
     */
    public Mailer() throws RuntimeException {
        String[] credentials = config.getGmailCredentials(this);
        GMAIL_ADDRESS = credentials[0];
        GMAIL_PWD = credentials[1];
    }

    /**
     * envoyer un email
     * @param subject sujet
     * @param text le corps
     * @param destinataire le destinataire
     */
    public void sendMessage(String subject, String text, String destinataire) {
        LOGGER.log(Level.INFO, "envoi mail " + destinataire);

        Properties properties = new Properties();
    
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.starttls.required", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(GMAIL_ADDRESS, GMAIL_PWD);
                    }
                });

        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(GMAIL_ADDRESS));
            message.setText("Bonjour, \nVotre mot de passe provisoire est : \n"+text);
            message.setSubject(subject);
            message.addRecipients(Message.RecipientType.TO, destinataire);
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(destinataire)
            );
            Transport.send(message);
        } catch (MessagingException e) {
            LOGGER.log(Level.WARNING, "envoi mail " + destinataire);

        }
    }
}
