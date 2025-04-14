package cv;

import java.io.PrintStream;

public class ParagraphWithList extends Paragraph {
    UnorderedList list = new UnorderedList();

    ParagraphWithList(String content) {
        super(content);
    }

    ParagraphWithList addItemToList(String content){
        list.addItem(content);
        return this;
    }

    void writeHTML(PrintStream out){
        super.writeHTML(out);
        list.writeHTML(out);
    }
}
