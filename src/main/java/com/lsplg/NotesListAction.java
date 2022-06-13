package com.lsplg;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.lsplg.model.JTableButtonRenderer;
import com.lsplg.model.Note;
import com.lsplg.service.impl.NoteServiceImpl;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

public class NotesListAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        final var noteService = new NoteServiceImpl();

        Project project = event.getProject();
        Editor editor = event.getData(PlatformDataKeys.EDITOR);
        String[] columnHeader = new String[] {"#", "lineNumber", "fileName", "note"};
        List<Note> savedNotes = noteService.findAllByProject(project.getName());
        String [][] savedNotesAsMatrix = new String[savedNotes.size()][4];
        for (int i = 0; i < savedNotes.size(); i++) {
            savedNotesAsMatrix[i][0] = i + 1 + "";
            savedNotesAsMatrix[i][1] = savedNotes.get(i).getLineNumber() + "";
            savedNotesAsMatrix[i][2] = savedNotes.get(i).getFileName();
            savedNotesAsMatrix[i][3] = savedNotes.get(i).getNote();
        }

        JTable savedNotesAsTable = new JTable(savedNotesAsMatrix, columnHeader);
//        savedNotesAsTable.setFillsViewportHeight(true);
        savedNotesAsTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

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
        })


//        TableCellRenderer buttonRenderer = new JTableButtonRenderer();
//        savedNotesAsTable.getColumn("delete").setCellRenderer(buttonRenderer);

        Box contents = new Box(BoxLayout.Y_AXIS);
        contents.add(new JScrollPane(savedNotesAsTable));
//        for (int i = 0; i < savedNotes.size(); i++) {
//            JComponent myPanel = new JPanel();
//            JLabel note = new JLabel(savedNotes[i].getNote());
//            JLabel lineNumber = new JLabel(savedNotes[i].getLineNumber() + "");
//            JLabel fileName = new JLabel(savedNotes[i].getFileName());
//            JPanel savedNote = new JPanel();
//            myPanel.add(note);
//            myPanel.add(lineNumber);
//            myPanel.add(fileName);
//            savedNotesAsJList.add(myPanel);
//        }

//        JPanel noteList = new JPanel();
//        noteList.add(savedNotesAsJList);
        JBPopupFactory.getInstance()

                .createComponentPopupBuilder(contents, contents)
                .setFocusable(true)
                .setRequestFocus(true)
                .createPopup()
                .showInBestPositionFor(editor);

    }

    @Override
    public boolean isDumbAware() {
        return super.isDumbAware();
    }

}
