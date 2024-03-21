/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package functionController;

import controller.BasedRequiredAuthenticationController;
import dal.AttendanceDBContext;
import dal.SessionDBContext;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import model.Attendance;
import model.Session;
import model.Student;
import model.User;

/**
 *
 * @author pc
 */
public class AttendanceTakingController extends BasedRequiredAuthenticationController {
   
  


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response, User user)
    throws ServletException, IOException {
        HttpSession session = request.getSession();
        int id = (int) session.getAttribute("uid");
        SessionDBContext db = new SessionDBContext();
        int sesid = Integer.parseInt(request.getParameter("id"));
        Session ses = db.getSessions(sesid,id);
        request.setAttribute("ses",ses);
        
        AttendanceDBContext attDb = new AttendanceDBContext();
        ArrayList<Attendance> atts = attDb.getAttendancesBySession(sesid, id);
        
        request.setAttribute("atts", atts);
        request.getRequestDispatcher("../view/instructor/att.jsp").forward(request, response);
    } 

 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response, User user)
    throws ServletException, IOException {
        Session ses = new Session();
        ses.setId(Integer.parseInt(request.getParameter("sesid")));
        String[] stuids = request.getParameterValues("stuid");
        for (String stuid : stuids) {
            Attendance a = new Attendance();
            a.setSession(ses);
            Student s = new Student();
            s.setId(Integer.parseInt(stuid));
            a.setStudent(s);
            a.setStatus(request.getParameter("status"+stuid).equals("present"));
            a.setDescription(request.getParameter("description"+stuid));
            ses.getAtts().add(a);
        }
        SessionDBContext sesDB = new SessionDBContext();
        sesDB.addAttendences(ses);
        sesDB.getIid(Integer.parseInt(request.getParameter("sesid")));
        response.sendRedirect("timetable?id="+sesDB.getIid(Integer.parseInt(request.getParameter("sesid"))));
    }



}
