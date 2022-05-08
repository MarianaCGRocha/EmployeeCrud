import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Employee {
    private JPanel Main;
    private JTextField textName;
    private JTextField textSalary;
    private JTextField textPhone;
    private JButton saveButton;
    private JTable table1;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton searchButton;
    private JTextField textId;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Employee");
        frame.setContentPane(new Employee().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    Connection con;
    PreparedStatement pst;

    public void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/mgcompany", "root", "");
            System.out.println("Sucess");
        } catch(ClassNotFoundException ex) {
            ex.printStackTrace();
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void table_load() {
        try {
            pst = con.prepareStatement("select * from employee");
            ResultSet rs = pst.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Employee() {
        connect();
        table_load();
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String employeeSalary, employeePhone, employeeName;

                employeeName = textName.getText();
                employeeSalary = textSalary.getText();
                employeePhone = textPhone.getText();

                try {
                    pst = con.prepareStatement("insert into employee(employeeName, employeeSalary, employeePhone) " +
                            "values(?, " +
                            "?, ?)");
                    pst.setString(1, employeeName);
                    pst.setString(2, employeeSalary);
                    pst.setString(3, employeePhone);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Added!");
                    // table_load();
                    textName.setText("");
                    textSalary.setText("");
                    textPhone.setText("");
                    textName.requestFocus();
                } catch (SQLException el) {
                    el.printStackTrace();
                }

            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String id = textId.getText();

                        pst = con.prepareStatement("select employeeName, employeeSalary, employeePhone from employee " +
                                "where id = ?");
                        pst.setString(1, id);
                        ResultSet rs = pst.executeQuery();

                    if(rs.next() == true) {
                        String employeeName = rs.getString(1);
                        String employeeSalary = rs.getString(2);
                        String employeePhone = rs.getString(3);

                        textName.setText((employeeName));
                        textSalary.setText(employeeSalary);
                        textPhone.setText(employeePhone);
                    } else {
                        textName.setText("");
                        textSalary.setText("");
                        textPhone.setText("");
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }



            }
        });
    }






}
