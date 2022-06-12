package com.lsplg;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.lsplg.model.JNote;
import com.lsplg.model.Note;
import com.lsplg.service.impl.NoteServiceImpl;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;

public class NotesListAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        final var noteService = new NoteServiceImpl();
        Project project = event.getProject();
        Editor editor = event.getData(PlatformDataKeys.EDITOR);
        JComponent myPanel = new JPanel();
        List<Note> savedNotes= noteService.findAllByProject(project.getName());
        JList<JNote> savedNotesAsJList;
        for (int i = 0; i < savedNotes.size(); i++) {
            Note note = savedNotes.get(i);
            savedNotesAsJList.add(Note);
        }

        myPanel.add(savedNote);
        JBPopupFactory.getInstance()
                .createComponentPopupBuilder(myPanel, savedNote)
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
