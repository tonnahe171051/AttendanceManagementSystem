/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Attendance;
import model.Session;
import model.Student;

/**
 *
 * @author pc
 */
public class AttendanceDBContext extends DBContext<Attendance> {

    public ArrayList<Attendance> getAttendancesBySession(int sesid, int iid) {
        ArrayList<Attendance> atts = new ArrayList<>();
        try {
            String sql = "SELECT s.stuid, s.stuname,\n"
                    + "       IFNULL(a.status, 0) as `status`,\n"
                    + "       IFNULL(a.description, '') as `description`,\n"
                    + "       IFNULL(a.att_datetime, NOW()) as `att_datetime`\n"
                    + "FROM `fall2023_assignment`.`Session` ses\n"
                    + "INNER JOIN `fall2023_assignment`.`Group` g ON g.gid = ses.gid\n"
                    + "INNER JOIN `fall2023_assignment`.`Group_Student` gs ON g.gid = gs.gid\n"
                    + "INNER JOIN `fall2023_assignment`.`Student` s ON s.stuid = gs.stuid\n"
                    + "LEFT JOIN `fall2023_assignment`.`Attendance` a ON a.sesid = ses.sesid AND s.stuid = a.stuid\n"
                    + "WHERE ses.sesid = ? AND g.sup_iis = ?;";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, sesid);
            stm.setInt(2, iid);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Attendance att = new Attendance();
                Student s = new Student();
                Session ses = new Session();
                s.setId(rs.getInt("stuid"));
                s.setName(rs.getString("stuname"));
                att.setStudent(s);
                ses.setId(sesid);
                att.setSession(ses);
                att.setStatus(rs.getBoolean("status"));
                att.setDescription(rs.getString("description"));
                att.setDatetime(rs.getTimestamp("att_datetime"));
                atts.add(att);
            }

        } catch (SQLException ex) {
            Logger.getLogger(AttendanceDBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
        return atts;
    }

    @Override
    public void insert(Attendance model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(Attendance model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void remove(Attendance model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Attendance get(Attendance model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList<Attendance> list() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
