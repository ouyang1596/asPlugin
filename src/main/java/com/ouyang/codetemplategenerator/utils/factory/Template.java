package com.ouyang.codetemplategenerator.utils.factory;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.ouyang.codetemplategenerator.dialog.InputDialog;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class Template {

    protected AnActionEvent anActionEvent;
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
     * 需要生成的代码文件位置路径
     */
    protected String codeFilePath;

    /**
     * 需要生成的布局文件位置路径
     */
    protected String layoutFilePath;

    /**
     * 需要生成的androidManifest文件位置路径
     */
    protected String androidManifestFilePath;

    /**
     * 布局文件名字
     */
    protected String layoutName;
    /**
     * 包的名字
     */
    protected String packageName;

    /**
     * 数据
     */
    protected String data;

    public void setAnActionEvent(AnActionEvent anActionEvent) {
        this.anActionEvent = anActionEvent;
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


    /**
     * 生成模板
     */
    public void generateTemplate() {
        setLayoutName();
        setDefaultCodePath();
        convertToPackageName(targetFolderPath);
        setLayoutFilePath();
        setAndroidManifestFilePath();
        generateCode();
        generateLayout();
        otherAction();
    }


    /**
     * 默认代码路径
     */
    private void setDefaultCodePath() {
        codeFilePath = targetFolderPath + "/" + className + "." + getForm();
    }

    /**
     * 生成布局名称
     */
    public abstract void setLayoutName();

    /**
     * 生成代码
     */
    public abstract void generateCode();

    /**
     * 其他操作
     */
    public abstract void otherAction();

    /**
     * 生成布局文件
     */
    public void generateLayout() {
        try {
            String xmlCode = generateViewLayout();
            write(layoutFilePath, xmlCode);
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

    /**
     * 将路径转换为包名
     */
    protected String convertToPackageName(String clickedDirectoryPath) {

        clickedDirectoryPath = clickedDirectoryPath.substring(clickedDirectoryPath.indexOf("java/") + 5);

        packageName = clickedDirectoryPath.replaceAll("/", ".");

        return packageName;
    }

    /**
     * 生成通用布局字符串
     */
    protected String generateViewLayout() {
        String xmlCode = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + "<androidx.constraintlayout.widget.ConstraintLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" + "    xmlns:app=\"http://schemas.android.com/apk/res-auto\"\n" + "    xmlns:tools=\"http://schemas.android.com/tools\"\n" + "    android:layout_width=\"match_parent\"\n" + "    android:layout_height=\"wrap_content\">\n" + "\n" + "    <TextView\n" + "        android:id=\"@+id/textView\"\n" + "        android:layout_width=\"wrap_content\"\n" + "        android:layout_height=\"wrap_content\"\n" + "        app:layout_constraintLeft_toLeftOf=\"parent\"\n" + "        app:layout_constraintTop_toTopOf=\"parent\"\n" + "        tools:text=\"xxx\" />\n" + "</androidx.constraintlayout.widget.ConstraintLayout>";

        return xmlCode;
    }

    /**
     * 生成布局文件路径
     */
    protected void setLayoutFilePath() {
        String folder = targetFolderPath.substring(0, targetFolderPath.indexOf("/java"));
        folder += "/res/layout";
        layoutFilePath = folder + "/" + layoutName + ".xml";
    }

    /**
     * 生成AndroidManifestFile文件路径
     */
    protected void setAndroidManifestFilePath() {
        String folder = targetFolderPath.substring(0, targetFolderPath.indexOf("/java"));
        androidManifestFilePath = folder + "/" + "AndroidManifest.xml";
    }

    /**
     * 获取文件格式
     */
    protected String getForm() {
        String form;
        if (InputDialog.FileForm.JAVA.equals(fileForm)) {
            form = "java";
        } else {
            form = "kt";
        }
        return form;
    }

    public String getData() {
        return data;
    }

}
