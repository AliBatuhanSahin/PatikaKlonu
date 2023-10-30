package com.patikadev.Helper;

import javax.swing.*;
import java.awt.*;

public class Helper {

    // Ekranı ortalamak için
    public static int screenCenterPoint(String axis, Dimension size) {
        int point = 0;

        switch (axis) {
            case "x":
                point = (Toolkit.getDefaultToolkit().getScreenSize().width - size.width) / 2;
                break;
            case "y":
                point = (Toolkit.getDefaultToolkit().getScreenSize().height - size.height) / 2;
                break;
            default:
                point = 0;
        }

        return point;
    }

    public static void setLayout() {
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                         UnsupportedLookAndFeelException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }
    }

    public static boolean isFieldEmpty(JTextField field) {
        return field.getText().trim().isEmpty();
    }


    public static boolean isFieldEmpty(JTextArea area) {
        return area.getText().trim().isEmpty();
    }


    public static void showMessage(String str) {
        String message;
        String title;
        switch (str) {
            case "fill":
                message = "You have made an incomplete entry!";
                title = "ERROR!";
                break;
            case "success":
                message = "Process completed successfully";
                title = "SUCCESS";
                break;
            case "error":
                message = "You have made an incorrect entry!";
                title = "ERROR!";
                break;
            default:
                message = str;
                title = "MESSAGE";
        }

        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirm(String str) {
        optionPageEN();
        String message;

        switch (str) {
            case "sure":
                message = "Are you sure you want to perform this action?";
                break;
            default:
                message = str;
        }

        return JOptionPane.showConfirmDialog(null, message, "ARE YOU SURE?", JOptionPane.YES_NO_OPTION) == 0;
    }

    public static void optionPageEN(){
        UIManager.put("OptionPane.yesButtonText", "YES");
        UIManager.put("OptionPane.noButtonText", "NO");
    }


}
