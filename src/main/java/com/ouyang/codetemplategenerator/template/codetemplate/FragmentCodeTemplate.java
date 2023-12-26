package com.ouyang.codetemplategenerator.template.codetemplate;

import com.intellij.openapi.ui.Messages;
import com.ouyang.codetemplategenerator.template.codetemplate.dialog.CodeInputDialog;

import java.io.IOException;

/**
 * 自定义View
 */
public class FragmentCodeTemplate extends CodeTemplate {

    @Override
    public void setLayoutName() {
        layoutName = "fragment_" + className.replace("Fragment", "").toLowerCase();
    }

    @Override
    public void generateCode() {

        try {

            String code;
            if (CodeInputDialog.FileForm.JAVA.equals(fileForm)) {
                code = "";
            } else {
                code = "package " + packageName + "\n" +
                        "\n" +
                        "import android.os.Bundle\n" +
                        "import android.view.LayoutInflater\n" +
                        "import android.view.View\n" +
                        "import android.view.ViewGroup\n" +
                        "import com.mosheng.R\n" +
                        "import com.mosheng.nearby.view.fragment.BaseLazyFragment\n" +
                        "\n" +
                        "private const val ARG_PARAM1 = \"param1\"\n" +
                        "\n" +
                        "class " + className + " : BaseLazyFragment() {\n" +
                        "    private var param1: String? = null\n" +
                        "\n" +
                        "    private var viewRoot: View? = null\n" +
                        "\n" +
                        "    override fun onCreate(savedInstanceState: Bundle?) {\n" +
                        "        super.onCreate(savedInstanceState)\n" +
                        "        arguments?.let {\n" +
                        "            param1 = it.getString(ARG_PARAM1)\n" +
                        "        }\n" +
                        "    }\n" +
                        "\n" +
                        "    override fun onCreateView(\n" +
                        "        inflater: LayoutInflater, container: ViewGroup?,\n" +
                        "        savedInstanceState: Bundle?\n" +
                        "    ): View? {\n" +
                        "        if (null != viewRoot) {\n" +
                        "            val parent = viewRoot?.parent as ViewGroup\n" +
                        "            parent?.removeView(viewRoot)\n" +
                        "        } else {\n" +
                        "            viewRoot = inflater.inflate(R.layout." + layoutName + ", container, false)\n" +
                        "            initView()\n" +
                        "        }\n" +
                        "\n" +
                        "        return viewRoot\n" +
                        "    }\n" +
                        "\n" +
                        "    private fun initView() {\n" +
                        "        viewRoot?.findViewById<TextView>(R.id.textView)?.setText(\"test\" + hashCode())\n" +
                        "    }\n" +
                        "\n" +
                        "    override fun onLazyLoadVisible() {\n" +
                        "    }\n" +
                        "\n" +
                        "    override fun onLazyLoadInvisible() {\n" +
                        "    }\n" +
                        "\n" +
                        "    companion object {\n" +
                        "        @JvmStatic\n" +
                        "        fun newInstance(param1: String) =\n" +
                        "            " + className + "().apply {\n" +
                        "                arguments = Bundle().apply {\n" +
                        "                    putString(ARG_PARAM1, param1)\n" +
                        "                }\n" +
                        "            }\n" +
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
