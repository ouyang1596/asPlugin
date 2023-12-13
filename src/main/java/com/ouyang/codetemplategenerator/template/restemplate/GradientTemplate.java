package com.ouyang.codetemplategenerator.template.restemplate;

import com.intellij.openapi.ui.Messages;

import java.io.IOException;

public class GradientTemplate extends ResTemplate {
    @Override
    public void otherAction() {

    }

    @Override
    public void setResFileName() {
        resName = "gradient_s_" + startColor.replace("#", "") + "_e_" + endColor.replace("#", "") + "_a_" + angle + "_r_" + radius;
    }

    @Override
    public void generateResFile() {
        try {
            String startColorName = handleColorName(startColor, getColorFilePath());
            String endColorName = handleColorName(endColor, getColorFilePath());
            String xmlCode = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<shape xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                    "    android:shape=\"rectangle\">\n" +
                    "\n" +
                    "    <gradient\n" +
                    "        android:angle=\"" + angle + "\"\n" +
                    "        android:endColor=\"" + endColorName + "\"\n" +
                    "        android:startColor=\"" + startColorName + "\" />\n" +
                    "    <corners android:radius=\"" + radius + "dp\" />\n" +
                    "</shape>";
            write(resFilePath, xmlCode);
        } catch (IOException ex) {
            Messages.showErrorDialog(project, "Failed to generate code: " + ex.getMessage(), "Error");
        }
    }

}
