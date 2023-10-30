package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.DBConnector;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Course;
import com.patikadev.Model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class InstructorGUI extends JFrame {
    private JPanel wrapper;
    private JLabel lbl_instructor_welcome;
    private JTabbedPane tab_instructor;
    private JTable tbl_myCourses;
    private JScrollPane scrl_myCourses;
    private JButton selectButton;
    private JTextField fld_courseName;
    private JButton btn_logout;
    private JTextField fld_course_id;
    private DefaultTableModel mdl_myCourse_list;
    private Object[] row_myCourse_list;
    private final User user;


    public InstructorGUI(User user) {
        this.user = user;
        add(wrapper);
        setSize(1000, 500);
        int x = Helper.screenCenterPoint("x", getSize());
        int y = Helper.screenCenterPoint("y", getSize());
        setLocation(x, y);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);
        fld_course_id.setVisible(false);
        lbl_instructor_welcome.setText("Welcome " + user.getName());

        mdl_myCourse_list = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0 || column == 1) {
                    return false;
                }
                return super.isCellEditable(row, column);
            }
        };

        Object[] col_myCourseList = {"Course ID", "Course Name"};
        mdl_myCourse_list.setColumnIdentifiers(col_myCourseList);
        row_myCourse_list = new Object[col_myCourseList.length];

        loadCourseModel();
        tbl_myCourses.setModel(mdl_myCourse_list);
        tbl_myCourses.getTableHeader().setReorderingAllowed(false);
        tbl_myCourses.getColumnModel().getColumn(0).setMaxWidth(100);

        tbl_myCourses.getSelectionModel().addListSelectionListener(e ->
        {
            try {
                String select_course_id = tbl_myCourses.getValueAt(tbl_myCourses.getSelectedRow(), 0).toString();
                String select_course_name = tbl_myCourses.getValueAt(tbl_myCourses.getSelectedRow(), 1).toString();
                fld_course_id.setText(select_course_id);
                fld_courseName.setText(select_course_name);
            } catch (Exception exception) {

            }
        });


        btn_logout.addActionListener(e ->

        {
            dispose();
            LoginGUI loginGUI = new LoginGUI();
        });

        selectButton.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_courseName)){
                Helper.showMessage("fill");
            } else {
                Course c = Course.getFetch(Integer.parseInt(fld_course_id.getText()));
                ContentGUI contentGUI = new ContentGUI(c);
            }
        });



    }

    public static ArrayList<Course> getListByUser(int user_id) {
        ArrayList<Course> courseList = new ArrayList<>();

        Course obj;

        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM course WHERE user_id = " + user_id);
            while (rs.next()) {
                int id = rs.getInt("id");
                int userId = rs.getInt("user_id");
                int pathId = rs.getInt("patika_id");
                String name = rs.getString("name");
                String lang = rs.getString("lang");
                obj = new Course(id, userId, pathId, name, lang);
                courseList.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseList;

    }

    private void loadCourseModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_myCourses.getModel();
        clearModel.setRowCount(0);

        for (Course obj : getListByUser(user.getId())) {
            row_myCourse_list[0] = obj.getId();
            row_myCourse_list[1] = obj.getName();
            mdl_myCourse_list.addRow(row_myCourse_list);
        }
    }




}
