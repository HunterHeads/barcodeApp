package barcodeApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BarcodeAppApplication {

    public static void main(String[] args) {
//		SpringApplication.run(BarcodeAppApplication.class, args);

        PdfCreator pdfCreator = new PdfCreator();
        // InputStream
        pdfCreator.receiveDataFromFormAndReturnPdfFile("128", "1234567890128     ,   9876543210128,1212,12121212,1212112");
  /*      pdfCreator.receiveDataFromFormAndReturnPdfFile("39", "1234567890039,21212,121212,9876543210039");
        pdfCreator.receiveDataFromFormAndReturnPdfFile("Codabar", "1234567890000,12121313121,12121212,9876543210000");
        pdfCreator.receiveDataFromFormAndReturnPdfFile("EAN", "1234567890001,9876543210001,121212,121213121");
        pdfCreator.receiveDataFromFormAndReturnPdfFile("Inter25", "1234567890002,9876543210002,12113412,12141312");
        pdfCreator.receiveDataFromFormAndReturnPdfFile("Postnet", "1234567890003,9876543210003,414131,141312");*/
        pdfCreator.receiveDataFromFormAndReturnPdfFile("QR", "1212,1212121,1212,1234567890004,9876543210004,121212,313112");
    }
}
