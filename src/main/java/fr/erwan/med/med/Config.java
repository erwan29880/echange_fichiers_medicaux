package fr.erwan.med.med;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.logging.Logger;
import java.util.logging.Level;

import fr.erwan.med.med.mel.Mailer;


/**
 * Singleton pour les constantes du programme
 */
public class Config {

    private static final Logger LOGGER = Logger.getLogger(Config.class.getPackage().getName() );

    /**
     * instance unique du Singleton
     */
    private static Config instance;

    /**
     * dossier upload/download
     */
    private static final String FOLDER_PATH;

    /**
     * db : admin autorisés, fichier texte : email=email1,email2...\npassword=pwd1,pwd2...
     */
    private static String ADMIN_PATH = System.getProperty("user.dir") + System.getProperty("file.separator") + "admin.txt";

    /**
     * db : utilisateurs autorisés pour le téléchargement du fichier, fichier texte : email:email1,email2,...
     */
    private static String DOWNLOADER_PATH = System.getProperty("user.dir") + System.getProperty("file.separator") + "downloader.txt";

    /**
     * chemin d'accès du dossier upload/download
     */
    static{
        FOLDER_PATH = new StringBuilder()
                .append(System.getProperty("user.dir"))
                .append(System.getProperty("file.separator"))
                .append("downloads")
                .toString();
    }

    /**
     * constructeur privé
     */
    private Config() {
       if (!new File(FOLDER_PATH).exists()) throw new RuntimeException("le folder de download n'existe pas");
       if (!new File(ADMIN_PATH).exists()) throw new RuntimeException("la base de données admin n'existe pas");
       if (!new File(DOWNLOADER_PATH).exists()) throw new RuntimeException("la base de données downloader n'existe pas");

    }

    /**
     * instance du Singleton
     * @return l'instance unique de la classe
     */
    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    /**
     * getter dossier upload/download
     * @return le chemin d'accès
     */
    public String getFolderDirectory() {
        return FOLDER_PATH;
    }

    /**
     * getter fichier db admin.txt
     * @return le chemin d'accès du fichier
     */
    public String getAdminPath() {
        return ADMIN_PATH;
    }

    /**
     * getter fichier db downloader.txt
     * @return le chemin d'accès du fichier
     */
    public String getDOwnloaderPath() {
        return DOWNLOADER_PATH;
    }

    /**
     * récupérer les identifiants gmail du fichier mail.txt
     * @param o doit être une instance de la classe Mailer
     * @return un tableau avec les identifiants
     * @throws RuntimeException en cas d'IOerror, ou si la classe n'est pas une instance de Mailer
     */
    public String[] getGmailCredentials(Object o) throws RuntimeException{
        if (o instanceof Mailer) {
            String gmailUser = null;
            String gmailPassword = null;

            try (BufferedReader b = new BufferedReader(new FileReader(new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "mail.txt")))){
               gmailUser = b.readLine().split("=")[1];
               gmailPassword = b.readLine().split("=")[1];
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "impossible d'obtenir le fichier de configuration gmail.");
                throw new RuntimeException("impossible d'obtenir le fichier de configuration gmail.");
            }

            if (gmailUser == null || gmailPassword == null) {
                LOGGER.log(Level.SEVERE, "impossible de trouver les identifiants gmail");
                throw new RuntimeException("impossible de trouver les identifiants gmail");
            }

            return new String[]{gmailUser, gmailPassword};
        } else {
            LOGGER.log(Level.SEVERE, "tentative d'accès aux credentials gmail d'une classe non autorisée");
            throw new RuntimeException("cette classe n'est pas autorisée pour l'obtention des credentials");
        }
    }
}
