package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignUpGUI extends JFrame{
    private JTextField fld_signup_name;
    private JTextField fld_signup_username;
    private JPasswordField fld_signup_password;
    private JButton signUpButton;
    private JPanel wrapper;

    public SignUpGUI() {
        add(wrapper);
        setSize(400,400);
        int x = Helper.screenCenterPoint("x", getSize());
        int y = Helper.screenCenterPoint("y", getSize());
        setLocation(x,y);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);

        signUpButton.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_signup_name) || Helper.isFieldEmpty(fld_signup_username) || Helper.isFieldEmpty(fld_signup_password)){
                Helper.showMessage("fill");
            } else {
                if (User.add(fld_signup_name.getText(), fld_signup_username.getText(), fld_signup_password.getText(), "student")) {
                    Helper.showMessage("success");
                    dispose();
                }
            }
        });
    }




}
