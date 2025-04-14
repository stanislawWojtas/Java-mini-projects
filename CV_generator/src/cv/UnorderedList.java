package cv;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class UnorderedList {
    List<ListItem> items = new ArrayList<>();

    UnorderedList addItem(String content){
        ListItem item = new ListItem(content);
        items.add(item);
        return this;
    }

    void writeHTML(PrintStream out){
        out.println("<ul>");
        for(ListItem item : items){
            item.writeHTML(out);
        }
        out.println("</ul>");
    }
}
