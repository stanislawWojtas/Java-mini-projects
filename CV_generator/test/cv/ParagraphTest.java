package cv;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class ParagraphTest {

    @Test
    public void testConstructor(){
        Paragraph paragraph = new Paragraph("Initial content");
        assertEquals("Initial content", paragraph.content);
    }

    @Test
    public void setContent() {
        Paragraph p = new Paragraph("content");
        p.setContent("NEWcontent");
        assertEquals("NEWcontent", p.content);
    }

    @Test
    public void testWriteHTML() {
        Paragraph p = new Paragraph("content");
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(os);
        p.writeHTML(printStream);
        String expected = "<p>content</p>\r\n";
        assertEquals(expected, os.toString());

    }
}