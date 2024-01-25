package fr.erwan.med.med.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.logging.Logger;
import java.util.logging.Level;

import fr.erwan.med.med.Config;
import fr.erwan.med.med.file.FileFinder;
import fr.erwan.med.med.models.Admin;
import fr.erwan.med.med.models.Downloader;

/**
 * controlleur pour les pages thymleaf
 */
@Controller
public class Controlleur {

    private static final Logger LOGGER = Logger.getLogger(Controlleur.class.getPackage().getName() );


    /**
     * instance de la classe des constantes
     */
    private Config config = Config.getInstance();

    /**
     * dossier de téléchargement
     */
    private String FOLDER_PATH = config.getFolderDirectory();

    /**
     * user downloader autorisé ou non
     */
    private boolean isSecured = false;

    /**
     * page d'index
     * @return la page d'index
     */
    @GetMapping("/")
    public String serveIndex() {
        LOGGER.log(Level.INFO, "page index");
        return "index";
    }

    /**
     * page sécurisée pour télécharger le document : se connecter
     * @return la page sécurisée
     */
    @GetMapping("/secured")
    public String serveSecure(Model model) {
        LOGGER.log(Level.INFO, "page secured, affichage du formulaire sans mot de passe");

        // reset user authorized
        if (isSecured) isSecured = !isSecured;

        model.addAttribute("message", 0); // affichage form connection sans pwd
        return "secure";
    }

    /**
     * page sécurisée pour télécharger le document : vérification de l'authentification
     * @return la page sécurisée
     */
    @PostMapping("/secured")
    public String serveSecurePost(@ModelAttribute Downloader loader, Model model) {

        if (loader.isPwdValidForSecondTask()) {
            LOGGER.log(Level.INFO, "page secured, POST, premier formulaire");
            loader.initFirstTask();
            model.addAttribute("message", 1); // afficher le formaulaire avec email et mot de passe
            return "secure";
        }

        boolean checkIfUserIsInDb = loader.compare();
        if (checkIfUserIsInDb) {
            FileFinder fileFineder = new FileFinder();
            if (fileFineder.hasFiles()) {
                LOGGER.log(Level.INFO, "page secured, credentials ok");
                model.addAttribute("message", 4); // credentials ok
                isSecured = true;
            } else {
                LOGGER.log(Level.INFO, "page secured, message erreur");
                model.addAttribute("message", 3); // error
                model.addAttribute("erreur", 0);    
            }
        } else {
            LOGGER.log(Level.INFO, "page secured, message erreur");
            model.addAttribute("message", 3); // error
            model.addAttribute("erreur", 1);
        }
        return "secure";
    }


    /**
     * le lien de téléchargement du fichier
     * @return le fichier à télécharger en download
     */
    @GetMapping("/file")
    public ResponseEntity<Resource> getFile() {
        LOGGER.log(Level.INFO, "telechargement d'un fichier");

        // si pas d'auhentification, téléchargement impossible
        if (!isSecured) return null;

        try {
            Resource file = new FileFinder().getLastRecord();
            isSecured = false;
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
        } catch (Exception e) { // malFormedUrl, noSuchDocument
            return null;
        }
  }

    /**
     * la page d'administration pour déposer un document
     * @return la page d'administration
     */
    @GetMapping("/admin")
    public String serveAdmin(Model model) {
        LOGGER.log(Level.INFO, "acces page admin");
        model.addAttribute("message", 1); // affichage form connection
        return "admin";
    }

    /**
     * page admin, vérification de l'authentification
     * @param admin les infos utilisateur
     * @param model les données à renvoyer au front
     * @return la page admin
     */
    @PostMapping("/admin")
    public String checkAdmin(@ModelAttribute Admin admin, Model model) {
        LOGGER.log(Level.INFO, "page admin, POST");
        boolean checkIfUserIsInDb = admin.compare();
        if (checkIfUserIsInDb) {
            model.addAttribute("message", 2);  // affichage lien téléchargement
        } else {
            model.addAttribute("message", 1); // affichage form connection
        }
        return "admin";
    }

    /**
     * enregistrement d'un fichier provenant de l'utilisateur
     * @param file le fichier provenant de l'utilisateur
     * @param model les données à renvoyer au front
     * @return la page admin
     */
    @PostMapping("/filepost")
    public String filePost(@RequestParam("file") MultipartFile file, Model model) {
        LOGGER.log(Level.INFO, "reception d'un fichier");

       // si fichier invalide 
       if (file.isEmpty()) {
        model.addAttribute("message", 3); // erreur
        return "admin";
       }

       // suffixe du fichier : timestamp_
       Date date = new Date();
       String d = String.valueOf(date.getTime());
       StringBuilder sb = new StringBuilder(FOLDER_PATH)
                    .append(System.getProperty("file.separator"))
                    .append(d)
                    .append("_");
                  
        // enregistrement du fichier sur l'espace de stockage
        try {            
            byte[] bytes = file.getBytes();
            Path path = Paths.get(sb.append(file.getOriginalFilename()).toString());
            Files.write(path, bytes);
            model.addAttribute("message", 4); // success
            return "admin";
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "erreur reception fichier");
            model.addAttribute("message", 3); // error
            return "admin";
        }
    }


    /**
     * supprimer les fichiers du serveur
     * @return une valeur par défaut
     */
    @DeleteMapping("delete")
    public ResponseEntity<String> deleteAllFIles() {
        LOGGER.log(Level.INFO, "delete files");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000*20); // 20 secondes
                } catch (InterruptedException e) {
                    LOGGER.log(Level.WARNING, "thread suppression après telechargement " + e.getMessage());
                }
                new FileFinder().deleteAllFiles();
            }
        });
        thread.start();
        return ResponseEntity.ok().body("deleted");
    }
}
