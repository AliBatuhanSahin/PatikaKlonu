package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Content;
import com.patikadev.Model.Course;
import com.patikadev.Model.User;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class ContentGUI extends JFrame {
    private JPanel wrapper;
    private JButton btn_content_add;
    private JTextField fld_content_name;
    private JButton btn_content_delete;
    private JButton btn_content_search;
    private JLabel lbl_course_name;
    private DefaultTableModel mdl_myContentList;
    private Object[] row_myContentList;
    private JTable tbl_contentList;
    private JTextField fld_hidden_delete;
    private JPopupMenu quizMenu;
    private Course course;

    public ContentGUI(Course course) {
        this.course = course;
        add(wrapper);
        setSize(1000, 500);
        int x = Helper.screenCenterPoint("x", getSize());
        int y = Helper.screenCenterPoint("y", getSize());
        setLocation(x, y);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);
        fld_hidden_delete.setVisible(false);
        lbl_course_name.setText(course.getName());

        mdl_myContentList = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0 || column == 4) {
                    return false;
                }
                return super.isCellEditable(row, column);
            }
        };


        quizMenu = new JPopupMenu();
        JMenuItem addQuiz = new JMenuItem("Add/Update Quiz");
        quizMenu.add(addQuiz);

        addQuiz.addActionListener((ActionListener) e -> {
            Content c = Content.getFetch(Integer.parseInt(fld_hidden_delete.getText()));

            QuizGUI qGUI = new QuizGUI(c);

        });


        Object[] col_myContentList = {"Content Id", "Content Name", "Description", "Youtube Link", "Quiz"};
        mdl_myContentList.setColumnIdentifiers(col_myContentList);
        row_myContentList = new Object[col_myContentList.length];
        loadContentModel(course.getId());
        tbl_contentList.setModel(mdl_myContentList);
        tbl_contentList.setComponentPopupMenu(quizMenu);
        tbl_contentList.getTableHeader().setReorderingAllowed(false);
        tbl_contentList.getColumnModel().getColumn(0).setMaxWidth(75);

        tbl_contentList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                int selectedRow = tbl_contentList.rowAtPoint(point);
                tbl_contentList.setRowSelectionInterval(selectedRow, selectedRow);
            }
        });

        btn_content_search.addActionListener(e -> {
            String searchKeyLow = fld_content_name.getText().toLowerCase();
            String searchKeyUp = fld_content_name.getText().toUpperCase();

            DefaultTableModel clearModel = (DefaultTableModel) tbl_contentList.getModel();
            clearModel.setRowCount(0);
            int i = 0;
            for (Content obj : Content.getListByCourse(course.getId())) {
                if (obj.getName().toLowerCase().contains(searchKeyLow) || obj.getName().toUpperCase().contains(searchKeyUp)) {
                    i = 0;
                    row_myContentList[i++] = obj.getId();
                    row_myContentList[i++] = obj.getName();
                    row_myContentList[i++] = obj.getDescription();
                    row_myContentList[i++] = obj.getYoutubeLink();
                    row_myContentList[i++] = obj.getQuizId();
                    mdl_myContentList.addRow(row_myContentList);
                }
            }
        });

        btn_content_add.addActionListener(e -> {

            if (Helper.isFieldEmpty(fld_content_name)) {
                Helper.showMessage("fill");
            } else {
                String name = fld_content_name.getText();
                if (Content.add(name)) {
                    Helper.showMessage("success");
                    loadContentModel(course.getId());
                    fld_content_name.setText(null);
                }
            }
        });

        btn_content_delete.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_hidden_delete)) {
                Helper.showMessage("fill");
            } else {
                if (Helper.confirm("sure")) {
                    int content_id = Integer.parseInt(fld_hidden_delete.getText());
                    if (Content.delete(content_id)) {
                        Helper.showMessage("success");
                        loadContentModel(course.getId());
                        fld_hidden_delete.setText(null);
                    } else {
                        Helper.showMessage("error");
                    }
                }
            }
        });

        tbl_contentList.getSelectionModel().addListSelectionListener(e -> {
            try {
                String select_content_id = tbl_contentList.getValueAt(tbl_contentList.getSelectedRow(), 0).toString();
                fld_hidden_delete.setText(select_content_id);
                String select_content_name = tbl_contentList.getValueAt(tbl_contentList.getSelectedRow(), 1).toString();
                fld_content_name.setText(select_content_name);
            } catch (Exception exception) {

            }
        });

        tbl_contentList.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int content_id = Integer.parseInt(tbl_contentList.getValueAt(tbl_contentList.getSelectedRow(), 0).toString());
                String content_name = tbl_contentList.getValueAt(tbl_contentList.getSelectedRow(), 1).toString();
                String description = tbl_contentList.getValueAt(tbl_contentList.getSelectedRow(), 2).toString();
                String youtube_link = tbl_contentList.getValueAt(tbl_contentList.getSelectedRow(), 3).toString();
                int quiz_id = Integer.parseInt(tbl_contentList.getValueAt(tbl_contentList.getSelectedRow(), 4).toString());

                if (Content.update(content_id, content_name, description, youtube_link, course.getId())) {
                    Helper.showMessage("success");
                }

                loadContentModel(course.getId());
            }
        });


    }


    private void loadContentModel(int id) {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_contentList.getModel();
        clearModel.setRowCount(0);
        int i = 0;
        for (Content obj : Content.getListByCourse(id)) {
            i = 0;
            row_myContentList[i++] = obj.getId();
            row_myContentList[i++] = obj.getName();
            row_myContentList[i++] = obj.getDescription();
            row_myContentList[i++] = obj.getYoutubeLink();
            row_myContentList[i++] = obj.getQuizId();
            mdl_myContentList.addRow(row_myContentList);
        }
    }
}
