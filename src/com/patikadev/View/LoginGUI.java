package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Operator;
import com.patikadev.Model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends JFrame{
    private JPanel wrapper;
    private JPanel wtop;
    private JPanel wBottom;
    private JTextField fld_user_username;
    private JPasswordField fld_user_password;
    private JButton btn_login;
    private JButton btn_signup;

    public LoginGUI(){
        add(wrapper);
        setSize(400,400);
        int x = Helper.screenCenterPoint("x", getSize());
        int y = Helper.screenCenterPoint("y", getSize());
        setLocation(x,y);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);

        btn_login.addActionListener(e -> {
                if (Helper.isFieldEmpty(fld_user_username) || Helper.isFieldEmpty(fld_user_password)){
                    Helper.showMessage("fill");
                } else {
                    User u = User.getFetch(fld_user_username.getText(), fld_user_password.getText());
                    if (u == null){
                        Helper.showMessage("User not found!");
                    } else {
                        switch (u.getType()){
                            case "operator":
                                OperatorGUI operatorGUI = new OperatorGUI((Operator) u);
                                break;
                            case "instructor":
                                InstructorGUI instructorGUI = new InstructorGUI(u);
                                break;
                            case "student":
                                StudentGUI studentGUI = new StudentGUI(u);
                                break;
                        }
                        dispose();
                    }
                }
        });
        btn_signup.addActionListener(e -> {
            SignUpGUI signUpGUI = new SignUpGUI();
        });
    }

    public static void main(String[] args) {
        Helper.setLayout();
        LoginGUI login = new LoginGUI();
    }
}
