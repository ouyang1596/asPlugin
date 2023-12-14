package com.ouyang.codetemplategenerator.template.restemplate.dialog;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.components.JBRadioButton;
import com.ouyang.codetemplategenerator.template.TemplateFactory;
import com.ouyang.codetemplategenerator.template.restemplate.ResTemplate;
import com.ouyang.codetemplategenerator.template.restemplate.SelectorTemplate;
import com.ouyang.codetemplategenerator.template.restemplate.generator.GradientTemplateGenerator;
import com.ouyang.codetemplategenerator.template.restemplate.generator.ShapeTemplateGenerator;
import com.ouyang.codetemplategenerator.template.restemplate.generator.StrokeTemplateGenerator;
import org.jdesktop.swingx.HorizontalLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 用户交互弹框
 */
public class SelectorInputDialog extends DialogWrapper implements ActionListener {
    private JTextField resNameField;
    private JTextField normalResField;
    private JTextField pressResField;
    /**
     * state_pressed
     */
    private JBRadioButton rbPressed;
    /**
     * state_enabled
     */
    private JBRadioButton rbEnabled;
    /**
     * state_selected
     */
    private JBRadioButton rbSelected;

    /**
     * 色值类型
     */
    private JBRadioButton rbColor;
    /**
     * 资源类型
     */
    private JBRadioButton rbDrawable;

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

        JPanel namePanel = new JPanel(new HorizontalLayout());
        JLabel nameLabel = new JLabel("name:");
        resNameField = new JTextField();
        namePanel.add(nameLabel);
        namePanel.add(resNameField);

        JLabel normalResLabel = new JLabel("normalRes:");
        normalResField = new JTextField();
        JLabel pressResLabel = new JLabel("stateChangeRes:");
        pressResField = new JTextField();
        JPanel stateResPanel = new JPanel(new HorizontalLayout());
        stateResPanel.add(normalResLabel);
        stateResPanel.add(normalResField);
        stateResPanel.add(pressResLabel);
        stateResPanel.add(pressResField);


        JLabel pressedLabel = new JLabel("pressed:");
        rbPressed = new JBRadioButton();
        rbPressed.addActionListener(this);
        rbPressed.setSelected(true);
        JLabel enabledLabel = new JLabel("enabled:");
        rbEnabled = new JBRadioButton();
        rbEnabled.addActionListener(this);
        JLabel selectedLabel = new JLabel("selected:");
        rbSelected = new JBRadioButton();
        rbSelected.addActionListener(this);
        ButtonGroup stateGroup = new ButtonGroup();
        stateGroup.add(rbPressed);
        stateGroup.add(rbEnabled);
        stateGroup.add(rbSelected);
        JPanel statePanel = new JPanel(new HorizontalLayout());
        statePanel.add(pressedLabel);
        statePanel.add(rbPressed);
        statePanel.add(enabledLabel);
        statePanel.add(rbEnabled);
        statePanel.add(selectedLabel);
        statePanel.add(rbSelected);


        resTypePanel = new JPanel(new HorizontalLayout());
        JLabel normalCLabel = new JLabel("normal:");
        normalResComboBox = new JComboBox<>(new String[]{TemplateFactory.ResType.SHAPE, TemplateFactory.ResType.STROKE, TemplateFactory.ResType.GRADIENT});
        normalResComboBox.addActionListener(this);
        JLabel pressResCLabel = new JLabel("stateChange:");
        pressResComboBox = new JComboBox<>(new String[]{TemplateFactory.ResType.SHAPE, TemplateFactory.ResType.STROKE, TemplateFactory.ResType.GRADIENT});
        pressResComboBox.addActionListener(this);
        resTypePanel.add(normalCLabel);
        resTypePanel.add(normalResComboBox);
        resTypePanel.add(pressResCLabel);
        resTypePanel.add(pressResComboBox);


        JLabel resColorLabel = new JLabel("resColor:");
        rbColor = new JBRadioButton();
        rbColor.addActionListener(this);
        JLabel resDrawableLabel = new JLabel("resDrawable:");
        rbDrawable = new JBRadioButton();
        rbDrawable.addActionListener(this);
        rbDrawable.setSelected(true);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(rbColor);
        buttonGroup.add(rbDrawable);
        JPanel otherPanel = new JPanel(new HorizontalLayout());
        otherPanel.add(resColorLabel);
        otherPanel.add(rbColor);
        otherPanel.add(resDrawableLabel);
        otherPanel.add(rbDrawable);


        panel.add(namePanel);
        panel.add(stateResPanel);
        panel.add(statePanel);
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

    public String getResName() {
        return resNameField.getText().trim();
    }

    public String getNormalRes() {
        return normalResField.getText().trim();
    }

    public String getPressRes() {
        return pressResField.getText().trim();
    }

    public int getResFlag() {
        if (rbColor.isSelected()) {
            return SelectorTemplate.ResFlag.COLOR;
        } else {
            return SelectorTemplate.ResFlag.DRAWABLE;
        }
    }

    public int getSelectorState() {
        if (rbPressed.isSelected()) {
            return SelectorTemplate.SelectorState.PRESSED;
        } else if (rbEnabled.isSelected()) {
            return SelectorTemplate.SelectorState.ENABLE;
        } else {
            return SelectorTemplate.SelectorState.SELECTED;
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == rbDrawable) {
            resTypePanel.setVisible(true);
        } else if (e.getSource() == rbColor) {
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
