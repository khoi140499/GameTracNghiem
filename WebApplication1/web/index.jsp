<%-- 
    Document   : index
    Created on : Dec 7, 2020, 4:55:08 AM
    Author     : Khoi
--%>

<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="Model.Users"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
        <title>Bảng xếp hạng</title>
        <style>
            html, body{
                margin: 0;
                padding: 0;
            }
            table{
                font-size: 18px;
                text-align: center;
            }
            .main{
                width: 100%;
                height: 100%;
                min-height: 100vh;
                background-image: url('a1.jpg');
                background-position: center;
                background-size: cover;
            }
            .main-bxh{
                position: absolute;
                width: 600px;
                height: 450px;
                background: white;
                top: 50%;
                left: 50%;
                transform: translateX(-50%) translateY(-50%);
                border-radius: 2%;
                box-shadow: 0 0 12px 6px #00000096;
            }
            .nenden{
                position: absolute;
                width: 100%;
                height: 100%;
                background: #3D634A;
                opacity: 0.4;
            }
            .bxh-text{
                width: 100%;
                height: 100px;
                float: left;
                text-align: center;
            }
            .bxh-text h1{
                font-family: "Roboto";
                font-size: 33px;
            }
            .bxh-tb{
                width: 100%;
                height: 100%;
                box-sizing: border-box;
                overflow-y: auto;
                padding: 70px;
            }
        </style>
    </head>
    <%ArrayList<Users> arr = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn;
            String sql = "jdbc:mysql://localhost/game";;
            String user = "root";
            String pass = "";
            conn = DriverManager.getConnection(sql, user, pass);

            String sql1 = "select * from users order by point desc";
            PreparedStatement ps = conn.prepareStatement(sql1);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                arr.add(new Users(rs.getString(2), rs.getFloat(5)));
            }

        } catch (Exception e) {
            System.out.println(e);
        }%>
    <body>
        <div class="main">
            <div class="nenden">
            </div>
            <div class="main-bxh">
                <div class="bxh-text">
                    <img src="logo.png" style="margin-top: 10px;">
                </div>
                <div class="bxh-tb">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th class="text-center">Name</th>
                                <th class="text-center">Point</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for(Users as:arr){ %>
                            <tr>
                                <td>
                                    <span>
                                        <% out.print(as.getUserName()); %>
                                    </span>
                                </td>
                                <td><span>
                                        <% out.print(as.getPoint()); %>
                                    </span></td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </body>
</html>
