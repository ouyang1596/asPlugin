package com.ouyang.codetemplategenerator.template.codetemplate.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.VerticalFlowLayout;
import org.jdesktop.swingx.HorizontalLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 用户交互弹框
 */
public class MVPCodeInputDialog extends DialogWrapper {

    private JTextField classNameField;

    public MVPCodeInputDialog(Project project) {
        super(project, true);
        setTitle("Generate MVP");

        init();
    }

    @Override
    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel(new VerticalFlowLayout());

        JPanel classNamePanel = new JPanel(new HorizontalLayout());
        JLabel classNameLabel = new JLabel("Class Name:");
        classNameField = new JTextField(15);
        classNamePanel.add(classNameLabel);
        classNamePanel.add(classNameField);

        panel.add(classNamePanel);

        return panel;
    }

    @Override
    protected JComponent createSouthPanel() {
        JPanel panel = new JPanel();

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (classNameField.getText().isEmpty()) {
                    Messages.showErrorDialog("Please enter a class name.", "Error");
                } else {
                    close(DialogWrapper.OK_EXIT_CODE);
                }
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close(DialogWrapper.CANCEL_EXIT_CODE);
            }
        });

        panel.add(okButton);
        panel.add(cancelButton);

        return panel;
    }

    public String getClassName() {
        return classNameField.getText().trim();
    }

}
