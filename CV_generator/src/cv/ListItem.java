package cv;

import java.io.PrintStream;

public class ListItem {
    String content;

    ListItem(String content) {
        this.content = content;
    }

    void writeHTML(PrintStream out){
        out.println("<li>" + content + "</li>");
    }
}
