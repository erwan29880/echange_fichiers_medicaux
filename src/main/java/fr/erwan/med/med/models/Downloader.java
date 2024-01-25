package fr.erwan.med.med.models;

import java.util.Random;
import java.util.logging.Logger;
import java.util.logging.Level;

import fr.erwan.med.med.mel.Mailer;
import fr.erwan.med.med.redis.PasswordRedisManager;

/**
 * classe de l'utilisateur qui a la permission de télécharger un document
 */
public class Downloader extends User{

    private static final Logger LOGGER = Logger.getLogger(Downloader.class.getPackage().getName() );

    /**
     * gestion ux redis
     */
    PasswordRedisManager passwordDownloader = new PasswordRedisManager();


    /**
     * constructeur
     */
    public Downloader(String email, String password) {
        super(email);
        this.password = password;
        this.getDownloader(); // récupérer les infos du fichier, AdminDao
    }

    /**
     * initialisation d'un password si l'utilisateur est enregistré
     * envoi d'un email 
     * enregistrement du password dans redis
     */
    public void initFirstTask() {
        if (this.password == null || this.password.equals("togenerate")) {

            // generate random password
            this.password = this.generatePassword();

            // make fields final to be used in thread
            final String passwordForMail = this.password;
            final String userEmail = this.email;
            // send mail to ux with generated password
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        LOGGER.log(Level.INFO, "thread");
                        new Mailer().sendMessage("credentials", passwordForMail, userEmail);
                        passwordDownloader.setByKey(userEmail, passwordForMail);
                    } catch (RuntimeException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }).start();
        } 
    }

    /**
     * première étape : le password == "togenerate"
     * le mail doit être enregistré
     * @return vrai si les 2 conditions sont réunies
     */
    public boolean isPwdValidForSecondTask() {
        if (this.password.equals("togenerate")) {
            if (this.getDownloaderDao().contains(this.email)) return true;
            return false;
        }
        return false;
    }


    /**
     * comparer credentials : email et password dans redis
     * @return vrai si les email correspondent
     */
    @Override 
    public boolean compare() {
        LOGGER.log(Level.INFO, "comparaison de credentials");
        if (this.email == null || this.getDownloaderDao() == null) return false;
        if (this.password == null || this.password.equals("")) return false;
        if (this.getDownloaderDao().contains(this.email) && passwordDownloader.compare(this.email, this.password)) return true;
        return false;
    }


    /**
     * génération d'un mot de passe de 36 caractères avec des caractères ascii aléatoires
     * @return
     */
    private String generatePassword() {
        LOGGER.log(Level.INFO, "generation password");
        Random random = new Random();
       
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <36 ; i++) {
            sb.append((char)random.nextInt(33, 125)); // éviter les caractères ascii espaces, tabs etc
        }
        return sb.toString(); 
    }

    @Override 
    public String toString() {
        return "no way to print credentials";
    }
}
