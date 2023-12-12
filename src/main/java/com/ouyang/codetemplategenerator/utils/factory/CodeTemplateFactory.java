package com.ouyang.codetemplategenerator.utils.factory;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.ouyang.codetemplategenerator.CodeTemplateGenerator;

public class CodeTemplateFactory {
    private static final CodeTemplateFactory instance = new CodeTemplateFactory();


    private Project project;

    /**
     * 类的名字
     */
    private String className;

    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 文件格式
     */
    private String fileForm;
    /**
     * 点击的文件夹位置相关类
     */
    private VirtualFile virtualFile;
    /**
     * 点击的文件夹位置路径
     */
    private String targetFolderPath;

    /**
     * 需要生成的文件位置路径
     */
    private String targetFilePath;


    public static CodeTemplateFactory getInstance() {
        return instance;
    }

    public void setProject(Project project) {
        this.project = project;
    }


    public void setClassName(String className) {
        this.className = className;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setFileForm(String fileForm) {
        this.fileForm = fileForm;
    }

    public void setVirtualFile(VirtualFile virtualFile) {
        this.virtualFile = virtualFile;
    }

    public void setTargetFolderPath(String targetFolderPath) {
        this.targetFolderPath = targetFolderPath;
    }

    public void setTargetFilePath(String targetFilePath) {
        this.targetFilePath = targetFilePath;
    }

    public Template createTemplate() {
        Template template = null;
        switch (fileType) {
            case CodeTemplateGenerator.InputDialog.FileType.CUSTOM_BINDER:
                template = new BinderTemplate();
                break;
            case CodeTemplateGenerator.InputDialog.FileType.CUSTOM_DIALOG:
                template = new DialogTemplate();
                break;
            case CodeTemplateGenerator.InputDialog.FileType.CUSTOM_LIST_ACTIVITY:
                template = new ListActivityTemplate();
                break;
            default:
                template = new ViewTemplate();
                break;
        }
        template.setProject(project);
        template.setVirtualFile(virtualFile);
        template.setClassName(className);
        template.setFileForm(fileForm);
        template.setFileType(fileType);
        template.setTargetFolderPath(targetFolderPath);
        template.setCodeFilePath(targetFilePath);
        return template;
    }
}
