package barcodeApp;

//import barcodeApp.service.BarcodeCreatorService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class BarcodeAppApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(BarcodeAppApplication.class, args);

	}
}
