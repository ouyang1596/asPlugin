package com.ouyang.codetemplategenerator.template.codetemplate.generator;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.ouyang.codetemplategenerator.template.TemplateFactory;
import com.ouyang.codetemplategenerator.template.codetemplate.CodeTemplate;
import com.ouyang.codetemplategenerator.template.codetemplate.dialog.CodeInputDialog;
import com.ouyang.codetemplategenerator.template.codetemplate.dialog.MVPCodeInputDialog;

/**
 * MVP代码模版生成器
 */
public class MVPCodeTemplateGenerator extends AnAction {


    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }

        // 创建并显示用户界面
        MVPCodeInputDialog dialog = new MVPCodeInputDialog(project);
        dialog.show();
        if (dialog.getExitCode() != DialogWrapper.OK_EXIT_CODE) {
            return;
        }

        String className = dialog.getClassName();

        CodeTemplate codeTemplate = TemplateFactory.createCodeTemplate(CodeInputDialog.FileType.MVP, e);
        if (codeTemplate != null) {
            codeTemplate.setClassName(className);
            codeTemplate.generateTemplate();
        }
    }

}
