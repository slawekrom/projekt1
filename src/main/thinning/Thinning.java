package main.thinning;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Arrays;

public class Thinning {

    private Integer delete_tab[] = {3, 5, 7, 12, 13, 14, 15, 20,
            21, 22, 23, 28, 29, 30, 31, 48,
            52, 53, 54, 55, 56, 60, 61, 62,
            63, 65, 67, 69, 71, 77, 79, 80,
            81, 83, 84, 85, 86, 87, 88, 89,
            91, 92, 93, 94, 95, 97, 99, 101,
            103, 109, 111, 112, 113, 115, 116, 117,
            118, 119, 120, 121, 123, 124, 125, 126,
            127, 131, 133, 135, 141, 143, 149, 151,
            157, 159, 181, 183, 189, 191, 192, 193,
            195, 197, 199, 205, 207, 208, 209, 211,
            212, 213, 214, 215, 216, 217, 219, 220,
            221, 222, 223, 224, 225, 227, 229, 231,
            237, 239, 240, 241, 243, 244, 245, 246,
            247, 248, 249, 251, 252, 253, 254, 255};

    private Integer tab4[] = {3,6,12,24,48,96,192,129,
                7,14,28,56,112,224,193,131,
                15,30,60,120,240,225,195,135};

    private ArrayList<Integer> deleteList;
    private ArrayList<Integer> tab4List;
    private boolean changed;

    public Thinning() {
        deleteList = new ArrayList<>(Arrays.asList(delete_tab));
        tab4List =  new ArrayList<>(Arrays.asList(tab4));
    }

    //oznaczenie 1
    private BufferedImage thinning1(BufferedImage image) {

        for(int w = 0; w < image.getWidth(); w++) {
            for(int h = 0; h < image.getHeight(); h++) {
                Color c = new Color(image.getRGB(w, h));
                if (c.getRed() ==0)
                    image.setRGB(w,h,new Color(1,1,1).getRGB());  // 1
            }
        }
        return image;
    }
    //oznaczenie 2
    private BufferedImage thinning2(BufferedImage image) {

        changed = false;

        for(int w = 1; w < image.getWidth()-1; w++) {
            for(int h = 1; h < image.getHeight()-1; h++) {
                Color c1 = new Color(image.getRGB(w+1, h));
                Color c = new Color(image.getRGB(w, h));


                if (c.getRed()==1) {
                    if (c1.getRed() == 255)
                        image.setRGB(w, h, new Color(2, 2, 2).getRGB()); //2


                    Color c2 = new Color(image.getRGB(w - 1, h));
                    if (c2.getRed() == 255)
                        image.setRGB(w, h, new Color(2, 2, 2).getRGB());


                    Color c3 = new Color(image.getRGB(w, h + 1));
                    if (c3.getRed() == 255)
                        image.setRGB(w, h, new Color(2, 2, 2).getRGB());


                    Color c4 = new Color(image.getRGB(w, h - 1));
                    if (c4.getRed() == 255)
                        image.setRGB(w, h, new Color(2, 2, 2).getRGB());

                }
            }
        }
        return image;
    }

    //oznaczenie 3
    private BufferedImage thinning3(BufferedImage image) {

        for(int w = 1; w < image.getWidth()-1; w++) {
            for (int h = 1; h < image.getHeight() - 1; h++) {
                Color c = new Color(image.getRGB(w, h));

                //sprawdzenie 3
                if (c.getRed()==1) {
                    Color c5 = new Color(image.getRGB(w - 1, h - 1));
                    if (c5.getRed() == 255)
                        image.setRGB(w, h, new Color(3, 3, 3).getRGB()); //3

                    Color c6 = new Color(image.getRGB(w - 1, h + 1));
                    if (c6.getRed() == 255)
                        image.setRGB(w, h, new Color(3, 3, 3).getRGB()); //3

                    Color c7 = new Color(image.getRGB(w + 1, h - 1));
                    if (c7.getRed() == 255)
                        image.setRGB(w, h, new Color(3, 3, 3).getRGB()); //3

                    Color c8 = new Color(image.getRGB(w + 1, h + 1));
                    if (c8.getRed() == 255)
                        image.setRGB(w, h, new Color(3, 3, 3).getRGB()); //3

                }

            }
        }
        return image;
    }

    //oznaczenie i usuniecie 4
    private BufferedImage thinning4(BufferedImage image) {

        Integer[] values  = {1,2,3,4};
        ArrayList<Integer> valuesList = new ArrayList<>(Arrays.asList(values));
        int suma;
        for (int w = 1; w < image.getWidth() - 1; w++) {
            for (int h = 1; h < image.getHeight() - 1; h++) {

                suma = calculate(w,h,image,valuesList);

                if (tab4List.contains(suma)){
                    if (deleteList.contains(suma))
                        image.setRGB(w, h, new Color(255, 255, 255).getRGB());
                    else
                        image.setRGB(w, h, new Color(4, 4, 4).getRGB()); //4
                    changed=true;
                }

            }
        }
        return image;
    }

    //usuniecie 2 i 3
    private BufferedImage thinning5(BufferedImage image, int N) {

           Integer values[] = new Integer[]{N};
        ArrayList<Integer> valuesList = new ArrayList<>(Arrays.asList(values));
        int suma;
        changed = false;
        for (int w = 1; w < image.getWidth() - 1; w++) {
            for (int h = 1; h < image.getHeight() - 1; h++) {

                suma = calculate(w,h,image,valuesList);
                Color c = new Color(image.getRGB(w,h));
                if (c.getRed()==N){
                    if (deleteList.contains(suma))
                        image.setRGB(w, h, new Color(255, 255, 255).getRGB());
                    else
                        image.setRGB(w, h, new Color(1, 1, 1).getRGB());
                    changed = true;
                }

            }
        }
        return image;
    }


    private int calculate(int w, int h, BufferedImage image, ArrayList checkValues){
        int suma;
        Integer[] values  = {1,2,3,4}; //1,2,3,4
        ArrayList<Integer> valuesList = new ArrayList<>(Arrays.asList(values));
        suma = 0;
        Color c = new Color(image.getRGB(w, h));
        int value = c.getRed();
        if (checkValues.contains(value)){
            //1
            c = new Color(image.getRGB(w, h-1));
            value = c.getRed();
            if (valuesList.contains(value))
                suma+=1;
            //2
            c = new Color(image.getRGB(w+1, h-1));
            value = c.getRed();
            if (valuesList.contains(value))
                suma+=2;
            //3
            c = new Color(image.getRGB(w+1, h));
            value = c.getRed();
            if (valuesList.contains(value))
                suma+=4;
            //4
            c = new Color(image.getRGB(w+1, h+1));
            value = c.getRed();
            if (valuesList.contains(value))
                suma+=8;
            //5
            c = new Color(image.getRGB(w, h+1));
            value = c.getRed();
            if (valuesList.contains(value))
                suma+=16;
            //6
            c = new Color(image.getRGB(w-1, h+1));
            value = c.getRed();
            if (valuesList.contains(value))
                suma+=32;
            //7
            c = new Color(image.getRGB(w-1, h));
            value = c.getRed();
            if (valuesList.contains(value))
                suma+=64;
            //8
            c = new Color(image.getRGB(w-1, h-1));
            value = c.getRed();
            if (valuesList.contains(value))
                suma+=128;
        }
        return suma;
    }

    public BufferedImage thin(BufferedImage image){

        image=thinning1(image);
        do {

            image=thinning2(image);
            image=thinning3(image);
            image=thinning4(image);
            image=thinning5(image,2);
            image=thinning5(image,3);
        }while (changed);

        return image;
    }

    public BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

}
