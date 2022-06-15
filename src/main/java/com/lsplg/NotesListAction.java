package com.lsplg;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.lsplg.model.Note;
import com.lsplg.model.panelMaker;
import com.lsplg.service.impl.ExportDataServiceImpl;
import com.lsplg.service.impl.NoteServiceImpl;
import org.jetbrains.annotations.NotNull;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

public class NotesListAction extends AnAction {
    final NoteServiceImpl noteService = new NoteServiceImpl();

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        final var exportDataService = new ExportDataServiceImpl();

        Project project = event.getProject();
        Editor editor = event.getData(PlatformDataKeys.EDITOR);
        String[] columnHeader = new String[] {"#", "lineNumber", "fileName", "note"};
        List<Note> savedNotes = noteService.findAllByProject(project.getName());
        String [][] savedNotesAsMatrix = noteService.getAllAsMatrixByProject(project.getName());

        JTable savedNotesAsTable = new JTable(savedNotesAsMatrix, columnHeader);
//        savedNotesAsTableAddMouseListener() {
//
//        }
        savedNotesAsTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    JPanel myPanel = new JPanel();
                    JLabel areYouSure = new JLabel("Are you sure?");
                    areYouSure.setBounds(0, 0, 100, 100);
                    JButton deleteButton = new JButton("delete");
                    JButton changeButton = new JButton("change");
                    deleteButton.addActionListener(event -> {
                        int rowToDelete = savedNotesAsTable.rowAtPoint(e.getPoint());
                        JPanel confirmDeletePanel = new JPanel();
                        JButton yesButton = new JButton("yes");
                        yesButton.addActionListener(event1 ->{
                            confirmDeletePanel.setVisible(false);
                            JLabel enteredNoteMessage = deleteMessageLabel(savedNotes.get(rowToDelete));
                            panelMaker.OkPanel(enteredNoteMessage, editor);
                            noteService.delete(savedNotes.get(rowToDelete));
//                            savedNotesAsTable.remove(rowToDelete);
                        });
                        JButton noButton = new JButton("no");
                        noButton.addActionListener(event1 -> {
                            confirmDeletePanel.setVisible(false);
                        });
                        panelMaker.createPopup(editor, confirmDeletePanel, yesButton, areYouSure, noButton);
                    });
                    changeButton.addActionListener(event -> {
                        int rowToChange = savedNotesAsTable.rowAtPoint(e.getPoint());
                        JPanel changePanel = new JPanel();
                        JTextField myTextField = new JTextField(savedNotes.get(rowToChange).getNote(), 20);
                        JButton myButton = new JButton("Save");
                        myButton.addActionListener(event1 -> {
                            Note changedNote = changedNoteToSave(savedNotes.get(rowToChange).getProjectName(), savedNotes.get(rowToChange), myTextField.getText());
                            noteService.save(changedNote);
                            changePanel.setVisible(false);
                            JLabel enteredNoteMessage = changeMessageLabel(myTextField.getText(), savedNotes, rowToChange);
                            panelMaker.OkPanel(enteredNoteMessage, editor);
                        });
                        panelMaker.createPopup(editor, changePanel, myTextField, myButton);
                    });
                    panelMaker.createPopup(editor, myPanel, myPanel, deleteButton, changeButton);                }
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
        Box contents = new Box(BoxLayout.Y_AXIS);
        JButton exportToPDFButton = new JButton("Export to pdf");
//        File exportInDocx = new File("C:\\Diplom\\" + project.getName() + "\\report.pdf");
        exportToPDFButton.addActionListener(e -> {
            exportDataService.toPDF(savedNotes, project.getName());
            contents.setVisible(false);
        });
        JButton exportToDocxButton = new JButton("Export to docx");
        exportToDocxButton.addActionListener(e -> {
            exportDataService.toDocx(savedNotes, project.getName());
            contents.setVisible(false);
        });
        panelMaker.addComponentsToPanel(contents, new JScrollPane(savedNotesAsTable),
                exportToPDFButton, exportToDocxButton);
        panelMaker.StandardPopup(editor, contents);
    }

    @Override
    public boolean isDumbAware() {
        return super.isDumbAware();
    }

    public JLabel changeMessageLabel(String noteText, List<Note> savedNotes, int rowToChange) {
        JLabel changeNoteMessage = new JLabel(
                "Note \"" + noteText + "\" to line " +
                        savedNotes.get(rowToChange).getLineNumber() + " was changed!" );
        return changeNoteMessage;
    }

    public JLabel deleteMessageLabel (Note note) {
        JLabel deleteNoteMessage = new JLabel(
                "Note \"" + note.getNote() + "\" to "
                        + note.getLineNumber() + " line was deleted.");
        return deleteNoteMessage;
    }

    public Note changedNoteToSave (String projectName, Note note, String changedText) {
        Note changedNote = new Note(changedText,
                note.getLineNumber(),
                note.getFileName(),
                projectName);
        return changedNote;
    }
}