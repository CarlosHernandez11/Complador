/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lex;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.ImageIcon;
/**
 *
 * @author ÓscarAlberto
 */
public class splash extends javax.swing.JPanel {
    ImageIcon imagen; // imagen para mostrar 
    BorderLayout esquema;
    /**
     * Creates new form splash
     */
    public splash() {
        initComponents();
        estados.setForeground(Color.red);        
        imagen = new ImageIcon(getClass().getResource("/lex/MTI5MDE2MDg5NjE4MzIyNzA2.gif")); // se obtiene la imagen 
        this.setSize(imagen.getIconWidth(), imagen.getIconHeight()); // se establece el tamaño del panel 
        // mediante la dimencion de la imagen ancho y alto 
    }
    @Override
    public void paintComponent(Graphics g){ // metodo paintComponent para dibujar la imagen 
        super.paintComponent(g); // para que el panel se despligue sin errores 
        g.drawImage(imagen.getImage(), 0, 0, imagen.getIconWidth(), imagen.getIconHeight(), this); // se dibuja la imagen 
        this.setOpaque(false); // se establece el panel transparente para que se pueda ver la imagen 
    } 

    public void velocidadDeCarga() throws InterruptedException{ // motodo para controlar la barra 
        for(int i = 0; i<=100; i++){ // establece el recorrido de la barra de progreso 
            Thread.sleep(150); // pausas para dar el efecto de carga 
            progreso.setForeground(Color.GREEN); // color de la barra de progreso 
            progreso.setValue(i); // se asigna el valor de la barra 
            if (i == 40) {
               estados.setText("Cargando Librerias.."); 
            }
            if (i == 70) {
                estados.setText("Cargando Librerias...");
            }
        } 
    } 

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        estados = new javax.swing.JLabel();
        progreso = new javax.swing.JProgressBar();

        estados.setText("Cargando Librerias.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(estados)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(progreso, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(258, Short.MAX_VALUE)
                .addComponent(estados)
                .addGap(14, 14, 14)
                .addComponent(progreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel estados;
    private javax.swing.JProgressBar progreso;
    // End of variables declaration//GEN-END:variables
}
