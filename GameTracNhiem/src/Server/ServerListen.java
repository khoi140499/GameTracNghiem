/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Model.Game;
import Model.Message;
import Model.Question;
import Model.User;
import database.Data;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerListen extends Thread {

    //Khai báo luồng gửi nhận
    private Socket socket;
    ObjectInputStream inputStream;
    ObjectOutputStream outputStream;

    public User user;
    public static Game game1;
    public static Game game2;

    Boolean execute = true;

    ServerListen(Socket socket) throws IOException {
        // truyền vào luồng socket
        this.socket = socket;
        inputStream = new ObjectInputStream(socket.getInputStream());
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        execute = true;
    }
    // Nhận dữ liệu

    void ReceiveMessage(Message msg) throws IOException {

        switch (msg.getType()) {
            // Nếu type = 0: Nhận từ Form Login. check đăng nhập
            case 0: {
                User temp = (User) msg.getObject();// Lấy dữ liệu
                Data df = new Data();
                ArrayList<User> lstUser = new ArrayList<User>();
                lstUser = df.getUserList();
                for (User obj : lstUser) {
                    if (obj.getUserName().contains(temp.getUserName()) && obj.getPassWord().contains(temp.getPassWord())) {
                        user = obj;
                    }
                }
                if (user != null) {
                    User objU = user;
                    objU.setOnline(1);
                    try {
                        Boolean a = df.updateUser(objU);
                    } catch (SQLException ex) {
                        Logger.getLogger(ServerListen.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (user != null) {
                        System.out.println("Server: Xin chao " + user.getUserName());
                    }
                }
                SendMessage(0, user);
                break;
            }
            //dang ky tai khoan
            case 140:{
                User a=(User) msg.getObject();
                Data df=new Data();
                String s=df.dangKyUser(a);
                if(s.equalsIgnoreCase("tc")==true){
                    SendMessage(140, null);
                }
                else{
                    SendMessage(140, a);
                }
            }
            // Nhận dữ liệu mời người tham gia trò chơi
            case 20: {
                Data df = new Data();
                ArrayList<User> lstUser = new ArrayList<User>();
                lstUser = df.getUserList();
                User objUser = new User();
                //objUser = lstUser.get(Integer.parseInt(msg.getObject().toString()));
                String a = msg.getObject().toString();
                for (User item : lstUser) {
                    if (item.getUserName().contains(a)) {
                        Main.userRoom2 = item;// Lưu tạp người tham gia 1
                        objUser = item;// Lấy thông tin người tham gia 1
                    }
                }
                Main.userRoom = this.user;// Lưu dữ liệu người vừa gửi yêu cầu lên
                for (ServerListen lstUser1 : Main.lstClient) {
                    if (lstUser1.user.getUserName().contains(objUser.getUserName())) {
                        lstUser1.SendMessage(30, null);// Gửi yêu cầu tham gia trò chơi đến người kia
                    }
                }
                break;
            }
            // Nhận dữ liệu từ danh sách
            case 21: {
                // Lấy thông tin người dùng online
                Data df = new Data();
                ArrayList<User> lstUser = new ArrayList<User>();
                lstUser = df.getUserList();
                ArrayList<User> lstUserOnline = new ArrayList<User>();
                for (User lstUser1 : lstUser) {
                    if (lstUser1.getOnline() == 1 && !lstUser1.getUserName().contains(this.user.getUserName())) {
                        lstUserOnline.add(lstUser1);
                    }
                }
                SendMessage(21, lstUserOnline);// gửi trả lst những người dùng online
                break;
            }
            // Thông báo người kia đã vào phòng
            case 31: {
                Data df = new Data();
                ArrayList<User> lstUser = new ArrayList<User>();
                lstUser = df.getUserList();
                User objUser = new User();
                for (ServerListen lstUser1 : Main.lstClient) {
                    if (lstUser1.user.getUserName().contains(Main.userRoom.getUserName())) { // lấy luồng của người gửi yêu cầu vào phòng
                        lstUser1.SendMessage(33, null); // gửi thông báo xác nhận
                    }
                }
                break;
            }
            // Thông báo từ chối
            case 32: {
                Data df = new Data();
                ArrayList<User> lstUser = new ArrayList<User>();
                lstUser = df.getUserList();
                User objUser = new User();
                for (ServerListen lstUser1 : Main.lstClient) {
                    if (lstUser1.user.getUserName().contains(Main.userRoom.getUserName())) {
                        lstUser1.SendMessage(34, null);// Gửi thông báo từ chối
                    }
                }
                break;
            }
            // Yêu cầu thông tin sẵn sàng chơi
            case 40: {
                Data df = new Data();
                ArrayList<User> lstUser = new ArrayList<User>();
                lstUser = df.getUserList();
                User objUser = new User();
                for (ServerListen lstUser1 : Main.lstClient) {
                    if (lstUser1.user.getUserName().contains(Main.userRoom2.getUserName()) && lstUser1 != this) {
                        lstUser1.SendMessage(41, null); // Gửi yêu cầu đến người còn lại
                    } else if (lstUser1.user.getUserName().contains(Main.userRoom.getUserName()) && lstUser1 != this) {
                        lstUser1.SendMessage(41, null);// Gửi yêu cầu đến người còn lại
                    }
                }
                break;
            }
            // Gửi chấp nhận
            case 44: {
                Data df = new Data();
                ArrayList<Question> questions = new ArrayList<Question>();
                questions = df.getQuestion();
                Message temp = new Message();
                temp.setType(46);
                temp.setQuestions(questions);
                // Tạo câu hỏi và gửi đến cho các máy
                for (ServerListen lstUser1 : Main.lstClient) {
                    lstUser1.SendMessage(temp);
                }
                break;
            }
            // Gửi từ chối
            case 45: {
                Data df = new Data();
                ArrayList<User> lstUser = new ArrayList<User>();
                lstUser = df.getUserList();
                User objUser = new User();
                for (ServerListen lstUser1 : Main.lstClient) {
                    if (lstUser1.user.getUserName().contains(Main.userRoom.getUserName())) {
                        lstUser1.SendMessage(47, null);// Thông báo từ chối
                    }
                }
                break;
            }
            // Gửi kết quả
            case 50: {
                if (game1 == null) {
                    game1 = (Game) msg.getObject();
                    if (game1.getCount() == 7) {
                        Main.Diem = 1;// Gửi lên đầu tiên, đúng 5 câu thì chiên thắng
                        game1.getUser().setPoint((game1.getUser().getPoint() + 1));
                        SendMessage(100, null);
                    } else {
                        //thông báo chờ
                        SendMessage(100, null);
                    }
                } else { // Đã có người gửi lên
                    game2 = (Game) msg.getObject();
                    if (Main.Diem == 1) {
                        Main.Diem = 0;
                        SendMessage(62, game1);//Thua
                        for (ServerListen lstUser1 : Main.lstClient) {
                            if (lstUser1.user.getUserName().contains(Main.userRoom.getUserName()) && !this.user.getUserName().contains(Main.userRoom.getUserName())) {
                                lstUser1.SendMessage(61, game2);
                            } else if (lstUser1.user.getUserName().contains(Main.userRoom2.getUserName()) && !this.user.getUserName().contains(Main.userRoom2.getUserName())) {
                                lstUser1.SendMessage(61, game2);
                            }
                        }
                    } else {
                        if (game2.getCount() == 7) {
                            Main.Diem = 1;
                            //thắng
                            game2.getUser().setPoint((game2.getUser().getPoint() + 1));
                            SendMessage(61, game1);
                            for (ServerListen lstUser1 : Main.lstClient) {
                                if (lstUser1.user.getUserName().contains(Main.userRoom.getUserName()) && !this.user.getUserName().contains(Main.userRoom.getUserName())) {
                                    lstUser1.SendMessage(61, game2);
                                } else if (lstUser1.user.getUserName().contains(Main.userRoom2.getUserName()) && !this.user.getUserName().contains(Main.userRoom2.getUserName())) {
                                    lstUser1.SendMessage(61, game2);
                                }
                            }
                        } else {
                            //hòa
                            Main.Diem = (float) 0.5;
                            SendMessage(60, game1);// Gửi thông tin về client
                            float d = (float) (game1.getUser().getPoint() + 0.5); // up điểm lên csdl
                            game1.getUser().setPoint(d);
                            d = (float) (game2.getUser().getPoint() + 0.5);
                            game2.getUser().setPoint(d);
                            for (ServerListen lstUser1 : Main.lstClient) {
                                if (lstUser1.user.getUserName().contains(Main.userRoom.getUserName()) && !this.user.getUserName().contains(Main.userRoom.getUserName())) {
                                    lstUser1.SendMessage(60, game2);
                                } else if (lstUser1.user.getUserName().contains(Main.userRoom2.getUserName()) && !this.user.getUserName().contains(Main.userRoom2.getUserName())) {
                                    lstUser1.SendMessage(60, game2);
                                }
                            }
                        }
                    }
                }
                break;
            }

            case 70: {
                Data df = new Data();
                ArrayList<User> lstUser = new ArrayList<User>();
                lstUser = df.getUserList();
                User objUser = new User();
                for (ServerListen lstUser1 : Main.lstClient) {
                    if (lstUser1.user.getUserName().contains(Main.userRoom.getUserName()) && lstUser1 != this) {
                        lstUser1.SendMessage(71, null);
                    } else if (lstUser1.user.getUserName().contains(Main.userRoom2.getUserName()) && lstUser1 != this) {
                        lstUser1.SendMessage(71, null);
                    }
                }
                break;
            }
        }
    }
    // Các hàm gửi tin với các đối số khác nhau

    public void SendMessage(int ty, Object obj) throws IOException {
        Message temp = new Message(ty, obj);
        SendMessage(temp);
    }

    public void SendMessage(int ty, ArrayList<User> obj) throws IOException {
        Message temp = new Message(ty, obj);
        SendMessage(temp);
    }

    public void SendMessage(int ty, ArrayList<User> obj, ArrayList<Question> questions) throws IOException {
        Message temp = new Message(ty, obj, questions);
        SendMessage(temp);
    }

    public void SendMessage(Message msg) throws IOException {
        outputStream.reset();
        outputStream.writeObject(msg);
    }

    public Boolean closeClient() throws Throwable {
        Data df = new Data();
        User objU = this.user;
        objU.setOnline(0);
        try {
            Boolean a = df.updateUser(objU);
        } catch (Exception e) {
        }
        Main.lstClient.remove(this);
        try {
            this.socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerListen.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Client Exit");
        execute = false;

        return true;
    }

    @Override
    public void run() {
        while (execute) {
            try {
                Object o = inputStream.readObject();
                if (o != null) {
                    ReceiveMessage((Message) o);
                }

            } catch (IOException e) {
                try {
                    closeClient();
                } catch (Throwable ex) {
                    Logger.getLogger(ServerListen.class.getName()).log(Level.SEVERE, null, ex);
                }
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
