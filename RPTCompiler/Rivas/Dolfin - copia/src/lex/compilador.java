/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Oscar
 */
package lex;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultListModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Carlos
 */ 
public final class compilador extends JFrameCanvas {
   
     
    private int posicionX;
    private int posicionY; 
    
    
    static ArrayList<identificador> listaDinamica;
    static ArrayList<String> listaEstatica;
    static ArrayList<String> listaErrores;
    static ArrayList<String> listaLexemas;
    static ArrayList<String> lista3 = new ArrayList<>();
    static identificador A[]= new identificador[100];
    static DefaultListModel modelo = new DefaultListModel();
    public static String c3d = "";
    static int indice = 0;
    static int t=0;
    static String DireccionPath = "";

    private static void llenarTablaDinamica(String identificador, String valor) {
        modelo.addElement("Identificador: " + identificador + " valor: " + valor);

       
    }
    DefaultStyledDocument doc;
    int R = 0;
    int E = 0;
    int cond = 0;
    String e = "";
    /**
     * Creates new form interfaz
     */
    List<identificador> tokenslist;

    private int findLastNonWordChar(String text, int index) {
        while (--index >= 0) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
        }
        return index;
    }

    public void guardarArchivo(){
    if(DireccionPath.equals("")){
        if(area1.getText().equals("")){
            javax.swing.JOptionPane.showMessageDialog(this, "No hay codigo que guardar","Aviso",JOptionPane.ERROR_MESSAGE);
        }
        else{
            try{
                JFileChooser guardarA = new JFileChooser();
                guardarA.showSaveDialog(this);
                File guardar = guardarA.getSelectedFile();
                
                if(guardar != null){
                    DireccionPath = guardar + ".nik";
                    FileWriter  save=new FileWriter(guardar+".nik");
                    save.write(area1.getText());
                    save.close();
                }
            }catch(IOException ex){
                System.out.println(ex);
            }
        }
        
        
        
    }
    else{
    File fichero = new File(DireccionPath);
        PrintWriter writer;
        try{
            writer = new PrintWriter(fichero);
            writer.print(area1.getText());
            writer.close();
        }catch(FileNotFoundException e){
            System.out.println(e);
        }
    }
 
    }
    private int findFirstNonWordChar(String text, int index) {
        while (index < text.length()) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
            index++;
        }
        return index;
    }

    long tiempoDeEjecucion;

     private FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos niklaus ","nik");
    public compilador() {

        compilador.listaLexemas = new ArrayList<>();
        compilador.listaErrores = new ArrayList<>();
        this.listaEstatica = new ArrayList<>();
        this.listaDinamica = new ArrayList<>();
        setExtendedState(MAXIMIZED_BOTH);
        final StyleContext cont = StyleContext.getDefaultStyleContext();
        final AttributeSet red = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.RED);
        final AttributeSet Black = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
        final AttributeSet blue = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.blue);
        final AttributeSet green = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.green);
        final AttributeSet yellow = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.yellow);
        final AttributeSet orange = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.orange);
        final AttributeSet pink = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.pink);

        doc = new DefaultStyledDocument() {

            public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
                super.insertString(offset, str, a);

                String text = area1.getText(0, getLength());
                int before = findLastNonWordChar(text, offset);
                if (before < 0) {
                    before = 0;
                }
                int after = findFirstNonWordChar(text, offset + str.length());
                int wordL = before;
                int wordR = before;

                while (wordR <= after) {
                    if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {

                        if (text.substring(wordL, wordR).matches("(\\W)*(function|procedure)")) {
                            setCharacterAttributes(wordL, wordR - wordL, red, false);
                        } else if (text.substring(wordL, wordR).matches("(\\W)*(for|while|if|else)")) {
                            setCharacterAttributes(wordL, wordR - wordL, blue, false);
                        } else if (text.substring(wordL, wordR).matches("(\\W)*(const|var|uses|type)")) {
                            setCharacterAttributes(wordL, wordR - wordL, green, false);
                        } else if (text.substring(wordL, wordR).matches("(\\W)*(entero|byte|word|longint|shortint|real|single|double|pchar|pointer|string|char|boolean|float)")) {
                            setCharacterAttributes(wordL, wordR - wordL, orange, false);
                        } else if (text.substring(wordL, wordR).matches("(\\W)*(and|or|not)")) {
                            setCharacterAttributes(wordL, wordR - wordL, yellow, false);
                        } else if (text.substring(wordL, wordR).matches("(\\W)*(begin|end)")) {
                            setCharacterAttributes(wordL, wordR - wordL, pink, false);
                        } else {
                            setCharacterAttributes(wordL, wordR - wordL, Black, false);
                        }

                        wordL = wordR;
                    }
                    wordR++;
                }
            }

            public void remove(int offs, int len) throws BadLocationException {
                super.remove(offs, len);
                String text = area1.getText(0, getLength());
                int before = findLastNonWordChar(text, offs);
                if (before < 0) {
                    before = 0;
                }
                int after = findFirstNonWordChar(text, offs);

                if (text.substring(before, after).matches("(\\W)*(function|private)")) {
                    setCharacterAttributes(before, after - before, red, false);
                } else if (text.substring(before, after).matches("(\\W)*(for|while)")) {
                    setCharacterAttributes(before, after - before, blue, false);
                } else if (text.substring(before, after).matches("(\\W)*(if|else)")) {
                    setCharacterAttributes(before, after - before, green, false);
                } else if (text.substring(before, after).matches("(\\W)*(int|string)")) {
                    setCharacterAttributes(before, after - before, orange, false);
                } else if (text.substring(before, after).matches("(\\W)*(>|<)")) {
                    setCharacterAttributes(before, after - before, yellow, false);
                } else if (text.substring(before, after).matches("(\\W)*(begin|end)")) {
                    setCharacterAttributes(before, after - before, pink, false);
                } else {
                    setCharacterAttributes(before, after - before, Black, false);
                }

            }
        };
        initComponents();
        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/logo_converted.png"));
        setIconImage(icon);
        setVisible(true);
         setSize(917, 600);
        setLocation(180,75);
        
        

        TextLineNumber lineas = new TextLineNumber(area1);
        lineas.setCurrentLineForeground(Color.white);//current line
        lineas.setForeground(Color.BLUE);//color linea

        jScrollPane4.setRowHeaderView(lineas);
        area1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {

            }

            public void keyTyped(java.awt.event.KeyEvent evt) {
                area1KeyTyped(evt);
            }
        });
        jScrollPane4.setViewportView(area1);
        llenartablaEstatica();
        tiempoDeEjecucion = 0;

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane4 = new javax.swing.JScrollPane();
        area1 = new javax.swing.JTextPane(doc);
        jLabel5 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        area3 = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Nicklaus Compiler");
        setBackground(new java.awt.Color(204, 204, 204));
        setMaximumSize(null);
        setPreferredSize(new java.awt.Dimension(930, 643));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                formKeyTyped(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        area1.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        area1.setText("programa suma1 ;\nvar\n\nnum1:real;\nsi(num1>4) inicio \n\n\nfin si_no inicio \nfin;\n\n\n\n\nfin.");
        area1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                area1KeyTyped(evt);
            }
        });
        jScrollPane4.setViewportView(area1);

        getContentPane().add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 71, 900, 352));

        jLabel5.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jLabel5.setText("Salida: ");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 430, 1262, -1));

        area3.setEditable(false);
        area3.setColumns(20);
        area3.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        area3.setRows(5);
        jScrollPane6.setViewportView(area3);

        getContentPane().add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 450, 900, 120));

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/add15.png"))); // NOI18N
        jButton2.setText("Nuevo archivo");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/folder3.png"))); // NOI18N
        jButton3.setText("Abrir archivo");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/computer210.png"))); // NOI18N
        jButton4.setText("Guardar archivo");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/text169.png"))); // NOI18N
        jButton5.setText("Compilar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jButton5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jButton5KeyReleased(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/run.png"))); // NOI18N
        jButton6.setText("Ejecutar");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jButton2)
                .addGap(27, 27, 27)
                .addComponent(jButton3)
                .addGap(31, 31, 31)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addComponent(jButton5)
                .addGap(55, 55, 55)
                .addComponent(jButton6)
                .addGap(31, 31, 31))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton5)
                    .addComponent(jButton6))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 910, -1));

        jMenu1.setText("Archivo");
        jMenu1.setFont(new java.awt.Font("Comic Sans MS", 0, 12)); // NOI18N
        jMenu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu1MouseClicked(evt);
            }
        });

        jMenuItem1.setText("Guardar");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem4.setText("Cerrar Ventana");
        jMenuItem4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem4MousePressed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Operaciones");
        jMenu2.setFont(new java.awt.Font("Comic Sans MS", 0, 12)); // NOI18N

        jMenuItem2.setText("Limpiar");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuItem3.setText("Analizar");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void area1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_area1KeyTyped

    }//GEN-LAST:event_area1KeyTyped

    private void jMenu1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu1MouseClicked

        print();
    }//GEN-LAST:event_jMenu1MouseClicked

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        area1.setText("");
      
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        limpiarTodo();
        try {
            run();
        } catch (IOException ex) {
            Logger.getLogger(compilador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
       this.guardarArchivo();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        limpiarTodo();
        if(listaDinamica.isEmpty()){System.out.println("SI");}
      System.out.println(listaDinamica.size());
        try {
            run();
        } catch (IOException ex) {
            Logger.getLogger(compilador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton5KeyReleased
        
    }//GEN-LAST:event_jButton5KeyReleased

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        
    }//GEN-LAST:event_formKeyReleased

    private void formKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyTyped
        
    }//GEN-LAST:event_formKeyTyped

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        this.abrirArchivo();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
   compilador.this.setLocation(evt.getXOnScreen() - posicionX, evt.getYOnScreen() - posicionY);
    }//GEN-LAST:event_formMouseDragged

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
       posicionX = evt.getX();
        posicionY = evt.getY();
    }//GEN-LAST:event_formMousePressed

    private void jMenuItem4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem4MousePressed
      System.exit(0);
    }//GEN-LAST:event_jMenuItem4MousePressed
    
    public void abrirArchivo(){
        
        JFileChooser AbrirA = new JFileChooser();
        AbrirA.setFileFilter(filter);
        int opcion = AbrirA.showOpenDialog(this);
        if(opcion==JFileChooser.APPROVE_OPTION){
            area1.setText("");
            area1.setEditable(true);
            DireccionPath = AbrirA.getSelectedFile().getPath();
            
            File archivo = new File(DireccionPath);
            try{
                BufferedReader leer = new BufferedReader(new FileReader(archivo));
                String linea = leer.readLine();
                String total = "";
                while(linea != null){
                    total = total + linea + "\n";
                    linea = leer.readLine();
                }
                area1.setText(total);
                
            }catch(Exception e){
                System.out.println(e);
            }
            
        }
    }
    
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new compilador().setVisible(true);

            }
        });

    }

    public void print() {

        File ficheros = new File("salida.txt");
        PrintWriter writers;
        try {
            writers = new PrintWriter(ficheros);
           
            writers.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(compilador.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void probarLexerFile() throws IOException {
        int contIDs = 0;
        tokenslist = new LinkedList<identificador>();
        File fichero = new File("fichero.txt");
        PrintWriter writer;
        try {
            writer = new PrintWriter(fichero);
            writer.print(area1.getText());
            writer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(compilador.class.getName()).log(Level.SEVERE, null, ex);
        }
        Reader reader = new BufferedReader(new FileReader("fichero.txt"));
        Lexer lexer = new Lexer(reader);
        String resultado = "";
        String errores = "";
    }

    public void tablaResultado() {
        Object[][] matriz = new Object[tokenslist.size()][2];
        for (int i = 0; i < tokenslist.size(); i++) {
            matriz[i][0] = tokenslist.get(i).nombre;
        }
    }
    // clase de colores

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane area1;
    private javax.swing.JTextArea area3;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    // End of variables declaration//GEN-END:variables

    private void run() throws IOException {
        //////GUARDANDO CÓDIGO
        String codigo = area1.getText();
        File archivoDeCodigo = new File("codigo.txt");
        FileWriter escribe = new FileWriter(archivoDeCodigo, false);
        escribe.write(codigo);
        escribe.close();
        ///////COMPILANDO CÓDIGO
        String codigoArray[] = {"codigo.txt"};
        Date hora = new Date();
        long tiempo = hora.getTime();
        Cup.main(codigoArray);
        long tiempo2 = new Date().getTime();
        setTiempoDeEjecucion(tiempo2 - tiempo);
        mostrarResultados();
        guardarArchivos();
    }
    
    public static void imprime3dir(String valor){
        System.err.println(valor);
    }
    
    public static void tres(String a){
        lista3.add(a);
    }
    public static String getLineaAnterior(){
        return lista3.get(lista3.size()-1);
    }
    
    public static String c3dgetString(){
        String resultado = "";
            for (String lista : lista3) {
                 resultado += lista + "\n";
            }
        return resultado;
    }
    
    private void mostrarResultados() {
        area3.setText("Compilado en " + getTiempoDeEjecucion() + " milisegundos.\n");
        if (listaErrores.isEmpty()) {
            area3.setText("Compilado con éxito!!\n");
           
        } else {
            for (String error : listaErrores) {
                System.err.println(error);
                area3.setText(area3.getText() + error + "\n");
            }
        }
        for (String lexema : listaLexemas) {
            System.out.println(lexema);
          
        }
    }

    public static void setError(String error) {
        listaErrores.add(error);
    }

    public static void setEstatica(String constante) {
        listaEstatica.add(constante);
    }
public static identificador retorna(String id){
        for (identificador listaDinamica1 : listaDinamica) {
            if(listaDinamica1.nombre.equals(id)){
            
            return listaDinamica1;
            }
        }

return null;
}



public static  boolean comprueba(String  tipo, String val,String var,int l, int c){
    boolean t= false;
    String cad= tipo.toUpperCase();
    Pattern p = Pattern.compile("[0-9]*");
    Matcher m = p.matcher(val);
    boolean b = m.matches();
    System.out.println(b);
 
  return t;}
    public static void setDinamica(String identificador, String valor, String tipo) {
        
    }

    public static void setLexema(String lexema) {
        listaLexemas.add(lexema);
    }

    public long getTiempoDeEjecucion() {
        return tiempoDeEjecucion;
    }

    public void setTiempoDeEjecucion(long tiempoDeEjecucion) {
        this.tiempoDeEjecucion = tiempoDeEjecucion;
    }

    private void limpiarTodo() {
        c3d = "";
        lista3.clear();
       
        area3.setText("");
        listaErrores.clear();
        listaDinamica.clear();
        listaLexemas.clear();
    }

    private void llenartablaEstatica() {
    }

    private void guardarArchivos() {
        try {
            File archivo = new File("TablaDinamica.txt");
            try (FileWriter escribir = new FileWriter(archivo, false)) {

            }
        } catch (Exception e) {
        }
        String TSEstatica
                = " integer " + " INTEGER "
                + " byte " + " BYTE "
                + " word " + " WORD "
                + " true " + " TRUE "
                + " false " + " FALSE "
                + " longint " + " LONGINT "
                + " shortint " + " SHORTINT "
                + " real " + " REAL "
                + " single " + " SINGLE "
                + " double " + " DOUBLE "
                + " string " + " STRING "
                + " char " + " STRING "
                + " Showmessage " + " MOSTRARMENSAJE "
                + " boolean " + " BOOLEAN "
                + " var " + " VARIABLE "
                + " const " + " CONSTANTE "
                + " ; " + " PUNTOYCOMA "
                + " := " + " ASIGNACION "
                + " <= " + " MAYORIGUALQUE "
                + " >= " + " MENORIGUALQUE "
                + " < " + " MENORQUE "
                + " > " + " MAYORQUE "
                + " = " + " IGUAL "
                + " <> " + " DISTINTODE "
                + " + " + " COMA "
                + " and " + " Y "
                + " or " + " O "
                + " not " + " NEGACION "
                + " if " + " IF "
                + " repeat " + " REPEAT "
                + " until " + " UNTIL "
                + " then " + " THEN "
                + " begin " + " BEGIN "
                + " end " + " END "
                + " else " + " ELSE "
                + " for " + " FOR "
                + " to " + " TO "
                + " do " + " DO "
                + " while " + " WHILE "
                + " Procedure " + " PROCEDURE "
                + " function " + " FUNCTION "
                + "  " + " PARENTESISIZQ "
                + "  " + " PARENTESISDER "
                + " : " + " DOSPUNTOS "
                + " + " + " SUMA "
                + " * " + " MULTIPLICACION "
                + " - " + " RESTA "
                + " / " + " DIVISION "
                + " mod " + " MODULO ";

        try {
            File archivo2 = new File("TablaEstatica.txt");
            try (FileWriter escribir2 = new FileWriter(archivo2, false)) {
                escribir2.write(TSEstatica + System.getProperty("line.separator"));
            }
        } catch (Exception e) {
        }
        try {
            File archivo3 = new File("salida.txt");
            try (FileWriter escribir2 = new FileWriter(archivo3, false)) {
                escribir2.write(area3.getText());
            }
        } catch (Exception e) {
        }
    }
    

}
