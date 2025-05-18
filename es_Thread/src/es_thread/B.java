/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es_thread;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class B extends Thread{
    public B(){
        setName("B");
    }
    int b;
    Scanner scanner = new Scanner(System.in);
    
    public void run(){
        System.out.println("inserisci un valore");
        b = scanner.nextInt();
        b = b / 2;
        System.out.println("il risultato del Thread " + Thread.currentThread().getName() + "è: " + b);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(B.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public int getB(){
        return b;
    }
}
