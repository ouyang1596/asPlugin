package com.ouyang.codetemplategenerator.template.codetemplate;

import com.intellij.openapi.ui.Messages;
import com.ouyang.codetemplategenerator.template.TemplateFactory;
import com.ouyang.codetemplategenerator.template.codetemplate.dialog.CodeInputDialog;

import java.io.IOException;

/**
 * 自定义ViewWithRv
 */
public class ViewWithRvCodeTemplate extends CodeTemplate {

    @Override
    public void setLayoutName() {
        layoutName = "view_" + className.replace("View", "").toLowerCase();
    }

    @Override
    public void generateCode() {

        try {

            CodeTemplate codeTemplate = TemplateFactory.createCodeTemplate(CodeInputDialog.FileType.CUSTOM_BINDER, anActionEvent);
            String binderClassName = className.replace("View", "Binder");
            if (codeTemplate != null) {
                codeTemplate.setClassName(binderClassName);
                codeTemplate.setFileForm(fileForm);
                codeTemplate.generateTemplate();
            }

            String code;
            if (CodeInputDialog.FileForm.JAVA.equals(fileForm)) {
                code = "";
            } else {
                code = "package " + packageName + ";\n" +
                        "\n" +
                        "import android.content.Context\n" +
                        "import android.util.AttributeSet\n" +
                        "import android.view.LayoutInflater\n" +
                        "import android.widget.FrameLayout\n" +
                        "import androidx.recyclerview.widget.LinearLayoutManager\n" +
                        "import com.ailiao.mosheng.commonlibrary.common.DPConstants\n" +
                        "import com.mosheng.R\n" +
                        "import com.mosheng.common.view.decoration.CommItemDecoration\n" +
                        "import kotlinx.android.synthetic.main." + layoutName + ".view.*\n" +
                        "import me.drakeet.multitype.MultiTypeAdapter\n" +
                        "\n" +
                        "class " + className + " @JvmOverloads constructor(\n" +
                        "    context: Context,\n" +
                        "    attrs: AttributeSet? = null,\n" +
                        "    defStyleAttr: Int = 0\n" +
                        ") : FrameLayout(context, attrs, defStyleAttr) {\n" +
                        "\n" +
                        "    var multiTypeAdapter: MultiTypeAdapter? = null\n" +
                        "    val list = mutableListOf<" + binderClassName + "." + codeTemplate.getData() + "?>()\n" +
                        "\n" +
                        "    private fun initView() {\n" +
                        "        initRv()\n" +
                        "    }\n" +
                        "\n" +
                        "    init {\n" +
                        "        LayoutInflater.from(context).inflate(R.layout." + layoutName + ", this)\n" +
                        "        initView()\n" +
                        "    }\n" +
                        "\n" +
                        "    private fun initRv() {\n" +
                        "        recyclerView.addItemDecoration(\n" +
                        "            CommItemDecoration.createHorizontal(\n" +
                        "                context,\n" +
                        "                0,\n" +
                        "                DPConstants.DP_10\n" +
                        "            )\n" +
                        "        )\n" +
                        "\n" +
                        "        multiTypeAdapter = MultiTypeAdapter(list)\n" +
                        "        var binder = " + binderClassName + "()\n" +
                        "        multiTypeAdapter?.register(\n" +
                        "            " + binderClassName + "." + codeTemplate.getData() + "::class.java,\n" +
                        "            binder\n" +
                        "        )\n" +
                        "        var manager = LinearLayoutManager(context)\n" +
                        "        manager.orientation = LinearLayoutManager.HORIZONTAL\n" +
                        "        recyclerView.layoutManager = manager\n" +
                        "        recyclerView.adapter = multiTypeAdapter\n" +
                        "    }\n" +
                        "\n" +
                        "}";
            }

            write(codeFilePath, code);
        } catch (IOException ex) {
            Messages.showErrorDialog(project, "Failed to generate code: " + ex.getMessage(), "Error");
        }

    }

    @Override
    public void generateLayout() {
        try {
            String xmlCode = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<androidx.constraintlayout.widget.ConstraintLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                    "    xmlns:app=\"http://schemas.android.com/apk/res-auto\"\n" +
                    "    xmlns:tools=\"http://schemas.android.com/tools\"\n" +
                    "    android:layout_width=\"match_parent\"\n" +
                    "    android:layout_height=\"match_parent\">\n" +
                    "\n" +
                    "    <androidx.recyclerview.widget.RecyclerView\n" +
                    "        android:id=\"@+id/recyclerView\"\n" +
                    "        android:layout_width=\"0dp\"\n" +
                    "        android:layout_height=\"0dp\"\n" +
                    "        app:layout_constraintBottom_toBottomOf=\"parent\"\n" +
                    "        app:layout_constraintLeft_toLeftOf=\"parent\"\n" +
                    "        app:layout_constraintRight_toRightOf=\"parent\"\n" +
                    "        app:layout_constraintTop_toTopOf=\"parent\" />\n" +
                    "</androidx.constraintlayout.widget.ConstraintLayout>";
            write(layoutFilePath, xmlCode);
        } catch (IOException ex) {
            Messages.showErrorDialog(project, "Failed to generate code: " + ex.getMessage(), "Error");
        }
    }

    @Override
    public void otherAction() {

    }

}
