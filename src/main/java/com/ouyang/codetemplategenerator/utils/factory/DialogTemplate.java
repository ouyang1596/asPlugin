package com.ouyang.codetemplategenerator.utils.factory;

import com.intellij.openapi.ui.Messages;
import com.ouyang.codetemplategenerator.CodeTemplateGenerator;

import java.io.IOException;

/**
 * 自定义Dialog
 */
public class DialogTemplate extends Template {

    @Override
    public void setLayoutName() {
        layoutName = "dialog_" + className.replace("Dialog", "").toLowerCase();
    }

    @Override
    public void generateCode() {

        try {
            String code;
            if (CodeTemplateGenerator.InputDialog.FileForm.JAVA.equals(fileForm)) {
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

            write(codeFilePath, code);
        } catch (IOException ex) {
            Messages.showErrorDialog(project, "Failed to generate code: " + ex.getMessage(), "Error");
        }

    }

}
