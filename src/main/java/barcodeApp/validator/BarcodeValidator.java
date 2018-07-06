package barcodeApp.validator;

import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Component;

// funkcje sprawdzajace zgodnosc z danym kodem oraz przechowywanie komunikatow o bledach
@Component
public final class BarcodeValidator {
    private String errorMessage;
    private String barcode;

    public BarcodeValidator() {}

    public BarcodeValidator(String errorMessage, String barcode) {
        this.errorMessage = errorMessage;
        this.barcode = barcode;
    }

    public boolean validateBarcode(String content, Barcode barcode){

        switch(barcode.getCodeType()){
            case 9: //128
                return isBarcode128(content);
            case 12: //CODABAR
                return isBarcodeCodabar(content);
            case 2:// EAN
                return isBarcodeEAN(content);
            case 7: //POSTNET
                return isBarcodePostnet(content);
            case 0: // Inter25 i 39
                if(barcode instanceof Barcode39)
                    return isBarcode39(content);
                else
                    return isBarcodeInter25(content);
            default:
                return isBarcodeQR(content);
        }
    }

    public boolean isBarcode128(String barcode) {
        char[] barcodeCharacters = barcode.toCharArray();
        for (char c : barcodeCharacters) {
            if (c > 255) {
                errorMessage = "128";
                return false;
            }
        }

        return true;
    }

    public boolean isBarcode39(String barcode) {
        char[] barcodeCharacters = barcode.toCharArray();
        for (char c : barcodeCharacters) {
            if (c != '-' && c != '$' && c != '*' && c != '/' && c != '.' && c != '+' && c != '%' && c != ' ' &&
                    (c < 'A' || c > 'Z') && (c < '0' || c > '9')) {
                errorMessage = "Invalid characters. Allowed are - $ * / . + % space or A-Z or 0-9";
                return false;
            }
        }

        return true;
    }

    public boolean isBarcodeCodabar(String barcode) {
        char[] barcodeCharacters = barcode.toCharArray();
        for (char c : barcodeCharacters) {
            if (c != '-' && c != '$' && c != '*' && c != '/' && c != '.' && c != '+' && c != '%' && c != ' ' &&
                    (c < '0' || c > '9')) {
                errorMessage = "Invalid characters. Allowed are - $ * / . + % space or 0-9";
                return false;
            }
        }

        if (barcodeCharacters[0] != 'A' && barcodeCharacters[0] != 'B' && barcodeCharacters[0] != 'C' && barcodeCharacters[0] != 'D') {
            errorMessage = "Invalid start character. Codabar must have one of 'ABCD' as start character";
            return false;
        }


        if (barcodeCharacters[barcodeCharacters.length - 1] != 'A' || barcodeCharacters[barcodeCharacters.length - 1] != 'B' ||
                barcodeCharacters[barcodeCharacters.length - 1] != 'C' || barcodeCharacters[barcodeCharacters.length - 1] != 'D') {
            errorMessage = "Invalid stop character. Codabar must have one of 'ABCD' as stop character";
            return false;
        }

        return true;
    }

    public boolean isBarcodeEAN(String barcode) {
        if (barcode.length() != 7 && barcode.length() != 13) {
            errorMessage = "Invalid barcode length. EAN must have 7 or 13 characters";
            return false;
        }

        char[] barcodeCharacters = barcode.toCharArray();
        for (char c : barcodeCharacters) {
            if (c < '0' || c > '9') {
                errorMessage = "Invalid characters. Allowed are 0-9";
                return false;
            }
        }

        return true;
    }

    public boolean isBarcodeInter25(String barcode) {
        if (barcode.length() % 2 == 1) {
            errorMessage = "Invalid barcode length. Inter25 must have even number of characters";
            return false;
        }

        return true;
    }

    public boolean isBarcodePostnet(String barcode) {
        if (barcode.length() < 5 || barcode.length() > 13){
            errorMessage = "Invalid barcode lenght. POSTNET must have 5-13 characters";
            return false;
        }

        char[] barcodeCharacters = barcode.toCharArray();
        for (char c : barcodeCharacters){
            if (c < '0' || c > '9'){
                errorMessage = "Invalid characters. Allowed are 0-9";
                return false;
            }
        }

        return true;
    }

    public boolean isBarcodeQR(String barcode) { // ?
        errorMessage = "QR";
        return true;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getBarcode() {
        return barcode;
    }
}