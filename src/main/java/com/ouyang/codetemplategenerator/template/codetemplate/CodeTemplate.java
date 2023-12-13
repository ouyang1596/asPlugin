package com.ouyang.codetemplategenerator.template.codetemplate;

import com.intellij.openapi.ui.Messages;
import com.ouyang.codetemplategenerator.template.codetemplate.dialog.CodeInputDialog;
import com.ouyang.codetemplategenerator.template.Template;

import java.io.IOException;

public abstract class CodeTemplate extends Template {

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

    public void setClassName(String className) {
        this.className = className;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setFileForm(String fileForm) {
        this.fileForm = fileForm;
    }


    /**
     * 生成模板
     */
    @Override
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
     * 生成代码文件
     */
    public abstract void generateCode();


    public void generateLayout() {
        try {
            String xmlCode = generateViewLayout();
            write(layoutFilePath, xmlCode);
        } catch (IOException ex) {
            Messages.showErrorDialog(project, "Failed to generate code: " + ex.getMessage(), "Error");
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
        String folder = rootFolderPath + "/res/layout";
        layoutFilePath = folder + "/" + layoutName + ".xml";
    }

    /**
     * 生成AndroidManifestFile文件路径
     */
    protected void setAndroidManifestFilePath() {
        androidManifestFilePath = rootFolderPath + "/" + "AndroidManifest.xml";
    }

    /**
     * 获取文件格式
     */
    protected String getForm() {
        String form;
        if (CodeInputDialog.FileForm.JAVA.equals(fileForm)) {
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
