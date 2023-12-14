package com.ouyang.codetemplategenerator.template.restemplate.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.VerticalFlowLayout;
import org.jdesktop.swingx.HorizontalLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GradientInputDialog extends DialogWrapper {
    private JTextField resNameField;
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
        JPanel panel = new JPanel(new VerticalFlowLayout());

        JPanel namePanel = new JPanel(new HorizontalLayout());
        JLabel nameLabel = new JLabel("name:");
        resNameField = new JTextField();
        namePanel.add(nameLabel);
        namePanel.add(resNameField);


        JPanel startColorPanel = new JPanel(new HorizontalLayout());
        JLabel startColoLabel = new JLabel("startColo:");
        startColoField = new JTextField();
        startColorPanel.add(startColoLabel);
        startColorPanel.add(startColoField);

        JPanel endColorPanel = new JPanel(new HorizontalLayout());
        JLabel endColorLabel = new JLabel("endColor:");
        endColorField = new JTextField();
        endColorPanel.add(endColorLabel);
        endColorPanel.add(endColorField);

        JPanel positionPanel = new JPanel(new HorizontalLayout());
        positionPanel.add(startColorPanel);
        positionPanel.add(endColorPanel);

        JPanel anglePanel = new JPanel(new HorizontalLayout());
        JLabel angleLabel = new JLabel("angle:");
        angleField = new JTextField();
        anglePanel.add(angleLabel);
        anglePanel.add(angleField);


        JPanel radiusPanel = new JPanel(new HorizontalLayout());
        JLabel radiusLabel = new JLabel("radius:");
        radiusField = new JTextField();
        radiusPanel.add(radiusLabel);
        radiusPanel.add(radiusField);

        JPanel otherPanel = new JPanel(new HorizontalLayout());
        otherPanel.add(anglePanel);
        otherPanel.add(radiusPanel);


        panel.add(namePanel);
        panel.add(positionPanel);
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

    public String getResName() {
        return resNameField.getText().trim();
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
