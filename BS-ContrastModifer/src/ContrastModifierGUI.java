// NA POTRZEBY LEKCJI INFORMATYKI, AUTORZY: BARTOSZ S, JAKUB J, ARTUR M
// WSZELKIE PRAWA ZASTRZEŻONE!!
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ContrastModifierGUI extends JFrame {

    private JLabel imageLabel;
    private JSlider contrastSlider;
    private JButton openButton;
    private JButton saveButton;

    private BufferedImage originalImage;
    private BufferedImage modifiedImage;

    public ContrastModifierGUI() {
        setTitle("Contrast Modifier");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
//        setPreferredSize(new Dimension(800, 600));

        imageLabel = new JLabel();
        add(imageLabel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        contrastSlider = new JSlider(0, 200, 100);
        openButton = new JButton("Open Image");
        saveButton = new JButton("Save Image");

        controlPanel.add(contrastSlider);
        controlPanel.add(openButton);
        controlPanel.add(saveButton);

        add(controlPanel, BorderLayout.SOUTH);

        openButton.addActionListener(e -> openImage());
        saveButton.addActionListener(e -> saveImage());

        contrastSlider.addChangeListener(e -> modifyContrast());

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void openImage() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("BMP Images", "bmp");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                originalImage = ImageIO.read(selectedFile);
                updateImage(originalImage);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Nie można otworzyć pliku obrazu.", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveImage() {
        if (modifiedImage == null) {
            JOptionPane.showMessageDialog(this, "Brak zmodyfikowanego obrazu.", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("BMP Images", "bmp");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                ImageIO.write(modifiedImage, "bmp", selectedFile);
                JOptionPane.showMessageDialog(this, "Obraz został zapisany.", "Informacja", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Nie można zapisać obrazu.", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void modifyContrast() {
        if (originalImage == null) {
            return;
        }

        float contrast = contrastSlider.getValue() / 100.0f;

        modifiedImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < originalImage.getHeight(); y++) {
            for (int x = 0; x < originalImage.getWidth(); x++) {
                int rgb = originalImage.getRGB(x, y);

                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;
                // Przetwarzanie kontrastu
                float normalizedRed = red / 255.0f;
                float normalizedGreen = green / 255.0f;
                float normalizedBlue = blue / 255.0f;

                float adjustedRed = (normalizedRed - 0.5f) * contrast + 0.5f;
                float adjustedGreen = (normalizedGreen - 0.5f) * contrast + 0.5f;
                float adjustedBlue = (normalizedBlue - 0.5f) * contrast + 0.5f;

                int newRed = (int) (adjustedRed * 255.0f);
                int newGreen = (int) (adjustedGreen * 255.0f);
                int newBlue = (int) (adjustedBlue * 255.0f);

                int newRgb = (newRed << 16) | (newGreen << 8) | newBlue;
                modifiedImage.setRGB(x, y, newRgb);
            }
        }

        updateImage(modifiedImage);
    }

    private void updateImage(BufferedImage image) {
        ImageIcon icon = new ImageIcon(image);
        imageLabel.setIcon(icon);
        pack();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ContrastModifierGUI::new);
    }
}