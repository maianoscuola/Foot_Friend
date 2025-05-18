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
public class Es_Thread {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a;
        System.out.println("inserisci un valore");
        a = scanner.nextInt();
        a = a*3*2;
        B b = new B();
        C c = new C();
        D d = new D();
        b.start();
        c.start();
        d.start();
        try {
            b.join();
            c.join();
        d.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Es_Thread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
