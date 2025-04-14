package cv;
import java.io.IOException;
import java.io.PrintStream;


public class Main {
    public static void main(String[] args) throws IOException {
        Document cv = new Document("Jana Kowalski - CV");
        cv.setPhoto("https://upload.wikimedia.org/wikipedia/commons/thumb/7/71/Calico_tabby_cat_-_Savannah.jpg/1200px-Calico_tabby_cat_-_Savannah.jpg");
        cv.addSection("Wykształcenie")
                .addParagraph("2000-2005 Przedszkdddole im. Królewny Snieżki w ...")
                .addParagraph("2006-2012 SP7 im Ronalda Regana w ...")
                .addParagraph(
                        new ParagraphWithList("Kursy")
                                .addItemToList("Języka Angielskiego")
                                .addItemToList("Języka Hiszpańskiego")
                                .addItemToList("Szydełkowania")
                );
        cv.addSection("Umiejętności")
                .addParagraph(
                        new ParagraphWithList("Znane technologie")
                                .addItemToList("C")
                                .addItemToList("C++")
                                .addItemToList("Java")
                );
        cv.writeHTML(new PrintStream("cv.html","UTF-8"));

        String json = cv.toJson();
        Document cv2 = cv.fromJson(json);
        System.out.println(cv2.toJson().equals(json)); //teraz po serializacji -> deserializacji -> serializacji te pliki są takie same
        PrintStream ps = new PrintStream("cv.json");
        ps.println(cv2.toJson()); //zapisanie do pliku cv.json
        ps.close();

    }
}
