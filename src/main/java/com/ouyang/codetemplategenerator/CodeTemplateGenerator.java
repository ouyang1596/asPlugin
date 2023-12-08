package com.ouyang.codetemplategenerator;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 代码模版生成器
 */
public class CodeTemplateGenerator extends AnAction {

    private Project project;
    /**
     * 类的名字
     */
    private String className;
    /**
     * 布局文件名字
     */
    private String layoutName;
    /**
     * 文件格式
     */
    private String fileType;
    /**
     * 点击的文件夹位置相关类
     */
    private VirtualFile virtualFile;
    /**
     * 点击的文件夹位置路径
     */
    private String targetFolderPath;

    @Override
    public void actionPerformed(AnActionEvent e) {
        project = e.getProject();
        if (project == null) {
            return;
        }

        // 获取点击的文件夹位置
        virtualFile = (VirtualFile) e.getDataContext().getData("virtualFile");
        if (virtualFile == null || !virtualFile.isDirectory()) {
            return;
        }


        // 创建并显示用户界面
        InputDialog dialog = new InputDialog(project);
        dialog.show();
        if (dialog.getExitCode() != DialogWrapper.OK_EXIT_CODE) {
            return;
        }

        className = dialog.getClassName();
        layoutName = "binder_" + className.replace("Binder", "").toLowerCase();
        String data = className.replace("Binder", "") + "Data";
        fileType = dialog.getFileType();

        targetFolderPath = virtualFile.getPath();
        String form;
        if (InputDialog.FileType.JAVA.equals(fileType)) {
            form = "java";
        } else {
            form = "kt";
        }
        String targetFilePath = targetFolderPath + "/" + className + "." + form;

        generateCode(data, targetFilePath);
        generateLayout();
    }

    /**
     * 生成代码
     */
    private void generateCode(String data, String targetFilePath) {
        try {

            // 将路径转换为包名
            String packageName = convertToPackageName(targetFolderPath);
            String code;
            if (InputDialog.FileType.JAVA.equals(fileType)) {
                code = "package " + packageName + ";\n" +
                        "\n" +
                        "import android.view.LayoutInflater;\n" +
                        "import android.view.View;\n" +
                        "import android.view.ViewGroup;\n" +
                        "import android.widget.TextView;\n" +
                        "\n" +
                        "import androidx.annotation.NonNull;\n" +
                        "import androidx.recyclerview.widget.RecyclerView;\n" +
                        "\n" +
                        "import com.ailiao.mosheng.commonlibrary.view.BaseItemViewBinder;\n" +
                        "import com.mosheng.R;\n" +
                        "\n" +
                        "import java.io.Serializable;\n" +
                        "\n" +
                        "public class " + className + " extends BaseItemViewBinder<" + className + "." + data + ", " + className + ".ViewHolder> {\n" +
                        "    private static final String TAG = \"" + className + "\";\n" +
                        "\n" +
                        "    @NonNull\n" +
                        "    @Override\n" +
                        "    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {\n" +
                        "        View view = inflater.inflate(R.layout."+layoutName+", parent, false);\n" +
                        "        return new ViewHolder(view);\n" +
                        "    }\n" +
                        "\n" +
                        "    @Override\n" +
                        "    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final " + data + " item) {\n" +
                        "        holder.textView.setText(\"xxxx\");\n" +
                        "    }\n" +
                        "\n" +
                        "    public class ViewHolder extends RecyclerView.ViewHolder {\n" +
                        "        TextView textView;\n" +
                        "\n" +
                        "        ViewHolder(View itemView) {\n" +
                        "            super(itemView);\n" +
                        "            textView = itemView.findViewById(R.id.textView);\n" +
                        "        }\n" +
                        "    }\n" +
                        "\n" +
                        "    public static class " + data + " implements Serializable {\n" +
                        "\n" +
                        "    }\n" +
                        "}";
            } else {
                code = "package " + packageName + "\n" +
                        "\n" +
                        "import android.view.LayoutInflater\n" +
                        "import android.view.View\n" +
                        "import android.view.ViewGroup\n" +
                        "import androidx.recyclerview.widget.RecyclerView\n" +
                        "import com.ailiao.mosheng.commonlibrary.view.BaseItemViewBinder\n" +
                        "import com.mosheng.R\n" +
                        "import kotlinx.android.synthetic.main." + layoutName + ".view.*\n" +
                        "import java.io.Serializable\n" +
                        "\n" +
                        "class " + className + " :\n" +
                        "    BaseItemViewBinder<" + className + "." + data + ", " + className + "." + "ViewHolder>() {\n" +
                        "\n" +
                        "    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {\n" +
                        "        return ViewHolder(inflater.inflate(R.layout." + layoutName + ", parent, false))\n" +
                        "    }\n" +
                        "\n" +
                        "    override fun onBindViewHolder(\n" +
                        "        holder: ViewHolder,\n" +
                        "        data: " + data + "\n" +
                        "    ) {\n" +
                        "        holder.itemView.textView.text = \"xxx\"\n" +
                        "    }\n" +
                        "\n" +
                        "\n" +
                        "    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {\n" +
                        "\n" +
                        "    }\n" +
                        "\n" +
                        "    class " + data + " : Serializable {\n" +
                        "\n" +
                        "    }\n" +
                        "\n" +
                        "}";
            }

            File targetFile = new File(targetFilePath);
            FileWriter writer = new FileWriter(targetFile);
            writer.write(code);
//            writer.write("public class " + className + " {\n");
//            writer.write("    // 生成的代码\n");
//            writer.write("}\n");
            writer.close();

            // 刷新项目文件
            VirtualFile createdFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(targetFile);
            if (createdFile != null) {
                FileEditorManager.getInstance(project).openFile(virtualFile, true);
            }
        } catch (IOException ex) {
            Messages.showErrorDialog(project, "Failed to generate code: " + ex.getMessage(), "Error");
        }
    }

    /**
     * 生成对应的布局
     */
    private void generateLayout() {
        try {

            targetFolderPath = targetFolderPath.substring(0, targetFolderPath.indexOf("/java"));
            targetFolderPath += "/res/layout";
            String targetFilePath = targetFolderPath + "/" + layoutName + ".xml";

            String xmlCode = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + "<androidx.constraintlayout.widget.ConstraintLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" + "    xmlns:app=\"http://schemas.android.com/apk/res-auto\"\n" + "    xmlns:tools=\"http://schemas.android.com/tools\"\n" + "    android:layout_width=\"match_parent\"\n" + "    android:layout_height=\"wrap_content\">\n" + "    \n" + "    <TextView\n" + "        android:id=\"@+id/textView\"\n" + "        android:layout_width=\"wrap_content\"\n" + "        android:layout_height=\"wrap_content\"\n" + "        app:layout_constraintLeft_toLeftOf=\"parent\"\n" + "        app:layout_constraintTop_toTopOf=\"parent\"\n" + "        tools:text=\"xxx\" />\n" + "</androidx.constraintlayout.widget.ConstraintLayout>";

            File targetFile = new File(targetFilePath);
            FileWriter writer = new FileWriter(targetFile);
            writer.write(xmlCode);
//            writer.write("public class " + className + " {\n");
//            writer.write("    // 生成的代码\n");
//            writer.write("}\n");
            writer.close();

            // 刷新项目文件
            VirtualFile createdFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(targetFile);
            if (createdFile != null) {
                FileEditorManager.getInstance(project).openFile(virtualFile, true);
            }
        } catch (IOException ex) {
            Messages.showErrorDialog(project, "Failed to generate code: " + ex.getMessage(), "Error");
        }
    }

    private static class InputDialog extends DialogWrapper {
        interface FileType {
            String JAVA = "java";
            String KOTLIN = "kotlin";
        }

        private JTextField classNameField;
        private JComboBox<String> fileTypeComboBox;

        protected InputDialog(Project project) {
            super(project, true);
            setTitle("Generate Code");

            init();
        }

        @Override
        protected JComponent createCenterPanel() {
            JPanel panel = new JPanel(new BorderLayout());

            JPanel classNamePanel = new JPanel(new BorderLayout());
            JLabel classNameLabel = new JLabel("Class Name:");
            classNameField = new JTextField();
            classNamePanel.add(classNameLabel, BorderLayout.WEST);
            classNamePanel.add(classNameField, BorderLayout.CENTER);

            JPanel fileTypePanel = new JPanel(new BorderLayout());
            JLabel fileTypeLabel = new JLabel("File Type:");
            fileTypeComboBox = new JComboBox<>(new String[]{FileType.KOTLIN, FileType.JAVA});
            fileTypePanel.add(fileTypeLabel, BorderLayout.WEST);
            fileTypePanel.add(fileTypeComboBox, BorderLayout.CENTER);

            panel.add(classNamePanel, BorderLayout.NORTH);
            panel.add(fileTypePanel, BorderLayout.CENTER);

            return panel;
        }

        @Override
        protected JComponent createSouthPanel() {
            JPanel panel = new JPanel();

            JButton okButton = new JButton("OK");
            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (classNameField.getText().isEmpty()) {
                        Messages.showErrorDialog("Please enter a class name.", "Error");
                    } else {
                        close(DialogWrapper.OK_EXIT_CODE);
                    }
                }
            });

            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    close(DialogWrapper.CANCEL_EXIT_CODE);
                }
            });

            panel.add(okButton);
            panel.add(cancelButton);

            return panel;
        }

        public String getClassName() {
            return classNameField.getText().trim();
        }

        public String getFileType() {
            return (String) fileTypeComboBox.getSelectedItem();
        }
    }


    public static String convertToPackageName(String clickedDirectoryPath) {

        clickedDirectoryPath = clickedDirectoryPath.substring(clickedDirectoryPath.indexOf("com/"));

        String packageName = clickedDirectoryPath.replaceAll("/", ".");

        return packageName;
    }

}
