import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class to read barcodes and prices from an Excel file.
 * This class uses Apache POI to parse the Excel file.
 *
 * @author Ignas Andrijauskas
 * @version 1.0
 *
 * <p>It provides a method to read barcodes and prices from a specified Excel file
 * and returns them as a map.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * Map<String, Double> barcodes = BarcodeUtils.readBarcodesFromExcel("path/to/excel/file.xlsx");
 * }
 * </pre>
 *
 * @see <a href="https://poi.apache.org/">Apache POI</a>
 * @see java.util.Map
 * @see java.util.HashMap
 * @see org.apache.poi.ss.usermodel.Workbook
 * @see org.apache.poi.ss.usermodel.Sheet
 * @see org.apache.poi.ss.usermodel.Row
 * @see org.apache.poi.ss.usermodel.Cell
 *
 * @version 1.0
 * @since 1.0
 */
public class BarcodeUtils {

    /**
     * Reads barcodes and prices from the specified Excel file.
     *
     * @param filePath Path to the Excel file.
     * @return A {@link Map} containing barcodes as keys and prices as values.
     */
    public static Map<String, Double> readBarcodesFromExcel(String filePath) {
        Map<String, Double> barcodes = new HashMap<>();
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                Cell cell1 = row.getCell(0); // Barcode column
                Cell cell2 = row.getCell(1); // Price column
                if (cell1 != null && cell2 != null && row.getRowNum() > 0) { // Skip header row
                    String barcode = cell1.getStringCellValue().trim(); // Trim to remove extra spaces
                    double price = parsePrice(cell2);
                    barcodes.put(barcode, price);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return barcodes;
    }

    /**
     * Parses the price from the given {@link Cell}.
     *
     * @param cell The {@link Cell} containing the price.
     * @return The parsed price as a {@code double}.
     */
    private static double parsePrice(Cell cell) {
        double price = 0.0;
        if (cell.getCellType() == CellType.NUMERIC) {
            price = cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            String priceStr = cell.getStringCellValue().trim();
            try {
                if (!priceStr.isEmpty()) {
                    price = Double.parseDouble(priceStr.replace(",", ".")); // Handle commas as decimals
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid price format: " + priceStr);
            }
        }
        return price;
    }
}
