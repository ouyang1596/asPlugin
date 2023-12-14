package com.ouyang.codetemplategenerator.template.restemplate;

import com.intellij.openapi.ui.Messages;
import org.apache.http.util.TextUtils;

import java.io.IOException;

public class SelectorTemplate extends ResTemplate {
    /**
     * 资源标识常量
     */
    public interface ResFlag {
        int DRAWABLE = 1;
        int COLOR = 2;
    }

    /**
     * selector状态
     */
    public interface SelectorState {
        int PRESSED = 1;
        int SELECTED = 2;
        int ENABLE = 3;
    }

    /**
     * 按下资源
     */
    protected String pressRes;

    /**
     * 默认资源
     */
    protected String normalRes;
    /**
     * 资源标识
     */
    private int resFlag = ResFlag.DRAWABLE;

    /**
     * 状态
     */
    private int selectorState = SelectorState.PRESSED;

    public int getSelectorState() {
        return selectorState;
    }

    public void setSelectorState(int selectorState) {
        this.selectorState = selectorState;
    }

    public void setPressRes(String pressRes) {
        this.pressRes = pressRes;
    }

    public void setNormalRes(String normalRes) {
        this.normalRes = normalRes;
    }

    public void setResFlag(int resFlag) {
        this.resFlag = resFlag;
    }

    @Override
    public void otherAction() {

    }

    @Override
    public void setResFileName() {
        if (resFlag == ResFlag.DRAWABLE) {
            resName = "selector_nd_" + normalRes + "_pd_" + pressRes;
        } else if (resFlag == ResFlag.COLOR) {
            resName = "selector_nc_" + normalRes.replace("#", "") + "_pc_" + pressRes.replace("#", "");
        }

    }

    @Override
    protected void setResFilePath() {
        if (resFlag == ResFlag.COLOR) {
            String folder = rootFolderPath + "/res/color";
            resFilePath = folder + "/" + resName + ".xml";
        } else {
            super.setResFilePath();
        }

    }

    @Override
    public void generateResFile() {
        try {
            String enable = "";
            String state = "";
            if (SelectorState.SELECTED == selectorState) {
                state = "state_selected";
            } else if (SelectorState.ENABLE == selectorState) {
                enable = "android:state_enabled=\"false\" ";
                state = "state_enabled";
            } else {
                state = "state_pressed";
            }
            String xmlCode = null;
            if (resFlag == ResFlag.DRAWABLE) {
//                xmlCode = "<selector xmlns:android=\"http://schemas.android.com/apk/res/android\">\n" +
//                        "<item android:" + state + "=\"true\" android:drawable=\"@drawable/" + pressRes + "\" />\n" +
//                        "<item android:drawable=\"@drawable/" + normalRes + "\" " + enable + "/>\n" +
//                        "</selector>";
                xmlCode = "<selector xmlns:android=\"http://schemas.android.com/apk/res/android\">\n" +
                        "    <item android:drawable=\"" + pressRes + "\" android:" + state + "=\"true\" />\n" +
                        "    <item android:drawable=\"" + normalRes + "\" " + enable + "/>\n" +
                        "</selector>";
            } else if (resFlag == ResFlag.COLOR) {
                String normalResName = handleColorName(normalRes, getColorFilePath());
                String pressResName = handleColorName(pressRes, getColorFilePath());
//                xmlCode = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
//                        "<selector xmlns:android=\"http://schemas.android.com/apk/res/android\">\n" +
//                        "    <item android:color=\"" + pressResName + "\" android:" + state + "=\"true\" />\n" +
//                        "    <item android:color=\"" + normalResName + "\" " + enable + "/>\n" +
//                        "</selector>";
                xmlCode = "<selector xmlns:android=\"http://schemas.android.com/apk/res/android\">\n" +
                        "    <item android:color=\"" + pressResName + "\" android:" + state + "=\"true\" />\n" +
                        "    <item android:color=\"" + normalResName + "\" " + enable + "/>\n" +
                        "</selector>";
            }

            if (TextUtils.isEmpty(xmlCode)) {
                Messages.showErrorDialog(project, "Failed to generate xml. xmlCode==null", "Error");
                return;
            }
            write(resFilePath, xmlCode);
        } catch (IOException ex) {
            Messages.showErrorDialog(project, "Failed to generate code: " + ex.getMessage(), "Error");
        }
    }

}
