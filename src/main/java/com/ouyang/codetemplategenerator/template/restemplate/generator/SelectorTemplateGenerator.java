package com.ouyang.codetemplategenerator.template.restemplate.generator;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.ouyang.codetemplategenerator.template.TemplateFactory;
import com.ouyang.codetemplategenerator.template.restemplate.ResTemplate;
import com.ouyang.codetemplategenerator.template.restemplate.SelectorTemplate;
import com.ouyang.codetemplategenerator.template.restemplate.dialog.SelectorInputDialog;

/**
 * Selector模板生成器
 */
public class SelectorTemplateGenerator extends AnAction {


    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }

        // 创建并显示用户界面
        SelectorInputDialog dialog = new SelectorInputDialog(project);
        dialog.setAnActionEvent(e);
        dialog.show();
        if (dialog.getExitCode() != DialogWrapper.OK_EXIT_CODE) {
            return;
        }

        String resName = dialog.getResName();
        String pressRes = dialog.getPressRes();
        String normalRes = dialog.getNormalRes();
        int resFlag = dialog.getResFlag();

        ResTemplate codeTemplate = TemplateFactory.createResTemplate(TemplateFactory.ResType.SELECTOR, e);
        if (codeTemplate instanceof SelectorTemplate) {
            SelectorTemplate template = (SelectorTemplate) codeTemplate;
            template.setResName(resName);
            template.setPressRes(pressRes);
            template.setNormalRes(normalRes);
            template.setResFlag(resFlag);
            template.generateTemplate();
        }

    }

}
