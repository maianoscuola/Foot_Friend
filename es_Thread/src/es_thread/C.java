/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es_thread;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author feder
 */
public class C extends Thread{
    public C(){
        setName("C");
    }
    int C;
    Scanner scanner = new Scanner(System.in);
    
    public void run(){
        System.out.println("inserisci un valore");
        C = scanner.nextInt();
        C = C*C - 12;
        System.out.println("il risultato del Thread " + Thread.currentThread().getName() + "è: " + C);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException ex) {
            Logger.getLogger(B.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public int getC(){
        return C;
    }
}
