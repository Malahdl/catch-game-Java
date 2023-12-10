package Project;

import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.io.*;

 public class Game extends JFrame
{
    private final int FRAME_WIDTH = 700;  //frame width
    private final int FRAME_HEIGHT = 700;  //frame height
    
    public Game() //frame constructor
    {
        //frame settings
        this.setLayout(new BorderLayout());  //set frame layout
        this.setSize(FRAME_WIDTH,FRAME_HEIGHT);  //set frame size
        this.setTitle("Catch Game");  //set frame title
        this.setLocationRelativeTo(null);  //set the location for frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //let the frame close when the (x) button is clicked
        
        
        //adding panel to frame
        DrawingPanel p = new DrawingPanel();
        add(p, BorderLayout.CENTER);
        
        pack();
        this.setVisible(true);  //make the frame visible on screen
    }

    public static void main(String[]args)
    {
        new Game();  //creating the frame
    }
}

class DrawingPanel extends JPanel implements Runnable
{
    private final int PANEL_WIDTH = 700;  //panel width
    private final int PANEL_HEIGHT = 700;  //panel height
    
    private static int PW = 40;  //polygon width
    private static int PH = 80;  //polygon height
    
    //polygon coordinates
    private static int x0 = 450;
    private static int y0 = 600;
    private static int x1 = x0 + PW;
    private static int y1 = y0;
    
    //top point of polygon
    private static int x2 = x0 + (PW / 2);
    private static int y2 = y0 - PH;
    
    private static int tx = 10; //transition quantity for x-axis
    private static int ty = tx; //transition quantity for y-axis
    
    private static int []xPoly = {x0,x1,x2};  //array of x coordinates for polygon
    private static int []yPoly = {y0,y1,y2};  //array of y coordinates for polygon
    
    private static int arraySize = 100; //size of the array
    private static int []xArr = new int[arraySize];  //array to store x points
    private static int []yArr = new int[arraySize];  //array to store y points
    
    private static int counter = 0;  //counter for points
    
    private static int Rx1 = 30;  //x1 coordinate for rectangle
    private static int Ry1 = 60;  //y1 coordinate for rectangle
    private static int Rx2 = 200; //x2 coordinate for rectangle
    private static int Ry2 = 0;   //y2 coordinate for rectangle
    private static int Rx3 = 30;  //x3 coordinate for rectangle
    private static int Ry3 = 600; //y3 coordinate for rectangle
    private static int Rx4 = 600; //x4 coordinate for rectangle
    private static int Ry4 = 30;  //y4 coordinate for rectangle
    private static int Rx5 = 0;   //x5 coordinate for rectangle
    private static int Ry5 = 0;   //y5 coordinate for rectangle
    
    private static int []xRec = {Rx1,Rx2,Rx3,Rx4,Rx5};  //array of x coordinates for all the rectangles
    private static int []yRec = {Ry1,Ry2,Ry3,Ry4 ,Ry5};  //array of y coordinates for all the rectangles
    
    private static int NO_OF_RECTS = 5;  //number of rectangles
    Rectangle []recArray = new Rectangle[NO_OF_RECTS];  //array to create the rectangles
    
    private static int RW = 50;  //rectangle width
    private static int RH = RW;  //rectangle height
    
    private static int []left   = new int[xRec.length];  //left side of each rectangle
    private static int []right  = new int[xRec.length];  //right side of each rectangle
    private static int []top    = new int[yRec.length];  //top side of each rectangle
    private static int []bottom = new int[yRec.length];  //bottom side of each rectangle
    
    Thread mythread;  //creating a variable of Thread class
    
    private static int tx1 = 5;  //transition quantity for x1
    private static int ty1 = 1;  //transition quantity for y1
    private static int tx2 = 4;  //transition quantity for x2
    private static int ty2 = 2;  //transition quantity for y2
    private static int tx3 = 3;  //transition quantity for x3
    private static int ty3 = 3;  //transition quantity for y3
    private static int tx4 = 2;  //transition quantity for x4
    private static int ty4 = 4;  //transition quantity for y4
    private static int tx5 = 1;  //transition quantity for x5
    private static int ty5 = 5;  //transition quantity for y5
    
    private static int []xMoveBy = {tx1,tx2,tx3,tx4,tx5};  //an array to store the amount of moving along x-axis
    private static int []yMoveBy = {ty1,ty2,ty3,ty4,ty5};  //an array to store the amount of moving along y-axis
    
    
    public DrawingPanel()  //panel constructor
    {
        //panel settings
        setLayout(new BorderLayout());  //set panel layout
        setPreferredSize(new Dimension(PANEL_WIDTH,PANEL_HEIGHT));  //set panel size
        setBackground(Color.BLACK);  //set panel background color
        setFocusable(true);  //set the capability of moving the objects
        
        //adding the key listner class to panel
        KeyPressListener LK = new KeyPressListener();
        addKeyListener(LK);
        
        //declring the Thread variable
        mythread = new Thread(this);
        mythread.start();
    }
    
        public void paintComponent(Graphics g)  //an override method to draw objects on panel
    {
        super.paintComponent(g);  //extinding all the components 
        Graphics2D g2 = (Graphics2D)g;  //casting graphics to draw 2D objects only
        
        g2.setStroke(new BasicStroke(3.0f));  //setting the size of points
        
        //drawing the polygon
        Polygon p = new Polygon(xPoly,yPoly,xPoly.length);
        Shape poly = p;
        g2.setColor(Color.BLUE);
        g2.fill(poly);
        
        //drawing the top points of the polygon
        g2.setColor(Color.WHITE);
        for(int i = 0;i<xArr.length;i++)
        {
            g2.drawLine(xArr[i], yArr[i], xArr[i], yArr[i]);
        }
        
        //drawing the rectangles
        g2.setColor(Color.GREEN);
        for(int i = 0;i<recArray.length;i++)
        {
           recArray[i] = new Rectangle(xRec[i],yRec[i],RW,RH);  //create each rectangle
           g2.fill(recArray[i]);  //fill each rectangle
           
           left[i]   = xRec[i];  
           right[i]  = xRec[i] + RW;
           top[i]    = yRec[i];
           bottom[i] = yRec[i] + RH;
        }
        
        g2.setColor(Color.yellow);
        g2.fill(recArray[0]);
        
        //collision detection between point and rectangle
        g2.setColor(Color.red);
        int counter = 0;
        for(int j = 0;j<xRec.length;j++)
    {
        if((xPoly[2]>left[j]) && 
           (xPoly[2]<right[j])&& 
           (yPoly[2]>top[j])  && 
           (yPoly[2]<bottom[j]))
        {
                //fill the rectangle with color red
                g2.fill(recArray[j]);
                
                //stop the rectangle movement
                xMoveBy[j] = 0;
                yMoveBy[j] = 0;
        }
        
        if(xMoveBy[j] == 0 
        && yMoveBy[j] == 0)
        {
            g2.fill(recArray[j]);
        }
    }
        
       
        
    }
    
    @Override
    public void run()  
    {
        //the animation loop
        while (true) 
        {
        	try
                {
        		Thread.sleep(10);
        	}
        	catch(InterruptedException e){}
                
        	for(int i = 0;i<xRec.length;i++)
                {
                    //moving the coordinates
                    xRec[i] = xRec[i] + xMoveBy[i];
                    yRec[i] = yRec[i] + yMoveBy[i];
                    
                    
                   //bounsing condition
                   if(xRec[i]<0 || xRec[i]>PANEL_WIDTH-RW) 
                         
                   {
                           xMoveBy[i] = -xMoveBy[i];
                   }
                   
                   if(yRec[i]<0 || yRec[i]>PANEL_HEIGHT-RH)
                   {
                       yMoveBy[i] = -yMoveBy[i];
                   }
                       for (int j = 1; j < xRec.length; j++) {
                        if (xRec[0] > left[j] || xRec[0] < right[j]) {
                            xMoveBy[0] = -xMoveBy[0];
                        }

                        if (yRec[0] < bottom[j] || yRec[0] > top[j]) {
                            yMoveBy[0] = -yMoveBy[0];
                        }
                    }
                }
                
                
                
        	                     
        	repaint();  //redraw the rectangle after each move
        }           
    }
    


    
    // inner class to handle keyboard events
    private class KeyPressListener extends KeyAdapter
    {
        
        public void keyPressed(KeyEvent e)
        {
            int keyCode = e.getKeyCode();
            
            switch(keyCode)
            {
                case KeyEvent.VK_LEFT:
                    moveLeft();
                    break;
                    
                case KeyEvent.VK_RIGHT:
                    moveRight();
                    break;
                    
                case KeyEvent.VK_UP:
                    moveUp();
                    break;
                    
                case KeyEvent.VK_DOWN:
                    moveDown();
                    break;    
            }
            
            //a condition to draw the tracing points while moving the polygon
            if(keyCode == KeyEvent.VK_UP
              ||keyCode == KeyEvent.VK_DOWN
              ||keyCode == KeyEvent.VK_RIGHT
              ||keyCode == KeyEvent.VK_LEFT)
            {
                xArr[counter] = xPoly[2];
                yArr[counter] = yPoly[2];
                counter++;
                
                if(counter == arraySize)
                {
                    counter = 0;
                }
            }
            
            repaint();  //redraw the polygon after each move
        }
    }
    
    public void moveUp()
    {
        for(int i = 0;i<yPoly.length;i++)
        {
            yPoly[i] = yPoly[i] - ty;
        }
    }
    
    public void moveDown()
    {
        for(int i = 0;i<yPoly.length;i++)
        {
            yPoly[i] = yPoly[i] + ty;
        }
    }
    
    public void moveRight()
    {
        for(int i = 0;i<xPoly.length;i++)
        {
            xPoly[i] = xPoly[i] + tx;
        }
    }
    
    public void moveLeft()
    {
        for(int i = 0;i<xPoly.length;i++)
        {
            xPoly[i] = xPoly[i] - tx;
        }
    }
    
    }