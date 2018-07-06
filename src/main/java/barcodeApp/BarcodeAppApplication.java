package barcodeApp;

//import barcodeApp.service.BarcodeCreatorService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BarcodeAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BarcodeAppApplication.class, args);

//		BarcodeCreatorService barcodeCreatorService = new BarcodeCreatorService();
		// zwraca void - powinien stworzyć sie folder results a w nim wynik
//		barcodeCreatorService.receiveDataFromFormAndReturnPdfFile("Barcode128", "1234567890128");
	}
}
