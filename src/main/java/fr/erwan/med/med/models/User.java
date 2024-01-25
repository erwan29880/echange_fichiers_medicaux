package fr.erwan.med.med.models;
import fr.erwan.med.med.dao.AdminDao;

/**
 * superclasse abstraite pour admin et downloader
 * étend adminDao pour l'implémentation en dur d'une bdd à partir de deux fichiers
 */
public abstract class User extends AdminDao {


    /**
     * email du downloader ou de l'admin
     */
    protected String email;

    /**
     * email du downloader ou de l'admin
     */
    protected String password;

    /**
     * constructeur par défaut
     */
    public User() {}
    
    /**
     * constructeur avec mail
     * @param email le mail du downloader ou de l'admin
     */
    public User(String email) {
        this.email = email;
    }

    /**
     * getter email
     * @return l'email
     */
    protected String getEmail() {
        return this.email;
    }

    /**
     * getter password
     * @return password
     */
    protected String getPassword() {
        return this.password;
    }

    /**
     * compare utilsateurs enregistrés et autentification utilisateur du front
     * @return vrai si l'utilisateur est enregistré
     */
    public abstract boolean compare();
}
