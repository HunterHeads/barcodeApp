package barcodeApp.service;

import barcodeApp.validator.BarcodeValidator;
import com.itextpdf.text.*;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Service
public class BarcodeCreatorService {
    @Autowired
    BarcodeCreatorService barcodeCreatorService;

    static {
        exetutionNumber = 0;
    }

    private static final String FILE_DESTINATION = "results/";
    private static int exetutionNumber;
    private PdfWriter pdfWriter;
    private List<BarcodeValidator> barcodeValidatorList;

    private Barcode getBarcodeType(String barcodeTypeFromForm) {
        switch (barcodeTypeFromForm) {
            case "128":
                return new Barcode128();
            case "39":
                return new Barcode39();
            case "Codabar":
                return new BarcodeCodabar();
            case "EAN":
                return new BarcodeEAN();
            case "Inter25":
                return new BarcodeInter25();
            case "Postnet":
                return new BarcodePostnet();
            case "QR":
                return null; // QR nie dziedziczy po Barode
        }

        return null;
    }

    // obiekt do testow bc - if false => zapisanie tego faktu na liscie wraz z odpowiednim komunikatem bledu
    private boolean isCorrectInputString(Barcode barcode, String input) {
        BarcodeValidator bc = new BarcodeValidator();
        if (barcode instanceof Barcode128) {
            if (bc.isBarcode128(input)) {
                return true;
            } else {
                barcodeValidatorList.add(new BarcodeValidator(bc.getErrorMessage(), input));
                return false;
            }
        }
        if (barcode instanceof Barcode39) {
            if (bc.isBarcode39(input)) {
                return true;
            } else {
                barcodeValidatorList.add(new BarcodeValidator(bc.getErrorMessage(), input));
                return false;
            }
        }
        if (barcode instanceof BarcodeCodabar) {
            if (bc.isBarcodeCodabar(input)) {
                return true;
            } else {
                barcodeValidatorList.add(new BarcodeValidator(bc.getErrorMessage(), input));
                return false;
            }
        }
        if (barcode instanceof BarcodeEAN) {
            if (bc.isBarcodeEAN(input)) {
                return true;
            } else {
                barcodeValidatorList.add(new BarcodeValidator(bc.getErrorMessage(), input));
                return false;
            }
        }
        if (barcode instanceof BarcodeInter25) {
            if (bc.isBarcodeInter25(input)) {
                return true;
            } else {
                barcodeValidatorList.add(new BarcodeValidator(bc.getErrorMessage(), input));
                return false;
            }
        }
        if (barcode instanceof BarcodePostnet) {
            if (bc.isBarcodePostnet(input)) {
                return true;
            } else {
                barcodeValidatorList.add(new BarcodeValidator(bc.getErrorMessage(), input));
                return false;
            }
        }

        return false;
    }

    private boolean isCorrectInputString(String input) {  // QR
        BarcodeValidator bc = new BarcodeValidator();
        if (bc.isBarcodeQR(input)) {
            return true;
        } else {
            barcodeValidatorList.add(new BarcodeValidator(bc.getErrorMessage(), input));
            return false;
        }
    }

    // null => w przypadku gdy String nie jest poprawny
    private List<Image> createImageBarcodeList(String barcodeTypeFromForm, String[] inputFromForm) throws BadElementException {
        List<Image> barcodeImageList = new LinkedList<>();
        Barcode barcodeType = getBarcodeType(barcodeTypeFromForm);
        PdfContentByte pdfContentByte = pdfWriter.getDirectContent();

        if (barcodeType == null) {      // QR
            BarcodeQRCode barcodeQRCode;
            for (String s : inputFromForm) {
                if (isCorrectInputString(s)) {
                    barcodeQRCode = new BarcodeQRCode(s, 200, 200, new HashMap<>());
                    barcodeImageList.add(barcodeQRCode.getImage());
                } else {
                    barcodeImageList.add(null); // nastapil blad
                }
            }
        } else {
            for (String s : inputFromForm) {
                if (isCorrectInputString(barcodeType, s)) {
                    barcodeType.setCode(s);
                    Image image = barcodeType.createImageWithBarcode(pdfContentByte, null, null);
                    image.scalePercent(300);
                    barcodeImageList.add(image);
                } else {
                    barcodeImageList.add(null); // nastapil blad
                }
            }
        }

        return barcodeImageList;
    }

    private Document createPdfFile(String barcodeTypeFromForm, String filePath, String[] inputFromForm) throws DocumentException, IOException {
        Document document = new Document();
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();
        try {
            List<Image> barcodeImageList = createImageBarcodeList(barcodeTypeFromForm, inputFromForm);
            document.add(new Paragraph("Results for Barcode" + barcodeTypeFromForm));
            int i = 0; // iterator barcodeChecker
            int j = 0; // iterator ilosci elementow na stronie
            for (Image b : barcodeImageList) {
                if (b == null) {     // w przypadku bledu wypisywanie czego on dotyczy - bledy przechowywane na liscie
                    document.add(new Paragraph(barcodeValidatorList.get(i).getBarcode() + " : " + barcodeValidatorList.get(i).getErrorMessage()));
                    i++;
                } else {
                    document.add(b);
                    document.add(new Paragraph("\n\n"));
                }

                if (++j % 3 == 0) {
                    document.newPage();
                    j = 0;
                }
            }

            document.close();
            return document;
        } catch (BadElementException e) {
            document.close();
            e.printStackTrace();
            return null;
        }
    }

    private File createFile(String filePath) {
        File outputFile = new File(filePath);
        outputFile.getParentFile().mkdirs();
        return outputFile;
    }

    private void clear() {
        barcodeValidatorList = null;
    }

    private InputStream performToPdfFile(String barcodeTypeFromForm, String[] inputFromForm) {
        InputStream inputStream = null;

        try {
            barcodeValidatorList = new LinkedList<>();
            String filePath = FILE_DESTINATION + "file" + ++exetutionNumber + ".pdf";
            File outputFile = createFile(filePath);
            createPdfFile(barcodeTypeFromForm, filePath, inputFromForm);
            inputStream = new FileInputStream(outputFile);
            clear();    // barcodeList !
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return inputStream;
        }
    }

    public InputStream receiveDataFromFormAndReturnPdfFile(String barcodeTypeFromForm, String inputFromForm) {
        InputStream inputStream;

        inputFromForm = inputFromForm.replace(" ", "");
        String[] inputArray = inputFromForm.split(",");
        inputStream = performToPdfFile(barcodeTypeFromForm, inputArray);

        return inputStream;
    }
}