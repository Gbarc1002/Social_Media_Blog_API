package Service;

import Model.Message;
import DAO.MessageDAO;
import DAO.AccountDAO;
import java.util.List;

public class MessageService {
    MessageDAO messageDAO;
    AccountDAO accountDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }

    /* Adds Message to database
     *  Input:  Message object without id
     *  Output: Message object with id or null
     *  
     *  Constraints:    message_text must not be blank or exceed 255 characters
     *                  posted_by must exist in account table
     */
    public Message addMessage(Message message) {
        if (message.getMessage_text() == "" ||
            message.getMessage_text().length() > 255 ||
            accountDAO.searchByAccountID(message.getPosted_by()) == null) {
            return null;
        } else {
            return messageDAO.insertMessage(message);
        }
    }

    /* Gets all messages from database
     *  No constraints to check for
     *  Returns list of messages
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    /* Gets message from database from message_id
     * Will return message object, even if empty
     */
    public Message getMessageFromID(int id) {
        return messageDAO.searchByMessageID(id);
    }
}
