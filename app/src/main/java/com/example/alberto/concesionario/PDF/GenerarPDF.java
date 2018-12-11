package com.example.alberto.concesionario.PDF;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.example.alberto.concesionario.Activities.Presupuestos.VerPresupuestoCocheNuevoActivity;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;

public class GenerarPDF {

    private Context context;
    private File pdfFile;
    private Document document;
    private PdfWriter pdfWriter;
    private Paragraph paragraph;

    public GenerarPDF(Context context) {
        this.context = context;
    }

    private void createFile(){
        File carpeta = new File(Environment.getExternalStorageDirectory().toString(), "PDF");
        if (!carpeta.exists()) {
            carpeta.mkdir();
        }
        this.pdfFile = new File(carpeta, "Prueba.pdf");
        Log.i("ASDF","CREATE FILE");
    }

    public void openDocument(){
        this.createFile();
        try {
            this.document = new Document(PageSize.A4);
            this.pdfWriter = PdfWriter.getInstance(this.document, new FileOutputStream(this.pdfFile));
            this.document.open();
            this.document.addTitle("holi");
            paragraph = new Paragraph("Esto se genera solo");
            document.add(paragraph);
            this.document.close();
        }catch (Exception e){
            Log.e("openDocument", e.toString());
        }
    }

    public void closeDocument(){
        this.document.close();
    }

    public String viewPDF(){
        return this.pdfFile.getAbsolutePath();
    }
}
