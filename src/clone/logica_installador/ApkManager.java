/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clone.logica_installador;

import static clone.logica_installador.BatchFileCreator.checkAndCreateCommandFileUninstall;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
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
      
        installers = new Thread(new ApkInstallerThread(this, this.progress));
        this.folderName = folderName;
    }
    
    
    public void iniciar(){
        //Debo verficar si existe este folder con apks de googl servercies. 
      
        // installDrivers(this.folderName);
             
       // installers.start();
        
       
    }
   public void unInstallGApps(){
          clearText();      
          setPrintText("proceso : Deshintalando, wait...");
            
            String rutaBatch = checkAndCreateCommandFileUninstall();

        try {
            // Ejecuta el comando pnputil
           
            ProcessBuilder processBuilder = new ProcessBuilder(rutaBatch);
          
            processBuilder.redirectErrorStream(true); // Combina la salida de error con la salida estándar
            Process process = processBuilder.start();
            
            // Lee la salida del proceso para obtener mensajes de instalación
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                int progreso = 10;
                
                while ((line = reader.readLine()) != null) {
                    if(progreso > 70){
                         progreso =- 30;
                    }
                   
                    progress.setValue(progreso);
                  
                    setPrintText(line);  // Imprime la salida (incluye errores)
                   
                    
                    progreso =+ 20;
                    
                }
                progress.setValue(100);
      
               
            }
                    
            //Thread.sleep(2000);      
            process.waitFor(15, TimeUnit.MINUTES);
             
       

        } catch (IOException e) {
            setPrintText("Error desintalando tus apk ");
           
            Thread.currentThread().interrupt();  // Restaura el estado de interrupción
        } catch (InterruptedException ex) {
            Logger.getLogger(ApkInstallerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
                
    }
   
   public void InstalacionSimple(){
        AntesInstalar i = new AntesInstalar();
        if(i.existeG()){     
          clearText();      
          setPrintText("proceso : Deshintalando, wait...");
                
            String rutaBatch = Simplificando.checkAndCreateBatchFile();
            Path driversPath = Paths.get("Binaries\\g", folderName+"\\");
             Path currentPath = Paths.get("").toAbsolutePath();
            File folderDir = new File(currentPath.toFile(), "Binaries/g/"+folderName);
            if(folderDir.exists()){
                try {




                  ProcessBuilder processBuilder = new ProcessBuilder(rutaBatch,driversPath.toAbsolutePath().toString() );

                  processBuilder.redirectErrorStream(true); // Combina la salida de error con la salida estándar
                  Process process = processBuilder.start();

                  // Lee la salida del proceso para obtener mensajes de instalación
                  try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                      String line;
                      int progreso = 30;

                      while ((line = reader.readLine()) != null) {
                          if(progreso > 70){
                               progreso =- 30;
                          }

                          progress.setValue(progreso);

                          setPrintText(line);  // Imprime la salida (incluye errores)


                          progreso =+ 20;

                      }
                      progress.setValue(100);


                  }

                  //Thread.sleep(2000);      
                  process.waitFor(15, TimeUnit.MINUTES);



              } catch (IOException e) {
                  setPrintText("Error desintalando tus apk ");

                  Thread.currentThread().interrupt();  // Restaura el estado de interrupción
              } catch (InterruptedException ex) {
                  Logger.getLogger(ApkInstallerThread.class.getName()).log(Level.SEVERE, null, ex);
              }
              }else{
               clearText();
               setPrintText("Dispositivo no soportado!, \nSI crees que es un error llama: 8293891045, iCodigo Desbloqueos");
               progress.setValue(0);
            }
        }else{
                  clearText();
                  setPrintText("Error: El programa ha sido comprometido. Desintalar y volver a instalar. \n SI el problema persiste, favor contactar iCodigo : 8293891045");
                  progress.setValue(0);
        }
                
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
        Path driversPath = Paths.get("Binaries\\g", folderName);
        
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
