package com.ouyang.codetemplategenerator.template.restemplate;

import com.intellij.openapi.ui.Messages;

import java.io.IOException;

public class ShapeTemplate extends ResTemplate {
    @Override
    public void otherAction() {

    }

    @Override
    public void setResFileName() {
        resName = "shape_color_" + color.replace("#", "") + "_r_" + radius;
    }

    @Override
    public void generateResFile() {
        try {
            String colorName = handleColorName(color, getColorFilePath());
            String xmlCode = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<shape xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                    "    android:shape=\"rectangle\">\n" +
                    "    <corners android:radius=\""+radius+"dp\" />\n" +
                    "    <solid android:color=\""+colorName+"\" />\n" +
                    "</shape>";
            write(resFilePath, xmlCode);
        } catch (IOException ex) {
            Messages.showErrorDialog(project, "Failed to generate code: " + ex.getMessage(), "Error");
        }
    }

}
