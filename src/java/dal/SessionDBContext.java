/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Attendance;
import model.Group;
import model.Room;
import model.Session;
import model.Subject;
import model.TimeSlot;

/**
 *
 * @author pc
 */
public class SessionDBContext extends DBContext<Session> {

    public ArrayList<Session> getSessions(int iid, Date from, Date to) {
        ArrayList<Session> sessions = new ArrayList<>();
        try {
            String sql = "SELECT\n"
                    + "    ses.sesid,\n"
                    + "    ses.`date`,\n"
                    + "    ses.`index`,\n"
                    + "    ses.isAtt,\n"
                    + "    r.roomid,\n"
                    + "    sub.subid,\n"
                    + "    sub.subname,\n"
                    + "    g.gid,\n"
                    + "    g.gname,\n"
                    + "    t.tid,\n"
                    + "    t.`description`\n"
                    + "FROM `fall2023_assignment`.`Session` ses\n"
                    + "INNER JOIN `fall2023_assignment`.`Group` g ON ses.gid = g.gid\n"
                    + "INNER JOIN `fall2023_assignment`.`Subject` sub ON g.subid = sub.subid\n"
                    + "INNER JOIN `fall2023_assignment`.`Room` r ON r.roomid = ses.rid\n"
                    + "INNER JOIN `fall2023_assignment`.`TimeSlot` t ON ses.tid = t.tid\n"
                    + "WHERE ses.iid = ? AND ses.`date` >= ? AND ses.`date` <= ?;";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, iid);
            stm.setDate(2, from);
            stm.setDate(3, to);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Session session = new Session();
                session.setId(rs.getInt("sesid"));
                session.setDate(rs.getDate("date"));
                session.setIndex(rs.getInt("index"));
                session.setIsAtt(rs.getBoolean("isAtt"));
                Room r = new Room();
                r.setId(rs.getString("roomid"));
                session.setRoom(r);

                Group g = new Group();
                g.setId(rs.getInt("gid"));
                g.setName(rs.getString("gname"));
                session.setGroup(g);

                Subject sub = new Subject();
                sub.setId(rs.getInt("subid"));
                sub.setName(rs.getString("subname"));
                g.setSubject(sub);

                TimeSlot time = new TimeSlot();
                time.setId(rs.getInt("tid"));
                time.setDescription(rs.getString("description"));
                session.setSlot(time);

                sessions.add(session);
            }

        } catch (SQLException ex) {
            Logger.getLogger(SessionDBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sessions;
    }

    public Session getSessions(int sesid, int id) {
        try {
            String sql = "SELECT\n"
                    + "    ses.sesid,\n"
                    + "    ses.`date`,\n"
                    + "    ses.`index`,\n"
                    + "    ses.isAtt,\n"
                    + "    r.roomid,\n"
                    + "    sub.subid,\n"
                    + "    sub.subname,\n"
                    + "    g.gid,\n"
                    + "    g.gname,\n"
                    + "    t.tid,\n"
                    + "    t.`description`\n"
                    + "FROM `fall2023_assignment`.`Session` ses\n"
                    + "INNER JOIN `fall2023_assignment`.`Group` g ON ses.gid = g.gid\n"
                    + "INNER JOIN `fall2023_assignment`.`Subject` sub ON g.subid = sub.subid\n"
                    + "INNER JOIN `fall2023_assignment`.`Room` r ON r.roomid = ses.rid\n"
                    + "INNER JOIN `fall2023_assignment`.`TimeSlot` t ON ses.tid = t.tid\n"
                    + "INNER JOIN `fall2023_assignment`.`Instructor` i ON g.`sup_iis` = i.iid\n"
                    + "WHERE ses.sesid = ? AND i.iid = ?;";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, sesid);
            stm.setInt(2, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Session session = new Session();
                session.setId(rs.getInt("sesid"));
                session.setDate(rs.getDate("date"));
                session.setIndex(rs.getInt("index"));
                session.setIsAtt(rs.getBoolean("isAtt"));
                Room r = new Room();
                r.setId(rs.getString("roomid"));
                session.setRoom(r);

                Group g = new Group();
                g.setId(rs.getInt("gid"));
                g.setName(rs.getString("gname"));
                session.setGroup(g);

                Subject sub = new Subject();
                sub.setId(rs.getInt("subid"));
                sub.setName(rs.getString("subname"));
                g.setSubject(sub);

                TimeSlot time = new TimeSlot();
                time.setId(rs.getInt("tid"));
                time.setDescription(rs.getString("description"));
                session.setSlot(time);
                return session;
            }

        } catch (SQLException ex) {
            Logger.getLogger(SessionDBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int getIid(int sesid) {
        try {
            String sql = "SELECT `iid`\n"
                    + "FROM `fall2023_assignment`.`Session`\n"
                    + "WHERE `sesid` = ?;";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, sesid);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                int in = rs.getInt("iid");
                return in;
            }

        } catch (SQLException ex) {
            Logger.getLogger(SessionDBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public void addAttendences(Session ses) {
        try {
            connection.setAutoCommit(false);
            //Update isAtt
            String sql_update_isAtt = "UPDATE `fall2023_assignment`.`Session`\n"
                    + "SET `isAtt` = 1\n"
                    + "WHERE `sesid` = ?;";
            PreparedStatement stm_update_isAtt = connection.prepareStatement(sql_update_isAtt);
            stm_update_isAtt.setInt(1, ses.getId());
            stm_update_isAtt.executeUpdate();

            //Remove existing attendences
            String sql_remove_atts = "DELETE FROM `fall2023_assignment`.`Attendance`\n"
                    + "WHERE `sesid` = ?;";
            PreparedStatement stm_remove_atts = connection.prepareStatement(sql_remove_atts);
            stm_remove_atts.setInt(1, ses.getId());
            stm_remove_atts.executeUpdate();

            //Insert attendences 
            for (Attendance att : ses.getAtts()) {
                String sql_insert_att = "INSERT INTO `fall2023_assignment`.`Attendance`\n"
                        + "           (`sesid`,\n"
                        + "            `stuid`,\n"
                        + "            `status`,\n"
                        + "            `description`,\n"
                        + "            `att_datetime`)\n"
                        + "     VALUES\n"
                        + "           (?, ?, ?, ?, NOW());";
                PreparedStatement stm_insert_att = connection.prepareStatement(sql_insert_att);
                stm_insert_att.setInt(1, ses.getId());
                stm_insert_att.setInt(2, att.getStudent().getId());
                stm_insert_att.setBoolean(3, att.isStatus());
                stm_insert_att.setString(4, att.getDescription());
                stm_insert_att.executeUpdate();
            }
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
                Logger.getLogger(SessionDBContext.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(SessionDBContext.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                Logger.getLogger(SessionDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void insert(Session model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(Session model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void remove(Session model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Session get(Session model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList<Session> list() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
