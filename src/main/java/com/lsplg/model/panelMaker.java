package com.lsplg.model;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.popup.JBPopupFactory;

import javax.swing.*;
import java.awt.*;

public class panelMaker {

    public static void createPopup(Editor editor, JPanel panel, JComponent preferedComponent,
                                   JComponent secondComponent) {
        addComponentsToPanel(panel, preferedComponent, secondComponent);
        StandardPopup(editor, panel, preferedComponent);
    }

    public static void createPopup(Editor editor, JPanel panel, JComponent preferedComponent,
                                   JComponent secondComponent, JComponent thirdComponent) {
        addComponentsToPanel(panel, preferedComponent, secondComponent, thirdComponent);
        StandardPopup(editor, panel, preferedComponent);
    }

    public static void OkPanel(JLabel message, Editor editor) {
        JButton okButton = new JButton("OK");
        JPanel enteredNoteMessagePanel = new JPanel();
        enteredNoteMessagePanel.add(message);
        enteredNoteMessagePanel.add(okButton);
        okButton.addActionListener(e1 -> {
            enteredNoteMessagePanel.setVisible(false);
        });
        StandardPopup(editor, enteredNoteMessagePanel, okButton);
    }

    public static void StandardPopup(Editor editor, JComponent firstComponent, JComponent seconfComponent) {
        JBPopupFactory.getInstance()
                .createComponentPopupBuilder(firstComponent, seconfComponent)
                .setFocusable(true)
                .setRequestFocus(true)
                .createPopup()
                .showInBestPositionFor(editor);
    }

    public static void StandardPopup(Editor editor, JComponent component) {
        JBPopupFactory.getInstance()
                .createComponentPopupBuilder(component, component)
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
