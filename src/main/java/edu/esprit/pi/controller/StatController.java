package edu.esprit.pi.controller;

import edu.esprit.pi.entities.Category;
import edu.esprit.pi.entities.SubCategory;
import edu.esprit.pi.services.CategoryService;
import edu.esprit.pi.services.SubCategoryService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.List;
import java.util.Random;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.ss.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;
public class StatController {

    @FXML
    private PieChart pieChart;
    private CategoryService categoryService;

    public void initialize() {
        categoryService = new CategoryService();

        List<Category> categories = categoryService.getAllData();

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        double totalBudget = categories.stream().mapToDouble(Category::getBudgetLimit).sum();

        for (Category category : categories) {
            double budgetLimit = category.getBudgetLimit();

            double spentPercentage = (budgetLimit / totalBudget) * 100;

            System.out.println("nom : " + category.getName() + " budgetLimit " + budgetLimit + " totalBudget " + totalBudget);

            PieChart.Data data = new PieChart.Data(category.getName(), spentPercentage);
            pieChartData.add(data);
        }

        pieChart.setData(pieChartData);

        for (int i = 0; i < pieChartData.size(); i++) {
            PieChart.Data data = pieChartData.get(i);
            data.getNode().setStyle("-fx-pie-color: " + Color.web(getRandomColor(i)));
        }

        for (PieChart.Data data : pieChartData) {
            Node node = data.getNode();
            Label label = new Label(data.getName() + ": " + String.format("%.2f%%", data.getPieValue()));
            node.setOnMouseEntered(event -> Tooltip.install(node, new Tooltip(label.getText())));
        }
    }

    private String getRandomColor(int index) {
        Random random = new Random(index * 100);
        return String.format("#%02X%02X%02X", random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }


    @FXML
    public void exportToPDF() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save PDF");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

            Stage stage = (Stage) pieChart.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                PDDocument document = new PDDocument();
                PDPage page = new PDPage();
                document.addPage(page);

                PDPageContentStream contentStream = new PDPageContentStream(document, page);
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 14);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 700);

                // Title
                contentStream.showText("Pie Chart Exported to PDF");
                contentStream.newLine();
                contentStream.endText();

                WritableImage pieChartImage = pieChart.snapshot(new SnapshotParameters(), null);

                int targetWidth = 500;
                int targetHeight = 500;

                ImageView imageView = new ImageView(pieChartImage);
                imageView.setFitWidth(targetWidth);
                imageView.setFitHeight(targetHeight);

                WritableImage resizedImage = new WritableImage(targetWidth, targetHeight);
                imageView.snapshot(new SnapshotParameters(), resizedImage);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(SwingFXUtils.fromFXImage(resizedImage, null), "png", byteArrayOutputStream);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, readFully(byteArrayInputStream), "chart");

                float imageX = 50;
                float imageY = 300;
                float imageWidth = pdImage.getWidth();
                float imageHeight = pdImage.getHeight();

                contentStream.drawImage(pdImage, imageX, imageY, imageWidth, imageHeight);

                contentStream.close();

                document.save(file.getAbsolutePath());
                document.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] readFully(InputStream input) throws IOException {
        byte[] buffer = new byte[8192];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }
        return baos.toByteArray();
    }

}

//   @FXML
//    private void exportToExcel() {
//        Workbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("PieChart Data");
//
//        ObservableList<PieChart.Data> pieChartData = pieChart.getData();
//
//        int rowIndex = 0;
//        for (PieChart.Data data : pieChartData) {
//            Row row = sheet.createRow(rowIndex++);
//            row.createCell(0).setCellValue(data.getName());
//            row.createCell(1).setCellValue(data.getPieValue());
//        }
//
//        try (FileOutputStream fileOut = new FileOutputStream("PieChart_Data.xlsx")) {
//            workbook.write(fileOut);
//            System.out.println("Excel file has been created successfully.");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }