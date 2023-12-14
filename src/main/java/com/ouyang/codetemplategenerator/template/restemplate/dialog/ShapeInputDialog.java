package com.ouyang.codetemplategenerator.template.restemplate.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ShapeInputDialog extends DialogWrapper {

    private JTextField coloField;
    private JTextField radiusField;

    public ShapeInputDialog(Project project) {
        super(project, true);
        setTitle("Generate Shape");

        init();
    }

    @Override
    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel colorPanel = new JPanel(new BorderLayout());
        JLabel coloLabel = new JLabel("Color:");
        coloField = new JTextField();
        colorPanel.add(coloLabel, BorderLayout.WEST);
        colorPanel.add(coloField, BorderLayout.CENTER);


        JPanel radiusPanel = new JPanel(new BorderLayout());
        JLabel radiusLabel = new JLabel("Radius:");
        radiusField = new JTextField();
        radiusPanel.add(radiusLabel, BorderLayout.WEST);
        radiusPanel.add(radiusField, BorderLayout.CENTER);

        panel.add(colorPanel, BorderLayout.NORTH);
        panel.add(radiusPanel, BorderLayout.SOUTH);

        return panel;
    }

    @Override
    protected JComponent createSouthPanel() {
        JPanel panel = new JPanel();

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (coloField.getText().isEmpty()) {
                    Messages.showErrorDialog("Please enter a color.", "Error");
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
        return coloField.getText().trim();
    }

    public String getRadius() {
        return radiusField.getText().trim();
    }


}
