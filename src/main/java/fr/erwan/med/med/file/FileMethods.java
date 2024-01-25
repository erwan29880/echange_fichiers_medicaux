package fr.erwan.med.med.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import java.util.logging.Logger;
import java.util.logging.Level;

import fr.erwan.med.med.Config;

/**
 * classe parente de FileFinder et FileRoutine
 */
public abstract class FileMethods {

    private static final Logger LOGGER = Logger.getLogger(FileMethods.class.getPackage().getName() );


    /**
     * instance du Singleton des constantes
     */
    private Config config = Config.getInstance();

    /**
     * dossier upload/download
     */
    private String FOLDER_PATH = config.getFolderDirectory();
    
    /**
     * la liste des fichiers du dossier upload/download
     */
    private List<File> list = new ArrayList<>();

    /**
     * dictionnaire timestamp/nom du fichier 
     * les fichiers téléchargés sont enregistrés sous la forme timestamp_nom_du_fichier.extension
     */
    private TreeMap<Long, File> map = new TreeMap<>();
    
    /**
     * constructeur
     */
    public FileMethods() {
        this.getFiles();
        this.getRecordDate();
    }

    /**
     * parcourir le dossier pour récupérer les fichiers
     */
    protected void getFiles() throws RuntimeException {
        clearList();
        File f = new File(FOLDER_PATH);

        // lève une exception dans le controlleur si le dossier downloads n'existe pas
        if (!f.exists()) {
            LOGGER.log(Level.SEVERE, "le dossier de download n'existe pas");
            throw new RuntimeException("Folder doesn't exists !");
        }

        // parcourir le dossier
        for (File file : f.listFiles()) {

            String path = file.getAbsolutePath();

            // si il n'y pas d'extension
            if (path.indexOf(".") == -1) continue;
            
            // récupérer l'extension
            String extension = path.substring(path.length() -3);

            // Enum Extensions : auhtorized extensions
            for (Extensions e : Extensions.values()) {
                if (extension.equals(e.getExtension())) {
                    list.add(file);
                    break;
                }
            }
        }
    }

    /**
     * clear list
     */
    protected void clearList() {
        if (!this.list.isEmpty()) {
            this.list.clear();
        }
    }

    protected void clearMap() {
        if (!this.map.isEmpty()) {
            this.map.clear();
        }
    }

    /**
     * fill map with time (files are recorded with time_filename.extension) and the file
     */
    protected void getRecordDate() {
        clearMap();
        if (this.list.isEmpty()) return;

        for (File f: this.list) {

            // avoir le dernier bout du fichier
            String filePath = f.getPath().replace(f.getParent(), "");

            if (filePath.equals("")) continue;

            String[] filePathSplitted = filePath.split("_"); // fichier au format timestamp_nom.extension
            if (filePathSplitted.length <= 1) continue;

            try {
                long dateFromPath = Long.parseLong(filePathSplitted[0].substring(1)); // \ au début de la chaine
                this.map.put(dateFromPath, f);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "probleme de timestamp au debut du nom de fichier");
                continue;
            }
        }
    }

    /**
     * getter map
     * @return le treeMap
     */
    protected TreeMap<Long, File> getMap() {
        return this.map;
    }

    /**
     * getter liste
     * @return la liste de fichiers
     */
    protected List<File> getFilesList() {
        return this.list;
    }
}
