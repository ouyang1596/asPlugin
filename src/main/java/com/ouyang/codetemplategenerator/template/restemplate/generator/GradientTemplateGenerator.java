package com.ouyang.codetemplategenerator.template.restemplate.generator;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.ouyang.codetemplategenerator.template.TemplateFactory;
import com.ouyang.codetemplategenerator.template.restemplate.ResTemplate;
import com.ouyang.codetemplategenerator.template.restemplate.dialog.GradientInputDialog;

/**
 * Gradient模板生成器
 */
public class GradientTemplateGenerator extends AnAction {


    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }

        handleDialog(e, project);


    }

    public static ResTemplate handleDialog(AnActionEvent e, Project project) {
        // 创建并显示用户界面
        GradientInputDialog dialog = new GradientInputDialog(project);
        dialog.show();
        if (dialog.getExitCode() != DialogWrapper.OK_EXIT_CODE) {
            return null;
        }

        String resName = dialog.getResName();
        String startColor = dialog.getStartColor();
        String endColor = dialog.getEndColor();
        String angle = dialog.getAngle();
        String radius = dialog.getRadius();

        ResTemplate codeTemplate = TemplateFactory.createResTemplate(TemplateFactory.ResType.GRADIENT, e);
        if (codeTemplate != null) {
            codeTemplate.setResName(resName);
            codeTemplate.setStartColor(startColor);
            codeTemplate.setEndColor(endColor);
            codeTemplate.setAngle(angle);
            codeTemplate.setRadius(radius);
            codeTemplate.generateTemplate();
        }
        return codeTemplate;
    }

}
