package com.ouyang.codetemplategenerator.template.restemplate;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.ouyang.codetemplategenerator.template.TemplateFactory;
import com.ouyang.codetemplategenerator.template.restemplate.dialog.ShapeInputDialog;

/**
 * Shape模板生成器
 */
public class ShapeTemplateGenerator extends AnAction {


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
        ShapeInputDialog dialog = new ShapeInputDialog(project);
        dialog.show();
        if (dialog.getExitCode() != DialogWrapper.OK_EXIT_CODE) {
            return null;
        }

        String color = dialog.getColor();
        String radius = dialog.getRadius();

        ResTemplate codeTemplate = TemplateFactory.createResTemplate(TemplateFactory.ResType.SHAPE, e);
        if (codeTemplate != null) {
            codeTemplate.setColor(color);
            codeTemplate.setRadius(radius);
            codeTemplate.generateTemplate();
        }
        return codeTemplate;
    }

}
