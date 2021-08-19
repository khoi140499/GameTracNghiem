package database;

import Model.Question;
import Model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
public class Data {

    Connection con = Connections.Newconnect();

    public ArrayList<User> getUserList() {

        PreparedStatement stm = null;
        ResultSet rs = null;
        ArrayList<User> uslist = new ArrayList<User>();
        try {
            String sql = "Select * from users";
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                User us = new User();
                us.setId(rs.getInt(1));
                us.setUserName(rs.getString(2));
                us.setPassWord(rs.getString(3));
                us.setOnline(rs.getInt(4));
                us.setPoint(rs.getFloat(5));
                uslist.add(us);
            }
        } catch (SQLException ex) {
        }
        return uslist;
    }

    public ArrayList<User> getUserRank() {
        PreparedStatement stm = null;
        ResultSet rs = null;
        ArrayList<User> uslist = new ArrayList<User>();
        try {
            String sql = "SELECT * FROM users\n"
                    + "ORDER BY point DESC";
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                User us = new User();
                us.setId(rs.getInt(1));
                us.setUserName(rs.getString(2));
                us.setPassWord(rs.getString(3));
                us.setOnline(rs.getInt(4));
                us.setPoint(rs.getFloat(5));
                uslist.add(us);
            }
        } catch (SQLException ex) {
        }
        return uslist;
    }

    public ArrayList<Question> getQuestion() {
        PreparedStatement stm = null;
        ResultSet rs = null;
        ArrayList<Question> list = new ArrayList<Question>();

        try {
            String sql = "SELECT * FROM question ";
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                Question question = new Question();
                question.setId(rs.getInt(1));
                question.setQuestion(rs.getString(2));
                question.setAnswerA(rs.getString(3));
                question.setAnswerB(rs.getString(4));
                question.setAnswerC(rs.getString(5));
                question.setAnswerD(rs.getString(6));
                question.setCorrectAnswer(rs.getString(7));
                list.add(question);
            }
        } catch (SQLException ex) {

        }
        return list;
    }

    public boolean updateUser(User user) throws SQLException {
        String sqlStatement
                = "update users "
                + "set status = ?"
                + " where userID = ?;";
        PreparedStatement updateQuery = con.prepareStatement(sqlStatement);
        updateQuery.setInt(1, user.getOnline());
        updateQuery.setInt(2, user.getId());
        updateQuery.execute();
        return true;
    }

    public void updateDiem(User user) {
        try {
            String sql = "UPDATE users SET point = ? WHERE userID = ?";
            PreparedStatement updateQuery = con.prepareStatement(sql);
            updateQuery.setFloat(1, user.getPoint());
            updateQuery.setInt(2, user.getId());
            updateQuery.execute();
        } catch (SQLException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateScore(String username, float point){
        try {
            String sql="update users set point = ? where account=?";
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setFloat(1, point);
            ps.setString(2, username);
            ps.executeUpdate();
        } catch (Exception e) {
        }
    }
    
    public String dangKyUser(User user){
        String s="";
        ArrayList<User> us=new ArrayList<>();
        try {
            String sql1="select * from users";
            PreparedStatement ps1=con.prepareStatement(sql1);
            ResultSet rs=ps1.executeQuery();
            while (rs.next()) {                
                User a=new User(rs.getString(2),rs.getString(3));
                us.add(a);
            }
        } catch (Exception e) {
        }
        try {
            String sql="insert into users values(?,?,?,?,?) ";
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setInt(1, 1+us.size());
            ps.setString(2, user.getUserName());
            ps.setString(3, user.getPassWord());
            ps.setInt(4, user.getOnline());
            ps.setFloat(5, user.getPoint());
            int r=ps.executeUpdate();
            if(r>0){
                s="tc";
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return s;
    }
}
