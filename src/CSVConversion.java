import java.io.*;

public class CSVConversion {

    // raw text to file
    public static void stringToFile(String fileName, String content) {
        try (PrintWriter out = new PrintWriter(new FileWriter(fileName))) {
            out.print(content);
        }
        catch (IOException e){
            System.out.println("Failed to write to file");
        }
    }

    // reads whole file as one string
    public static String fileToString(String fileName) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }
        catch (IOException e){
            System.out.println("Failed to read file");
        }
        return sb.toString();
    }
}
