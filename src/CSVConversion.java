import java.io.*;

public class CSVConversion {

    // raw text to file
    public static void stringToFile(String fileName, String content) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(fileName))) {
            out.print(content);
        }
    }

    // reads whole file as one string
    public static String fileToString(String fileName) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }
}
