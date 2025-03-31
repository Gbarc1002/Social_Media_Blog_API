package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import static org.mockito.ArgumentMatchers.nullable;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;
    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::allMessageHandler);
        app.get("/messages/{message_id}", this::messageIDHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    /* Handler to register new accounts into database
     * No repeating usernames, no blank usernames, 
     * no password with less than 4 characters
     */
    private void registerHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account newAccount = accountService.addAccount(account);
        if (newAccount == null) {
            ctx.status(400);
        } else {
            ctx.status(200).json(mapper.writeValueAsString(newAccount));
        }
    }

    /* Handler to get accounts from database using username and password
     * 
     */
    private void loginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account newAccount = accountService.loginAccount(account);
        if (newAccount == null) {
            ctx.status(401);
        } else {
            ctx.status(200).json(mapper.writeValueAsString(newAccount));
        }
    }

    /* Handler to post new messages into database
     * Message text should not be blank and should not exceed 255 characters
     * posted_by should link to the account_id from the account table
     */
    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message newMessage = messageService.addMessage(message);
        if (newMessage == null) {
            ctx.status(400);
        } else {
            ctx.status(200).json(mapper.writeValueAsString(newMessage));
        }
    }

    /* Handler to get all messages from database
     * No error codes should be needed
     * Works even if table is empty
     */
    private void allMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Message> messages = messageService.getAllMessages();
        ctx.status(200).json(mapper.writeValueAsString(messages));
    }

    /* Handler to get a message from database based on given message_id
     * Will return message object or empty
     */
    private void messageIDHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = messageService.getMessageFromID(Integer.valueOf(ctx.pathParam("message_id")));
        if (message == null) {
            ctx.status(200).result("");
        } else 
            ctx.status(200).json(mapper.writeValueAsString(message));
    }
    
    /* Handler to delete message from database based on given message_id
     * Returns message object that was deleted, or empty
     */
    private void deleteMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = messageService.deleteMessage(Integer.valueOf(ctx.pathParam("message_id")));
        if (message == null) {
            ctx.status(200).result("");
        } else 
            ctx.status(200).json(mapper.writeValueAsString(message));
    }

    /* Handler to update message from database based on given message_id
     * Sends json of newly updated message object, or empty
     */
    private void updateMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String text = mapper.readValue(ctx.body(), Message.class).getMessage_text();
        Message updatedMessage = messageService.updateMessage(Integer.valueOf(ctx.pathParam("message_id")), text);
        if (updatedMessage == null) {
            ctx.status(400);
        } else 
            ctx.status(200).json(mapper.writeValueAsString(updatedMessage));
    }
}