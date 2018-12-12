package com.example.alberto.concesionario.PDF;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.example.alberto.concesionario.Activities.Presupuestos.DatosCliente;
import com.example.alberto.concesionario.Activities.Presupuestos.Presupuesto;
import com.example.alberto.concesionario.Activities.Presupuestos.VerPresupuestoCocheNuevoActivity;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class GenerarPDF {

    private Context context;
    private File pdfFile;
    private Document document;
    private PdfWriter pdfWriter;
    private Paragraph paragraph;
    private Font fTitulo = new Font(Font.FontFamily.HELVETICA, 60, Font.BOLD);
    private Font fSubTitulo = new Font(Font.FontFamily.HELVETICA, 40, Font.BOLD);
    private Font fFecha = new Font(Font.FontFamily.HELVETICA, 20, Font.ITALIC);
    private Font fText = new Font(Font.FontFamily.HELVETICA, 20, Font.NORMAL);
    private Font fTextCliente = new Font(Font.FontFamily.HELVETICA, 15, Font.ITALIC);
    private Font fHeadTable = new Font(Font.FontFamily.HELVETICA, 30, Font.BOLD, BaseColor.WHITE);
    private Font fPieTable = new Font(Font.FontFamily.HELVETICA, 25, Font.ITALIC, BaseColor.WHITE);


    public GenerarPDF(Context context) {
        this.context = context;
    }

    /**
     * Crea la carpeta contenedora y el archivo PDF en el que se trabajará
     */
    private void createFile(){
        File carpeta = new File(Environment.getExternalStorageDirectory().toString(), "PDF");
        if (!carpeta.exists()) {
            carpeta.mkdir();
        }
        this.pdfFile = new File(carpeta, "Prueba.pdf");
    }

    /**
     * Abre el documento como escritura para poder modificarlo
     */
    public void openDocument(){
        this.createFile();
        try {
            this.document = new Document(PageSize.A4);
            this.pdfWriter = PdfWriter.getInstance(this.document, new FileOutputStream(this.pdfFile));
            this.document.open();
        }catch (Exception e){
            Log.e("openDocument", e.toString());
        }
    }

    /**
     * Cierra el documento
     */
    public void closeDocument(){
        this.document.close();
    }

    /**
     * Añade los metadatos del PDF
     *
     * @param titulo :String
     * @param tema :String
     * @param autor :String
     */
    public void addMetaData(String titulo, String tema, String autor){
        this.document.addTitle(titulo);
        this.document.addSubject(tema);
        this.document.addAuthor(autor);

    }

    /**
     * Añade una cabecera al PDF
     *
     * @param titulo :String
     * @param subtitulo :String
     */
    public void addTtitulo(String titulo, String subtitulo){
        try {
        this.paragraph = new Paragraph();
        this.addParagraph(new Paragraph(titulo, fTitulo));
        this.addParagraph(new Paragraph(subtitulo, fSubTitulo));
        this.addParagraph(new Paragraph("Generado: " + new Date().toString(), fFecha));
        this.paragraph.setSpacingAfter(30);
        this.document.add(this.paragraph);
        }catch (Exception e){
            Log.e("addTtitulo", e.toString());
        }
    }

    /**
     *
     * @param paragraphHijo
     */
    private void addParagraph(Paragraph paragraphHijo){
        paragraphHijo.setAlignment(Element.ALIGN_CENTER);
        this.paragraph.add(paragraphHijo);
    }

    /**
     *
     * @param paragraphHijo
     */
    private void addParagraphCliente(Paragraph paragraphHijo){
        paragraphHijo.setAlignment(Element.ALIGN_LEFT);
        this.paragraph.add(paragraphHijo);
    }

    /**
     * Añade un parrafo al documento
     *
     * @param texto :String
     */
    public void addParagraph(String texto){
        try {
            this.paragraph = new Paragraph(texto, fText);
            this.paragraph.setSpacingBefore(5);
            this.paragraph.setSpacingAfter(5);
            this.document.add(this.paragraph);
        }catch (Exception e){
            Log.e("addParagraph", e.toString());
        }
    }

    public void addDatosCliente(DatosCliente datosCliente){
        try {
            this.paragraph = new Paragraph();
            this.addParagraphCliente(new Paragraph("Cliente: " + datosCliente.getNombre() + " "
                    + datosCliente.getApellidos(), fTextCliente));
            this.addParagraphCliente(new Paragraph("Telefono: " + datosCliente.getTelefono(), fTextCliente));
            this.addParagraphCliente(new Paragraph("Email: " + datosCliente.getEmail(), fTextCliente));
            this.addParagraphCliente(new Paragraph("Dirección: " + datosCliente.getDireccion(), fTextCliente));
            this.addParagraphCliente(new Paragraph("Población: " + datosCliente.getPoblacion(), fTextCliente));
            this.addParagraphCliente(new Paragraph("Fecha de nacimiento: " + datosCliente.getFechaNacimiento(), fTextCliente));

            this.paragraph.setSpacingBefore(5);
            this.paragraph.setSpacingAfter(5);
            this.document.add(this.paragraph);
        }catch (Exception e){
            Log.e("addDatosCliente", e.toString());
        }
    }

    public void addNombreCliente(String nombre){
        try {
            this.paragraph = new Paragraph("Nombre: " + nombre, fTextCliente);
            this.paragraph.setSpacingBefore(5);
            this.paragraph.setSpacingAfter(5);
            this.document.add(this.paragraph);
        }catch (Exception e){
            Log.e("addParagraph", e.toString());
        }
    }

    /**
     * Crea la tabla de presupuesto
     *
     * @param presupuesto :Presupuesto
     */
    public void crearTablaPresupuesto(Presupuesto presupuesto){
        try {
            this.paragraph = new Paragraph();
            this.paragraph.setFont(fText);
            /* Se crea la tabla */
            PdfPTable pdfPTable = new PdfPTable(presupuesto.numColumnasPresupuesto());
            pdfPTable.setWidthPercentage(100);
            PdfPCell pdfPCell; //La celda donde se escribirá

            /* CABECERA */
            int headIndex = 0;
            while (headIndex < presupuesto.numColumnasPresupuesto()) {
                pdfPCell = new PdfPCell(new Phrase(presupuesto.cabeceraFactura().get(headIndex), fHeadTable));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setPadding(10);
                pdfPCell.setBackgroundColor(new BaseColor(0, 133, 119));
                pdfPTable.addCell(pdfPCell);
                headIndex++;
            }

            /* CUERPO */
            for (int i = 0; i < presupuesto.numTotalExtras(); i++){
                pdfPCell = new PdfPCell(new Phrase(presupuesto.getNombreExtraIndividual(i), fText));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setPadding(10);
                pdfPTable.addCell(pdfPCell);

                pdfPCell = new PdfPCell(new Phrase(presupuesto.getPrecioExtraIndividual(i), fText));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setPadding(10);
                pdfPTable.addCell(pdfPCell);
            }

            /* PIE */
            pdfPCell = new PdfPCell(new Phrase("Total", fPieTable));
            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfPCell.setBackgroundColor(new BaseColor(0, 133, 119));
            pdfPCell.setPadding(10);
            pdfPTable.addCell(pdfPCell);

            pdfPCell = new PdfPCell(new Phrase(presupuesto.getPrecioTotalString(), fPieTable));
            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfPCell.setBackgroundColor(new BaseColor(0, 133, 119));
            pdfPCell.setPadding(10);
            pdfPTable.addCell(pdfPCell);

            this.paragraph.setSpacingBefore(10);
            this.paragraph.setSpacingAfter(10);
            this.paragraph.add(pdfPTable);
            this.document.add(this.paragraph);
        }catch (Exception e){
            Log.e("crearTablaPresupuesto", e.toString());
        }
    }

    /**
     * Muestra la ruta absoluta del archivo PDF
     *
     * @return :String
     */
    public String verPathPDF(){
        return this.pdfFile.getAbsolutePath();
    }

    public Uri verUriPDF(){
        return Uri.parse(this.pdfFile.getAbsoluteFile().toString());
    }
}
