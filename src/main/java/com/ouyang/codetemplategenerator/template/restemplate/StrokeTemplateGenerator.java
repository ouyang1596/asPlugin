package com.ouyang.codetemplategenerator.template.restemplate;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.ouyang.codetemplategenerator.template.TemplateFactory;
import com.ouyang.codetemplategenerator.template.restemplate.dialog.StrokeInputDialog;

/**
 * Stroke模板生成器
 */
public class StrokeTemplateGenerator extends AnAction {


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
        StrokeInputDialog dialog = new StrokeInputDialog(project);
        dialog.show();
        if (dialog.getExitCode() != DialogWrapper.OK_EXIT_CODE) {
            return null;
        }

        String strokeColor = dialog.getStrokeColor();
        String strokeWidth = dialog.getStrokeWidth();
        String color = dialog.getColor();
        String radius = dialog.getRadius();

        ResTemplate codeTemplate = TemplateFactory.createResTemplate(TemplateFactory.ResType.STROKE, e);
        if (codeTemplate != null) {
            codeTemplate.setStrokeColor(strokeColor);
            codeTemplate.setStrokeWidth(strokeWidth);
            codeTemplate.setColor(color);
            codeTemplate.setRadius(radius);
            codeTemplate.generateTemplate();
        }
        return codeTemplate;
    }

}
