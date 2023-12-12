package com.ouyang.codetemplategenerator.utils.factory;

import com.intellij.openapi.ui.Messages;
import com.ouyang.codetemplategenerator.CodeTemplateGenerator;

import java.io.IOException;

/**
 * 自定义Binder
 */
public class BinderTemplate extends Template {

    @Override
    public void setLayoutName() {
        layoutName = "binder_" + className.replace("Binder", "").toLowerCase();
    }

    @Override
    public void generateCode() {

        try {
            String data = className.replace("Binder", "") + "Data";
            // 将路径转换为包名
            String packageName = convertToPackageName(targetFolderPath);
            String code;
            if (CodeTemplateGenerator.InputDialog.FileForm.JAVA.equals(fileForm)) {
                code = "package " + packageName + ";\n" + "\n" + "import android.view.LayoutInflater;\n" + "import android.view.View;\n" + "import android.view.ViewGroup;\n" +
                        "import android.widget.TextView;\n" + "\n" + "import androidx.annotation.NonNull;\n" + "import androidx.recyclerview.widget.RecyclerView;\n" +
                        "\n" + "import com.ailiao.mosheng.commonlibrary.view.BaseItemViewBinder;\n" + "import com.mosheng.R;\n" +
                        "\n" + "import java.io.Serializable;\n" + "\n" + "public class " + className + " extends BaseItemViewBinder<" + className +
                        "." + data + ", " + className + ".ViewHolder> {\n" + "    private static final String TAG = \"" + className + "\";\n" + "\n" +
                        "    @NonNull\n" + "    @Override\n" + "    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {\n" +
                        "        View view = inflater.inflate(R.layout." + layoutName + ", parent, false);\n" + "        return new ViewHolder(view);\n" + "    }\n" + "\n" +
                        "    @Override\n" + "    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final " + data + " item) {\n" +
                        "        holder.textView.setText(\"xxxx\");\n" + "    }\n" + "\n" + "    public class ViewHolder extends RecyclerView.ViewHolder {\n" +
                        "        TextView textView;\n" + "\n" + "        ViewHolder(View itemView) {\n" + "            super(itemView);\n" +
                        "            textView = itemView.findViewById(R.id.textView);\n" + "        }\n" + "    }\n" + "\n" +
                        "    public static class " + data + " implements Serializable {\n" + "\n" + "    }\n" + "}";
            } else {
                code = "package " + packageName + "\n" + "\n" + "import android.view.LayoutInflater\n" +
                        "import android.view.View\n" + "import android.view.ViewGroup\n" +
                        "import androidx.recyclerview.widget.RecyclerView\n" + "import com.ailiao.mosheng.commonlibrary.view.BaseItemViewBinder\n" +
                        "import com.mosheng.R\n" + "import kotlinx.android.synthetic.main." + layoutName + ".view.*\n" + "import java.io.Serializable\n" + "\n" +
                        "class " + className + " :\n" + "    BaseItemViewBinder<" + className + "." + data + ", " + className + "." + "ViewHolder>() {\n" + "\n" +
                        "    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {\n" +
                        "        return ViewHolder(inflater.inflate(R.layout." + layoutName + ", parent, false))\n" + "    }\n" +
                        "\n" + "    override fun onBindViewHolder(\n" + "        holder: ViewHolder,\n" +
                        "        data: " + data + "\n" + "    ) {\n" +
                        "        holder.itemView.textView.text = \"xxx\"\n" + "    }\n" +
                        "\n" + "\n" + "    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {\n" + "\n" + "    }\n" +
                        "\n" + "    class " + data + " : Serializable {\n" + "\n" + "    }\n" + "\n" + "}";
            }

            write(targetFilePath, code);
        } catch (IOException ex) {
            Messages.showErrorDialog(project, "Failed to generate code: " + ex.getMessage(), "Error");
        }

    }

}
