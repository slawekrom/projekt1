package main.viewer;

import main.minutions.CrossingNumber;
import main.shared.ImageSharedOperations;
import main.thinning.Thinning;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class Viewer extends JFrame {

    private JMenuBar menuBar = new JMenuBar();
    private JMenu files = new JMenu("File");
    private JMenu thinning_label = new JMenu("Thinning");
    private JMenu binarization = new JMenu("Binarization");
    private JMenu minutions = new JMenu("Detect minutions");
    private JMenuItem loadImage = new JMenuItem("Load image");
    private JMenuItem saveImage = new JMenuItem("Save image");
    private JMenuItem bin = new JMenuItem("Binarization");
    private JMenuItem thin = new JMenuItem("Thinn");
    private JMenuItem crossingNumber = new JMenuItem("Crossing Number algorithm");
    private JLabel imageLabel = new JLabel();
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
        thinning_label.add(thin);

        menuBar.add(binarization);
        binarization.add(bin);

        menuBar.add(minutions);
        minutions.add(crossingNumber);

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
                    return fileName.endsWith(".jpg") || fileName.endsWith(".png")
                            || fileName.endsWith(".tiff") || fileName.endsWith(".jpeg");
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


        thin.addActionListener(e -> {
            BufferedImage image = ImageSharedOperations.convertIconToImage((ImageIcon) imageLabel.getIcon());
            imageLabel.setIcon(new ImageIcon(thinning.thin(image)));
        });

        crossingNumber.addActionListener(e -> {
            BufferedImage image = (ImageSharedOperations.convertIconToImage((ImageIcon) imageLabel.getIcon()));
            CrossingNumber.filterImage(image);
            CrossingNumber.minutions.forEach(m->{
                System.out.println("w: "+m.getX()+", h: "+m.getY());
                drawCircle(image, (int)m.getX(), (int)m.getY());

            });
            imageLabel.setIcon(new ImageIcon(image));
        });

    }

    void drawCircle(BufferedImage image, int w, int h){
        if(w> 2 && w< image.getWidth()-2 && h > 2 && h< image.getHeight()-2){
            int red = Color.RED.getRGB();
            image.setRGB(w-1,h-2,red);
            image.setRGB(w,h-2,red);
            image.setRGB(w+1,h-2,red);
            image.setRGB(w-2,h-1,red);
            image.setRGB(w+2,h-1,red);
            image.setRGB(w-2,h,red);
            image.setRGB(w+2,h,red);
            image.setRGB(w-2,h+1,red);
            image.setRGB(w+2,h+1,red);
            image.setRGB(w-1,h+2,red);
            image.setRGB(w,h+2,red);
            image.setRGB(w+1,h+2,red);
        }
    }

}
