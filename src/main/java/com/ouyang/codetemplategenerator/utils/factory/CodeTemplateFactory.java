package com.ouyang.codetemplategenerator.utils.factory;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.ouyang.codetemplategenerator.CodeTemplateGenerator;
import com.ouyang.codetemplategenerator.dialog.InputDialog;

public class CodeTemplateFactory {


    public static Template createTemplate(String fileType, AnActionEvent e) {
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

        Template template = null;
        switch (fileType) {
            case InputDialog.FileType.CUSTOM_BINDER:
                template = new BinderTemplate();
                break;
            case InputDialog.FileType.CUSTOM_DIALOG:
                template = new DialogTemplate();
                break;
            case InputDialog.FileType.CUSTOM_LIST_ACTIVITY:
                template = new ListActivityTemplate();
                break;
            default:
                template = new ViewTemplate();
                break;
        }
        template.setAnActionEvent(e);
        template.setFileType(fileType);
        template.setProject(project);
        template.setVirtualFile(virtualFile);
        template.setTargetFolderPath(targetFolderPath);
        return template;
    }

}
