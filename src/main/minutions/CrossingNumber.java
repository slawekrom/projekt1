package main.minutions;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CrossingNumber {

    public static List<Point> minutions = new ArrayList<>();
    public static List<Point> minutionsRozgalezienia = new ArrayList<>();

    public static void filterImage(BufferedImage img) {
        clearLists();
        for(int w = 1; w < img.getWidth() - 1; w++) {
            for(int h = 1; h < img.getHeight() - 1; h++) {
                Color c =new Color(img.getRGB(w,h));
                if(c.getRed()!=255){
                    checkPixel(img, w, h);
                }
            }
        }
    }

    public static void checkPixel(BufferedImage img, int w, int h) {
        int num=0;
        int []tab = new int[8];
        for(int i = -1; i < 2; i++) {
            for(int j = -1; j < 2; j++) {
                if(i==0&&j==0){

                }else{
                    Color color = new Color(img.getRGB(w + i, h + j));
                    if(color.getRed()==255){
                        tab[num]=0;
                    }else {
                        tab[num]=1;
                    }
                    num++;
                }
            }
        }
        int sum1 = Math.abs(tab[4]-tab[2]);
        int sum2 = Math.abs(tab[2]-tab[1]);
        int sum3 = Math.abs(tab[1]-tab[0]);
        int sum4 = Math.abs(tab[0]-tab[3]);
        int sum5 = Math.abs(tab[3]-tab[5]);
        int sum6 = Math.abs(tab[5]-tab[6]);
        int sum7 = Math.abs(tab[6]-tab[7]);
        int sum8 = Math.abs(tab[7]-tab[4]);
        int sum=(sum1+sum2+sum3+sum4+sum5+sum6+sum7+sum8)/2;
        if(sum!=2){
            minutions.add(new Point(w,h));
            if(sum>2){
                minutionsRozgalezienia.add(new Point(w,h));
            }
        }
    }

    public static void filterMinutions(int width, int height){
        List<Point> minutionsToDelete = new ArrayList<>();
        minutions.forEach(m->{
            if(m.getX()> 4 && m.getX()< width-4 && m.getY()> 4 && m.getY()< height-4){
                for(int i=-5;i<=5;i++){
                    for(int j=-5;j<=5;j++){
                        if(!(i==0||j==0)) {
                            Point point = new Point((int) (m.getX() + i), (int) (m.getY() + j));
                            if (minutions.contains(point)) {
                                minutionsToDelete.add(point);
                            }
                        }
                    }
                }
            }
        });
        List<Point> minutions2 = minutionsToDelete.stream().distinct().collect(Collectors.toList());
        for(int i =0; i<minutions2.size();i=i+2){
            minutions.remove(minutions2.get(i));
        }
    }
    private static void clearLists(){
        minutions.clear();
        minutionsRozgalezienia.clear();
    }
}

