package barcodeApp.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.*;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class BarcodeCreatorService {
    {
        exetutionNumber = 0;
    }

    private static final String FILE_DESTINATION = "results/";
    private static int exetutionNumber;

    private Barcode getBarcodeType(String barcodeTypeFromForm){
        switch(barcodeTypeFromForm){
            case "Barcode128":
                return new Barcode128();
            case "Barcode39":
                return new Barcode39();
            case "BarcodeCodabar":
                return new BarcodeCodabar();
            case "BarcodeEAN":
                return new BarcodeEAN();
            case "BarcodeInter25":
                return new BarcodeInter25();
            case "BarcodePostnet":
                return new BarcodePostnet();
        }

        return null;
    }

    private List<java.awt.Image> createImageBarcodeList(String barcodeTypeFromForm, String... inputFromForm){
        List<java.awt.Image> barcodeImageList = new LinkedList<>();
        Barcode barcodeType = getBarcodeType(barcodeTypeFromForm);
        if (barcodeType != null) {
            for (String s : inputFromForm) {
                barcodeType.setCode(s);
                barcodeImageList.add(barcodeType.createAwtImage(Color.BLACK, Color.WHITE));
            }
        }

        return barcodeImageList;
    }

    private Document createPdfFile(List<java.awt.Image> barcodeImageList, String filePath) throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();
        document.add(new Paragraph("Hello World!"));
        document.close();
        return document;
    }

    private File createFile(String filePath){
        File outputFile = new File(filePath);
        outputFile.getParentFile().mkdirs();
        return outputFile;
    }

    public void receiveDataFromFormAndReturnPdfFile(String barcodeTypeFromForm, String... inputFromForm){
        List<java.awt.Image> barcodeImageList = createImageBarcodeList(barcodeTypeFromForm, inputFromForm);
        File outputFile;

        try {
            String filePath = FILE_DESTINATION + "file" + ++exetutionNumber + ".pdf";
            outputFile = createFile(filePath);
            createPdfFile(barcodeImageList, filePath);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
