package barcodeApp.service;

import barcodeApp.validator.BarcodeValidator;
import com.itextpdf.text.*;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.List;

@Service
public class BarcodeCreatorService {
    @Autowired
    BarcodeValidator barcodeValidator;

    static {
        exetutionNumber = 0;
    }

    private static final String FILE_DESTINATION = "results/";
    private static int exetutionNumber;
    private PdfWriter pdfWriter;
    private Map<String, String> barcodeValidatorMap;

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
            default:
                return null; // QR nie dziedziczy po Barode
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
                if (barcodeValidator.validateBarcode(s, barcodeType)) {
                    barcodeQRCode = new BarcodeQRCode(s, 170, 170, new HashMap<>());
                    barcodeImageList.add(barcodeQRCode.getImage());
                } else {
                    barcodeValidatorMap.put(barcodeValidator.getBarcode(), barcodeValidator.getErrorMessage());
                    barcodeImageList.add(null); // nastapil blad
                }
            }
        } else {
            for (String s : inputFromForm) {
                if (barcodeValidator.validateBarcode(s, barcodeType)) {
                    barcodeType.setCode(s);
                    Image image = barcodeType.createImageWithBarcode(pdfContentByte, null, null);
                    image.scalePercent(300);
                    barcodeImageList.add(image);
                } else {
                    barcodeValidatorMap.put(barcodeValidator.getBarcode(), barcodeValidator.getErrorMessage());
                    barcodeImageList.add(null); // nastapil blad
                }
            }
        }

        return barcodeImageList;
    }

    private InputStream createPdfFile(String barcodeTypeFromForm, String[] inputFromForm) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4, 100, 100, 36, 36);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        pdfWriter = PdfWriter.getInstance(document, out);
        document.open();
        try {
            List<Image> barcodeImageList = createImageBarcodeList(barcodeTypeFromForm, inputFromForm);
//            document.add(new Paragraph("Results for Barcode" + barcodeTypeFromForm));
            int j = 0; // iterator ilosci elementow na stronie
            for (Image b : barcodeImageList) {
                if (b != null) {
                    document.add(b);
                    document.add(new Paragraph("\n\n\n\n"));

                    if (++j % 4 == 0) {
                        document.newPage();
                        j = 0;
                    }
                }
            }
            if (!barcodeValidatorMap.isEmpty()) {
                document.newPage();
                document.add(new Paragraph("Errors:"));
                for (Map.Entry<String, String> e : barcodeValidatorMap.entrySet()){
                    document.add(new Paragraph(e.getKey() + " : " + e.getValue()));
                }
            }

            document.close();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (BadElementException e) {
            document.close();
            e.printStackTrace();
            return null;
        }
    }

    private void clear() {
        barcodeValidatorMap = null;
    }

    private InputStream performToPdfFile(String barcodeTypeFromForm, String[] inputFromForm) {
        InputStream inputStream = null;

        try {
            barcodeValidatorMap = new LinkedHashMap<>();
            inputStream = createPdfFile(barcodeTypeFromForm, inputFromForm);
            clear();    // barcodeList !
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return inputStream;
        }
    }

    public InputStream receiveDataFromFormAndReturnPdfFile(String barcodeTypeFromForm, String inputFromForm) {
        InputStream inputStream;

//        inputFromForm = inputFromForm.replace(" ", "");
        String[] inputArray = inputFromForm.split(",");
        inputStream = performToPdfFile(barcodeTypeFromForm, inputArray);

        return inputStream;
    }
}