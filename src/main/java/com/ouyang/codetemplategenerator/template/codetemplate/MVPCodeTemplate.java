package com.ouyang.codetemplategenerator.template.codetemplate;

import com.intellij.openapi.ui.Messages;
import com.ouyang.codetemplategenerator.template.codetemplate.dialog.CodeInputDialog;

import java.io.IOException;

/**
 * 自定义View
 */
public class MVPCodeTemplate extends CodeTemplate {

    @Override
    public void setLayoutName() {

    }

    @Override
    public void generateCode() {
        generatePresenter();
        generateContract();
    }

    /**
     * 生成Presenter
     */
    private void generatePresenter() {
        String classReName = className.replace("Presenter", "");
        try {
            String code;
            if (CodeInputDialog.FileForm.JAVA.equals(fileForm)) {
                code = "";
            } else {
                code = "package " + packageName + "\n" +
                        "\n" +
                        "class " + classReName + "Presenter : " + classReName + "Contract.Presenter {\n" +
                        "\n" +
                        "    private var view" + classReName + ": " + classReName + "Contract.View" + classReName + "? = null\n" +
                        "\n" +
                        "\n" +
                        "    constructor(view" + classReName + ": " + classReName + "Contract.View" + classReName + "?) {\n" +
                        "        this.view" + classReName + " = view" + classReName + "\n" +
                        "        view" + classReName + "?.setPresenter(this)\n" +
                        "    }\n" +
                        "\n" +
                        "\n" +
                        "    override fun start() {\n" +
                        "\n" +
                        "    }\n" +
                        "\n" +
                        "    override fun onDetached() {\n" +
                        "        view" + classReName + " = null\n" +
                        "    }\n" +
                        "\n" +
                        "}";
            }

            write(codeFilePath, code);
        } catch (IOException ex) {
            Messages.showErrorDialog(project, "Failed to generate code: " + ex.getMessage(), "Error");
        }
    }

    /**
     * 生成Contract
     */
    private void generateContract() {
        String contractName = className.replace("Presenter", "Contract");
        String classReName = contractName.replace("Contract", "");
        try {
            String code;
            if (CodeInputDialog.FileForm.JAVA.equals(fileForm)) {
                code = "";
            } else {
                code = "package " + packageName + "\n" +
                        "\n" +
                        "import com.ailiao.mosheng.commonlibrary.presenter.BasePresenter\n" +
                        "import com.ailiao.mosheng.commonlibrary.presenter.BaseView\n" +
                        "\n" +
                        "class " + classReName + "Contract {\n" +
                        "\n" +
                        "\n" +
                        "    interface View" + classReName + " : BaseView<Presenter> {\n" +
                        "\n" +
                        "    }\n" +
                        "\n" +
                        "    interface Presenter : BasePresenter {\n" +
                        "\n" +
                        "    }\n" +
                        "}";
            }

            String contractFilePath = codeFilePath.replace(className, contractName);
            write(contractFilePath, code);


        } catch (IOException ex) {
            Messages.showErrorDialog(project, "Failed to generate code: " + ex.getMessage(), "Error");
        }
    }

    @Override
    public void otherAction() {

    }

}
