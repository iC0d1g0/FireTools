/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clone.logica_installador;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Stream;
import javax.swing.JTextArea;

/**
 *
 * @author adder
 */
public class ApkManager {
    
    
    private static final int DRIVERS_INSTALLERS =1;
    private JTextArea console;
    private Queue<Apk> cola = new LinkedList<>(); 
    private final String folderName;
    Object lock = new Object();
    private Thread[] installers;
    
    private int cantidadDrivers;
    
    public ApkManager(String folderName, JTextArea console){
        this.console = console;
        installers = new Thread[DRIVERS_INSTALLERS];
        for(int i= 0; i<DRIVERS_INSTALLERS; i++){
            installers[i] = new Thread(new ApkInstallerThread(this, "Installer"+(i+1)));
        }
        this.folderName = folderName;
    }
    
    public void iniciar(){
        installDrivers(this.folderName);
        
        for(Thread install :installers){
            install.start();
        }
       
    }
       public void extraer(){
        extraerDrivers();        
        Thread extractor = new Thread(new DriveresExtractor(this,this.folderName));
        extractor.start();
       
    }
    public void setPrintText(String texto){
         this.console.append( texto + "\n");
    }
    public void clearText(){
         this.console.setText("");
    }
    public void agregarDrivers(Apk driver){
        synchronized(lock){
            cola.offer(driver);
            lock.notify();
        }
    }
    
    public Apk obtenerDriver() throws InterruptedException{
        synchronized(lock){
            while(cola.isEmpty()){
                this.console.append("Driver restantes :" + cola.size() + "/" + this.cantidadDrivers);
                lock.wait();
            }
        }
        this.console.append("Driver restantes :" + cola.size() + "/" + this.cantidadDrivers +"\n");
        return cola.poll();
    }
    
       public void installCustomDriver(String custom) {
        
      
        Path driversPath = Paths.get(custom);

        if (!driversPath.toFile().exists()) {
            System.out.println("Carpeta de drivers no encontrada en: " + driversPath);
            return;
        }

        try (Stream<Path> paths = Files.walk(driversPath)) {
            paths.filter(Files::isRegularFile) // Filtra solo archivos
                .filter(path -> path.toString().toLowerCase().endsWith(".apk")) // Filtra archivos .inf
                .forEach(path -> agregarDrivers(new Apk(path))); // Usar expresión lambda en lugar de referencia de método
              
            this.cantidadDrivers = this.cola.size();
         } catch (IOException e) {
            this.console.append("Error al recorrer la carpeta: " + driversPath.toAbsolutePath());
            e.printStackTrace();
        }
          for(Thread install :installers){
            install.start();
        }
    }
        // Método para iniciar la instalación de drivers desde la carpeta Binaries
    public void installDrivers(String folderName) {
        Path driversPath = Paths.get("Binaries", folderName);

        if (!driversPath.toFile().exists()) {
            System.out.println("Carpeta de drivers no encontrada en: " + driversPath);
            return;
        }

        try (Stream<Path> paths = Files.walk(driversPath)) {
            paths.filter(Files::isRegularFile) // Filtra solo archivos
                .filter(path -> path.toString().toLowerCase().endsWith(".inf")) // Filtra archivos .inf
                .forEach(path -> agregarDrivers(new Apk(path))); // Usar expresión lambda en lugar de referencia de método
              
            this.cantidadDrivers = this.cola.size();
         } catch (IOException e) {
            this.console.append("Error al recorrer la carpeta: " + driversPath.toAbsolutePath());
            e.printStackTrace();
        }
    }
    public void extraerDrivers(){

        String sourcePath = System.getenv("windir") + "\\System32\\DriverStore\\FileRepository";
                     
        try {
            Files.walk(Paths.get(sourcePath))
                 .forEach(path -> agregarDrivers(new Apk(path)));
              this.cantidadDrivers = this.cola.size();
        } catch (IOException e) {
            this.console.append("\nError al acceder o copiar los archivos de la carpeta FileRepository. " + e.getMessage());
           
             }
    }
    
    
     public synchronized int getQueueSize() {
        return cola.size();
    }

    public synchronized boolean isQueueEmpty() {
        return cola.isEmpty();
    }
}
