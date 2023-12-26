package com.ouyang.codetemplategenerator.template.codetemplate;

import com.intellij.openapi.ui.Messages;
import com.ouyang.codetemplategenerator.template.TemplateFactory;
import com.ouyang.codetemplategenerator.template.codetemplate.dialog.CodeInputDialog;
import org.apache.http.util.TextUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;

/**
 * 自定义ListActivity模板
 */
public class ViewPagerActivityCodeTemplate extends CodeTemplate {

    @Override
    public void setLayoutName() {
        layoutName = "activity_" + className.replace("Activity", "").toLowerCase();
    }

    @Override
    public void generateCode() {
        try {

            CodeTemplate fragmentCodeTemplate = TemplateFactory.createCodeTemplate(CodeInputDialog.FileType.CUSTOM_FRAGMENT, anActionEvent);
            String fragmentClassName = className.replace("Activity", "Fragment");
            if (fragmentCodeTemplate != null) {
                fragmentCodeTemplate.setClassName(fragmentClassName);
                fragmentCodeTemplate.setFileForm(fileForm);
                fragmentCodeTemplate.generateTemplate();
            }

            CodeTemplate tabAdapterCodeTemplate = TemplateFactory.createCodeTemplate(CodeInputDialog.FileType.CUSTOM_TAB_ADAPTER, anActionEvent);
            String adapterClassName = className.replace("Activity", "TabAdapter");
            if (tabAdapterCodeTemplate != null) {
                tabAdapterCodeTemplate.setClassName(adapterClassName);
                tabAdapterCodeTemplate.setFileForm(fileForm);
                tabAdapterCodeTemplate.generateTemplate();
            }

            String code;
            if (CodeInputDialog.FileForm.JAVA.equals(fileForm)) {
                code = "";
            } else {
                code = "package " + packageName + "\n" +
                        "\n" +
                        "import android.os.Bundle\n" +
                        "import androidx.fragment.app.Fragment\n" +
                        "import androidx.fragment.app.FragmentActivity\n" +
                        "import com.ailiao.mosheng.commonlibrary.view.tablayout.XMTabLayout\n" +
                        "import com.ailiao.mosheng.commonlibrary.view.viewpager.BasePagerFragment\n" +
                        "import com.mosheng.R\n" +
                        "import com.mosheng.common.view.XMTitleView\n" +
                        "import com.mosheng.find.view.NoScrollViewPager\n" +
                        "import com.mosheng.view.BaseMoShengActivity\n" +
                        "import com.mosheng.view.pager.BaseFragmentPagerAdapter\n" +
                        "\n" +
                        "class " + className + " : BaseMoShengActivity() {\n" +
                        "\n" +
                        "    var xmTitleView: XMTitleView? = null\n" +
                        "    var xmTabLayout: XMTabLayout? = null\n" +
                        "    var tabAdapter: " + adapterClassName + "? = null\n" +
                        "    var viewpager: NoScrollViewPager? = null\n" +
                        "    var testPagerAdapter: TestPagerAdapter? = null\n" +
                        "\n" +
                        "    override fun onCreate(savedInstanceState: Bundle?) {\n" +
                        "        super.onCreate(savedInstanceState)\n" +
                        "        setContentView(R.layout." + layoutName + ")\n" +
                        "\n" +
                        "        initTitle()\n" +
                        "\n" +
                        "        xmTabLayout = findViewById(R.id.xmTabLayout)\n" +
                        "        viewpager = findViewById(R.id.viewpager)\n" +
                        "\n" +
                        "        var list = mutableListOf<" + adapterClassName + "." + tabAdapterCodeTemplate.getData() + "?>()\n" +
                        "        list.add(" + adapterClassName + "." + tabAdapterCodeTemplate.getData() + "(\"name1\", \"title1\"))\n" +
                        "        list.add(" + adapterClassName + "." + tabAdapterCodeTemplate.getData() + "(\"name2\", \"title2\"))\n" +
                        "\n" +
                        "        viewpager?.offscreenPageLimit = list.size\n" +
                        "        testPagerAdapter = TestPagerAdapter(this)\n" +
                        "        testPagerAdapter?.setTypeList(list as List<" + adapterClassName + "." + tabAdapterCodeTemplate.getData() + "?>?)\n" +
                        "        viewpager?.adapter = testPagerAdapter\n" +
                        "        xmTabLayout?.setupWithViewPager(viewpager)\n" +
                        "        tabAdapter = " + adapterClassName + "()\n" +
                        "        tabAdapter?.tabList = list\n" +
                        "        xmTabLayout?.setAdapter(tabAdapter)\n" +
                        "    }\n" +
                        "\n" +
                        "    private fun initTitle() {\n" +
                        "        xmTitleView = findViewById(R.id.xmTitleView)\n" +
                        "        xmTitleView?.subTitle?.tv_title?.text = \"xxxxx\"\n" +
                        "        xmTitleView?.subTitle?.iv_left?.setOnClickListener {\n" +
                        "            finish()\n" +
                        "        }\n" +
                        "    }\n" +
                        "\n" +
                        "    class TestPagerAdapter(fragmentActivity: FragmentActivity?) :\n" +
                        "        BaseFragmentPagerAdapter<" + adapterClassName + "." + tabAdapterCodeTemplate.getData() + "?>(fragmentActivity) {\n" +
                        "\n" +
                        "        override fun getPageTitle(\n" +
                        "            position: Int,\n" +
                        "            data: " + adapterClassName + "." + tabAdapterCodeTemplate.getData() + "?\n" +
                        "        ): CharSequence {\n" +
                        "            return data?.name ?: \"\"\n" +
                        "        }\n" +
                        "\n" +
                        "        override fun generateFragment(\n" +
                        "            position: Int,\n" +
                        "            data: " + adapterClassName + "." + tabAdapterCodeTemplate.getData() + "?\n" +
                        "        ): Fragment {\n" +
                        "            val bundle = Bundle()\n" +
                        "            val clazz: Class<out Fragment>\n" +
                        "\n" +
                        "            if (\"test\" == data?.name) {\n" +
                        "                clazz = " + fragmentClassName + "::class.java\n" +
                        "            } else {\n" +
                        "                clazz = " + fragmentClassName + "::class.java\n" +
                        "            }\n" +
                        "            bundle.putString(\"name\", data?.name)\n" +
                        "            return BasePagerFragment.newInstance(mContext, clazz, bundle, position == 0)\n" +
                        "        }\n" +
                        "\n" +
                        "    }\n" +
                        "\n" +
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
            String xmlCode = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<androidx.constraintlayout.widget.ConstraintLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                    "    xmlns:app=\"http://schemas.android.com/apk/res-auto\"\n" +
                    "    android:layout_width=\"match_parent\"\n" +
                    "    android:layout_height=\"match_parent\">\n" +
                    "\n" +
                    "    <com.mosheng.common.view.XMTitleView\n" +
                    "        android:id=\"@+id/xmTitleView\"\n" +
                    "        android:layout_width=\"0dp\"\n" +
                    "        android:layout_height=\"wrap_content\"\n" +
                    "        app:layout_constraintLeft_toLeftOf=\"parent\"\n" +
                    "        app:layout_constraintRight_toRightOf=\"parent\"\n" +
                    "        app:layout_constraintTop_toTopOf=\"parent\" />\n" +
                    "\n" +
                    "    <com.ailiao.mosheng.commonlibrary.view.tablayout.XMTabLayout\n" +
                    "        android:id=\"@+id/xmTabLayout\"\n" +
                    "        android:layout_width=\"0dp\"\n" +
                    "        android:layout_height=\"80dp\"\n" +
                    "        android:layout_marginLeft=\"16dp\"\n" +
                    "        android:layout_marginTop=\"10dp\"\n" +
                    "        android:layout_marginRight=\"16dp\"\n" +
                    "        app:layout_constraintLeft_toLeftOf=\"parent\"\n" +
                    "        app:layout_constraintRight_toRightOf=\"parent\"\n" +
                    "        app:layout_constraintTop_toBottomOf=\"@+id/xmTitleView\"\n" +
                    "        app:tabIndicatorHeight=\"0dp\"\n" +
                    "        app:tabMode=\"fixed\"\n" +
                    "        app:tabPaddingEnd=\"0dp\"\n" +
                    "        app:tabPaddingStart=\"0dp\" />\n" +
                    "\n" +
                    "    <com.mosheng.find.view.NoScrollViewPager\n" +
                    "        android:id=\"@+id/viewpager\"\n" +
                    "        android:layout_width=\"0dp\"\n" +
                    "        android:layout_height=\"0dp\"\n" +
                    "        app:layout_constraintBottom_toBottomOf=\"parent\"\n" +
                    "        app:layout_constraintLeft_toLeftOf=\"parent\"\n" +
                    "        app:layout_constraintRight_toRightOf=\"parent\"\n" +
                    "        app:layout_constraintTop_toBottomOf=\"@+id/xmTabLayout\" />\n" +
                    "</androidx.constraintlayout.widget.ConstraintLayout>";
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
