import com.example.newdesign.controller.*;
import com.example.newdesign.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessagingTest {

    private List<Message> messages;
    private Message message;
    public int id;
    public String text;

    private MessageDAOImpl messageDAO = new MessageDAOImpl();


    @Test
    public void testSendMessage(){
        messageDAO.addMessage("Hello", 10,20, 10);
        messages = messageDAO.getMessages(10,20);
        id = messages.getFirst().getId();
        assertEquals(1, messages.toArray().length);
    }

    @Test
    public void testEditingMessage(){
        messages = messageDAO.getMessages(10,20);
        messageDAO.editMessage(messages.getFirst().getId(), "I'm good, thank you");
        text = messages.getFirst().getMessageText();
        assertEquals("I'm good, thank you", text);
    }

    @Test
    public void testDeleteMessage(){
        messageDAO.deleteMessage(id);
        messages = messageDAO.getMessages(10,20);
        assertEquals(0, messages.toArray().length);
    }



}