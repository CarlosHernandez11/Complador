/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lex;
/**
 *
 * @author ÓscarAlberto
 */

 
import java.awt.*;
import javax.swing.*;
import static java.awt.GraphicsDevice.WindowTranslucency.*;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
 
public class JFrameCanvas extends JFrame {
    Thread hilo;
    int n, puntox1,puntoy1,puntox2,puntoy2,a,b;
    double valor;
    boolean mas;
    int R;
    int G;
    int B;
    
    public JFrameCanvas() {
        this.R = (int)(Math.random()*255+1);
        this.G = (int)(Math.random()*255+1);
        this.B = (int)(Math.random()*255+1);
        setUndecorated(true);
        setBackground(new Color(0,0,0,0));
        setSize(new Dimension(300,200));
        setLocationRelativeTo(null);
        n=0;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        a=0;
        b=160;
        mas=true;
        puntox1=0;
        puntoy1=0;
        puntox2=0;
        puntoy2=getHeight();
        valor=0;
        
        hilo=new Thread(){
            public void run(){
                while(true){
                    try {
                        
                        sleep(10);
                        if(mas)a++;
                        else a--;
                        b=130-a;
                        
                        if((int)(Math.random()*2+1)==2){
                                R+=(int)(Math.random()*5+1);
                                if(R>255)R=510-R;
                                G+=(int)(Math.random()*5+1);
                                if(G>255)G=510-G;
                                B+=(int)(Math.random()*5+1);
                                if(B>255)B=510-B;
                            }
                            else{
                                R-=(int)(Math.random()*5+1);
                                if(R<0)R=-R;
                                G-=(int)(Math.random()*5+1);
                                if(G<0)G=-G;
                                B-=(int)(Math.random()*5+1);
                                if(B<0)B=-B;
                            }
                        
                        if(a==0){mas=true;
                        }
                        if(a==130){
                                mas=false;
                        }
                        repaint();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(JFrameCanvas.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        hilo.start();
        
        JPanel panel = new JPanel() {
            @Override
            
            protected void paintComponent(Graphics g) {
                if (g instanceof Graphics2D) {
                    
 
                    Paint p =new GradientPaint(0.0f, 0.0f, new Color(R, G, B, 150),
                            0.0f, getHeight()/2, new Color(R, G, B, 155), true);
                    Paint p2 =new GradientPaint(0.0f, 0.0f, new Color(R, G, B , 150),
                            getWidth()/2, 0.0f, new Color(R, G, B, 155), true);
                    Graphics2D g2d = (Graphics2D)g;
                    g2d.setPaint(p);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    g2d.setPaint(p2);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        
        
        setContentPane(panel);
        setLayout(new GridBagLayout());
    }
 
    public static void main(String[] args) {
        // Determine what the GraphicsDevice can support.
        GraphicsEnvironment ge = 
            GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        boolean isPerPixelTranslucencySupported = 
            gd.isWindowTranslucencySupported(PERPIXEL_TRANSLUCENT);
 
        //If translucent windows aren't supported, exit.
        if (!isPerPixelTranslucencySupported) {
            System.out.println(
                "Per-pixel translucency is not supported");
                System.exit(0);
        }
 
        JFrame.setDefaultLookAndFeelDecorated(true);
 
        // Create the GUI on the event-dispatching thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrameCanvas gtw = new JFrameCanvas();
 
                // Display the window.
                gtw.setVisible(true);
            }
        });
    }
}