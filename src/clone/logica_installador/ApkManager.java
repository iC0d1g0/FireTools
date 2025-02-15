
package clone.logica_installador;

import static clone.logica_installador.BatchFileCreator.checkAndCreateCommandFileUninstall;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

public class ApkManager {
    
    private final JProgressBar progress;
    private final JTextArea console;
    private final String folderName;

    
    public ApkManager(String folderName, JTextArea console,JProgressBar progress){
        this.console = console;
        this.progress = progress;
     
        this.folderName = folderName;
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

    public void setPrintText(String texto){
         this.console.append( texto + "\n");
    }
    public void clearText(){
         this.console.setText("");
    }
}
