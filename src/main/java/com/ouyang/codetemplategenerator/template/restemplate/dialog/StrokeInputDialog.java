package com.ouyang.codetemplategenerator.template.restemplate.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.VerticalFlowLayout;
import org.jdesktop.swingx.HorizontalLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 用户交互弹框
 */
public class StrokeInputDialog extends DialogWrapper {
    private JTextField resNameField;
    private JTextField colorField;
    private JTextField strokeColorField;
    private JTextField strokeWidthField;
    private JTextField radiusField;

    public StrokeInputDialog(Project project) {
        super(project, true);
        setTitle("Generate Stroke");

        init();
    }

    @Override
    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel(new VerticalFlowLayout());

        JPanel namePanel = new JPanel(new HorizontalLayout());
        JLabel nameLabel = new JLabel("name:");
        resNameField = new JTextField(20);
        namePanel.add(nameLabel);
        namePanel.add(resNameField);

        JPanel colorPanel = new JPanel(new HorizontalLayout());
        JLabel coloLabel = new JLabel("color:");
        colorField = new JTextField(10);
        colorPanel.add(coloLabel);
        colorPanel.add(colorField);

        JPanel strokeColorPanel = new JPanel(new HorizontalLayout());
        JLabel strokeColorLabel = new JLabel("strokeColor:");
        strokeColorField = new JTextField(10);
        strokeColorPanel.add(strokeColorLabel);
        strokeColorPanel.add(strokeColorField);

        JPanel colorGroupPanel = new JPanel(new HorizontalLayout());
        colorGroupPanel.add(strokeColorPanel);
        colorGroupPanel.add(colorPanel);

        JPanel strokeWidthPanel = new JPanel(new HorizontalLayout());
        JLabel strokeWidthLabel = new JLabel("strokeWidth:");
        strokeWidthField = new JTextField();
        strokeWidthPanel.add(strokeWidthLabel);
        strokeWidthPanel.add(strokeWidthField);


        JPanel radiusPanel = new JPanel(new HorizontalLayout());
        JLabel radiusLabel = new JLabel("radius:");
        radiusField = new JTextField();
        radiusPanel.add(radiusLabel);
        radiusPanel.add(radiusField);

        JPanel otherPanel = new JPanel(new HorizontalLayout());
        otherPanel.add(strokeWidthPanel);
        otherPanel.add(radiusPanel);

        panel.add(namePanel);
        panel.add(colorGroupPanel);
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
                if (strokeWidthField.getText().isEmpty()) {
                    Messages.showErrorDialog("Please enter a strokeWidth.", "Error");
                } else if (strokeColorField.getText().isEmpty()) {
                    Messages.showErrorDialog("Please enter a strokeColor.", "Error");
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

    public String getColor() {
        return colorField.getText().trim();
    }

    public String getStrokeColor() {
        return strokeColorField.getText().trim();
    }

    public String getStrokeWidth() {
        return strokeWidthField.getText().trim();
    }

    public String getRadius() {
        return radiusField.getText().trim();
    }


}
