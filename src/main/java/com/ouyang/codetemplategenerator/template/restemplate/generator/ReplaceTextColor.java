package com.ouyang.codetemplategenerator.template.restemplate.generator;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.http.util.TextUtils;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

/**
 * 批量替换textColor
 */
public class ReplaceTextColor extends AnAction {

    private String colorFilePath;

    @Override
    public void actionPerformed(AnActionEvent e) {
//        Project project = e.getProject();
//        if (project == null) {
//            return;
//        }
//
//        handleDialog(e, project);

        Project project = e.getProject();
        if (project == null) {
            return;
        }
        // 获取点击的文件夹位置
        VirtualFile virtualFile = (VirtualFile) e.getDataContext().getData("virtualFile");
        if (virtualFile == null || virtualFile.isDirectory()) {
            return;
        }

        String filePath = virtualFile.getPath();

        String rootFolderPath = filePath.substring(0, filePath.indexOf("/main") + 5);
        String folder = rootFolderPath + "/res/values";
        colorFilePath = folder + "/" + "colors.xml";

        parseXml(filePath);
    }


    public void parseXml(String filePath) {
        try {
            // 创建 DocumentBuilder 对象
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // 加载 XML 文件
            Document doc = builder.parse(new File(filePath));
            doc.setXmlStandalone(true);

            // 获取根元素
            Element rootElement = doc.getDocumentElement();

            // 递归遍历所有元素
            traverseElements(rootElement);

            // 将修改后的Document保存回原文件
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 递归遍历元素
    private void traverseElements(Element element) {
        // 获取元素的所有属性
        NamedNodeMap attributes = element.getAttributes();

        // 遍历属性
        for (int i = 0; i < attributes.getLength(); i++) {
            Node attribute = attributes.item(i);
            String attributeName = attribute.getNodeName();
            String attributeValue = attribute.getNodeValue();

            // 检查属性名是否为 android:textColor
            if (attributeName.equals("android:textColor")) {
                System.out.println("android:textColor value: " + attributeValue);
                if (attributeValue.length() >= 9) {
                    String substring = attributeValue.substring(0, 3);
                    if (substring.equals("#ff")) {
                        attributeValue.replace("#ff", "#");
                    }
                }
                if (attributeValue.contains("#")) {
                    attribute.setNodeValue("@color/" + getColorName(attributeValue, colorFilePath));
                }
            }
        }

        // 获取子元素
        NodeList children = element.getChildNodes();

        // 遍历子元素
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);

            // 检查子节点是否为元素节点
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) child;

                // 递归遍历子元素
                traverseElements(childElement);
            }
        }
    }

    @Nullable
    private String getColorName(String colorValue, String colorsFilePath) {
        String colorName = getInnerColorName(colorValue, colorsFilePath);
        if (colorName != null) return colorName;

        if (TextUtils.isEmpty(colorName)) {
            String substring = colorsFilePath.substring(0, colorsFilePath.indexOf("/src/main"));
            String end = colorsFilePath.substring(colorsFilePath.indexOf("/src/main"));
            String start = substring.substring(0, substring.lastIndexOf("/") + 1);
            if (!"commonlibrary".equals(substring)) {
                colorsFilePath = start + "commonlibrary" + end;
                colorName = getInnerColorName(colorValue, colorsFilePath);
            }
        }
        if (TextUtils.isEmpty(colorName)) {
            // 读取原始文件
            try {
                File inputFile = new File(colorsFilePath);
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
                colorName = "common_c_" + colorValue.replace("#", "");
                String xmlCode = "    <color name=\"" + colorName + "\">" + colorValue + "</color>\n" +
                        "</resources>";
                modifiedContent = modifiedContent.replace("</resources>", xmlCode);

                // 将修改后的内容写回到文件中（使用 UTF-8 编码）
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(colorsFilePath), "UTF-8"));
                writer.write(modifiedContent);
                writer.close();
                return colorName;
            } catch (IOException e) {
                String localizedMessage = e.getLocalizedMessage();
            }
        }
        return colorName;
    }

    private static String getInnerColorName(String colorValue, String filePath) {
        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            NodeList colorNodeList = doc.getElementsByTagName("color");
            for (int i = 0; i < colorNodeList.getLength(); i++) {
                Node node = colorNodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String colorName = element.getAttribute("name");
                    String colorHexValue = element.getTextContent();
                    if (colorHexValue.equalsIgnoreCase(colorValue)) {
                        return colorName;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
