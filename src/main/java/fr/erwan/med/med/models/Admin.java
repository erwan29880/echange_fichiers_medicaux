package fr.erwan.med.med.models;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * classe de l'administrateur
 */
public class Admin extends User{

    private static final Logger LOGGER = Logger.getLogger(Admin.class.getPackage().getName() );

    
    /**
     * contrsucteur 
     * @param email l'email
     * @param password le mot de passe
     */
    public Admin(String email, String password) {
        super(email);
        this.setPassword(password);
        this.getAdmin();
    }

    /**
     * setter password pour le constructeur
     * @param password le password venant du controlleur
     */
    private void setPassword(String password) {
        //uuid
        if (!password.matches("^[0-9A-Za-z-]{36}$")) this.password = "";
        this.password = password;
    }

    /**
     * comparer les données enregistrées avec les données reçues
     * @return vrai si les données sont identiques
     */
    @Override
    public boolean compare() {
        LOGGER.log(Level.INFO, "comparaison de credentials");

        if (this.getEmailDao() == null || this.getPasswordDao() == null)  return false;
        if (this.email == null || this.password.equals("") || this.password == null)  return false;
        if (this.getEmailDao().contains(this.email) && 
            this.getPasswordDao().contains(this.password) && 
            this.getEmailDao().indexOf(this.email) == this.getPasswordDao().indexOf(this.password)) {
                return true;
            }
        return false;
    }

    /**
     * to string effacé
     */
    @Override 
    public String toString() {
        return "not allowed to see credentials";
    }
}
