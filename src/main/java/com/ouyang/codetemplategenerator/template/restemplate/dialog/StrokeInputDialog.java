package com.ouyang.codetemplategenerator.template.restemplate.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 用户交互弹框
 */
public class StrokeInputDialog extends DialogWrapper {

    private JTextField colorField;
    private JTextField strokeColorField;
    private JTextField strokeWidthField;
    private JTextField radiusField;

    public StrokeInputDialog(Project project) {
        super(project, true);
        setTitle("Generate Code");

        init();
    }

    @Override
    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel colorPanel = new JPanel(new BorderLayout());
        JLabel coloLabel = new JLabel("color:");
        colorField = new JTextField();
        colorPanel.add(coloLabel, BorderLayout.WEST);
        colorPanel.add(colorField, BorderLayout.CENTER);

        JPanel strokeColorPanel = new JPanel(new BorderLayout());
        JLabel strokeColorLabel = new JLabel("strokeColor:");
        strokeColorField = new JTextField();
        strokeColorPanel.add(strokeColorLabel, BorderLayout.WEST);
        strokeColorPanel.add(strokeColorField, BorderLayout.CENTER);

        JPanel colorGroupPanel = new JPanel(new BorderLayout());
        colorGroupPanel.add(strokeColorPanel, BorderLayout.WEST);
        colorGroupPanel.add(colorPanel, BorderLayout.EAST);

        JPanel strokeWidthPanel = new JPanel(new BorderLayout());
        JLabel strokeWidthLabel = new JLabel("strokeWidth:");
        strokeWidthField = new JTextField();
        strokeWidthPanel.add(strokeWidthLabel, BorderLayout.WEST);
        strokeWidthPanel.add(strokeWidthField, BorderLayout.CENTER);


        JPanel radiusPanel = new JPanel(new BorderLayout());
        JLabel radiusLabel = new JLabel("radius:");
        radiusField = new JTextField();
        radiusPanel.add(radiusLabel, BorderLayout.WEST);
        radiusPanel.add(radiusField, BorderLayout.CENTER);

        JPanel otherPanel = new JPanel(new BorderLayout());
        otherPanel.add(strokeWidthPanel, BorderLayout.WEST);
        otherPanel.add(radiusPanel, BorderLayout.EAST);

        panel.add(colorGroupPanel, BorderLayout.NORTH);
        panel.add(otherPanel, BorderLayout.SOUTH);

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
