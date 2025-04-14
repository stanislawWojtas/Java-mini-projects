package cv;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class ListItemTest {

    @Test
    public void testWriteHTML() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        ListItem it = new ListItem("Hello1234");
        it.writeHTML(ps);
        String expected = "<li>Hello1234</li>\r\n"; // \r\n żeby wyłapał znak nowej lini zrobiony przez println()
        assertEquals(expected, os.toString());
    }
}