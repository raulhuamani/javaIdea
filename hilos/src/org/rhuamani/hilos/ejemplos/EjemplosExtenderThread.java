package org.rhuamani.hilos.ejemplos;

import org.rhuamani.hilos.ejemplos.threads.NombreThread;

public class EjemplosExtenderThread {
    public static void main(String[] args) throws InterruptedException {
        Thread hilo = new NombreThread("Jhon Doe");
        hilo.start();
        //Thread.sleep(100);
        Thread hilo2 = new NombreThread("Maria");
        hilo2.start();

        NombreThread hilo3 = new NombreThread("Pepe");
        hilo3.start();
        System.out.println(hilo.getState());
    }
}
