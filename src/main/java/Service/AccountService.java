package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }
    
    //Adds Account to Database
    //  Input:  Account Object without id
    //  Output: Account Object with id or null
    public Account addAccount(Account account) {
        if (accountDAO.getAccountByUsername(account.getUsername()) != null ||
            account.getPassword().length() < 4 || account.getUsername() == "") {
            return null;
        } else {
            return accountDAO.insertAccount(account);
        }
    }
}
