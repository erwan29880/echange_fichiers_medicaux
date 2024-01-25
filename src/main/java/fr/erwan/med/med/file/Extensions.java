package fr.erwan.med.med.file;

/**
 * extension autorisées pour l'upload de fichiers
 */
public enum Extensions {
    DOC("doc"),
    ODT("odt");


    /**
     * l'enum sous forme String
     */
    private String extension;

    /**
     * constructeur
     * @param extension
     */
    Extensions(String extension) {
        this.extension = extension;
    }

    /**
     * getter extension
     */
    public String getExtension() {
        return this.extension;
    }
}
