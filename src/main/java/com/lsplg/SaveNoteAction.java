package com.lsplg;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.lsplg.model.Note;
import com.lsplg.service.impl.NoteServiceImpl;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SaveNoteAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        final var noteService = new NoteServiceImpl();

        Project project = event.getProject();
        VirtualFile file = event.getData(PlatformDataKeys.VIRTUAL_FILE);
        Editor editor = event.getData(PlatformDataKeys.EDITOR);
        int lineNumber = editor.getCaretModel().getLogicalPosition().line;

//        Note savedNote = noteService.findByProjectAndFileNameAndLine(project.getName(), file.getName(), line);
//        String text = savedNote != null ? savedNote.getNote() : "";
        JLabel enterNoteFieldDescription = new JLabel("Enter note for " + lineNumber + " line:");
        JComponent myPanel = new JPanel();
        Note savedNote = noteService.findAllByProject(project.getName()).stream()
                .filter(note -> note.getFileName().equals(file.getName())
                        && note.getLineNumber() == lineNumber)
                .findFirst()
                .orElse(null);
        String textFieldSavedNote = "";
        if (savedNote != null) {
            textFieldSavedNote = savedNote.getNote();
        }
        JTextField myTextField = new JTextField(textFieldSavedNote, 20);
//        myTextField.addMouseListener(new MouseAdapter() {
//            public void mouseClicked(MouseEvent e) {
//                if (e.getButton() == MouseEvent.BUTTON1) {
//                    myTextField.setText("..");
//                }
//            }
//        });
//        Font font = myTextField.getFont();
//        font = font.deriveFont(Font.ITALIC);
//        myTextField.setFont(font);
        JButton myButton = new JButton("Save");
        myButton.addActionListener(e -> {
            Note noteToSave = new Note(myTextField.getText(), lineNumber,  file.getName(), project.getName());
            noteService.save(noteToSave);
//            const popupActive = document
//            noteService.delete(noteToSave);
        });
        myPanel.add(enterNoteFieldDescription);
        myPanel.add(myTextField);
        myPanel.add(myButton);
        JBPopupFactory.getInstance()
                .createComponentPopupBuilder(myPanel, myTextField)
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
