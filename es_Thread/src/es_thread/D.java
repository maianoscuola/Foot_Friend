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
public class D extends Thread{
    public D(){
        setName("D");
    }
    int D;
    Scanner scanner = new Scanner(System.in);
    
    public void run(){
        System.out.println("inserisci un valore");
        D = scanner.nextInt();
        D = D + 5;
        System.out.println("il risultato del Thread " + Thread.currentThread().getName() + "è: " + D);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(B.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public int getD(){
        return D;
    }
}
