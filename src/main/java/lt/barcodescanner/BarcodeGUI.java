import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Graphical User Interface (GUI) for a Barcode Scanner application using Java Swing.
 * This class provides a simple GUI to upload an Excel file containing barcodes and prices,
 * display the data, and delete entries.
 *
 * @author Ignas Andrijauskas
 * @version 1.0
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * public static void main(String[] args) {
 *     SwingUtilities.invokeLater(new Runnable() {
 *         @Override
 *         public void run() {
 *             new BarcodeGUI().setVisible(true);
 *         }
 *     });
 * }
 * }
 * </pre>
 *
 * @see javax.swing.JFrame
 * @see javax.swing.JTextArea
 * @see javax.swing.JButton
 * @see javax.swing.JFileChooser
 * @see javax.swing.JOptionPane
 * @see javax.swing.JPanel
 *
 * @version 1.0
 * @since 1.0
 */
public class BarcodeGUI extends JFrame {
    /**
     * Text area for displaying barcode and price information.
     */
    private JTextArea textArea;

    /**
     * Button for selecting an Excel file with barcode data.
     */
    private JButton uploadButton;

    /**
     * Button for printing the list of imported barcodes and prices.
     */
    private JButton printButton;

    /**
     * Button for deleting an entry from the barcode map.
     */
    private JButton deleteButton;

    /**
     * Map containing barcodes and their corresponding prices.
     */
    private Map<String, Double> barcodeMap;

    /**
     * Constructs the BarcodeGUI and initializes UI components.
     */
    public BarcodeGUI() {
        setTitle("Barcode Scanner");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        uploadButton = new JButton("Select Excel File");
        printButton = new JButton("Print List");
        deleteButton = new JButton("Delete Entry");

        barcodeMap = new HashMap<>();

        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                    barcodeMap = BarcodeUtils.readBarcodesFromExcel(selectedFile.getAbsolutePath());
                    System.out.println("Barcode map size: " + barcodeMap.size());
                }
            }
        });

        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (barcodeMap.isEmpty()) {
                    textArea.setText("No data available.\n");
                } else {
                    textArea.setText("Here are all the products imported:\n\n");
                    for (Map.Entry<String, Double> entry : barcodeMap.entrySet()) {
                        textArea.append(entry.getKey() + " - " + entry.getValue() + "\n");
                    }
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyToDelete = JOptionPane.showInputDialog("Enter the barcode to delete:");
                if (keyToDelete != null && barcodeMap.containsKey(keyToDelete)) {
                    barcodeMap.remove(keyToDelete);
                    JOptionPane.showMessageDialog(null, "Entry deleted.");
                } else {
                    JOptionPane.showMessageDialog(null, "Barcode not found.");
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(uploadButton);
        buttonPanel.add(printButton);
        buttonPanel.add(deleteButton);

        add(new JScrollPane(textArea), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Main method to start the BarcodeGUI application.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BarcodeGUI().setVisible(true);
            }
        });
    }
}
