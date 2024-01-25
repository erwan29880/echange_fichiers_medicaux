package fr.erwan.med.med.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

import fr.erwan.med.med.Config;

/**
 * récupération des informations utilisateurs dans des fichiers "bdd" .txt
 */
public abstract class AdminDao {

    private static final Logger LOGGER = Logger.getLogger(AdminDao.class.getPackage().getName() );

    /**
     * récupération des constantes
     */
    private Config config = Config.getInstance();

    /**
     * chemin d'accès du fichier admin.txt
     */
    private String ADMIN_PATH = config.getAdminPath();

    /**
     * chemin d'accès du fichier downloader.txt
     */
    private String DOWNLOADER_PATH = config.getDOwnloaderPath();

    /**
     * emails des administrateurs
     */
    private List<String> emailDao = new ArrayList<>();

    /**
     * passwords des administrateurs
     */
    private List<String> passwordDao = new ArrayList<>();

    /**
     * emails de ceux qui peuvent télécharger un fichier sécurisé
     */
    private List<String> downloaderDao = new ArrayList<>();


    /**
     * constructeur
     */
    protected AdminDao() {
    }


    /**
     * simulacre de bdd
     * récupérer les identifiants admin en dur
     */
    protected void getAdmin() {
        try (BufferedReader b = new BufferedReader(new FileReader(new File(ADMIN_PATH)))){
            emailDao = Arrays.asList(b.readLine().split("=")[1].split(","));
            passwordDao = Arrays.asList(b.readLine().split("=")[1].split(","));
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "le fichier admin.txt est introuvable");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "erreur d'ouverture du fichier admin.txt");
        }
    }

    /**
     * simulacre de bdd
     * récupérer l'email du downloaderUser en dur
     */
    protected void getDownloader() {
        try (BufferedReader b = new BufferedReader(new FileReader(new File(DOWNLOADER_PATH)))){
            downloaderDao = Arrays.asList(b.readLine().split("=")[1].split(","));
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "le fichier downloader.txt est introuvable");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "erreur d'ouverture du fichier downloader.txt");
        }
    }

    /**
     * getter email
     * @return l'email admin
     */
    protected List<String> getEmailDao() {
        return this.emailDao;
    }

    /**
     * getter password
     * @return le password admin
     */
    protected List<String> getPasswordDao() {
        return this.passwordDao;
    }


    /**
     * getter downloadUser
     * @return l'email downloader
     */
    protected List<String> getDownloaderDao() {
        return this.downloaderDao;
    }

}
