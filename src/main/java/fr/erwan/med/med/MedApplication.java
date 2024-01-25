package fr.erwan.med.med;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import fr.erwan.med.med.file.FileRoutine;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.LogManager;


@SpringBootApplication
public class MedApplication {

	private static final LogManager logManager = LogManager.getLogManager();

	static{
        try {
            logManager.readConfiguration(new FileInputStream(System.getProperty("user.dir") + System.getProperty("file.separator") + "jul.properties"));
        } catch ( IOException exception ) {
			System.out.println("probleme log");
		}
    }

	public static void main(String[] args) {
		SpringApplication.run(MedApplication.class, args);
		
		// thread de suppression de fichiers du dossier upload/download
		new FileRoutine();
	}
}
