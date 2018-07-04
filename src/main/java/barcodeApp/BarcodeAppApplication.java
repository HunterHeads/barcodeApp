package barcodeApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class BarcodeAppApplication {

	public static void main(String[] args) {
//		SpringApplication.run(BarcodeAppApplication.class, args);

		PdfCreator pdfCreator = new PdfCreator();
		// zwraca void - powinien stworzyć sie folder results a w nim wynik
		pdfCreator.receiveDataFromFormAndReturnPdfFile("Barcode128", "1234567890128");
	}
}
