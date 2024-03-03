package Entities;
// CSVExporter.java
import com.opencsv.CSVWriter;
import javafx.collections.ObservableList;

import java.io.FileWriter;
import java.io.IOException;

public class CSVExporter {

    public static void exportToCSV(ObservableList<TransactionTableRow> transactions, String filePath) {
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(filePath))) {

            // Write header
            String[] header = {"Date","Type","Description","Amount","From Account","To Account","Category","Payee"};
            csvWriter.writeNext(header);

            // Write data
            for (TransactionTableRow transaction : transactions) {
                String[] rowData = {
                        transaction.getDate().toString(),
                        String.valueOf(transaction.getType()),
                        transaction.getDescription(),
                        String.valueOf(transaction.getAmount()),
                        transaction.getFromAccountName(),
                        transaction.getToAccountName(),
                        transaction.getCategoryName(),
                        transaction.getPayeeName()
                        // Add more fields as needed
                };
                csvWriter.writeNext(rowData);
            }

            System.out.println("CSV Export Successful!");

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error exporting CSV file.");
        }
    }
}

