package fr.erwan.med.med.file;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.NoSuchElementException;

import java.util.logging.Logger;
import java.util.logging.Level;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

/**
 * classe pour trouver le dernier fichier uploadé par l'administrateur
 */
public class FileFinder extends FileMethods{
    
    private static final Logger LOGGER = Logger.getLogger(FileMethods.class.getPackage().getName() );


    /**
     * constructeur
     */
    public FileFinder() {
        super();
    }

    /**
     * trouve le dernier enregistrement du dossier downloads
     * @return le dernier enregistrement
     * @throws NoSuchElementException si aucun fichier n'est présent dans le dossier ou si le nom de fichier n'a pas pour début un timestamp, ou l'extension n'est pas autorisée
     * @throws MalformedURLException si problème avec la transformation File to Path
     */
    public Resource getLastRecord() throws NoSuchElementException, MalformedURLException {
        File f = this.getMap().get(this.getMap().lastKey());
        if (f == null) {
            LOGGER.log(Level.INFO, "pas de fichier trouvé");
            throw new NoSuchElementException("aucun élément ne correspond");
        }
        Path path = f.toPath();
        LOGGER.log(Level.INFO, "formation de l'url du fichier");
        Resource file  = new UrlResource(path.toUri());
        return file;
    }

    /**
     * supprimer tous les fichiers
     */
    public void deleteAllFiles() {
        LOGGER.log(Level.INFO, "suppression de tous les fichiers");
        for (File f: this.getFilesList()) {
            try {
                f.delete();
            } catch (Exception e) {}
        }
        this.clearList();
        this.clearMap();
    }

    /**
     * vérifier si il y a des fichiers
     * @return vrai si il y a des fichiers
     */
    public boolean hasFiles() {
        return this.getFilesList().isEmpty() ? false : true;
    }
}
