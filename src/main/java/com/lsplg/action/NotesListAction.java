package com.lsplg.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.lsplg.PanelMaker;
import com.lsplg.model.Note;
import com.lsplg.service.impl.ExportDataServiceImpl;
import com.lsplg.service.impl.MakeNoteServiceImpl;
import org.jetbrains.annotations.NotNull;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

public class NotesListAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        final var noteService = new MakeNoteServiceImpl();
        final var exportDataService = new ExportDataServiceImpl();
        Project project = event.getProject();
        Editor editor = event.getData(PlatformDataKeys.EDITOR);
        String[] columnHeader = new String[] {"#", "lineNumber", "lineText", "fileName", "note"};
        List<Note> savedNotes = noteService.findAllByProject(project.getName());
        String [][] savedNotesAsMatrix = noteService.getAllAsMatrixByProject(project.getName());

        JTable savedNotesAsTable = new JTable(savedNotesAsMatrix, columnHeader);
        savedNotesAsTableAddMouseListener(noteService, editor, savedNotes, savedNotesAsTable);
        Box savedNotesPanel = new Box(BoxLayout.Y_AXIS);
        JButton exportToPDFButton = new JButton("Export to pdf");
        exportToPDFButton.setAlignmentX(-10f);
        exportToPDFButton.addActionListener(e -> {
            JPanel exportToPDFPanel = new JPanel();
            JLabel doYouWantToExportDataToPDF = new JLabel("Do you want to export data to PDF?");
            JButton yesButton = new JButton("yes");
            yesButton.addActionListener(e1 -> {
                doYouWantToExportDataToPDF.setVisible(false);
                exportDataService.toPDF(savedNotes, project.getName());
                exportToPDFPanel.setVisible(false);
                JLabel exportedToPDFMassage = new JLabel("Data was exported to PDF. You can find report by path C:\\Audit\\" + project.getName() + "\\Report.pdf" );
                PanelMaker.OkPanel(exportedToPDFMassage, editor);
            });
            JButton noButton = new JButton("no");
            noButton.addActionListener(e1 -> {
                doYouWantToExportDataToPDF.setVisible(false);
            });
            PanelMaker.createPopup(editor, exportToPDFPanel, doYouWantToExportDataToPDF, yesButton, noButton);
        });
        JButton close = new JButton("close");
        close.setAlignmentX(10f);
        close.addActionListener( e1 -> {
            savedNotesPanel.setVisible(false);
        });
        PanelMaker.createPopup(editor, savedNotesPanel,
                new JScrollPane(savedNotesAsTable), exportToPDFButton, close);
    }

    public JLabel changeMessageLabel(String noteText, int lineNumber) {
        return new JLabel("Note \"" + noteText + "\" to line " + lineNumber + " was changed!" );
    }

    public JLabel deleteMessageLabel (Note note) {
        return new JLabel("Note \"" + note.getNote() + "\" to " + note.getLineNumber() + " line was deleted.");
    }

    public Note changeNoteText(Note note, String changedText, String selectedLine) {
        return new Note(changedText,
                note.getLineNumber(),
                selectedLine,
                note.getFileName(),
                note.getProjectName());
    }

    public void savedNotesAsTableAddMouseListener (MakeNoteServiceImpl noteService, Editor editor, List<Note> savedNoteList, JTable savedNotesAsTable) {
        savedNotesAsTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    JButton deleteNoteButton = new JButton("delete");
                    deleteNoteButton.addActionListener(event -> {
                        int rowToDelete = savedNotesAsTable.rowAtPoint(e.getPoint());
                        JPanel confirmDeletePanel = new JPanel();
                        JButton yesButton = new JButton("yes");
                        yesButton.addActionListener(event1 ->{
                            confirmDeletePanel.setVisible(false);
                            JLabel enteredNoteMessage = deleteMessageLabel(savedNoteList.get(rowToDelete));
                            PanelMaker.OkPanel(enteredNoteMessage, editor);
                            noteService.delete(savedNoteList.get(rowToDelete));
                        });
                        JButton noButton = new JButton("no");
                        noButton.addActionListener(event1 -> {
                            confirmDeletePanel.setVisible(false);
                        });
                        JLabel areYouSure = new JLabel("Are you sure?");
                        PanelMaker.createPopup(editor, confirmDeletePanel, areYouSure, yesButton, noButton);
                    });
                    JButton changeButton = new JButton("change");
                    changeButton.addActionListener(event -> {
                        int rowToChange = savedNotesAsTable.rowAtPoint(e.getPoint());
                        JPanel changeTextNotePanel = new JPanel();
                        JTextField savedNoteText = new JTextField(savedNoteList.get(rowToChange).getNote(), 20);
                        JButton saveNoteButton = new JButton("Save");
                        saveNoteButton.addActionListener(event1 -> {
                            Note changedNote = changeNoteText(savedNoteList.get(rowToChange), savedNoteText.getText(), savedNoteList.get(rowToChange).getLineText());
                            noteService.save(changedNote);
                            changeTextNotePanel.setVisible(false);
                            JLabel enteredNoteMessage = changeMessageLabel(savedNoteText.getText(), savedNoteList.get(rowToChange).getLineNumber());
                            PanelMaker.OkPanel(enteredNoteMessage, editor);
                        });
                        PanelMaker.createPopup(editor, changeTextNotePanel, savedNoteText, saveNoteButton);
                    });
                    JPanel noteActionPanel = new JPanel();
                    PanelMaker.addComponentsToPanel(noteActionPanel, deleteNoteButton, changeButton);
                    PanelMaker.StandardPopup(editor, noteActionPanel, noteActionPanel);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    Point point = e.getPoint();
                    int selectedRow = savedNotesAsTable.rowAtPoint(point);
                    savedNotesAsTable.setRowSelectionInterval(selectedRow, selectedRow);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });
    }

    @Override
    public boolean isDumbAware() {
        return super.isDumbAware();
    }
}