package com.ouyang.codetemplategenerator.template.restemplate.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.VerticalFlowLayout;
import org.jdesktop.swingx.HorizontalLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ShapeInputDialog extends DialogWrapper {
    private JTextField resNameField;
    private JTextField coloField;
    private JTextField radiusField;

    public ShapeInputDialog(Project project) {
        super(project, true);
        setTitle("Generate Shape");

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
        JLabel coloLabel = new JLabel("Color:");
        coloField = new JTextField(10);
        colorPanel.add(coloLabel);
        colorPanel.add(coloField);


        JPanel radiusPanel = new JPanel(new HorizontalLayout());
        JLabel radiusLabel = new JLabel("Radius:");
        radiusField = new JTextField();
        radiusPanel.add(radiusLabel);
        radiusPanel.add(radiusField);

        panel.add(namePanel);
        panel.add(colorPanel);
        panel.add(radiusPanel);

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

    public String getResName() {
        return resNameField.getText().trim();
    }

    public String getColor() {
        return coloField.getText().trim();
    }

    public String getRadius() {
        return radiusField.getText().trim();
    }


}
