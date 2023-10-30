package com.patikadev.View;

import com.patikadev.Helper.*;
import com.patikadev.Model.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class OperatorGUI extends JFrame {
    private JPanel wrapper;
    private JTabbedPane tab_operator;
    private JLabel lbl_welcome;
    private JPanel pnl_top;
    private JButton btn_logout;
    private JPanel pnl_user_list;
    private JScrollPane scrl_user_list;
    private JTable tbl_user_list;
    private JPanel pnl_user_form;
    private JTextField fld_user_name;
    private JTextField fld_user_username;
    private JPasswordField fld_user_password;
    private JComboBox cmb_user_type;
    private JButton btn_user_add;
    private JTextField fld_user_id;
    private JButton btn_user_delete;
    private JTextField fld_sh_user_name;
    private JTextField fld_sh_user_username;
    private JComboBox cmb_sh_user_type;
    private JButton btn_user_sh;
    private JPanel pnl_patika_list;
    private JScrollPane scrl_patika_list;
    private JTable tbl_patika_list;
    private JPanel pnl_patika_add;
    private JTextField fld_patika_name;
    private JButton btn_patika_add;
    private JPanel pnl_user_top;
    private JPanel pnl_course_list;
    private JScrollPane scrl_course_list;
    private JTable tbl_course_list;
    private JPanel pnl_course_add;
    private JTextField fld_course_name;
    private JTextField fld_course_lang;
    private JComboBox cmb_course_patika;
    private JComboBox cmb_course_user;
    private JButton btn_course_add;
    private JButton deleteButtonContent;
    private JTextField fld_contentId;
    private JTable tbl_allContent;
    private JButton deleteButtonQuiz;
    private JTextField fld_quizId;
    private JTable tbl_allQuiz;
    private JButton deleteButtonCourse;
    private JTextField fld_hiddenCourseId;
    private DefaultTableModel mdl_user_list;
    private Object[] row_user_list;
    private DefaultTableModel mdl_patika_list;
    private Object[] row_patika_list;
    private JPopupMenu patikaMenu;
    private DefaultTableModel mdl_course_list;
    private Object[] row_course_list;

    private DefaultTableModel mdl_contentList;
    private Object[] row_contentList;
    private DefaultTableModel mdl_quizList;
    private Object[] row_quizList;
    private final Operator operator;

    public OperatorGUI(Operator operator) {
        this.operator = operator;
        add(wrapper);
        setSize(1000, 500);
        int x = Helper.screenCenterPoint("x", getSize());
        int y = Helper.screenCenterPoint("y", getSize());
        setLocation(x, y);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);
        fld_hiddenCourseId.setVisible(false);

        lbl_welcome.setText("Welcome " + operator.getName());

        // ModelUserList
        mdl_user_list = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0) {
                    return false;
                }
                return super.isCellEditable(row, column);
            }
        };

        Object[] col_user_list = {"ID", "Full Name", "Username", "Password", "User Type"};
        mdl_user_list.setColumnIdentifiers(col_user_list);

        row_user_list = new Object[col_user_list.length];
        loadUserModel();


        tbl_user_list.setModel(mdl_user_list);
        tbl_user_list.getTableHeader().setReorderingAllowed(false);

        tbl_user_list.getSelectionModel().addListSelectionListener(e -> {
            try {
                String select_user_id = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 0).toString();
                fld_user_id.setText(select_user_id);
            } catch (Exception exception) {

            }
        });

        tbl_user_list.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int user_id = Integer.parseInt(tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 0).toString());
                String user_name = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 1).toString();
                String user_username = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 2).toString();
                String user_password = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 3).toString();
                String user_type = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 4).toString();

                if (User.update(user_id, user_name, user_username, user_password, user_type)) {
                    Helper.showMessage("success");
                }

                loadUserModel();
                loadInstructorCombo();
                loadCourseModel();
            }
        });
        // ## ModelUserList

        // ModelPatikaList

        patikaMenu = new JPopupMenu();
        JMenuItem updateMenu = new JMenuItem("Edit");
        JMenuItem deleteMenu = new JMenuItem("Delete");
        patikaMenu.add(updateMenu);
        patikaMenu.add(deleteMenu);

        updateMenu.addActionListener(e -> {
            int select_id = Integer.parseInt(tbl_patika_list.getValueAt(tbl_patika_list.getSelectedRow(), 0).toString());
            UpdatePatikaGUI updateGUI = new UpdatePatikaGUI(Patika.getFetch(select_id));
            updateGUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadPatikaModel();
                    loadPatikaCombo();
                    loadCourseModel();
                }
            });
        });

        deleteMenu.addActionListener(e -> {
            if (Helper.confirm("sure")) {
                int select_id = Integer.parseInt(tbl_patika_list.getValueAt(tbl_patika_list.getSelectedRow(), 0).toString());
                if (Patika.delete(select_id)) {
                    Helper.showMessage("success");
                    loadPatikaModel();
                    loadPatikaCombo();
                    loadCourseModel();
                } else {
                    Helper.showMessage("error");
                }
            }
        });


        mdl_patika_list = new DefaultTableModel();
        Object[] col_patika_list = {"ID", "Patika"};
        mdl_patika_list.setColumnIdentifiers(col_patika_list);
        row_patika_list = new Object[col_patika_list.length];
        loadPatikaModel();


        tbl_patika_list.setModel(mdl_patika_list);
        tbl_patika_list.setComponentPopupMenu(patikaMenu);
        tbl_patika_list.getTableHeader().setReorderingAllowed(false);
        tbl_patika_list.getColumnModel().getColumn(0).setMaxWidth(100);

        tbl_patika_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                int selected_row = tbl_patika_list.rowAtPoint(point);
                tbl_patika_list.setRowSelectionInterval(selected_row, selected_row);
            }
        });
        // ## ModelPatikaList

        // CourseList
        mdl_course_list = new DefaultTableModel();
        Object[] col_courseList = {"ID", "Course Name", "Programming Language", "Patika", "Instructor"};
        mdl_course_list.setColumnIdentifiers(col_courseList);
        row_course_list = new Object[col_courseList.length];

        loadCourseModel();

        tbl_course_list.setModel(mdl_course_list);
        tbl_course_list.getColumnModel().getColumn(0).setMaxWidth(75);
        tbl_course_list.getTableHeader().setReorderingAllowed(false);


        loadPatikaCombo();
        loadInstructorCombo();

        tbl_course_list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try{
                    String select_courseId=tbl_course_list.getValueAt(tbl_course_list.getSelectedRow(),0).toString();
                    fld_hiddenCourseId.setText(select_courseId);
                }catch (Exception exception){

                }
            }
        });
        // ## CourseList

        mdl_contentList = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0) {
                    return false;
                }
                return super.isCellEditable(row, column);
            }
        };


        Object[] col_contentList = {"ID", "Name"};
        mdl_contentList.setColumnIdentifiers(col_contentList);
        row_contentList = new Object[col_contentList.length];

        loadContentModel();

        tbl_allContent.setModel(mdl_contentList);
        tbl_allContent.getColumnModel().getColumn(0).setMaxWidth(30);
        tbl_allContent.getTableHeader().setReorderingAllowed(false);


        tbl_allContent.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if(e.getType()==TableModelEvent.UPDATE){
                    int content_id = Integer.parseInt(tbl_allContent.getValueAt(tbl_allContent.getSelectedRow(),0).toString());
                    String content_name = tbl_allContent.getValueAt(tbl_allContent.getSelectedRow(),1).toString();


                    if(Content.updateName(content_id,content_name)){
                        Helper.showMessage("done");
                    }

                    loadContentModel();
                }
            }
        });

        tbl_allContent.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    String select_contentId = tbl_allContent.getValueAt(tbl_allContent.getSelectedRow(), 0).toString();
                    fld_contentId.setText(select_contentId);
                } catch (Exception exception) {

                }
            }
        });

        mdl_quizList = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0) {
                    return false;
                }
                return super.isCellEditable(row, column);
            }
        };


        Object[] col_quizList = {"ID", "Name"};
        mdl_quizList.setColumnIdentifiers(col_quizList);
        row_quizList = new Object[col_quizList.length];

        loadQuizModel();

        tbl_allQuiz.setModel(mdl_quizList);
        tbl_allQuiz.getColumnModel().getColumn(0).setMaxWidth(30);
        tbl_allQuiz.getTableHeader().setReorderingAllowed(false);

        tbl_allQuiz.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if(e.getType()==TableModelEvent.UPDATE){
                    int quiz_id = Integer.parseInt(tbl_allQuiz.getValueAt(tbl_allQuiz.getSelectedRow(),0).toString());
                    String quiz_name = tbl_allQuiz.getValueAt(tbl_allQuiz.getSelectedRow(),1).toString();


                    if(Quiz.updateName(quiz_id,quiz_name)){
                        Helper.showMessage("done");
                    }

                    loadContentModel();
                }
            }
        });

        tbl_allQuiz.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    String select_quizId = tbl_allQuiz.getValueAt(tbl_allQuiz.getSelectedRow(), 0).toString();
                    fld_quizId.setText(select_quizId);
                } catch (Exception exception) {

                }
            }
        });

        btn_user_add.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_user_name) || Helper.isFieldEmpty(fld_user_username) || Helper.isFieldEmpty(fld_user_password)) {
                Helper.showMessage("fill");
            } else {
                String name = fld_user_name.getText();
                String userName = fld_user_username.getText();
                String password = fld_user_password.getText();
                String type = cmb_user_type.getSelectedItem().toString();
                if (User.add(name, userName, password, type)) {
                    Helper.showMessage("success");
                    loadUserModel();
                    loadInstructorCombo();
                    fld_user_name.setText(null);
                    fld_user_username.setText(null);
                    fld_user_password.setText(null);
                }
            }
        });


        btn_user_delete.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_user_id)) {
                Helper.showMessage("fill");
            } else {
                if (Helper.confirm("sure")) {
                    int user_id = Integer.parseInt(fld_user_id.getText());
                    if (User.delete(user_id)) {
                        Helper.showMessage("success");
                        loadUserModel();
                        loadInstructorCombo();
                        loadCourseModel();
                        fld_user_id.setText(null);
                    } else {
                        Helper.showMessage("error");
                    }
                }
            }
        });

        btn_user_sh.addActionListener(e -> {
            String name = fld_sh_user_name.getText();
            String username = fld_sh_user_username.getText();
            String type = cmb_sh_user_type.getSelectedItem().toString();
            String query = User.searchQuery(name, username, type);

            ArrayList<User> searchUser = User.searchUserList(query);
            loadUserModel(searchUser);
        });

        btn_logout.addActionListener(e -> {
            dispose();
            LoginGUI loginGUI = new LoginGUI();
        });

        btn_patika_add.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_patika_name)) {
                Helper.showMessage("fill");
            } else {
                if (Patika.add(fld_patika_name.getText())) {
                    Helper.showMessage("success");
                    loadPatikaModel();
                    loadPatikaCombo();
                    fld_patika_name.setText(null);
                } else {
                    Helper.showMessage("error");
                }
            }
        });
        btn_course_add.addActionListener(e -> {
            Item patikaItem = (Item) cmb_course_patika.getSelectedItem();
            Item userItem = (Item) cmb_course_user.getSelectedItem();
            if (Helper.isFieldEmpty(fld_course_name) || Helper.isFieldEmpty(fld_course_lang)) {
                Helper.showMessage("success");
            } else {
                if (Course.add(userItem.getKey(), patikaItem.getKey(), fld_course_name.getText(), fld_course_lang.getText())) {
                    Helper.showMessage("success");
                    loadCourseModel();
                    fld_course_name.setText(null);
                    fld_course_lang.setText(null);
                } else {
                    Helper.showMessage("error");
                }
            }
        });
        deleteButtonContent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Helper.isFieldEmpty(fld_contentId)) {
                    Helper.showMessage("Pick a content to delete");
                } else {
                    if(Helper.confirm("sure")){
                        int contentId = Integer.parseInt(fld_contentId.getText());
                        if (Content.delete(contentId)) {
                            Quiz.deleteQuizByContent(contentId);
                            Helper.showMessage("success");
                            loadContentModel();
                            loadQuizModel();
                            fld_contentId.setText(null);

                        } else {
                            Helper.showMessage("error");
                        }
                    }
                }
            }
        });
        deleteButtonQuiz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Helper.isFieldEmpty(fld_quizId)) {
                    Helper.showMessage("Pick a quiz to delete");
                } else {
                    if(Helper.confirm("sure")){
                        int quizId = Integer.parseInt(fld_quizId.getText());
                        if (Quiz.delete(quizId)) {
                            Helper.showMessage("success");
                            loadQuizModel();
                            fld_quizId.setText(null);

                        } else {
                            Helper.showMessage("error");
                        }
                    }
                }
            }
        });
        deleteButtonCourse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Helper.isFieldEmpty(fld_hiddenCourseId)) {
                    Helper.showMessage("Pick a course to delete");
                } else {
                    if(Helper.confirm("sure")){
                        int courseId = Integer.parseInt(fld_hiddenCourseId.getText());
                        if (Course.delete(courseId)) {
                            Helper.showMessage("done");
                            loadCourseModel();
                            fld_hiddenCourseId.setText(null);

                        } else {
                            Helper.showMessage("error");
                        }
                    }
                }
            }
        });
    }

    private void loadCourseModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_course_list.getModel();
        clearModel.setRowCount(0);
        int i;

        for (Course obj : Course.getList()) {
            i = 0;
            row_course_list[i++] = obj.getId();
            row_course_list[i++] = obj.getName();
            row_course_list[i++] = obj.getLang();
            row_course_list[i++] = obj.getPatika().getName();
            row_course_list[i++] = obj.getInstructor().getName();
            mdl_course_list.addRow(row_course_list);
        }
    }

    private void loadPatikaModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_patika_list.getModel();
        clearModel.setRowCount(0);
        int i = 0;

        for (Patika obj : Patika.getList()) {
            i = 0;
            row_patika_list[i++] = obj.getId();
            row_patika_list[i++] = obj.getName();
            mdl_patika_list.addRow(row_patika_list);
        }
    }

    public void loadUserModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_user_list.getModel();
        clearModel.setRowCount(0);
        int i = 0;

        for (User obj : User.getList()) {
            i = 0;
            row_user_list[i++] = obj.getId();
            row_user_list[i++] = obj.getName();
            row_user_list[i++] = obj.getUsername();
            row_user_list[i++] = obj.getPassword();
            row_user_list[i++] = obj.getType();
            mdl_user_list.addRow(row_user_list);
        }
    }

    public void loadUserModel(ArrayList<User> list) {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_user_list.getModel();
        clearModel.setRowCount(0);
        int i = 0;

        for (User obj : list) {
            i = 0;
            row_user_list[i++] = obj.getId();
            row_user_list[i++] = obj.getName();
            row_user_list[i++] = obj.getUsername();
            row_user_list[i++] = obj.getPassword();
            row_user_list[i++] = obj.getType();
            mdl_user_list.addRow(row_user_list);
        }
    }

    public void loadPatikaCombo() {
        cmb_course_patika.removeAllItems();

        for (Patika obj : Patika.getList()) {
            cmb_course_patika.addItem(new Item(obj.getId(), obj.getName()));
        }
    }

    public void loadInstructorCombo() {
        cmb_course_user.removeAllItems();
        for (User obj : User.getListOnlyInstructor()) {
            cmb_course_user.addItem(new Item(obj.getId(), obj.getName()));
        }
    }

    public static ArrayList<Quiz> getQuizList(){
        ArrayList<Quiz> quizList= new ArrayList<>();
        Quiz obj;

        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM quiz");
            while(rs.next()){
                obj=new Quiz(rs.getInt("id"),rs.getInt("content_id"),rs.getString("quiz_name"),rs.getString("quiz_text"));
                quizList.add(obj);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizList;
    }
    public void loadQuizModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_allQuiz.getModel();
        clearModel.setRowCount(0);
        for (Quiz obj : getQuizList()) {
            int i = 0;
            row_quizList[i++] = obj.getId();
            row_quizList[i++] = obj.getQuiz_name();

            mdl_quizList.addRow(row_quizList);
        }
    }
    public static ArrayList<Content> getContentList(){
        ArrayList<Content> contentList= new ArrayList<>();
        Content obj;

        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM content");
            while(rs.next()){
                obj=new Content(rs.getInt("id"),rs.getString("name"),rs.getInt("course_id"),rs.getString("description"),rs.getString("youtubeLink"));
                contentList.add(obj);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contentList;
    }
    public void loadContentModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_allContent.getModel();
        clearModel.setRowCount(0);
        for (Content obj : getContentList()) {
            int i = 0;
            row_contentList[i++] = obj.getId();
            row_contentList[i++] = obj.getName();

            mdl_contentList.addRow(row_contentList);
        }
    }


}
