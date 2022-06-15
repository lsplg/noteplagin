package com.lsplg.service.impl;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
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
import java.util.List;

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
        for (int i = 0; i < savedNotes.size(); i++) {
            Chunk chunk = new Chunk("Name of class: " + savedNotes.get(i).getFileName()
                     + "\n\n Number of line: " + savedNotes.get(i).getLineNumber()
                     + "\n\n Note: " + savedNotes.get(i).getNote());
            try {
                document.add(chunk);
            } catch (DocumentException ex) {
                ex.printStackTrace();
            }
        }
        document.close();
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

    @Override
    public StringBuilder toString(List<Note> savedNotes) {
        StringBuilder report = new StringBuilder();
        for (int i = 0; i < savedNotes.size(); i++) {

            String currentNote = ("Name of class: " + savedNotes.get(i).getFileName()
                    + System.lineSeparator() + " Number of line: " + savedNotes.get(i).getLineNumber()
                    + System.lineSeparator() + " Note: " + savedNotes.get(i).getNote());
            report.append(currentNote);
        }
        return report;
    }
}
