package com.epiis.app.controller;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.epiis.app.business.PersonBusiness;
import com.epiis.app.dto.DtoPerson;

@RestController
@RequestMapping(path = "person/export")
public class PersonExportController {

    @Autowired
    private PersonBusiness personBusiness;

    /**
     * Exporta la lista de personas a un archivo de texto (.txt).
     * Permite filtrar por nombre o apellido mediante el parámetro 'search'.
     *
     * @param search Término de búsqueda opcional.
     * @return Archivo .txt descargable con los datos de las personas.
     */
    @GetMapping("/txt")
    public ResponseEntity<byte[]> exportTxt(@RequestParam(required = false) String search) {
        List<DtoPerson> persons = personBusiness.getFiltered(search);

        StringBuilder sb = new StringBuilder();
        for (DtoPerson p : persons) {
            sb.append(p.getIdPerson()).append(" | ")
                    .append(p.getFirstName()).append(" | ")
                    .append(p.getSurName()).append(" | ")
                    .append(p.getDni()).append(" | ")
                    .append(p.isGender()).append(" | ")
                    .append(p.getBirthDate()).append("\n");
        }

        byte[] content = sb.toString().getBytes(StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=personas.txt")
                .contentType(Objects.requireNonNull(MediaType.TEXT_PLAIN))
                .body(content);
    }

    /**
     * Exporta la lista de personas a un archivo PDF (.pdf).
     * Utiliza la librería iTextPDF para generar el documento.
     * Incluye una tabla con formato y encabezados.
     *
     * @param search Término de búsqueda opcional.
     * @return Archivo .pdf descargable.
     * @throws Exception Si ocurre un error durante la generación del PDF.
     */
    @GetMapping("/pdf")
    public ResponseEntity<byte[]> exportPdf(@RequestParam(required = false) String search) throws Exception {
        List<DtoPerson> persons = personBusiness.getFiltered(search);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        com.itextpdf.kernel.pdf.PdfWriter writer = new com.itextpdf.kernel.pdf.PdfWriter(out);
        com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(writer);
        com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdf);

        // Título del documento
        document.add(new com.itextpdf.layout.element.Paragraph("LISTADO DE PERSONAS")
                .setBold().setFontSize(18).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));
        document.add(new com.itextpdf.layout.element.Paragraph("\n")); // Espacio

        // Crear tabla con 6 columnas (ajusta según tus campos)
        float[] columnWidths = { 1, 3, 3, 2, 2, 3 };
        com.itextpdf.layout.element.Table table = new com.itextpdf.layout.element.Table(columnWidths);
        table.useAllAvailableWidth();

        // Encabezados de la tabla
        String[] headers = { "ID", "Nombre", "Apellido", "DNI", "Género", "Fec. Nac." };
        for (String header : headers) {
            table.addHeaderCell(new com.itextpdf.layout.element.Cell()
                    .add(new com.itextpdf.layout.element.Paragraph(header).setBold())
                    .setBackgroundColor(com.itextpdf.kernel.colors.ColorConstants.LIGHT_GRAY));
        }

        // Datos de las personas
        for (DtoPerson p : persons) {
            table.addCell(String.valueOf(p.getIdPerson()));
            table.addCell(p.getFirstName() != null ? p.getFirstName() : "");
            table.addCell(p.getSurName() != null ? p.getSurName() : "");
            table.addCell(p.getDni() != null ? p.getDni() : "");
            table.addCell(p.isGender() ? "Masculino" : "Femenino");
            table.addCell(p.getBirthDate() != null ? p.getBirthDate().toString() : "");
        }

        document.add(table);
        document.close();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=personas.pdf")
                .contentType(Objects.requireNonNull(MediaType.APPLICATION_PDF))
                .body(out.toByteArray());
    }

    /**
     * Exporta la lista de personas a un archivo Excel (.xlsx).
     * Utiliza Apache POI para generar la hoja de cálculo.
     *
     * @param search Término de búsqueda opcional.
     * @return Archivo .xlsx descargable.
     * @throws Exception Si ocurre un error durante la generación del Excel.
     */
    @GetMapping("/xlsx")
    public ResponseEntity<byte[]> exportXlsx(@RequestParam(required = false) String search) throws Exception {
        List<DtoPerson> persons = personBusiness.getFiltered(search);

        org.apache.poi.xssf.usermodel.XSSFWorkbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
        org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Personas");

        int rowNum = 0;
        for (DtoPerson p : persons) {
            org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(p.getIdPerson());
            row.createCell(1).setCellValue(p.getFirstName());
            row.createCell(2).setCellValue(p.getSurName());
            row.createCell(3).setCellValue(p.getDni());
            row.createCell(4).setCellValue(p.isGender());
            row.createCell(4).setCellValue(p.getBirthDate());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=personas.xlsx")
                .contentType(Objects.requireNonNull(MediaType.APPLICATION_OCTET_STREAM))
                .body(out.toByteArray());
    }
}
