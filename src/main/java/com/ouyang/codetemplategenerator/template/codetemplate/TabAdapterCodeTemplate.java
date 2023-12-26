package com.ouyang.codetemplategenerator.template.codetemplate;

import com.intellij.openapi.ui.Messages;
import com.ouyang.codetemplategenerator.template.codetemplate.dialog.CodeInputDialog;

import java.io.IOException;

/**
 * 自定义TabAdapter
 */
public class TabAdapterCodeTemplate extends CodeTemplate {

    @Override
    public void setLayoutName() {
        layoutName = "tab_item_" + className.replace("TabAdapter", "").toLowerCase();
    }

    @Override
    public void generateCode() {

        try {
            data = className.replace("TabAdapter", "") + "Data";
            String code;
            if (CodeInputDialog.FileForm.JAVA.equals(fileForm)) {
                code = "";
            } else {
                code = "package " + packageName + "\n" +
                        "\n" +
                        "import android.content.Context\n" +
                        "import android.view.LayoutInflater\n" +
                        "import android.view.View\n" +
                        "import android.view.ViewGroup\n" +
                        "import android.widget.TextView\n" +
                        "import com.ailiao.mosheng.commonlibrary.view.tablayout.XMTabItem\n" +
                        "import com.ailiao.mosheng.commonlibrary.view.tablayout.XMTabLayoutAdapter\n" +
                        "import com.mosheng.R\n" +
                        "import com.mosheng.control.init.ApplicationBase\n" +
                        "\n" +
                        "class " + className + " :\n" +
                        "    XMTabLayoutAdapter<" + className + "." + data + "?, " + className + ".ViewHolder?>() {\n" +
                        "    override fun createViewHolder(context: Context, parent: ViewGroup): ViewHolder {\n" +
                        "        val itemView = LayoutInflater.from(context).inflate(R.layout." + layoutName + ", parent, false)\n" +
                        "        return ViewHolder(itemView)\n" +
                        "    }\n" +
                        "\n" +
                        "    override fun onBindViewHolder(holder: ViewHolder?, position: Int, data: " + data + "?) {\n" +
                        "        holder?.textView?.text = data?.name\n" +
                        "    }\n" +
                        "\n" +
                        "    override fun onTabSelected(holder: ViewHolder?, position: Int, data: " + data + "?) {\n" +
                        "        holder?.textView?.setTextColor(ApplicationBase.ctx.resources.getColor(R.color.black))\n" +
                        "    }\n" +
                        "\n" +
                        "    override fun onTabUnselected(holder: ViewHolder?, position: Int, data: " + data + "?) {\n" +
                        "        holder?.textView?.setTextColor(ApplicationBase.ctx.resources.getColor(R.color.white))\n" +
                        "    }\n" +
                        "\n" +
                        "    class ViewHolder(itemView: View) : XMTabLayoutAdapter.ViewHolder(itemView) {\n" +
                        "        var textView: TextView\n" +
                        "\n" +
                        "        init {\n" +
                        "            textView = itemView.findViewById(R.id.textView)\n" +
                        "        }\n" +
                        "    }\n" +
                        "\n" +
                        "    data class " + data + "(var name2: String?, var title: String?) : XMTabItem {\n" +
                        "        override fun getName(): String? {\n" +
                        "            return name2\n" +
                        "        }\n" +
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
    public void otherAction() {

    }

}
