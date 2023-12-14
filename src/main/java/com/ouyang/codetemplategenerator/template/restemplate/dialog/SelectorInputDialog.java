package com.ouyang.codetemplategenerator.template.restemplate.dialog;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.components.JBRadioButton;
import com.ouyang.codetemplategenerator.template.TemplateFactory;
import com.ouyang.codetemplategenerator.template.restemplate.*;
import org.jdesktop.swingx.HorizontalLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 用户交互弹框
 */
public class SelectorInputDialog extends DialogWrapper implements ActionListener {

    private JTextField normalResField;
    private JTextField pressResField;
    private JBRadioButton radioButtonColor;
    private JBRadioButton radioButtonDrawable;

    private Project project;

    AnActionEvent anActionEvent;

    /**
     * 默认资源文件类型
     */
    private JComboBox<String> normalResComboBox;

    /**
     * 按住资源文件类型
     */
    private JComboBox<String> pressResComboBox;

    /**
     * 资源文件类型面板
     */
    private JPanel resTypePanel;

    public SelectorInputDialog(Project project) {
        super(project, true);
        this.project = project;
        setTitle("Generate Selector");

        init();
    }

    public void setAnActionEvent(AnActionEvent anActionEvent) {
        this.anActionEvent = anActionEvent;
    }

    @Override
    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel(new VerticalFlowLayout());

        JPanel normalResPanel = new JPanel(new HorizontalLayout());
        JLabel normalResLabel = new JLabel("normalRes:");
        normalResField = new JTextField();
        normalResPanel.add(normalResLabel);
        normalResPanel.add(normalResField);

        JPanel pressResPanel = new JPanel(new HorizontalLayout());
        JLabel pressResLabel = new JLabel("pressRes:");
        pressResField = new JTextField();
        pressResPanel.add(pressResLabel);
        pressResPanel.add(pressResField);

        JPanel stateGroupPanel = new JPanel(new HorizontalLayout());
        stateGroupPanel.add(pressResPanel);
        stateGroupPanel.add(normalResPanel);


        JPanel resColorPanel = new JPanel(new HorizontalLayout());
        JLabel resColorLabel = new JLabel("resColor:");
        radioButtonColor = new JBRadioButton();
        radioButtonColor.addActionListener(this);
        resColorPanel.add(resColorLabel);
        resColorPanel.add(radioButtonColor);

        JPanel resDrawablePanel = new JPanel(new HorizontalLayout());
        JLabel resDrawableLabel = new JLabel("resDrawable:");
        radioButtonDrawable = new JBRadioButton();
        radioButtonDrawable.addActionListener(this);
        radioButtonDrawable.setSelected(true);
        resDrawablePanel.add(resDrawableLabel);
        resDrawablePanel.add(radioButtonDrawable);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButtonColor);
        buttonGroup.add(radioButtonDrawable);

        JPanel otherPanel = new JPanel(new HorizontalLayout());
        otherPanel.add(resColorPanel);
        otherPanel.add(resDrawablePanel);


        resTypePanel = new JPanel(new HorizontalLayout());
        JLabel pressResCLabel = new JLabel("press:");
        pressResComboBox = new JComboBox<>(new String[]{TemplateFactory.ResType.SHAPE, TemplateFactory.ResType.STROKE, TemplateFactory.ResType.GRADIENT});
        pressResComboBox.addActionListener(this);
        resTypePanel.add(pressResCLabel);
        resTypePanel.add(pressResComboBox);

        JLabel normalCLabel = new JLabel("normal:");
        normalResComboBox = new JComboBox<>(new String[]{TemplateFactory.ResType.SHAPE, TemplateFactory.ResType.STROKE, TemplateFactory.ResType.GRADIENT});
        normalResComboBox.addActionListener(this);
        resTypePanel.add(normalCLabel);
        resTypePanel.add(normalResComboBox);

        panel.add(stateGroupPanel);
        panel.add(resTypePanel);
        panel.add(otherPanel);

        return panel;
    }

    @Override
    protected JComponent createSouthPanel() {
        JPanel panel = new JPanel();

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (normalResField.getText().isEmpty()) {
                    Messages.showErrorDialog("Please enter a normalRes.", "Error");
                } else if (pressResField.getText().isEmpty()) {
                    Messages.showErrorDialog("Please enter a pressRes.", "Error");
                } else {
                    close(DialogWrapper.OK_EXIT_CODE);
                }
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close(DialogWrapper.CANCEL_EXIT_CODE);
            }
        });

        panel.add(okButton);
        panel.add(cancelButton);

        return panel;
    }

    public String getNormalRes() {
        return normalResField.getText().trim();
    }

    public String getPressRes() {
        return pressResField.getText().trim();
    }

    public int getResFlag() {
        if (radioButtonColor.isSelected()) {
            return SelectorTemplate.ResFlag.COLOR;
        } else {
            return SelectorTemplate.ResFlag.DRAWABLE;
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == radioButtonDrawable) {
            resTypePanel.setVisible(true);
        } else if (e.getSource() == radioButtonColor) {
            normalResField.setText("");
            pressResField.setText("");
            resTypePanel.setVisible(false);
        } else if (e.getSource() == normalResComboBox || e.getSource() == pressResComboBox) {
            JComboBox<String> jComboBox = (JComboBox<String>) e.getSource();
            jComboBox.hidePopup();
            String resType = (String) jComboBox.getSelectedItem();
            ResTemplate resTemplate = null;
            switch (resType) {
                case TemplateFactory.ResType.SHAPE:
                    resTemplate = ShapeTemplateGenerator.handleDialog(anActionEvent, project);
                    break;
                case TemplateFactory.ResType.STROKE:
                    resTemplate = StrokeTemplateGenerator.handleDialog(anActionEvent, project);
                    break;
                case TemplateFactory.ResType.GRADIENT:
                    resTemplate = GradientTemplateGenerator.handleDialog(anActionEvent, project);
                    break;
            }
            if (resTemplate != null) {
                if (jComboBox == normalResComboBox) {
                    normalResField.setText(resTemplate.getResName());
                } else {
                    pressResField.setText(resTemplate.getResName());
                }
            }
        }
    }
}
