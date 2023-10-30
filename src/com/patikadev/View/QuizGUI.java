package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Content;
import com.patikadev.Model.Quiz;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuizGUI extends JFrame {

    private JPanel wrapper;
    private JLabel lbl_content_name;
    private JButton btn_select;
    private JTextField fld_quiz_name;
    private JTextArea area_questions;
    private JButton btn_delete;
    private JButton btn_update;
    private JButton btn_add;
    private JTable tbl_quiz_list;
    private JButton btn_new_quiz;
    private JTextField fld_hidden_id;
    private DefaultTableModel mdl_myQuizList;
    private Object[] row_myQuizList;
    private static Content content;

    public QuizGUI(Content content) {
        this.content = content;
        add(wrapper);
        setSize(700, 700);
        int x = Helper.screenCenterPoint("x", getSize());
        int y = Helper.screenCenterPoint("y", getSize());
        setLocation(x, y);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);
        area_questions.setSize(50, 50);
        area_questions.setLineWrap(true);
        lbl_content_name.setText(content.getName());
        fld_hidden_id.setVisible(false);

        mdl_myQuizList = new DefaultTableModel();

        Object[] col_myQuizList = {"Id", "Quiz Name"};
        mdl_myQuizList.setColumnIdentifiers(col_myQuizList);
        row_myQuizList = new Object[col_myQuizList.length];
        //loadContentModel(course.getId());
        tbl_quiz_list.setModel(mdl_myQuizList);
        tbl_quiz_list.getTableHeader().setReorderingAllowed(false);
        tbl_quiz_list.getColumnModel().getColumn(0).setMaxWidth(30);
        loadQuizModel();

        tbl_quiz_list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {

                    String select_quizId = tbl_quiz_list.getValueAt(tbl_quiz_list.getSelectedRow(), 0).toString();


                    fld_hidden_id.setText(select_quizId);
                } catch (Exception exception) {

                }
            }
        });


        btn_add.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_quiz_name) || Helper.isFieldEmpty(area_questions)) {
                Helper.showMessage("fill");
            } else {
                String quizName = fld_quiz_name.getText();
                String quizText = area_questions.getText();

                if (Quiz.add(quizName, quizText, content.getId())) {
                    Helper.showMessage("success");
                    loadQuizModel();
                    fld_quiz_name.setText(null);
                    area_questions.setText(null);
                }
            }
        });

        btn_delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Helper.isFieldEmpty(fld_hidden_id)) {
                    Helper.showMessage("Pick a quiz to delete");
                } else {
                    if (Helper.confirm("sure")) {
                        int quizId = Integer.parseInt(fld_hidden_id.getText());
                        if (Quiz.delete(quizId)) {
                            Helper.showMessage("success");
                            loadQuizModel();
                            fld_hidden_id.setText(null);
                        } else {
                            Helper.showMessage("error");
                        }
                    }
                }
            }
        });

        btn_new_quiz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fld_quiz_name.setText(null);
                area_questions.setText(null);
            }
        });

        btn_select.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(fld_hidden_id.getText());
                String name=Quiz.getFetchName(id).getQuiz_name();
                fld_quiz_name.setText(name);
                String text=Quiz.getFetchText(id).getQuiz_text();
                area_questions.setText(text);
            }
        });

        btn_update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(fld_hidden_id.getText());
                String quizName = fld_quiz_name.getText();
                String quizText = area_questions.getText();
                if(Quiz.update(id,quizName,quizText)){
                    Helper.showMessage("success");
                    loadQuizModel();
                    fld_quiz_name.setText(null);
                    area_questions.setText(null);
                } else {
                    Helper.showMessage("error");
                }
            }
        });
    }

    private void loadQuizModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_quiz_list.getModel();
        clearModel.setRowCount(0);
        int i = 0;
        for (Quiz obj : Quiz.getListByContent(content.getId())) {
            i = 0;
            row_myQuizList[i++] = obj.getId();
            row_myQuizList[i++] = obj.getQuiz_name();

            mdl_myQuizList.addRow(row_myQuizList);

        }
    }

}
