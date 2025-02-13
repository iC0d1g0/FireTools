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
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

/**
 *
 * @author adder
 */
public class ApkManager {
    
    private final JProgressBar progress;
    private static final int DRIVERS_INSTALLERS =1;
    private JTextArea console;
    private Queue<Apk> cola = new LinkedList<>(); 
    private final String folderName;
    Object lock = new Object();
    private Thread installers;
    
    private int cantidadDrivers;
    
    public ApkManager(String folderName, JTextArea console,JProgressBar progress){
        this.console = console;
        this.progress = progress;
      
        installers = new Thread(new ApkInstallerThread(this, "installador1", this.progress));
        this.folderName = folderName;
    }
    
    
    public void iniciar(){
        installDrivers(this.folderName);
             
        installers.start();
        
       
    }
   public Thread getInstaller(){
        return this.installers;
    }
   
   public void extraer(){
        downloadApks();        
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
    
            installers.start();
            
    }
        // Método para iniciar la instalación de drivers desde la carpeta Binaries
    public void installDrivers(String folderName) {
        Path driversPath = Paths.get("Binaries", folderName);

        if (!driversPath.toFile().exists()) {
            System.out.println("Carpeta de drivers no encontrada en: " + driversPath);
            downloadApks();
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
    }
    public void downloadApks(){
        //Revisar conexion de internet 
        //descargar zip con las apks
        //descomprimir zip
        //Confirmar la existencia de las carpetas : GServices y SocialApps
        //analizar la existencia de la carpeta $VersionAndroid, else : Notificar que este modelo no esta soportado por el momento y cancelar el proceso. 
        //Si todo anda bien, pues volver a ejecutar la instalacion. 

  
    }
    
    
     public synchronized int getQueueSize() {
        return cola.size();
    }

    public synchronized boolean isQueueEmpty() {
        return cola.isEmpty();
    }
  
}
