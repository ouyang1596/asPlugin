package com.ouyang.codetemplategenerator;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

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
     * 布局文件名字集合
     */
    private HashMap<String, String> layoutNameHashMap = new HashMap<>();

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
        String data = className.replace("Binder", "") + "Data";
        fileType = dialog.getFileType();
        fileForm = dialog.getFileForm();

        targetFolderPath = virtualFile.getPath();
        String form;
        if (InputDialog.FileForm.JAVA.equals(fileForm)) {
            form = "java";
        } else {
            form = "kt";
        }
        String targetFilePath = targetFolderPath + "/" + className + "." + form;
        setLayoutName();
        generateCode(data, targetFilePath);
        generateLayout();
    }

    private void setLayoutName() {
        String layoutName;
        if (InputDialog.FileType.CUSTOM_BINDER.equals(fileType)) {
            layoutName = "binder_" + className.replace("Binder", "").toLowerCase();
        } else if (InputDialog.FileType.CUSTOM_DIALOG.equals(fileType)) {
            layoutName = "dialog_" + className.replace("Dialog", "").toLowerCase();
        } else {
            layoutName = "view_" + className.replace("View", "").toLowerCase();
        }
        layoutNameHashMap.put(fileType, layoutName);
    }

    /**
     * 生成代码
     */
    private void generateCode(String data, String targetFilePath) {
        try {

            // 将路径转换为包名
            String packageName = convertToPackageName(targetFolderPath);
            String code;
            if (InputDialog.FileType.CUSTOM_BINDER.equals(fileType)) {
                code = generateBinderCode(data, packageName);
            } else if (InputDialog.FileType.CUSTOM_DIALOG.equals(fileType)) {
                code = generateDialogCode(packageName);
            } else {
                code = generateViewCode(packageName);
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

    @NotNull
    private String generateBinderCode(String data, String packageName) {
        //布局文件名字
        String layoutName = layoutNameHashMap.get(fileType);
        String code;
        if (InputDialog.FileForm.JAVA.equals(fileForm)) {
            code = "package " + packageName + ";\n" + "\n" + "import android.view.LayoutInflater;\n" + "import android.view.View;\n" + "import android.view.ViewGroup;\n" + "import android.widget.TextView;\n" + "\n" + "import androidx.annotation.NonNull;\n" + "import androidx.recyclerview.widget.RecyclerView;\n" + "\n" + "import com.ailiao.mosheng.commonlibrary.view.BaseItemViewBinder;\n" + "import com.mosheng.R;\n" + "\n" + "import java.io.Serializable;\n" + "\n" + "public class " + className + " extends BaseItemViewBinder<" + className + "." + data + ", " + className + ".ViewHolder> {\n" + "    private static final String TAG = \"" + className + "\";\n" + "\n" + "    @NonNull\n" + "    @Override\n" + "    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {\n" + "        View view = inflater.inflate(R.layout." + layoutName + ", parent, false);\n" + "        return new ViewHolder(view);\n" + "    }\n" + "\n" + "    @Override\n" + "    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final " + data + " item) {\n" + "        holder.textView.setText(\"xxxx\");\n" + "    }\n" + "\n" + "    public class ViewHolder extends RecyclerView.ViewHolder {\n" + "        TextView textView;\n" + "\n" + "        ViewHolder(View itemView) {\n" + "            super(itemView);\n" + "            textView = itemView.findViewById(R.id.textView);\n" + "        }\n" + "    }\n" + "\n" + "    public static class " + data + " implements Serializable {\n" + "\n" + "    }\n" + "}";
        } else {
            code = "package " + packageName + "\n" + "\n" + "import android.view.LayoutInflater\n" + "import android.view.View\n" + "import android.view.ViewGroup\n" + "import androidx.recyclerview.widget.RecyclerView\n" + "import com.ailiao.mosheng.commonlibrary.view.BaseItemViewBinder\n" + "import com.mosheng.R\n" + "import kotlinx.android.synthetic.main." + layoutName + ".view.*\n" + "import java.io.Serializable\n" + "\n" + "class " + className + " :\n" + "    BaseItemViewBinder<" + className + "." + data + ", " + className + "." + "ViewHolder>() {\n" + "\n" + "    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {\n" + "        return ViewHolder(inflater.inflate(R.layout." + layoutName + ", parent, false))\n" + "    }\n" + "\n" + "    override fun onBindViewHolder(\n" + "        holder: ViewHolder,\n" + "        data: " + data + "\n" + "    ) {\n" + "        holder.itemView.textView.text = \"xxx\"\n" + "    }\n" + "\n" + "\n" + "    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {\n" + "\n" + "    }\n" + "\n" + "    class " + data + " : Serializable {\n" + "\n" + "    }\n" + "\n" + "}";
        }
        return code;
    }

    @NotNull
    private String generateViewCode(String packageName) {
        //布局文件名字
        String layoutName = layoutNameHashMap.get(fileType);
        String code;
        if (InputDialog.FileForm.JAVA.equals(fileForm)) {
            code = "package " + packageName + ";\n" +
                    "\n" +
                    "import android.content.Context;\n" +
                    "import android.util.AttributeSet;\n" +
                    "import android.view.LayoutInflater;\n" +
                    "import android.widget.FrameLayout;\n" +
                    "import android.widget.TextView;\n" +
                    "\n" +
                    "import androidx.annotation.NonNull;\n" +
                    "import androidx.annotation.Nullable;\n" +
                    "\n" +
                    "import com.mosheng.R;\n" +
                    "\n" +
                    "public class " + className + " extends FrameLayout {\n" +
                    "    private TextView textView;\n" +
                    "\n" +
                    "    public " + className + "(@NonNull Context context) {\n" +
                    "        this(context, null);\n" +
                    "    }\n" +
                    "\n" +
                    "    public " + className + "(@NonNull Context context, @Nullable AttributeSet attrs) {\n" +
                    "        this(context, attrs, 0);\n" +
                    "    }\n" +
                    "\n" +
                    "    public " + className + "(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {\n" +
                    "        super(context, attrs, defStyleAttr);\n" +
                    "        LayoutInflater.from(context).inflate(R.layout." + layoutName + ", this);\n" +
                    "        initView();\n" +
                    "    }\n" +
                    "\n" +
                    "    private void initView() {\n" +
                    "        textView = findViewById(R.id.textView);\n" +
                    "    }\n" +
                    "}";
        } else {
            code = "package " + packageName + "\n" +
                    "\n" +
                    "import android.content.Context\n" +
                    "import android.util.AttributeSet\n" +
                    "import android.view.LayoutInflater\n" +
                    "import android.widget.FrameLayout\n" +
                    "import android.widget.TextView\n" +
                    "import com.mosheng.R\n" +
                    "\n" +
                    "class " + className + " @JvmOverloads constructor(\n" +
                    "    context: Context,\n" +
                    "    attrs: AttributeSet? = null,\n" +
                    "    defStyleAttr: Int = 0\n" +
                    ") : FrameLayout(context, attrs, defStyleAttr) {\n" +
                    "    private var textView: TextView? = null\n" +
                    "    private fun initView() {\n" +
                    "        textView = findViewById(R.id.textView)\n" +
                    "    }\n" +
                    "\n" +
                    "    init {\n" +
                    "        LayoutInflater.from(context).inflate(R.layout." + layoutName + ", this)\n" +
                    "        initView()\n" +
                    "    }\n" +
                    "}";
        }
        return code;
    }

    @NotNull
    private String generateDialogCode(String packageName) {
        //布局文件名字
        String layoutName = layoutNameHashMap.get(fileType);
        String code;
        if (InputDialog.FileForm.JAVA.equals(fileForm)) {
            code = "";
        } else {
            code = "package " + packageName + "\n" +
                    "\n" +
                    "import android.content.Context\n" +
                    "import android.os.Bundle\n" +
                    "import android.view.View\n" +
                    "import android.view.ViewGroup\n" +
                    "import com.ailiao.mosheng.commonlibrary.common.DPConstants\n" +
                    "import com.ailiao.mosheng.commonlibrary.view.dialog.BaseDialog\n" +
                    "import com.mosheng.R\n" +
                    "import kotlinx.android.synthetic.main." + layoutName + ".*\n" +
                    "\n" +
                    "class " + className + "(val dialogContext: Context) : BaseDialog(dialogContext, R.style.commonMyDialog2) {\n" +
                    "    var onDialogClickListener: OnDialogClickListener<Any?>? = null\n" +
                    "\n" +
                    "    override fun onCreate(savedInstanceState: Bundle?) {\n" +
                    "        super.onCreate(savedInstanceState)\n" +
                    "        setContentView(\n" +
                    "                View.inflate(dialogContext, R.layout." + layoutName + ", null),\n" +
                    "                ViewGroup.LayoutParams(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT)\n" +
                    "        )\n" +
                    "        initView()\n" +
                    "        initData()\n" +
                    "    }\n" +
                    "\n" +
                    "\n" +
                    "    val dialogWidth: Int\n" +
                    "        get() = screenWidth - DPConstants.DP_60\n" +
                    "\n" +
                    "    private fun initView() {\n" +
                    "\n" +
                    "    }\n" +
                    "\n" +
                    "    private fun initData() {\n" +
                    "        textView.text = \"xxxx\"\n" +
                    "    }\n" +
                    "\n" +
                    "\n" +
                    "    override fun onClick(v: View) {\n" +
                    "        super.onClick(v)\n" +
                    "\n" +
                    "    }\n" +
                    "\n" +
                    "    interface OnDialogClickListener<T> {\n" +
                    "        fun onClick(view: View?, item: T)\n" +
                    "    }\n" +
                    "\n" +
                    "\n" +
                    "    init {\n" +
                    "        window?.setBackgroundDrawableResource(android.R.color.transparent)\n" +
                    "        setCanceledOnTouchOutside(false)\n" +
                    "        setCancelable(false)\n" +
                    "    }\n" +
                    "}";
        }
        return code;
    }

    /**
     * 生成对应的布局
     */
    private void generateLayout() {
        try {
            String layoutName = layoutNameHashMap.get(fileType);
            String xmlCode = generateViewLayout();

            targetFolderPath = targetFolderPath.substring(0, targetFolderPath.indexOf("/java"));
            targetFolderPath += "/res/layout";
            String targetFilePath = targetFolderPath + "/" + layoutName + ".xml";


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


    @NotNull
    private static String generateViewLayout() {
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

    private static class InputDialog extends DialogWrapper {
        interface FileForm {
            String JAVA = "java";
            String KOTLIN = "kotlin";
        }

        interface FileType {
            String CUSTOM_VIEW = "custom_view";
            String CUSTOM_BINDER = "custom_binder";

            String CUSTOM_DIALOG = "custom_dialog";
        }

        private JTextField classNameField;
        /**
         * 文件格式
         */
        private JComboBox<String> fileFormComboBox;

        /**
         * 文件类型
         */
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

            JPanel fileFormPanel = new JPanel(new BorderLayout());
            JLabel fileFormLabel = new JLabel("File Form:");
            fileFormComboBox = new JComboBox<>(new String[]{FileForm.KOTLIN, FileForm.JAVA});
            fileFormPanel.add(fileFormLabel, BorderLayout.WEST);
            fileFormPanel.add(fileFormComboBox, BorderLayout.CENTER);

            JPanel fileTypePanel = new JPanel(new BorderLayout());
            JLabel fileTypeLabel = new JLabel("File Type:");
            fileTypeComboBox = new JComboBox<>(new String[]{FileType.CUSTOM_VIEW, FileType.CUSTOM_BINDER, FileType.CUSTOM_DIALOG});
            fileTypePanel.add(fileTypeLabel, BorderLayout.WEST);
            fileTypePanel.add(fileTypeComboBox, BorderLayout.CENTER);

            panel.add(classNamePanel, BorderLayout.NORTH);
            panel.add(fileTypePanel, BorderLayout.CENTER);
            panel.add(fileFormPanel, BorderLayout.EAST);

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

        public String getFileForm() {
            return (String) fileFormComboBox.getSelectedItem();
        }

        public String getFileType() {
            return (String) fileTypeComboBox.getSelectedItem();
        }
    }


    public static String convertToPackageName(String clickedDirectoryPath) {


        clickedDirectoryPath = clickedDirectoryPath.substring(clickedDirectoryPath.indexOf("java/") + 5);

        String packageName = clickedDirectoryPath.replaceAll("/", ".");

        return packageName;
    }

}
