package com.ouyang.codetemplategenerator;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.ouyang.codetemplategenerator.dialog.InputDialog;
import com.ouyang.codetemplategenerator.utils.factory.CodeTemplateFactory;
import com.ouyang.codetemplategenerator.utils.factory.Template;

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

        // 创建并显示用户界面
        InputDialog dialog = new InputDialog(project);
        dialog.show();
        if (dialog.getExitCode() != DialogWrapper.OK_EXIT_CODE) {
            return;
        }

        String className = dialog.getClassName();
        String fileType = dialog.getFileType();
        String fileForm = dialog.getFileForm();

        Template template = CodeTemplateFactory.createTemplate(fileType, e);
        if (template != null) {
            template.setClassName(className);
            template.setFileForm(fileForm);
            template.generateTemplate();
        }
    }

}
