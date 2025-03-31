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

    /* Identify message from database based on message_id and deletes it
     * Will return message object that should be identified from message_id before it gets deleted
     * Returns null if it didn't exist prior. Does not need to run delete function if it doesn't exist already
     */
    public Message deleteMessage(int id) {
        Message message = messageDAO.searchByMessageID(id);
        if (message != null) {
            messageDAO.deleteMessage(id);
        }
        return message;
    }

    /* Identify message from database based on message_id and updates text
     * Will return new message object with revised message_text
     * Will be null if it doesn't follow constraints
     *  Constraints:    message_text is not blank or more than 255 characters
     *                  message_id is found to be null and does not exist on table already
     */
    public Message updateMessage(int id, String text) {
        Message message = messageDAO.searchByMessageID(id);
        if (message == null || text.isBlank() || text.length() > 255) {
            return null;
        } else {
            messageDAO.updateMessage(id, text);
            return new Message(id, message.getPosted_by(), text, message.getTime_posted_epoch());
        }
    }

    /* Gets all messages from database that is sent from a particular account
     * Does not need to check if account exists. 
     * Will return empty if account does not exist or if table does not have a record with account_id
     */
    public List<Message> getMessagesFromAccount(int accountID) {
        return messageDAO.messagesByAccount(accountID);
    }
}
