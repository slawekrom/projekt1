package main.viewer;

import main.shared.ImageSharedOperations;
import main.thinning.Thinning;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class Viewer extends JFrame {

    private final JMenuBar menuBar = new JMenuBar();
    private final JMenu files = new JMenu("File");
    private final JMenu thinning_label = new JMenu("Thinning");
    private final JMenu binarization = new JMenu("Binarization");
    private final JMenuItem loadImage = new JMenuItem("Load image");
    private final JMenuItem saveImage = new JMenuItem("Save image");
    private final JMenuItem bin = new JMenuItem("Binarization");
    private final JMenuItem thin1 = new JMenuItem("Thinning1");
    private final JMenuItem thin2 = new JMenuItem("Thinning2");
    private final JLabel imageLabel = new JLabel();
    private Thinning thinning;

    public Viewer() {
        this.setLayout(new BorderLayout());
        this.setTitle("Podstawy Biometrii");
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.menuBar.add(this.files);
        this.files.add(this.loadImage);
        this.files.add(this.saveImage);
        menuBar.add(thinning_label);
        thinning_label.add(thin1);
        thinning_label.add(thin2);

        menuBar.add(binarization);
        binarization.add(bin);

        this.add(this.menuBar, BorderLayout.NORTH);
        this.add(this.imageLabel, BorderLayout.CENTER);
        this.imageLabel.setHorizontalAlignment(JLabel.CENTER);
        this.imageLabel.setVerticalAlignment(JLabel.CENTER);

        this.setVisible(true);
        thinning = new Thinning();

        this.loadImage.addActionListener((ActionEvent e) -> {
            JFileChooser imageOpener = new JFileChooser();
            imageOpener.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    String fileName = f.getName().toLowerCase();
                    if(fileName.endsWith(".jpg") || fileName.endsWith(".png")
                            || fileName.endsWith(".tiff") || fileName.endsWith(".jpeg")) {
                        return true;
                    } else return false;
                }

                @Override
                public String getDescription() {
                    return "Image files (.jpg, .png, .tiff)";
                }
            });

            int returnValue = imageOpener.showDialog(null, "Select image");
            if(returnValue == JFileChooser.APPROVE_OPTION) {
                BufferedImage img = ImageSharedOperations.loadImage(imageOpener.getSelectedFile().getPath());
                this.imageLabel.setIcon(new ImageIcon(img));
            }
        });

        this.saveImage.addActionListener((ActionEvent e) -> {
            String path = "./image.jpg";
            BufferedImage img = ImageSharedOperations.convertIconToImage((ImageIcon) this.imageLabel.getIcon());
            ImageSharedOperations.saveImage(img, path);
        });

        bin.addActionListener(e -> {
            BufferedImage image = ImageSharedOperations.convertIconToImage((ImageIcon) imageLabel.getIcon());
            for(int w = 0; w < image.getWidth(); w++) {
                for(int h = 0; h < image.getHeight(); h++) {
                    Color c = new Color(image.getRGB(w, h));
                    image.setRGB(w, h,
                            c.getRed() >= 100 ? Color.WHITE.getRGB() : Color.BLACK.getRGB());
                }
            }
            imageLabel.setIcon(new ImageIcon(image));
        });

        thin1.addActionListener(e -> {
            BufferedImage image = ImageSharedOperations.convertIconToImage((ImageIcon) imageLabel.getIcon());
            imageLabel.setIcon(new ImageIcon(thinning.thinning1(image)));
        });

        thin2.addActionListener(e -> {
            BufferedImage image = ImageSharedOperations.convertIconToImage((ImageIcon) imageLabel.getIcon());
            imageLabel.setIcon(new ImageIcon(thinning.thinning2(image)));
        });

    }

}
