package cv;

import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class SectionTest {

    private Section section = new Section("");

    @Test
    public void setTitle() {
        section.setTitle("Test123");
        assertEquals("Test123", section.title);
    }

    @Test
    public void addParagraph() {
        section.addParagraph("Paragraph 1");
        assertEquals(1, section.paragraphs.size());
    }

    @Test
    public void testAddParagraph() {
        Paragraph paragraph = new Paragraph("Paragraph 2");
        section.addParagraph(paragraph);
        assertEquals(1, section.paragraphs.size());
    }

    @Test
    public void writeHTML() {
        section.setTitle("Title");
        section.addParagraph("Par 1.");
        section.addParagraph("Par 2.");
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        section.writeHTML(ps);

        String expected = "<h2>Title</h2>\r\n" + "<p>Par 1.</p>\r\n" + "<p>Par 2.</p>\r\n";
        assertEquals(expected, os.toString());
    }
}