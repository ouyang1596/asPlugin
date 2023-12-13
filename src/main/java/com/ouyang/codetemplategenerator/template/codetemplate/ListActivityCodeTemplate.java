package com.ouyang.codetemplategenerator.template.codetemplate;

import com.intellij.openapi.ui.Messages;
import com.ouyang.codetemplategenerator.template.codetemplate.dialog.CodeInputDialog;
import com.ouyang.codetemplategenerator.template.TemplateFactory;
import org.apache.http.util.TextUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;

/**
 * 自定义ListActivity模板
 */
public class ListActivityCodeTemplate extends CodeTemplate {

    @Override
    public void setLayoutName() {
        layoutName = "activity_" + className.replace("Activity", "").toLowerCase();
    }

    @Override
    public void generateCode() {
        try {

            CodeTemplate codeTemplate = TemplateFactory.createCodeTemplate(CodeInputDialog.FileType.CUSTOM_BINDER, anActionEvent);
            String binderClassName = className.replace("Activity", "Binder");
            if (codeTemplate != null) {
                codeTemplate.setClassName(binderClassName);
                codeTemplate.setFileForm(fileForm);
                codeTemplate.generateTemplate();
            }

            String code;
            if (CodeInputDialog.FileForm.JAVA.equals(fileForm)) {
                code = "";
            } else {
                code = "package " + packageName + "\n" +
                        "\n" +
                        "import android.os.Bundle\n" +
                        "import androidx.recyclerview.widget.LinearLayoutManager\n" +
                        "import com.mosheng.R\n" +
                        "import com.mosheng.common.util.appbar.AppBar\n" +
                        "import com.mosheng.view.BaseMoShengActivity\n" +
                        "import kotlinx.android.synthetic.main." + layoutName + ".*\n" +
                        "import me.drakeet.multitype.MultiTypeAdapter\n" +
                        "\n" +
                        "class " + className + " : BaseMoShengActivity() {\n" +
                        "\n" +
                        "    var multiTypeAdapter: MultiTypeAdapter? = null\n" +
                        "    val list = mutableListOf<Any?>()\n" +
                        "\n" +
                        "    override fun onCreate(savedInstanceState: Bundle?) {\n" +
                        "        super.onCreate(savedInstanceState)\n" +
                        "        setContentView(R.layout." + layoutName + ")\n" +
                        "        AppBar.setStatusBarFullTransparent(this)\n" +
                        "        AppBar.setBarHeight(findViewById(R.id.statusBarTintView))\n" +
                        "\n" +
                        "        initRv()\n" +
                        "    }\n" +
                        "\n" +
                        "    private fun initRv() {\n" +
                        "        list.add("+binderClassName+"."+ codeTemplate.getData()+"())\n" +
                        "        multiTypeAdapter = MultiTypeAdapter(list)\n" +
                        "        multiTypeAdapter?.register("+binderClassName+"."+ codeTemplate.getData()+"::class.java, "+binderClassName+"())\n" +
                        "        var manager = LinearLayoutManager(this)\n" +
                        "        manager.orientation = LinearLayoutManager.VERTICAL\n" +
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
    public void otherAction() {
        editAndroidManifest();
    }

    @Override
    public void generateLayout() {
        try {
            String xmlCode = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + "<androidx.constraintlayout.widget.ConstraintLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" + "    xmlns:app=\"http://schemas.android.com/apk/res-auto\"\n" + "    xmlns:tools=\"http://schemas.android.com/tools\"\n" + "    android:layout_width=\"match_parent\"\n" + "    android:layout_height=\"match_parent\"\n" + "    android:background=\"@color/blue\">\n" + "\n" + "    <View\n" + "        android:id=\"@+id/statusBarTintView\"\n" + "        android:layout_width=\"0dp\"\n" + "        android:layout_height=\"0dp\"\n" + "        app:layout_constraintLeft_toLeftOf=\"parent\"\n" + "        app:layout_constraintRight_toRightOf=\"parent\"\n" + "        app:layout_constraintTop_toTopOf=\"parent\" />\n" + "\n" + "    <androidx.recyclerview.widget.RecyclerView\n" + "        android:id=\"@+id/recyclerView\"\n" + "        android:layout_width=\"0dp\"\n" + "        android:layout_height=\"0dp\"\n" + "        app:layout_constraintBottom_toBottomOf=\"parent\"\n" + "        app:layout_constraintLeft_toLeftOf=\"parent\"\n" + "        app:layout_constraintRight_toRightOf=\"parent\"\n" + "        app:layout_constraintTop_toBottomOf=\"@+id/statusBarTintView\" />\n" + "</androidx.constraintlayout.widget.ConstraintLayout>";
            write(layoutFilePath, xmlCode);
        } catch (IOException ex) {
            Messages.showErrorDialog(project, "Failed to generate code: " + ex.getMessage(), "Error");
        }
    }

    /**
     * 修改AndroidManifest
     */
    public void editAndroidManifest() {
        try {
            // 读取XML文件
            File xmlFile = new File(androidManifestFilePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            // 获取根元素 <manifest>
            Element manifestElement = doc.getDocumentElement();
            // 获取 package 属性值
            String package_ = manifestElement.getAttribute("package");

            String pName;
            if (!TextUtils.isEmpty(package_)) {
                pName = packageName.replace(package_, "");
            } else {
                pName = packageName;
            }

            // 读取原始文件
            File inputFile = new File(androidManifestFilePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF-8"));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(System.lineSeparator());
            }
            reader.close();

            // 在 <application> 元素末尾插入新的 <activity> 代码段
            String modifiedContent = stringBuilder.toString();
            String newActivityCode = "<activity\n" +
                    "            android:name=\"" + pName + "." + className + "\"\n" +
                    "            android:screenOrientation=\"portrait\"\n" +
                    "            android:theme=\"@style/commonAppTheme\" />\n" +
                    "    </application>";
            modifiedContent = modifiedContent.replace("</application>", newActivityCode);

            // 将修改后的内容写回到文件中（使用 UTF-8 编码）
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(androidManifestFilePath), "UTF-8"));
            writer.write(modifiedContent);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
