package com.ouyang.codetemplategenerator.utils.factory;

import com.intellij.openapi.ui.Messages;
import com.ouyang.codetemplategenerator.CodeTemplateGenerator;
import com.ouyang.codetemplategenerator.dialog.InputDialog;

import java.io.IOException;

/**
 * 自定义View
 */
public class ViewTemplate extends Template {

    @Override
    public void setLayoutName() {
        layoutName = "view_" + className.replace("View", "").toLowerCase();
    }

    @Override
    public void generateCode() {

        try {

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

            write(codeFilePath, code);
        } catch (IOException ex) {
            Messages.showErrorDialog(project, "Failed to generate code: " + ex.getMessage(), "Error");
        }

    }

    @Override
    public void otherAction() {

    }

}
