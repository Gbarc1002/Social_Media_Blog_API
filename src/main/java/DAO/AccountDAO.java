package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    

    //Get Account by Username
    //  Input:    String username
    //  Output:   Account Object or null if it doesn't exist
    public Account getAccountByUsername(String username) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            Account account = new Account(rs.getInt("account_id"), 
                                        rs.getString("username"),
                                        rs.getString("password"));
            return account;
        } catch (SQLException e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
        return null;
    }

    //Add new Account to database
    //  Input:  Account object
    //  Output: Same Account object with account_id if inserted correctly
    //          null if not inputted into database right
    public Account insertAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());

            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int accountID = (int) rs.getLong(1);
                return new Account(accountID, account.getUsername(), account.getPassword());
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.print(e.getMessage());
        }
        return null;
    }
}
