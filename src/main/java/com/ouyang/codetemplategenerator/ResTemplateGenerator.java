package com.ouyang.codetemplategenerator;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.ouyang.codetemplategenerator.dialog.ResInputDialog;
import com.ouyang.codetemplategenerator.template.TemplateFactory;
import com.ouyang.codetemplategenerator.template.restemplate.ResTemplate;

/**
 * 代码模版生成器
 */
public class ResTemplateGenerator extends AnAction {


    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }

        // 创建并显示用户界面
        ResInputDialog dialog = new ResInputDialog(project);
        dialog.show();
        if (dialog.getExitCode() != DialogWrapper.OK_EXIT_CODE) {
            return;
        }

        String startColor = dialog.getStartColor();
        String endColor = dialog.getEndColor();
        String angle = dialog.getAngle();
        String radius = dialog.getRadius();

        ResTemplate codeTemplate = TemplateFactory.createResTemplate(e);
        if (codeTemplate != null) {
            codeTemplate.setStartColor(startColor);
            codeTemplate.setEndColor(endColor);
            codeTemplate.setAngle(angle);
            codeTemplate.setRadius(radius);
            codeTemplate.generateTemplate();
        }


    }

}
