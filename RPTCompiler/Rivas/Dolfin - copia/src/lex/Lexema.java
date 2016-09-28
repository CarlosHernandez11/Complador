/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lex;

/**
 *
 * @author Usuario
 */
//Este es para el cup---------------------
public class Lexema {
    String lexema = "";
    String tipo = "";
    String valor = "";
    String temporal = "";
    String error = "";
    String tres = "";
    
    public Lexema(String lexema) {
        this.lexema = lexema;
    }
    
    public Lexema(String lexema, String tipo) {
        this.lexema = lexema;
        this.tipo = tipo;
    }

    public Lexema(String lexema, String tipo, String codigo) {
        this.lexema = lexema;
        this.tipo = tipo;
        this.temporal = codigo;
    }
    
    public String getCodigo() {
        return temporal;
    }

    public void setCodigo(String codigo) {
        this.temporal = codigo;
    }

    public Lexema() {
        this.lexema = "";
        this.tipo = "";
        this.valor = "";
        this.temporal = "";
        this.error = "";
    }
    public String getLexema() {
        return lexema;
    }
    
    public String getTipo() {
        return tipo ;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }
    
    public void setTipo(String tipo){
    this.tipo = tipo;
    }
    
}
