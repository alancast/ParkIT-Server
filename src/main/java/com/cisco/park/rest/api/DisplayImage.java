package com.cisco.park.rest.api;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



public class DisplayImage {


public static long threshold_empty = 1000; 
    public static void main(String avg[]) throws IOException
    {
    /* //DisplayImage abc=new DisplayImage();
   
    //BufferedImage image = ((BufferedImage) abc).getAsBufferedImage();
        // BufferedImage croppedImage = originalImage.getSubimage(x, y, width, height);
    // RescaleOp op = new RescaleOp(brightenFactor, 0, null);
    // img = op.filter(img, img);
        BufferedImage imgCrop= cropImage(img_original); 
        DisplayImage abc = new DisplayImage(imgCrop);
        long avgColor = getAverageColor(cannyDetector(imgCrop));
     DisplayImage abc = new DisplayImage(cannyDetector(imgCrop)); 
        System.out.println(avgColor);
        spotCheck(avgColor);*/
    BufferedImage img_original =ImageIO.read(new File("/Users/sachija3/Desktop/car_photos/photo10.jpg"));
    processImage(img_original);    
 
    }
    
    
    
    
    public static boolean processImage(BufferedImage img_original) throws IOException
    {
    
    //take image from folder
       
        
        // crop image
        BufferedImage imgCrop= cropImage(img_original); 
       
        // display image
        //DisplayImage abc = new DisplayImage(cannyDetector(imgCrop));
        
        // get the average amount of white color from an image of edges 
        long avgColor = getAverageColor(cannyDetector(imgCrop));
        System.out.println(avgColor);
        
        // determine if the color is above the neccessary threshhold. 
        return spotCheck(avgColor);
    }
    
    public static BufferedImage cropImage(BufferedImage img) throws IOException
    {
   
    int w = img.getWidth();  //width
    int h = img.getHeight(); //height
   
    int x = w/4; 
    int y = h/4; 
   
    int width = w/2; 
    int height = 3*h/4; 
        //BufferedImage img_original=ImageIO.read(new File("/Users/sachija3/Desktop/full.jpeg"));
    BufferedImage imgCrop = img.getSubimage(x, y, width, height);
    return imgCrop; 
        
    }
    
    
    /*public static long getAverageColor(BufferedImage img)
    {
    long redBucket = 0;
    long greenBucket = 0;
    long blueBucket = 0;
    long pixelCount = 0;

    for (int y = 0; y < img.getHeight(); y++)
    {
       for (int x = 0; x < img.getWidth(); x++)
       {
                Color c = new Color(img.getRGB(x,y));

           pixelCount++;
           redBucket += c.getRed();
           greenBucket += c.getGreen();
           blueBucket += c.getBlue();
       }
    }
    //long averageColor = (redBucket/pixelCount + greenBucket/pixelCount + blueBucket/pixelCount);
    long averageColor = (redBucket + greenBucket + blueBucket)/pixelCount; 
    return averageColor; 
    }*/
    
   public static boolean getWhiteColor (Color c)
   {
  if ((c.getRed() == 255) && (c.getBlue() ==255) && (c.getGreen() == 255))
  {
  return true; 
  }
  return false; 
   } 
   public static long getAverageColor(BufferedImage img)
    {
  
long pixelCount = 0; 
    for (int y = 0; y < img.getHeight(); y++)
    {
    for (int x = 0; x < img.getWidth(); x++)
    {
    Color c = new Color(img.getRGB(x,y));
    if (getWhiteColor(c) == true)
    {
    pixelCount++; 
    }
    }
    }
    //System.out.println(pixelCount);
        return pixelCount;
   
    }
   
    public static boolean spotCheck(long averageColor)
    {
    long epsilon_value = (long) 1000;
long upperBound = threshold_empty + epsilon_value; 
    long lowerBound = threshold_empty - epsilon_value; 
    if ((averageColor >= lowerBound) && (averageColor <= upperBound)) 
    {
    System.out.println("Car has not been detected");
    return false; 
    }
    else
    {
    System.out.println("Car has been detected");
    return true; 
    }
    }
    
    
    public DisplayImage(BufferedImage img) throws IOException
    {
        ImageIcon icon=new ImageIcon(img);
        JFrame frame=new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(1000,1000);
        JLabel lbl=new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public static BufferedImage cannyDetector(BufferedImage img)
    {
    CannyEdgeDetector detector = new CannyEdgeDetector();
    detector.setHighThreshold(8f);
    detector.setSourceImage(img);
    detector.process();
    img = detector.getEdgesImage();
    return img; 
    }
}
