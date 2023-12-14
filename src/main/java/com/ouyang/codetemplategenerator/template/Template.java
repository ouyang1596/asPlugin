package com.ouyang.codetemplategenerator.template;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class Template {
    protected AnActionEvent anActionEvent;
    protected Project project;

    /**
     * 点击的文件夹位置相关类
     */
    protected VirtualFile virtualFile;

    /**
     * 点击的文件夹位置路径
     */
    protected String targetFolderPath;

    /**
     * 点击的文件夹位置路径
     */
    protected String rootFolderPath;

    /**
     * 生成模板
     */
    public abstract void generateTemplate();

    /**
     * 其他操作
     */
    public abstract void otherAction();

    public void setAnActionEvent(AnActionEvent anActionEvent) {
        this.anActionEvent = anActionEvent;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setVirtualFile(VirtualFile virtualFile) {
        this.virtualFile = virtualFile;
    }

    public void setTargetFolderPath(String targetFolderPath) {
        this.targetFolderPath = targetFolderPath;
        rootFolderPath = targetFolderPath.substring(0, targetFolderPath.indexOf("/main") + 5);
    }

    protected void write(String targetFilePath, String code) throws IOException {
        File targetFile = new File(targetFilePath);
        if (targetFile.exists()) {
            Messages.showErrorDialog("文件已存在", "Error");
            return;
        }
        FileWriter writer = new FileWriter(targetFile);
        writer.write(code);
        writer.close();

        // 刷新项目文件
        VirtualFile createdFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(targetFile);
        if (createdFile != null) {
            FileEditorManager.getInstance(project).openFile(virtualFile, true);
        }
    }

}
