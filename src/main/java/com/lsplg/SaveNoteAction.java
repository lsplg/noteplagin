package com.lsplg;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.lsplg.model.panelMaker;
import com.lsplg.model.Note;
import com.lsplg.service.impl.NoteServiceImpl;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class SaveNoteAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        final var noteService = new NoteServiceImpl();

        Project project = event.getProject();
        VirtualFile file = event.getData(PlatformDataKeys.VIRTUAL_FILE);
        Editor editor = event.getData(PlatformDataKeys.EDITOR);
        int lineNumber = editor.getCaretModel().getLogicalPosition().line;
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
        JButton myButton = new JButton("Save");
        myButton.addActionListener(e -> {
            Note noteToSave = new Note(myTextField.getText(), lineNumber,  file.getName(), project.getName());
            noteService.save(noteToSave);
            myPanel.setVisible(false);
            JLabel enteredNoteMessage = new JLabel(
                    "Note \"" + myTextField.getText() + "\" was entered to line " + lineNumber);
            panelMaker.OkPanel(enteredNoteMessage, editor);
        });
        panelMaker.addComponentsToPanel(myPanel, enterNoteFieldDescription, myTextField, myButton );
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

    public static void makeSaveNotePanel() {

    }
}
