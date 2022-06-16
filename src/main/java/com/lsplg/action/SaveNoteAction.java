package com.lsplg.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.lsplg.PanelMaker;
import com.lsplg.model.Note;
import com.lsplg.service.impl.MakeNoteServiceImpl;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class SaveNoteAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        final var noteService = new MakeNoteServiceImpl();

        Project project = event.getProject();
        String file = event.getData(PlatformDataKeys.VIRTUAL_FILE).getPath().substring(39);
        Editor editor = event.getData(PlatformDataKeys.EDITOR);
        int lineNumber = editor.getCaretModel().getLogicalPosition().line;
        JLabel enterNotePanelLabel = new JLabel("Enter note for " + (lineNumber + 1) + " line:");
        JPanel enterNotePanel = new JPanel();
        String selectedLine = event.getData(PlatformDataKeys.EDITOR).getSelectionModel().getSelectedText();
        Note savedNoteToChange = noteService.findAllByProject(project.getName()).stream()
                .filter(note -> note.getFileName().equals(file)
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
            Note noteToSave = new Note(enterNoteTextField.getText(), lineNumber, selectedLine,  file, project.getName());
            noteService.save(noteToSave);
            enterNotePanel.setVisible(false);
            JLabel enteredNoteMessage = new JLabel(
                    "Note \"" + enterNoteTextField.getText() + "\" to line " + lineNumber + " was saved.");
            PanelMaker.OkPanel(enteredNoteMessage, editor);
        });
        PanelMaker.createPopup(editor, enterNotePanel, enterNotePanelLabel, enterNoteTextField, saveButton);
    }

    @Override
    public boolean isDumbAware() {
        return super.isDumbAware();
    }

    public static void makeSaveNotePanel() {

    }
}
