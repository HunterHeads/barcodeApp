package barcodeApp;

import com.itextpdf.text.*;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class PdfCreator {
    {
        exetutionNumber = 0;
    }

    private static final String FILE_DESTINATION = "results/";
    private static int exetutionNumber;
    private PdfWriter pdfWriter;

    private Barcode getBarcodeType(String barcodeTypeFromForm){
        switch(barcodeTypeFromForm){
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
        }

        return null;
    }

    private List<Image> createImageBarcodeList(String barcodeTypeFromForm, String... inputFromForm){
        List<Image> barcodeImageList = new LinkedList<>();
        Barcode barcodeType = getBarcodeType(barcodeTypeFromForm);
        PdfContentByte pdfContentByte = pdfWriter.getDirectContent();
        if (barcodeType != null) {
            for (String s : inputFromForm) {
                barcodeType.setCode(s);
                barcodeImageList.add(barcodeType.createImageWithBarcode(pdfContentByte,null,null));
            }
        }

        return barcodeImageList;
    }

    private Document createPdfFile(String barcodeTypeFromForm, String filePath, String... inputFromForm) throws DocumentException, IOException {
        Document document = new Document();
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();
        List<Image> barcodeImageList = createImageBarcodeList(barcodeTypeFromForm, inputFromForm);

        document.add(new Paragraph("Results for Barcode" + barcodeTypeFromForm));
        for (Image b : barcodeImageList){
            document.add(b);
            document.add(new Paragraph("\n"));
        }

        document.close();
        return document;
    }

    private File createFile(String filePath){
        File outputFile = new File(filePath);
        outputFile.getParentFile().mkdirs();
        return outputFile;
    }

    public InputStream receiveDataFromFormAndReturnPdfFile(String barcodeTypeFromForm, String... inputFromForm){
        InputStream inputStream = null;

        try {
            String filePath = FILE_DESTINATION + "file" + ++exetutionNumber + ".pdf";
            File outputFile = createFile(filePath);
            createPdfFile(barcodeTypeFromForm, filePath, inputFromForm);
            inputStream = new FileInputStream(outputFile);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            return inputStream;
        }
    }
}
