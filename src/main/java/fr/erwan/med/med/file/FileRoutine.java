package fr.erwan.med.med.file;

import java.io.File;
import java.util.Map;
import java.util.Date;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Thread de suppression de fichiers du dossier upload/download
 */
public class FileRoutine extends FileMethods implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(FileRoutine.class.getPackage().getName() );


    /**
     * seuil temporel pour supprimer les fichiers
     */
    private final long SEUIL = 24*60*60; // 1 jour

    /**
     * délai en secondes pour réactiver le thread
     */
    private final long DELAY = 3*60*60; // secondes
    

    /**
     * constructeur
     */
    public FileRoutine() {
        super();
        Thread thread = new Thread(this, "routines for files");
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * méthode principale
     */
    @Override 
    public void run() {
        System.out.println("thread started");
        while(true) {
            this.clearList(); // réinitialiser la liste des fichiers
            this.getFiles();  // récupérer tous les fichiers
            try {
                Thread.sleep(DELAY*1000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
            deleteFilesToOld(); // supprimer les fichiers trop anciens
            LOGGER.log(Level.INFO, "Thread de nettoyage de fichiers");

        }  
    }

    /**
     * suprimer les fichiers en fonction de la date
     */
    private void deleteFilesToOld() {
        for (Map.Entry<Long, File> es : this.getMap().entrySet()) {
            long now = new Date().getTime();
            if (now - es.getKey() >= SEUIL) {
                try {
                    es.getValue().delete();
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "probleme de suppression de fichier");
                    System.out.println(e.getMessage());
                }
            } 
        }
    }    
}
