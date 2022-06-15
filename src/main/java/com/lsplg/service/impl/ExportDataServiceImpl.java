package com.lsplg.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.lsplg.model.Note;
import com.lsplg.service.ExportDataService;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class ExportDataServiceImpl implements ExportDataService {
    @Override
    public void toPDF(List<Note> savedNotes, String projectName) {
//        StringBuilder report = toString(savedNotes);
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("C:\\Diplom\\" + projectName + "\\Report.pdf"));
        } catch (DocumentException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        document.open();
        PdfPTable table = new PdfPTable(5);
        table.setTotalWidth(37f);

        float[] widths = new float[] {1f, 6f , 3f, 12f, 12f };
        try {
            table.setWidths(widths);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        addTableHeader(table);
        addRows(table, savedNotes);
//        try {
//            addCustomRows(table);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        } catch (BadElementException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();
//        for (int i = 0; i < savedNotes.size(); i++) {
//            Chunk chunk = new Chunk("Name of class: " + savedNotes.get(i).getFileName() + );
//            chunk.append(" " + "Number of line: " + savedNotes.get(i).getLineNumber());
//            chunk.append(" Note: " + savedNotes.get(i).getNote());
//            try {
//                document.add(chunk);
//            } catch (DocumentException ex) {
//                ex.printStackTrace();
//            }
//        }
//        document.close();
    }

    @Override
    public void toDocx(List<Note> savedNotes, String projectName) {
        WordprocessingMLPackage wordPackage = null;
        try {
            wordPackage = WordprocessingMLPackage.createPackage();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        MainDocumentPart mainDocumentPart = wordPackage.getMainDocumentPart();
        mainDocumentPart.addParagraphOfText(toString(savedNotes).toString());
        File exportFile = new File("C:\\Diplom\\" + projectName + "\\Report.docx");
        try {
            exportFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            wordPackage.save(exportFile);
        } catch (Docx4JException e) {
            e.printStackTrace();
        }
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("#", "Name of File", "# line", "Text in the line", "Note")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(1);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    private void addRows(PdfPTable table, List<Note> savedNotes) {
        for (int i = 0; i < savedNotes.size(); i++) {
            Note currentNote = savedNotes.get(i);
            table.addCell(i + "");
            table.addCell(currentNote.getFileName());
            table.addCell(currentNote.getLineNumber() + "");
            table.addCell(currentNote.getLineText());
            table.addCell(currentNote.getNote());
        }
    }

//    private void addCustomRows(PdfPTable table)
//            throws URISyntaxException, BadElementException, IOException {
//        Path path = Paths.get(ClassLoader.getSystemResource("Java_logo.png").toURI());
//        Image img = Image.getInstance(path.toAbsolutePath().toString());
//        img.scalePercent(10);
//
//        PdfPCell imageCell = new PdfPCell(img);
//        table.addCell(imageCell);
//
//        PdfPCell horizontalAlignCell = new PdfPCell(new Phrase("row 2, col 2"));
//        horizontalAlignCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        table.addCell(horizontalAlignCell);
//
//        PdfPCell verticalAlignCell = new PdfPCell(new Phrase("row 2, col 3"));
//        verticalAlignCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
//        table.addCell(verticalAlignCell);
//    }

    @Override
    public StringBuilder toString(List<Note> savedNotes) {
        StringBuilder report = new StringBuilder();
        for (int i = 0; i < savedNotes.size(); i++) {

            String currentNote = ("Name of class: " + savedNotes.get(i).getFileName()
                    + System.lineSeparator() + " Number of line: " + savedNotes.get(i).getLineNumber()
                    + System.lineSeparator() + " Text in the line: " + savedNotes.get(i).getLineText()
                    + System.lineSeparator() + " Note: " + savedNotes.get(i).getNote());
            report.append(currentNote);
        }
        return report;
    }
}
