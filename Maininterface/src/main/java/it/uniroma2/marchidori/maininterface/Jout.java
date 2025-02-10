package it.uniroma2.marchidori.maininterface;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Jout {
    private static final  PrintStream OUT =  new PrintStream(new FileOutputStream(FileDescriptor.out));
    private String name;
    public Jout(String name) {
        this.name = name;
    }

    public synchronized void print(String msg){
        OUT.println('[' + name + "]: " + msg);
    }

}
