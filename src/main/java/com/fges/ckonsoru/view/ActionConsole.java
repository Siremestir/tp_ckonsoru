/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fges.ckonsoru.view;

import java.util.Scanner;

/**
 *
 * @author julie.jacques
 */
public abstract class ActionConsole {
    
    protected int numero;
    protected String description;
    
    public ActionConsole(int numero, String description){
        this.numero         = numero;
        this.description    = description;
    }
    
    public abstract void executer(Scanner scanner);

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
}
