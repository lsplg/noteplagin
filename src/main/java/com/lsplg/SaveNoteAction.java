package com.lsplg;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.lsplg.model.PanelMaker;
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
        JLabel enterNotePanelLable = new JLabel("Enter note for " + lineNumber + " line:");
        JPanel enterNotePanel = new JPanel();
        String selectedLine = event.getData(PlatformDataKeys.EDITOR).getSelectionModel().getSelectedText();
        Note savedNoteToChange = noteService.findAllByProject(project.getName()).stream()
                .filter(note -> note.getFileName().equals(file.getName())
                        && note.getLineNumber() == lineNumber)
                .findFirst()
                .orElse(null);
        String textFieldNoteToSave = "";
        if (savedNoteToChange != null) {
            textFieldNoteToSave = savedNoteToChange.getNote();
            noteService.delete(savedNoteToChange);
        }
        JTextField enterNoteTextField = new JTextField(textFieldNoteToSave, 20);
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            Note noteToSave = new Note(enterNoteTextField.getText(), lineNumber, selectedLine,  file.getName(), project.getName());
            noteService.save(noteToSave);
            enterNotePanel.setVisible(false);
            JLabel enteredNoteMessage = new JLabel(
                    "Note \"" + enterNoteTextField.getText() + "\" to line " + lineNumber + " was saved.");
            PanelMaker.OkPanel(enteredNoteMessage, editor);
        });
        PanelMaker.createPopup(editor, enterNotePanel, enterNoteTextField, enterNotePanelLable, saveButton);
    }

    @Override
    public boolean isDumbAware() {
        return super.isDumbAware();
    }

    public static void makeSaveNotePanel() {

    }
}
