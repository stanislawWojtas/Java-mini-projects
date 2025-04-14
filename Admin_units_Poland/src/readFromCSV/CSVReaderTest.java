package readFromCSV;

import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;

//PLIK GPT PRZEANALIZUJ !!!!!!!!!!!
public class CSVReaderTest {    

    public static void main(String[] args) throws Exception {
        // Test 1: Odczyt i wypisanie wszystkich pól jako String
        CSVReader reader = new CSVReader("src/titanic-part.csv", ",", true);
        while (reader.next()) {
            for (String column : reader.getColumnLabels()) {
                System.out.print(reader.get(column) + " | ");
            }
            System.out.println();
        }

        // Test 2: Odczyt danych z brakującymi wartościami
        reader = new CSVReader("src/titanic-part.csv", ",", true);
        while (reader.next()) {
            try {
                int id = reader.getInt("PassengerId");
                String name = reader.get("Name");
                double fare = reader.isMissing("Fare") ? 0.0 : reader.getDouble("Fare");
                System.out.printf(Locale.US, "%d %s %.2f\n", id, name, fare);
            } catch (Exception e) {
                System.out.println("Błąd odczytu: " + e.getMessage());
            }
        }

        // Test 3: Odczyt z innego źródła niż plik
        String text = """
                      a,b,c
                      123.4,567.8,91011.12
                      """;
        reader = new CSVReader(new StringReader(text), ",", true);
        while (reader.next()) {
            System.out.println(reader.getDouble("a") + " " + reader.getDouble("b") + " " + reader.getDouble("c"));
        }

        // Test 4: Odczyt daty i czasu
        String dateText = """
                           date,time
                           2024-11-06,15:45:00
                           """;
        reader = new CSVReader(new StringReader(dateText), ",", true);
        while (reader.next()) {
            LocalDate date = reader.getDate("date", "yyyy-MM-dd");
            LocalTime time = reader.getTime("time", "HH:mm:ss");
            System.out.println("Data: " + date + ", Czas: " + time);
        }

        // Test 5: Odczyt nieistniejących kolumn
        try {
            System.out.println(reader.getInt(100));
        } catch (Exception e) {
            System.out.println("Błąd: indeks kolumny poza zakresem - " + e.getMessage());
        }
    }
}
