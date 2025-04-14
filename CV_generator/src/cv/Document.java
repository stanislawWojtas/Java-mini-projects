package cv;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Document {
    String title;
    Photo photo;
    List<Section> sections = new ArrayList<>();

    Document(String title){
        this.title = title;
    }

    Document setTitle(String title){
        this.title = title;
        return this;
    }

    Document setPhoto(String photoUrl){
        this.photo = new Photo(photoUrl);
        return this;
    }

    Section addSection(String sectionTitle){
        // utwórz sekcję o danym tytule i dodaj do sections
        Section section = new Section(sectionTitle);
        sections.add(section);
        return section;
    }
    Document addSection(Section s){
        return this;
    }


    void writeHTML(PrintStream out){
    // zapisz niezbędne znaczniki HTML
    // dodaj tytuł i obrazek
    // dla każdej sekcji wywołaj section.writeHTML(out)
        out.println("<html>");
        out.println("<body>");
        out.println("<meta charset=\"UTF-8\">");
        out.println("<link rel=\"stylesheet\" href=\"style.css\">");
        out.println("<h1>" + title + "</h1>");
        if(photo != null){
            photo.writeHTML(out);
        }
        for(Section section : sections){
            section.writeHTML(out);
        }
        out.println("</body>");
        out.println("</html>");
    }



    Document fromJson(String jsonString){
        RuntimeTypeAdapterFactory<Paragraph> adapter =
                RuntimeTypeAdapterFactory
                        .of(Paragraph.class)
                        .registerSubtype(Paragraph.class)
                        .registerSubtype(ParagraphWithList.class);
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(adapter).create();
        return gson.fromJson(jsonString, Document.class);
    }

    String toJson() {
        RuntimeTypeAdapterFactory<Paragraph> adapter =
                RuntimeTypeAdapterFactory
                        .of(Paragraph.class)
                        .registerSubtype(Paragraph.class)
                        .registerSubtype(ParagraphWithList.class);
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(adapter).setPrettyPrinting().create();
        return gson.toJson(this);
    }
}
