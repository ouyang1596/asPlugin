package com.ouyang.codetemplategenerator.template.codetemplate.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 用户交互弹框
 */
public class CodeInputDialog extends DialogWrapper {
    public interface FileForm {
        String JAVA = "java";
        String KOTLIN = "kotlin";
    }

    public interface FileType {
        String CUSTOM_VIEW = "custom_view";
        String CUSTOM_BINDER = "custom_binder";

        String CUSTOM_DIALOG = "custom_dialog";

        String CUSTOM_LIST_ACTIVITY = "custom_list_activity";


        String CUSTOM_VIEW_WITH_RV = "custom_view_with_rv";

        String MVP = "mvp";
    }

    private JTextField classNameField;
    /**
     * 文件格式
     */
    private JComboBox<String> fileFormComboBox;

    /**
     * 文件类型
     */
    private JComboBox<String> fileTypeComboBox;

    public CodeInputDialog(Project project) {
        super(project, true);
        setTitle("Generate Code");

        init();
    }

    @Override
    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel classNamePanel = new JPanel(new BorderLayout());
        JLabel classNameLabel = new JLabel("Class Name:");
        classNameField = new JTextField(20);
        classNamePanel.add(classNameLabel, BorderLayout.WEST);
        classNamePanel.add(classNameField, BorderLayout.CENTER);

        JPanel fileFormPanel = new JPanel(new BorderLayout());
        JLabel fileFormLabel = new JLabel("File Form:");
        fileFormComboBox = new JComboBox<>(new String[]{FileForm.KOTLIN, FileForm.JAVA});
        fileFormPanel.add(fileFormLabel, BorderLayout.WEST);
        fileFormPanel.add(fileFormComboBox, BorderLayout.CENTER);

        JPanel fileTypePanel = new JPanel(new BorderLayout());
        JLabel fileTypeLabel = new JLabel("File Type:");
        fileTypeComboBox = new JComboBox<>(new String[]{FileType.CUSTOM_VIEW, FileType.CUSTOM_VIEW_WITH_RV, FileType.CUSTOM_BINDER, FileType.CUSTOM_DIALOG, FileType.CUSTOM_LIST_ACTIVITY});
        fileTypePanel.add(fileTypeLabel, BorderLayout.WEST);
        fileTypePanel.add(fileTypeComboBox, BorderLayout.CENTER);

        panel.add(classNamePanel, BorderLayout.NORTH);
        panel.add(fileTypePanel, BorderLayout.CENTER);
        panel.add(fileFormPanel, BorderLayout.EAST);

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

    public String getFileForm() {
        return (String) fileFormComboBox.getSelectedItem();
    }

    public String getFileType() {
        return (String) fileTypeComboBox.getSelectedItem();
    }
}
