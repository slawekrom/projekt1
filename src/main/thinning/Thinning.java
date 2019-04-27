package main.thinning;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class Thinning {

    public BufferedImage thinning1(BufferedImage img) {

        BufferedImage image = deepCopy(img);

        for(int w = 0; w < image.getWidth(); w++) {
            for(int h = 0; h < image.getHeight(); h++) {
                Color c = new Color(image.getRGB(w, h));
                if (c.getRed() ==0)
                    image.setRGB(w,h,new Color(100,100,1).getRGB());
            }
        }
        return image;
    }
    public BufferedImage thinning2(BufferedImage img) {

        BufferedImage image = deepCopy(img);

        int licznik;
        for(int w = 1; w < image.getWidth()-1; w++) {
            for(int h = 1; h < image.getHeight()-1; h++) {
                licznik = 0;
                Color c1 = new Color(image.getRGB(w+1, h));
                Color c = new Color(image.getRGB(w, h));


                if (c.getRed()==100) {
                    if (c1.getRed() == 255) {
                        image.setRGB(w, h, new Color(2, 2, 2).getRGB());
                        licznik++;
                    }

                    Color c2 = new Color(image.getRGB(w - 1, h));
                    if (c2.getRed() == 255) {
                        image.setRGB(w, h, new Color(2, 2, 2).getRGB());
                        licznik++;
                    }

                    Color c3 = new Color(image.getRGB(w, h + 1));
                    if (c3.getRed() == 255) {
                        image.setRGB(w, h, new Color(2, 2, 2).getRGB());
                        licznik++;
                    }

                    Color c4 = new Color(image.getRGB(w, h - 1));
                    if (c4.getRed() == 255) {
                        image.setRGB(w, h, new Color(2, 2, 2).getRGB());
                        licznik++;
                    }

                }

               /* if (licznik ==4)
                    image.setRGB(w, h, new Color(255, 255, 255).getRGB());*/
            }
        }
        return image;
    }

    public BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
}
