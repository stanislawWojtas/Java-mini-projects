package readFromCSV;

import java.io.*;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class CSVReader {
    BufferedReader reader;
    String delimiter;
    boolean hasHeader;

    List<String> columnLabels = new ArrayList<>();
    Map<String,Integer> columnLabelsToInt = new HashMap<>();
    String[] current; //przechowuje bieżący wiersz

    //ta część pozwoli stworzyć pattern uwzględniając różny delimiter ";"
    private static Pattern pattern = Pattern.compile(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
    private Pattern createPattern(String delimiter){
        String regex = String.format("%s(?=([^\"]*\"[^\"]*\")*[^\"]*$)", Pattern.quote(delimiter));
        return Pattern.compile(regex);
    }



    public CSVReader(String filename,String delimiter,boolean hasHeader) {
        try {
            reader = new BufferedReader(new FileReader(filename, Charset.forName("UTF-8")));
            this.delimiter = delimiter;
            this.hasHeader = hasHeader;
            this.pattern = createPattern(delimiter); //  regex dla separatora
            if(hasHeader)parseHeader();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public CSVReader(String filename, String delimiter) {
        this(filename, delimiter, true);
    }

    public CSVReader(String filename) {
        this(filename, ",", true);
    }

    //odczyt z dowolnego źródła
    public CSVReader(Reader reader, String delimiter, boolean hasHeader){
        this.reader = new BufferedReader(reader);
        this.delimiter = delimiter;
        this.hasHeader = hasHeader;
        this.pattern = createPattern(delimiter); //  regex dla separatora
        if(hasHeader) {
            parseHeader();
        }
    }

    void parseHeader() {
        try {
            String line = reader.readLine();
            if (line == null) {
                return;
            }
            String[] header = pattern.split(line); //podzielenie wedle pattern
            for (int i = 0; i < header.length; i++) {
                header[i] = header[i].replace("\"", "").trim();//zastępuje cudzysłowy pustym znakiem i usuwa znaki białe
                columnLabels.add(header[i]);
                columnLabelsToInt.put(header[i], i);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean next(){
        // czyta następny wiersz, dzieli na elementy i przypisuje do current
        try{
            String line = reader.readLine();
            if (line == null) {
                return false;
            }
            current = pattern.split(line);
            for(int i = 0; i < current.length; i++){
                current[i] = current[i].replace("\"", "").trim(); //zamienia cudzysłów na ciąg pusty i usuwa znaki białe
            }
            return true;
        }catch(IOException e){
            return false;
        }
    }

    List <String> getColumnLabels(){
        return columnLabels;
    }

    int getRecordLength(){
        if(current == null){
            return 0;
        }
        return current.length;
    }

    boolean isMissing(int columnIndex){
        if(columnIndex >= current.length || columnIndex < 0){
            return true;
        }
        //jeżeli nie ma elenentu o tym indexie to zwraca true
        else if(current[columnIndex].isEmpty()){
            return true;
        }
        return false;
    }

    boolean isMissing(String columnLabel){
        Integer idx = columnLabelsToInt.get(columnLabel);
        if(idx == null){
            return true;
        }
        return isMissing(idx);
    }

    String get(int columnIndex){
        //zwraca pusty tekst jeśli isMissing = true
        return isMissing(columnIndex) ? "" : current[columnIndex];

    }

    String get(String columnLabel){
        Integer idx = columnLabelsToInt.get(columnLabel);
        //dodatkowo jeżeli index = null to zwraca pusty tekst
        return idx == null ? "" : get(idx);
    }

    int getInt(int columnIndex){
        return Integer.parseInt(get(columnIndex));
    }

    int getInt(String columnLabel){
        return Integer.parseInt(get(columnLabel));
    }

    double getDouble(int columnIndex){
        return Double.parseDouble(get(columnIndex));
    }

    double getDouble(String columnLabel){
        return Double.parseDouble(get(columnLabel));
    }

    long getLong(int columnIndex){
        return Long.parseLong(get(columnIndex));
    }

    long getLong(String columnLabel){
        return Long.parseLong(get(columnLabel));
    }

    //funkcję zwracające czas i datę
    LocalTime getTime(int columnIndex, String format){
        LocalTime time = LocalTime.parse(get(columnIndex), DateTimeFormatter.ofPattern(format));
        return time;
    }

    LocalTime getTime(String columnLabel, String format){
        LocalTime time = LocalTime.parse(get(columnLabel), DateTimeFormatter.ofPattern(format));
        return time;
    }

    LocalDate getDate(int columnIndex, String format){
        LocalDate date = LocalDate.parse(get(columnIndex), DateTimeFormatter.ofPattern(format));
        return date;
    }

    LocalDate getDate(String columnLabel, String format){
        LocalDate date = LocalDate.parse(get(columnLabel), DateTimeFormatter.ofPattern(format));
        return date;
    }


}
