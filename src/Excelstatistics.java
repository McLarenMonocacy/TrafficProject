import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Scanner;

public class Excelstatistics {
    public void writeDataToExcel( String excelTemplatePath, String excelGeneratedStatsResult) throws IOException {
        DataInput data = new DataInput("OUTPUTDATA.TXT");
        data.setLine();
        try (InputStream is = new FileInputStream(excelTemplatePath);
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            Scanner scanner = new Scanner(data.line);
            String line = scanner.nextLine();
//Writing data into workbook
            String[] token = line.split(",");
            int rowNum = 6;
            int colNum = 2;
            Cell cell = null;
            Row row = null;
            for (int i = 0; token[i] != null; i++) {

                try {
                    double result = Double.parseDouble(token[i]);
                    row = sheet.createRow(rowNum++);
                    cell.setCellValue(result);
                } catch (NumberFormatException e) {
                    cell = row.createCell(colNum++);
                    cell.setCellValue(token[i]);

                }


            }
            //Saving modified workbook
            try{
                OutputStream outputStream = new FileOutputStream("Excel Generated Stats Result.xlsx");
                workbook.write(outputStream);
            } catch (IOException e) {
                System.out.println("Excel file with graph NOT generated successfully at: " + "Excel Generated Stats Result");
                throw new RuntimeException(e);
            }

        }

    }

    private class DataInput {
        public DataInput(String dataFile) {
            this.dataFile = dataFile;
        }

        public String dataFile;
        public String line;

        public void setLine() {
            line = CSVConversion.fileToString(dataFile);
        }


        public String getLine() {
            return line;
        }

    }
}
