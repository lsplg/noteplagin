package com.lsplg;

import com.google.zxing.pdf417.PDF417Writer;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.lsplg.model.JTableButtonRenderer;
import com.lsplg.model.Note;
import com.lsplg.service.impl.NoteServiceImpl;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.List;
import java.lang.*;

public class NotesListAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        final var noteService = new NoteServiceImpl();

        Project project = event.getProject();
        Editor editor = event.getData(PlatformDataKeys.EDITOR);
        String[] columnHeader = new String[] {"#", "lineNumber", "fileName", "note"};
        List<Note> savedNotes = noteService.findAllByProject(project.getName());
        String [][] savedNotesAsMatrix = noteService.getAllAsMatrixByProject(project.getName());

        JTable savedNotesAsTable = new JTable(savedNotesAsMatrix, columnHeader);
//        savedNotesAsTable.setFillsViewportHeight(true);
        savedNotesAsTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    JComponent myPanel = new JPanel();
                    JLabel areYouSure = new JLabel("Are you sure?");
                    areYouSure.setBounds(0, 0, 100, 100);
                    JButton deleteButton = new JButton("delete");
                    JButton changeButton = new JButton("change");
                    deleteButton.addActionListener(event -> {
                        JPanel confirmDeletePanel = new JPanel();
                        JPanel deletingPanel = new JPanel();
                        JButton yesButton = new JButton("yes");
                        yesButton.addActionListener(event1 ->{
                            int rowToDelete = savedNotesAsTable.rowAtPoint(e.getPoint());
                            noteService.delete(savedNotes.get(rowToDelete));
                            savedNotesAsTable.remove(rowToDelete);
                        });
                        JButton noButton = new JButton("no");
                        deletingPanel.add(yesButton);
                        deletingPanel.add(noButton);
                        confirmDeletePanel.add(areYouSure);
                        confirmDeletePanel.add(deletingPanel);
                        JBPopupFactory.getInstance()
                                .createComponentPopupBuilder(confirmDeletePanel, yesButton)
                                .setFocusable(true)
                                .setRequestFocus(true)
                                .createPopup()
                                .showInBestPositionFor(editor);

                    });
                    changeButton.addActionListener(event -> {
                        int rowToChange = savedNotesAsTable.rowAtPoint(e.getPoint());
                        JPanel changePanel = new JPanel();
                        JTextField myTextField = new JTextField(savedNotes.get(rowToChange).getNote(), 20);
                        JButton myButton = new JButton("Save");
                        myButton.addActionListener(event1 -> {
                            Note changedNote = new Note(myTextField.getText(),
                                    savedNotes.get(rowToChange).getLineNumber(),
                                    savedNotes.get(rowToChange).getFileName(),
                                    project.getName());
                            noteService.save(changedNote);
                            noteService.delete(savedNotes.get(rowToChange));
                        });
                        changePanel.add(myTextField);
                        changePanel.add(myButton);
                        JBPopupFactory.getInstance()
                                .createComponentPopupBuilder(changePanel, myTextField)
                                .setFocusable(true)
                                .setRequestFocus(true)
                                .createPopup()
                                .showInBestPositionFor(editor);
                    });
                    myPanel.add(deleteButton);
                    myPanel.add(changeButton);
                    JBPopupFactory.getInstance()
                            .createComponentPopupBuilder(myPanel, myPanel)
                            .setFocusable(true)
                            .setRequestFocus(true)
                            .createPopup()
                            .showInBestPositionFor(editor);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    Point point = e.getPoint();
                    int row = savedNotesAsTable.rowAtPoint(point);
                    savedNotesAsTable.setRowSelectionInterval(row, row);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        JButton exportButton = new JButton("Export");
//        File exportInDocx = new File("C:\\Diplom\\" + project.getName() + "\\report.pdf");
        exportButton.addActionListener(e -> {
            Document document = new Document();
            try {
                PdfWriter.getInstance(document, new FileOutputStream("C:\\Diplom\\" + project.getName() + "\\report.pdf"));
            } catch (DocumentException ex) {
                ex.printStackTrace();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            document.open();
            Chunk chunk = new Chunk(savedNotes.toString());
            try {
                document.add(chunk);
            } catch (DocumentException ex) {
                ex.printStackTrace();
            }
            document.close();
        });

        Box contents = new Box(BoxLayout.Y_AXIS);
        contents.add(new JScrollPane(savedNotesAsTable));
        contents.add(exportButton);

        JBPopupFactory.getInstance()

                .createComponentPopupBuilder(contents, contents)
                .setRequestFocus(true)
                .createPopup()
                .showInBestPositionFor(editor);

    }

    @Override
    public boolean isDumbAware() {
        return super.isDumbAware();
    }

}
