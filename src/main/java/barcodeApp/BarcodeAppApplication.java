package barcodeApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BarcodeAppApplication {

    public static void main(String[] args) {
//		SpringApplication.run(BarcodeAppApplication.class, args);

        PdfCreator pdfCreator = new PdfCreator();
        // InputStream
        pdfCreator.receiveDataFromFormAndReturnPdfFile("128", "1234567890128,9876543210128");
        pdfCreator.receiveDataFromFormAndReturnPdfFile("39", "1234567890039,9876543210039");
        pdfCreator.receiveDataFromFormAndReturnPdfFile("Codabar", "1234567890000,9876543210000");
        pdfCreator.receiveDataFromFormAndReturnPdfFile("EAN", "1234567890001,9876543210001");
        pdfCreator.receiveDataFromFormAndReturnPdfFile("Inter25", "1234567890002,9876543210002");
        pdfCreator.receiveDataFromFormAndReturnPdfFile("Postnet", "1234567890003,9876543210003");
        pdfCreator.receiveDataFromFormAndReturnPdfFile("QR", "1234567890004,9876543210004");
    }
}
