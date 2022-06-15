package com.lsplg.model;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.popup.JBPopupFactory;

import javax.swing.*;

public class PanelMaker {

    public static void createPopup(Editor editor, JComponent panel, JComponent preferableComponent,
                                   JComponent secondComponent) {
        addComponentsToPanel(panel, preferableComponent, secondComponent);
        StandardPopup(editor, panel, preferableComponent);
    }

    public static void createPopup(Editor editor, JComponent panel, JComponent preferableComponent,
                                   JComponent secondComponent, JComponent thirdComponent) {
        addComponentsToPanel(panel, preferableComponent, secondComponent, thirdComponent);
        StandardPopup(editor, panel, preferableComponent);
    }

    public static void OkPanel(JLabel label, Editor editor) {
        JButton okButton = new JButton("OK");
        JPanel enteredNoteMessagePanel = new JPanel();
        enteredNoteMessagePanel.add(label);
        enteredNoteMessagePanel.add(okButton);
        okButton.addActionListener(e -> {
            enteredNoteMessagePanel.setVisible(false);
        });
        StandardPopup(editor, enteredNoteMessagePanel, okButton);
    }

    public static void StandardPopup(Editor editor, JComponent firstComponent, JComponent secondComponent) {
        JBPopupFactory.getInstance()
                .createComponentPopupBuilder(firstComponent, secondComponent)
                .setFocusable(true)
                .setRequestFocus(true)
                .createPopup()
                .showInBestPositionFor(editor);
    }

    public static void addComponentsToPanel (JComponent panel, JComponent firstComponent, JComponent secondComponent) {
        panel.add(firstComponent);
        panel.add(secondComponent);
    }

    public static void addComponentsToPanel (JComponent panel, JComponent firstComponent, JComponent secondComponent, JComponent thirdComponent) {
        panel.add(firstComponent);
        panel.add(secondComponent);
        panel.add(thirdComponent);
    }
}
