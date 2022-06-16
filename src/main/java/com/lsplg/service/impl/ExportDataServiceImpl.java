package com.lsplg.service.impl;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.lsplg.model.Note;
import com.lsplg.service.ExportDataService;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.stream.Stream;

public class ExportDataServiceImpl implements ExportDataService {
    @Override
    public void toPDF(List<Note> savedNotes, String projectName) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("C:\\Audit\\" + projectName + "\\Report.pdf"));
        } catch (DocumentException | FileNotFoundException ex) {
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
        try {
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();
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
            table.addCell((i + 1) + "");
            table.addCell(currentNote.getFileName());
            table.addCell(currentNote.getLineNumber() + "");
            table.addCell(currentNote.getLineText());
            table.addCell(currentNote.getNote());
        }
    }

}
