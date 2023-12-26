package com.ouyang.codetemplategenerator.template;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.ouyang.codetemplategenerator.template.codetemplate.*;
import com.ouyang.codetemplategenerator.template.codetemplate.dialog.CodeInputDialog;
import com.ouyang.codetemplategenerator.template.restemplate.*;

public class TemplateFactory {
    public interface ResType {
        String GRADIENT = "GRADIENT";
        String SHAPE = "SHAPE";
        String STROKE = "STROKE";
        String SELECTOR = "SELECTOR";
    }

    public static CodeTemplate createCodeTemplate(String fileType, AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return null;
        }
        // 获取点击的文件夹位置
        VirtualFile virtualFile = (VirtualFile) e.getDataContext().getData("virtualFile");
        if (virtualFile == null || !virtualFile.isDirectory()) {
            return null;
        }

        String targetFolderPath = virtualFile.getPath();

        CodeTemplate codeTemplate = null;
        switch (fileType) {
            case CodeInputDialog.FileType.MVP:
                codeTemplate = new MVPCodeTemplate();
                break;
            case CodeInputDialog.FileType.CUSTOM_BINDER:
                codeTemplate = new BinderCodeTemplate();
                break;
            case CodeInputDialog.FileType.CUSTOM_DIALOG:
                codeTemplate = new DialogCodeTemplate();
                break;
            case CodeInputDialog.FileType.CUSTOM_LIST_ACTIVITY:
                codeTemplate = new ListActivityCodeTemplate();
                break;
            case CodeInputDialog.FileType.CUSTOM_VIEW_WITH_RV:
                codeTemplate = new ViewWithRvCodeTemplate();
                break;
            default:
                codeTemplate = new ViewCodeTemplate();
                break;
        }
        codeTemplate.setAnActionEvent(e);
        codeTemplate.setFileType(fileType);
        codeTemplate.setProject(project);
        codeTemplate.setVirtualFile(virtualFile);
        codeTemplate.setTargetFolderPath(targetFolderPath);
        return codeTemplate;
    }


    public static ResTemplate createResTemplate(String resType, AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return null;
        }
        // 获取点击的文件夹位置
        VirtualFile virtualFile = (VirtualFile) e.getDataContext().getData("virtualFile");
        if (virtualFile == null || !virtualFile.isDirectory()) {
            return null;
        }

        String targetFolderPath = virtualFile.getPath();

        ResTemplate resTemplate = null;
        switch (resType) {
            case ResType.SELECTOR:
                resTemplate = new SelectorTemplate();
                break;
            case ResType.STROKE:
                resTemplate = new StrokeTemplate();
                break;
            case ResType.SHAPE:
                resTemplate = new ShapeTemplate();
                break;
            default:
                resTemplate = new GradientTemplate();
                break;
        }
        resTemplate.setAnActionEvent(e);
        resTemplate.setProject(project);
        resTemplate.setVirtualFile(virtualFile);
        resTemplate.setTargetFolderPath(targetFolderPath);
        return resTemplate;
    }

}
