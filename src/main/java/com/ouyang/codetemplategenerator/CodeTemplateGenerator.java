package com.ouyang.codetemplategenerator;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.ouyang.codetemplategenerator.utils.factory.CodeTemplateFactory;
import com.ouyang.codetemplategenerator.utils.factory.Template;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 代码模版生成器
 */
public class CodeTemplateGenerator extends AnAction {


    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }

        // 获取点击的文件夹位置
        VirtualFile virtualFile = (VirtualFile) e.getDataContext().getData("virtualFile");
        if (virtualFile == null || !virtualFile.isDirectory()) {
            return;
        }


        // 创建并显示用户界面
        InputDialog dialog = new InputDialog(project);
        dialog.show();
        if (dialog.getExitCode() != DialogWrapper.OK_EXIT_CODE) {
            return;
        }

        String className = dialog.getClassName();
        String fileType = dialog.getFileType();
        String fileForm = dialog.getFileForm();

        String targetFolderPath = virtualFile.getPath();
        String form;
        if (InputDialog.FileForm.JAVA.equals(fileForm)) {
            form = "java";
        } else {
            form = "kt";
        }
        String targetFilePath = targetFolderPath + "/" + className + "." + form;

        CodeTemplateFactory.getInstance().setProject(project);
        CodeTemplateFactory.getInstance().setVirtualFile(virtualFile);
        CodeTemplateFactory.getInstance().setClassName(className);
        CodeTemplateFactory.getInstance().setFileForm(fileForm);
        CodeTemplateFactory.getInstance().setFileType(fileType);
        CodeTemplateFactory.getInstance().setTargetFolderPath(targetFolderPath);
        CodeTemplateFactory.getInstance().setTargetFilePath(targetFilePath);

        Template template = CodeTemplateFactory.getInstance().createTemplate();
        template.generateTemplate();

    }


    public static class InputDialog extends DialogWrapper {
        public interface FileForm {
            String JAVA = "java";
            String KOTLIN = "kotlin";
        }

        public interface FileType {
            String CUSTOM_VIEW = "custom_view";
            String CUSTOM_BINDER = "custom_binder";

            String CUSTOM_DIALOG = "custom_dialog";

            String CUSTOM_LIST_ACTIVITY = "custom_list_activity";
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

        protected InputDialog(Project project) {
            super(project, true);
            setTitle("Generate Code");

            init();
        }

        @Override
        protected JComponent createCenterPanel() {
            JPanel panel = new JPanel(new BorderLayout());

            JPanel classNamePanel = new JPanel(new BorderLayout());
            JLabel classNameLabel = new JLabel("Class Name:");
            classNameField = new JTextField();
            classNamePanel.add(classNameLabel, BorderLayout.WEST);
            classNamePanel.add(classNameField, BorderLayout.CENTER);

            JPanel fileFormPanel = new JPanel(new BorderLayout());
            JLabel fileFormLabel = new JLabel("File Form:");
            fileFormComboBox = new JComboBox<>(new String[]{FileForm.KOTLIN, FileForm.JAVA});
            fileFormPanel.add(fileFormLabel, BorderLayout.WEST);
            fileFormPanel.add(fileFormComboBox, BorderLayout.CENTER);

            JPanel fileTypePanel = new JPanel(new BorderLayout());
            JLabel fileTypeLabel = new JLabel("File Type:");
            fileTypeComboBox = new JComboBox<>(new String[]{FileType.CUSTOM_VIEW, FileType.CUSTOM_BINDER, FileType.CUSTOM_DIALOG, FileType.CUSTOM_LIST_ACTIVITY});
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


}
