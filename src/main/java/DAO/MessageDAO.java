package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    
    /* Adds new Message to database
     *  Input:  Message Object
     *  Output: Message Object with message_id or null
     */
    public Message insertMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int messageID = (int) rs.getLong(1);
                return new Message(messageID, message.getPosted_by(), 
                                    message.getMessage_text(), message.getTime_posted_epoch());
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
        return null;
    }

    /* Retrieves all messages from message table
     *  Input:  none
     *  Output: List of all messages in message table, even if blank
     */
    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message";

            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                messages.add(new Message(   rs.getInt("message_id"),
                                            rs.getInt("posted_by"),
                                            rs.getString("message_text"),
                                            rs.getLong("time_posted_epoch")));
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
        return messages;
    }

    /* Retrieves message based on message_id
     *  Input:  int message_id
     *  Output: Message object, even if empty
     */
    public Message searchByMessageID(int id) {
        Connection connection = ConnectionUtil.getConnection();
        Message message = null;
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                message = new Message( rs.getInt("message_id"),
                                    rs.getInt("posted_by"),
                                    rs.getString("message_text"),
                                    rs.getLong("time_posted_epoch"));
            } 
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
        return message;
    }
}
