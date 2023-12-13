package com.ouyang.codetemplategenerator.template.codetemplate;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.ouyang.codetemplategenerator.template.codetemplate.dialog.CodeInputDialog;
import com.ouyang.codetemplategenerator.template.TemplateFactory;

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
        CodeInputDialog dialog = new CodeInputDialog(project);
        dialog.show();
        if (dialog.getExitCode() != DialogWrapper.OK_EXIT_CODE) {
            return;
        }

        String className = dialog.getClassName();
        String fileType = dialog.getFileType();
        String fileForm = dialog.getFileForm();

        CodeTemplate codeTemplate = TemplateFactory.createCodeTemplate(fileType, e);
        if (codeTemplate != null) {
            codeTemplate.setClassName(className);
            codeTemplate.setFileForm(fileForm);
            codeTemplate.generateTemplate();
        }
    }

}
