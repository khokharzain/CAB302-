import com.example.newdesign.model.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageTest {

private Message message;

@BeforeEach
    public void SetUp(){
    message = new Message(1,19,  3,"Hello");
    }

    @Test
    public void testGetId(){
        message.setId(1);
        assertEquals(1, message.getId());
    }

    @Test
    public void testGetSenderId(){
        assertEquals(19, message.getSenderId());
    }

    @Test
    public void testSetSenderId(){
        message.setSenderId(4);
        assertEquals(4, message.getSenderId());
    }

    @Test
    public void testGetRecieverId(){
        assertEquals(3, message.getReceiverId());
    }

    @Test
    public void testSetRecieverId(){
        message.setReceiverId(5);
        assertEquals(5, message.getReceiverId());
    }

    @Test
    public void testGetMessage(){
        assertEquals("Hello", message.getMessageText());
    }

    @Test
    public void testSetMessage(){
        message.setMessageText("How are you?");
        assertEquals("How are you?", message.getMessageText());
    }


}
