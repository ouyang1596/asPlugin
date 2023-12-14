package com.ouyang.codetemplategenerator.template.restemplate;

import com.ouyang.codetemplategenerator.template.Template;
import org.apache.http.util.TextUtils;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;

public abstract class ResTemplate extends Template {
    private static final String translucent = "#00000000";
    /**
     * 资源文件名字
     */
    protected String resName;
    /**
     * 需要生成的资源文件位置路径
     */
    protected String resFilePath;

    /**
     * 描边颜色
     */
    protected String strokeColor;

    /**
     * 描边宽度
     */
    protected String strokeWidth;
    /**
     * 开始颜色
     */
    protected String color;
    /**
     * 开始颜色
     */
    protected String startColor;
    /**
     * 结束颜色
     */
    protected String endColor;
    /**
     * 角度
     */
    protected String angle = "0";
    /**
     * 圆角
     */
    protected String radius = "0";

    /**
     * 生成资源文件名称
     */
    public abstract void setResFileName();

    /**
     * 生成资源文件
     */
    public abstract void generateResFile();

    @Override
    public void generateTemplate() {
        setResFileName();
        setResFilePath();
        generateResFile();
        otherAction();
    }

    public String getResName() {
        return resName;
    }

    public void setStrokeColor(String strokeColor) {
        this.strokeColor = strokeColor;
    }

    public void setStrokeWidth(String strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public void setColor(String color) {
        if (TextUtils.isEmpty(color)) {
            color = translucent;
        }
        this.color = color;
    }

    public void setStartColor(String startColor) {
        if (TextUtils.isEmpty(startColor)) {
            startColor = translucent;
        }
        this.startColor = startColor;
    }

    public void setEndColor(String endColor) {
        if (TextUtils.isEmpty(endColor)) {
            endColor = translucent;
        }
        this.endColor = endColor;
    }

    public void setAngle(String angle) {
        if (TextUtils.isEmpty(angle)) {
            angle = "0";
        }
        this.angle = angle;
    }

    public void setRadius(String radius) {
        if (TextUtils.isEmpty(radius)) {
            radius = "0";
        }
        this.radius = radius;
    }

    /**
     * 生成资源文件路径
     */
    protected void setResFilePath() {
        String folder = rootFolderPath + "/res/drawable";
        resFilePath = folder + "/" + resName + ".xml";
    }

    /**
     * 获取color文件的路径
     */
    protected String getColorFilePath() {
        String folder = rootFolderPath + "/res/values";
        String colorFilePath = folder + "/" + "colors.xml";
        return colorFilePath;
    }

    /**
     * 处理色值对应的name的值
     */
    protected String handleColorName(String colorValue, String filePath) {
        String colorName = getColorName(colorValue, filePath);
        if (TextUtils.isEmpty(colorName)) {
            return colorValue;
        } else {
            return String.format("@color/%s", colorName);
        }
    }

    @Nullable
    private String getColorName(String colorValue, String colorsFilePath) {
        String colorName = getInnerColorName(colorValue, colorsFilePath);
        if (colorName != null) return colorName;

        //由于不同模块之间的xml资源不能引用，所以注释掉此段代码
//        if (TextUtils.isEmpty(colorName)) {
//            String substring = colorsFilePath.substring(0, colorsFilePath.indexOf("/src/main"));
//            String end = colorsFilePath.substring(colorsFilePath.indexOf("/src/main"));
//            String start = substring.substring(0, substring.lastIndexOf("/") + 1);
//            if (!"commonlibrary".equals(substring)) {
//                colorsFilePath = start + "commonlibrary" + end;
//                colorName = getInnerColorName(colorValue, colorsFilePath);
//            }
//        }
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


    @Nullable
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
