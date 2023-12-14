package com.ouyang.codetemplategenerator.template.restemplate.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GradientInputDialog extends DialogWrapper {

    private JTextField startColoField;
    private JTextField endColorField;
    private JTextField angleField;
    private JTextField radiusField;

    public GradientInputDialog(Project project) {
        super(project, true);
        setTitle("Generate Gradient");

        init();
    }

    @Override
    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel startColorPanel = new JPanel(new BorderLayout());
        JLabel startColoLabel = new JLabel("startColo:");
        startColoField = new JTextField();
        startColorPanel.add(startColoLabel, BorderLayout.WEST);
        startColorPanel.add(startColoField, BorderLayout.CENTER);

        JPanel endColorPanel = new JPanel(new BorderLayout());
        JLabel endColorLabel = new JLabel("endColor:");
        endColorField = new JTextField();
        endColorPanel.add(endColorLabel, BorderLayout.WEST);
        endColorPanel.add(endColorField, BorderLayout.CENTER);

        JPanel positionPanel = new JPanel(new BorderLayout());
        positionPanel.add(startColorPanel, BorderLayout.WEST);
        positionPanel.add(endColorPanel, BorderLayout.EAST);

        JPanel anglePanel = new JPanel(new BorderLayout());
        JLabel angleLabel = new JLabel("angle:");
        angleField = new JTextField();
        anglePanel.add(angleLabel, BorderLayout.WEST);
        anglePanel.add(angleField, BorderLayout.CENTER);


        JPanel radiusPanel = new JPanel(new BorderLayout());
        JLabel radiusLabel = new JLabel("radius:");
        radiusField = new JTextField();
        radiusPanel.add(radiusLabel, BorderLayout.WEST);
        radiusPanel.add(radiusField, BorderLayout.CENTER);

        JPanel otherPanel = new JPanel(new BorderLayout());
        otherPanel.add(anglePanel, BorderLayout.WEST);
        otherPanel.add(radiusPanel, BorderLayout.EAST);


        panel.add(positionPanel, BorderLayout.NORTH);
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
                if (startColoField.getText().isEmpty()) {
                    Messages.showErrorDialog("Please enter a startColor.", "Error");
                } else if (endColorField.getText().isEmpty()) {
                    Messages.showErrorDialog("Please enter a endColor.", "Error");
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

    public String getStartColor() {
        return startColoField.getText().trim();
    }

    public String getEndColor() {
        return endColorField.getText().trim();
    }

    public String getAngle() {
        return angleField.getText().trim();
    }

    public String getRadius() {
        return radiusField.getText().trim();
    }


}
