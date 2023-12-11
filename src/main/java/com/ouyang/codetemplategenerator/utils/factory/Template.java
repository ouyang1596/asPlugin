package com.ouyang.codetemplategenerator.utils.factory;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class Template {
    protected Project project;
    /**
     * 类的名字
     */
    protected String className;

    /**
     * 文件类型
     */
    protected String fileType;
    /**
     * 文件格式
     */
    protected String fileForm;
    /**
     * 点击的文件夹位置相关类
     */
    protected VirtualFile virtualFile;
    /**
     * 点击的文件夹位置路径
     */
    protected String targetFolderPath;

    /**
     * 数据
     */
    protected String data;
    /**
     * 需要生成的文件位置路径
     */
    protected String targetFilePath;

    /**
     * 布局文件名字
     */
    protected String layoutName;
    /**
     * 包的名字
     */
    protected String packageName;

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

    public void setData(String data) {
        this.data = data;
    }

    public void setTargetFilePath(String targetFilePath) {
        this.targetFilePath = targetFilePath;
    }

    /**
     * 生成模板
     */
    public void generateTemplate() {
        // 将路径转换为包名
        packageName = convertToPackageName(targetFolderPath);
        setLayoutName();
        generateCode();
        generateLayout();
    }

    public abstract void setLayoutName();

    /**
     * 生成代码
     */
    public abstract void generateCode();

    /**
     * 生成布局
     */
    public void generateLayout() {
        try {
            String xmlCode = generateViewLayout();
            setTargetFilePath();
            write(targetFilePath, xmlCode);
        } catch (IOException ex) {
            Messages.showErrorDialog(project, "Failed to generate code: " + ex.getMessage(), "Error");
        }
    }


    protected void write(String targetFilePath, String code) throws IOException {
        File targetFile = new File(targetFilePath);
        FileWriter writer = new FileWriter(targetFile);
        writer.write(code);
        writer.close();

        // 刷新项目文件
        VirtualFile createdFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(targetFile);
        if (createdFile != null) {
            FileEditorManager.getInstance(project).openFile(virtualFile, true);
        }
    }

    protected String convertToPackageName(String clickedDirectoryPath) {


        clickedDirectoryPath = clickedDirectoryPath.substring(clickedDirectoryPath.indexOf("java/") + 5);

        String packageName = clickedDirectoryPath.replaceAll("/", ".");

        return packageName;
    }

    protected String generateViewLayout() {
        String xmlCode = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<androidx.constraintlayout.widget.ConstraintLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "    xmlns:app=\"http://schemas.android.com/apk/res-auto\"\n" +
                "    xmlns:tools=\"http://schemas.android.com/tools\"\n" +
                "    android:layout_width=\"match_parent\"\n" +
                "    android:layout_height=\"wrap_content\">\n" +
                "\n" +
                "    <TextView\n" +
                "        android:id=\"@+id/textView\"\n" +
                "        android:layout_width=\"wrap_content\"\n" +
                "        android:layout_height=\"wrap_content\"\n" +
                "        app:layout_constraintLeft_toLeftOf=\"parent\"\n" +
                "        app:layout_constraintTop_toTopOf=\"parent\"\n" +
                "        tools:text=\"xxx\" />\n" +
                "</androidx.constraintlayout.widget.ConstraintLayout>";

        return xmlCode;
    }

    protected void setTargetFilePath() {
        targetFolderPath = targetFolderPath.substring(0, targetFolderPath.indexOf("/java"));
        targetFolderPath += "/res/layout";
        targetFilePath = targetFolderPath + "/" + layoutName + ".xml";
    }
}
