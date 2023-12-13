package com.ouyang.codetemplategenerator.template.restemplate;

import com.intellij.openapi.ui.Messages;

import java.io.IOException;

public class StrokeTemplate extends ResTemplate {
    @Override
    public void otherAction() {

    }

    @Override
    public void setResFileName() {
        resName = "stroke_sc_" + strokeColor.replace("#", "") + "_sw_" + strokeWidth + "_c_" + color.replace("#", "") + "_r_" + radius;
    }

    @Override
    public void generateResFile() {
        try {
            String colorName = handleColorName(color, getColorFilePath());
            String strokeColorName = handleColorName(strokeColor, getColorFilePath());
            String xmlCode = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<shape xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                    "    android:shape=\"rectangle\">\n" +
                    "    <corners android:radius=\"" + radius + "dp\" />\n" +
                    "    <solid android:color=\"" + colorName + "\" />\n" +
                    "    <stroke\n" +
                    "        android:width=\"" + strokeWidth + "dp\"\n" +
                    "        android:color=\"" + strokeColorName + "\" />\n" +
                    "</shape>";
            write(resFilePath, xmlCode);
        } catch (IOException ex) {
            Messages.showErrorDialog(project, "Failed to generate code: " + ex.getMessage(), "Error");
        }
    }

}
