/*
 * AnnotationSelectUserDialog.java
 *
 * Created on 19 มิถุนายน 2551, 14:00 น.
 */
package annotation;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author  Somprasong Damyos
 */
public class AnnotationSelectUserDialog extends javax.swing.JDialog {

    private AnnotationPaintPanel paintPanel;
    private Vector username;
    private Vector whocan_view = new Vector();
    private Vector whocan_edit = new Vector();
    private Vector whocan_del = new Vector();
    private Object[][] data;
    private ArrayList user_id;
    private boolean is_ok;
    private String owner;
    private Object[][] data2;
    private Vector user;
    private ArrayList user_id2 = new ArrayList();
    private Vector username2 = new Vector();
    private Vector view_users = new Vector();
    private Vector user_id_old = new Vector();
    private Vector username_old = new Vector();
    private Vector removed_user;
    private String host;
    private boolean public_chked = false;

    /** Creates new form AnnotationSelectUserDialog */
    public AnnotationSelectUserDialog(AnnotationPaintPanel paintPanel, String host, String owner, Vector view_users, Vector edit_users, Vector del_users) {
        super(null, DEFAULT_MODALITY_TYPE); // set donnt do anything else before close this dialog
        this.paintPanel = paintPanel;
        this.view_users = view_users;
        this.owner = owner;
        this.host = host;
        get_user(this.host, "");

        data2 = new Object[this.username.size()][1];
        for (int i = 0; i < this.username.size(); i++) {
            data2[i][0] = (String) username.get(i);
        }

        initTable(edit_users, del_users);
        initComponents();


        this.setLocationRelativeTo(null);
        this.show();
    }

    private void initTable(Vector edit_users, Vector del_users) {
        System.out.println("sdsad:  " + username2.size());
        data = new Object[this.username2.size()][4];
        for (int i = 0; i < this.username2.size(); i++) {
            data[i][0] = (String) username2.get(i);
            data[i][1] = new Boolean(true);
            boolean edit_chk = false;
            for (int j = 0; j < edit_users.size(); j++) {
                if (edit_users.get(j).toString().equals(user_id2.get(i).toString() + ">")) {
                    edit_chk = true;
                    data[i][2] = new Boolean(true);
                }
            }
            if (edit_chk == false) {
                data[i][2] = new Boolean(false);
            }
            boolean del_chk = false;
            for (int j = 0; j < del_users.size(); j++) {
                if (del_users.get(j).toString().equals(user_id2.get(i).toString() + ">")) {
                    del_chk = true;
                    data[i][3] = new Boolean(true);
                }
            }
            if (del_chk == false) {
                data[i][3] = new Boolean(false);
            }

        }
    }

    private void get_user(String host, String text) {

        Vector getuser = new Vector();
        try {
            URL urlServlet = new URL(host);
            URLConnection connection = urlServlet.openConnection();
            connection.setRequestProperty("CONTENT_TYPE", "text/plain");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            PrintWriter p = new PrintWriter(connection.getOutputStream());
            p.println("0");
            if (!text.equals("")) {
                p.println(text);
                System.out.println("text : " + text);
            }
            p.close();
            InputStreamReader ir = new InputStreamReader(connection.getInputStream());
            BufferedReader br = new BufferedReader(ir);
            int i = 0;
            do {
                getuser.add(br.readLine());
            } while (getuser.get(i++) != null);

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("getuser : " + getuser.size());
        System.out.println("getuser : " + getuser.get(0));
        set_user(getuser);
    }

    private void set_user(Vector getuser) {
        user = new Vector();
        user_id = new ArrayList();
        username = new Vector();
        int i = 0;
        // ขณะที่ Dialog ทำงานอยู่
        if (user_id2.size() > 0 && username2.size() > 0) {
            try {
                while (getuser.get(i) != null) {
                    String str_temp = (String) getuser.get(i++);
                    int index = str_temp.indexOf(",");
                    if (!str_temp.substring(0, index).equals(owner)) {
                        boolean same = false;

                        for (int j = 0; j < user_id2.size(); j++) {

                            String old_user = user_id2.get(j).toString();
                            if (str_temp.substring(0, index).equals(old_user)) {
                                same = true;
                            }
                        }

                        if (same == false) {
                            user_id.add(str_temp.substring(0, index));
                            username.add(str_temp.substring(index + 1, str_temp.length()));
                            user.add(str_temp);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {// ตอนเปิด dialog
            try {
                while (getuser.get(i) != null) {
                    String str_temp = (String) getuser.get(i++);
                    int index = str_temp.indexOf(",");
                    if (!str_temp.substring(0, index).equals(owner)) {
                        boolean same = false;

                        for (int j = 0; j < view_users.size(); j++) {
                            String view = view_users.get(j).toString();
                            String old_user = view.substring(0, view.length() - 1);
                            if (str_temp.substring(0, index).equals(old_user)) {
                                user_id2.add(str_temp.substring(0, index));
                                username2.add(str_temp.substring(index + 1, str_temp.length()));
                                same = true;
                            }
                        }

                        if (same == false) {
                            user_id.add(str_temp.substring(0, index));
                            username.add(str_temp.substring(index + 1, str_temp.length()));
                            user.add(str_temp);
                        }
                    }
                }
                view_users.removeAllElements();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private void addUser(Vector user) {
        try {
            for (int i = 0; i < user.size(); i++) {
                String str_temp = (String) user.get(i);
                int index = str_temp.indexOf(",");
                if (!str_temp.substring(0, index).equals(owner)) {
                    user_id2.add(str_temp.substring(0, index));
                    username2.add(str_temp.substring(index + 1, str_temp.length()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTable() {
        data = new Object[this.username2.size()][4];
        for (int i = 0; i < this.username2.size(); i++) {

            data[i][0] = (String) username2.get(i);
            data[i][1] = new Boolean(true);
            data[i][2] = new Boolean(false);
            data[i][3] = new Boolean(false);

        }
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                data,
                new String[]{
            "รายชื่อ", "สิทธิ์เรียกดู", "สิทธิ์แก้ใข", "สิทธิ์ลบ"
        }) {

            Class[] types = new Class[]{
                java.lang.String.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean[]{
                false, true, true, true
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
    //jTable1.setAutoCreateRowSorter(true);
    }

    public boolean getOK() {
        return is_ok;
    }

    private void searchUser(String text){
        get_user(this.host, text);
        data2 = new Object[this.username.size()][1];
        for (int i = 0; i < this.username.size(); i++) {
            data2[i][0] = (String) username.get(i);
        }
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
                data2,
                new String[]{
            "รายชื่อ"
        }) {

            Class[] types = new Class[]{
                java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        input = new javax.swing.JTextField();
        seach = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        public_chk = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        add_button = new javax.swing.JButton();
        remove_button = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        ok = new javax.swing.JButton();
        cancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("กำหนดสิทธิ์ Annotation");
        setResizable(false);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        jPanel2.setMaximumSize(new java.awt.Dimension(32767, 30));
        jPanel2.setPreferredSize(new java.awt.Dimension(100, 30));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        input.setMaximumSize(new java.awt.Dimension(150, 20));
        input.setPreferredSize(new java.awt.Dimension(150, 20));
        jPanel2.add(input);

        seach.setText("ค้นหา");
        seach.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seachActionPerformed(evt);
            }
        });
        jPanel2.add(seach);

        public_chk.setText("กำหนดสิทธิ์ให้ผู้ใช้ทุกคนในระบบ");
        jPanel5.add(public_chk);
        public_chk.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    jTable1.setEnabled(false);
                    jTable2.setEnabled(false);
                    add_button.setEnabled(false);
                    remove_button.setEnabled(false);
                    public_chked = true;
                } else {
                    jTable1.setEnabled(true);
                    jTable2.setEnabled(true);
                    add_button.setEnabled(true);
                    remove_button.setEnabled(true);
                    public_chked = false;
                }
            }
        });

        jPanel2.add(jPanel5);

        getContentPane().add(jPanel2);

        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));

        jScrollPane2.setPreferredSize(new java.awt.Dimension(150, 200));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            data2,
            new String [] {
                "รายชื่อ"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);
        //jTable2.setAutoCreateRowSorter(true);

        jPanel3.add(jScrollPane2);

        jPanel4.setMaximumSize(new java.awt.Dimension(73, 46));
        jPanel4.setMinimumSize(new java.awt.Dimension(59, 46));
        jPanel4.setPreferredSize(new java.awt.Dimension(59, 46));
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));

        add_button.setText(">>");
        add_button.setMaximumSize(new java.awt.Dimension(59, 23));
        add_button.setMinimumSize(new java.awt.Dimension(59, 23));
        add_button.setPreferredSize(new java.awt.Dimension(59, 23));
        add_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                add_buttonActionPerformed(evt);
            }
        });
        jPanel4.add(add_button);

        remove_button.setText("<<");
        remove_button.setMaximumSize(new java.awt.Dimension(59, 23));
        remove_button.setMinimumSize(new java.awt.Dimension(59, 23));
        remove_button.setPreferredSize(new java.awt.Dimension(59, 23));
        remove_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                remove_buttonActionPerformed(evt);
            }
        });
        jPanel4.add(remove_button);

        jPanel3.add(jPanel4);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(500, 200));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            data,
            new String [] {
                "รายชื่อ", "สิทธิ์เรียกดู", "สิทธิ์แก้ใข", "สิทธิ์ลบ"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(200);
        //jTable1.getColumnModel().getColumn(1).setResizable(false);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(100);
        //jTable1.getColumnModel().getColumn(2).setResizable(false);
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(100);
        //jTable1.getColumnModel().getColumn(3).setResizable(false);
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(100);

        //jTable1.setAutoCreateRowSorter(true);

        jPanel3.add(jScrollPane1);

        getContentPane().add(jPanel3);

        jPanel1.setMaximumSize(new java.awt.Dimension(300, 30));
        jPanel1.setMinimumSize(new java.awt.Dimension(100, 20));
        jPanel1.setPreferredSize(new java.awt.Dimension(300, 30));

        ok.setText("ตกลง");
        ok.setMaximumSize(new java.awt.Dimension(63, 23));
        ok.setMinimumSize(new java.awt.Dimension(63, 23));
        ok.setPreferredSize(new java.awt.Dimension(63, 23));
        ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okActionPerformed(evt);
            }
        });
        jPanel1.add(ok);

        cancel.setText("ยกเลิก");
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });
        jPanel1.add(cancel);

        getContentPane().add(jPanel1);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        is_ok = false;
        this.dispose();
    }//GEN-LAST:event_cancelActionPerformed

    private void okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okActionPerformed
        if (public_chked) {
            whocan_view.add("alluser>");
            whocan_edit.add("alluser>");
            whocan_del.add("alluser>");
            paintPanel.setViewUser(whocan_view);
            paintPanel.setEditUser(whocan_edit);
            paintPanel.setDeleteUser(whocan_del);
        } else {
            boolean view_owner_selected = false;
            boolean edit_owner_selected = false;
            boolean del_owner_selected = false;
            int ii = username2.size();
            for (int i = 0; i < this.username2.size(); i++) {

                if (jTable1.getValueAt(i, 1).toString().equals("true")) {
                    if (user_id2.get(i).toString().equals(owner)) {
                        view_owner_selected = true;
                    }
                    whocan_view.add(user_id2.get(i).toString() + ">");
                }
                if (jTable1.getValueAt(i, 2).toString().equals("true")) {
                    if (user_id2.get(i).toString().equals(owner)) {
                        edit_owner_selected = true;
                    }
                    whocan_edit.add(user_id2.get(i).toString() + ">");
                }
                if (jTable1.getValueAt(i, 3).toString().equals("true")) {
                    if (user_id2.get(i).toString().equals(owner)) {
                        del_owner_selected = true;
                    }
                    whocan_del.add(user_id2.get(i).toString() + ">");
                }
            }

            if (whocan_view.size() > 0) {
                if (edit_owner_selected == false) {
                    whocan_view.add(owner + ">");
                }
                paintPanel.setViewUser(whocan_view);
            } else {
                whocan_view.add(owner + ">");
                paintPanel.setViewUser(whocan_view);
            }
            if (whocan_edit.size() > 0) {
                if (edit_owner_selected == false) {
                    whocan_edit.add(owner + ">");
                }
                paintPanel.setEditUser(whocan_edit);
            } else {
                whocan_edit.add(owner + ">");
                paintPanel.setEditUser(whocan_edit);
            }
            if (whocan_del.size() > 0) {
                if (edit_owner_selected == false) {
                    whocan_del.add(owner + ">");
                }
                paintPanel.setDeleteUser(whocan_del);
            } else {
                whocan_del.add(owner + ">");
                paintPanel.setDeleteUser(whocan_del);
            }
        }

        for (int j = 0; j < whocan_view.size(); j++) {
            System.out.println(j + ":Who can view: " + whocan_view.get(j));
        }
        for (int k = 0; k < whocan_edit.size(); k++) {
            System.out.println(k + ":Who can edit: " + whocan_edit.get(k));
        }
        for (int l = 0; l < whocan_del.size(); l++) {
            System.out.println(l + ":Who can del: " + whocan_del.get(l));
        }
        is_ok = true;
        this.dispose();
}//GEN-LAST:event_okActionPerformed

    private void add_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_add_buttonActionPerformed
        Vector new_user = new Vector();

        int[] select_rows = jTable2.getSelectedRows();
        for (int i = 0; i < select_rows.length; i++) {
            new_user.add(user.get(select_rows[i]));
        }
        //jTable2.
        addUser(new_user);
        setTable();
        searchUser("");
}//GEN-LAST:event_add_buttonActionPerformed

    private void remove_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_remove_buttonActionPerformed
        removed_user = new Vector();
        int[] select_rows = jTable1.getSelectedRows();
        for (int i = 0; i < select_rows.length; i++) {
            removed_user.add(user_id2.get(select_rows[i]).toString() + "," +
                    username2.get(select_rows[i]).toString());
            if (select_rows.length == 1) {
                user_id2.remove(select_rows[i]);
                username2.remove(select_rows[i]);
            }
        }
        if (select_rows.length > 1) {
            for (int j = select_rows.length - 1; j >= 0; j--) {
                System.out.println("select_rows : " + select_rows[j]);
                user_id2.remove(select_rows[j]);
                username2.remove(select_rows[j]);
            }
        }
        System.out.println("size: " + user_id2.size() + ", " + username2.size());
        this.setTable();
        searchUser("");
}//GEN-LAST:event_remove_buttonActionPerformed

    private void seachActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seachActionPerformed
        searchUser(input.getText());
        
        //        get_user(this.host, input.getText());
//        data2 = new Object[this.username.size()][1];
//        for (int i = 0; i < this.username.size(); i++) {
//            data2[i][0] = (String) username.get(i);
//        }
//        jTable2.setModel(new javax.swing.table.DefaultTableModel(
//                data2,
//                new String[]{
//            "รายชื่อ"
//        }) {
//
//            Class[] types = new Class[]{
//                java.lang.String.class
//            };
//            boolean[] canEdit = new boolean[]{
//                false
//            };
//
//            @Override
//            public Class getColumnClass(int columnIndex) {
//                return types[columnIndex];
//            }
//
//            @Override
//            public boolean isCellEditable(int rowIndex, int columnIndex) {
//                return canEdit[columnIndex];
//            }
//        });
}//GEN-LAST:event_seachActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton add_button;
    private javax.swing.JButton cancel;
    private javax.swing.JTextField input;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JButton ok;
    private javax.swing.JCheckBox public_chk;
    private javax.swing.JButton remove_button;
    private javax.swing.JButton seach;
    // End of variables declaration//GEN-END:variables
}
