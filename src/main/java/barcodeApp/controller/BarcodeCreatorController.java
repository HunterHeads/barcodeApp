package barcodeApp.controller;

import barcodeApp.service.BarcodeCreatorService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class BarcodeCreatorController {
    private final BarcodeCreatorService barcodeCreatorService;

    @Autowired
    public BarcodeCreatorController(BarcodeCreatorService barcodeCreatorService) {
        this.barcodeCreatorService = barcodeCreatorService;
    }

    @GetMapping(value = {"/", "createBarcode"})
    public String getBarcodeCreator(){
        return "barcodeCreator";
    }

    @GetMapping(value="/submitBarcodeForm")
    public void createBarcode(  @RequestParam(value="input", required=false) String input,
                                @RequestParam(value="barcodeType", required=true) String barcodeType,
                                HttpServletResponse response) throws IOException {
        InputStream inputStream = barcodeCreatorService.receiveDataFromFormAndReturnPdfFile(barcodeType, input);
        IOUtils.copy(inputStream, response.getOutputStream());
        response.flushBuffer();
//        return "barcodeCreator";
    }
}
