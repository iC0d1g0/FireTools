
package clone.logica_installador;

import static clone.logica_installador.BatchFileCreator.checkAndCreateBatchFile;
import static clone.logica_installador.BatchFileCreator.checkAndCreateCommandFile;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApkInstallerThread implements Runnable {
    private final ApkManager manager;
    private final String installerName;
    
    

    public ApkInstallerThread(ApkManager manager, String installerName) {
        this.manager = manager;
        this.installerName = installerName;
    }
    
 
    @Override
    public void run() {
        try {
            while(true){
            var driver = manager.obtenerDriver();
  
            if(driver == null){    
                System.out.println("EL QUEUE DEBE DE ESTAR VACIO++++++++++++++++++++++");
                return;
            }
            this.manager.clearText();
            this.manager.setPrintText("ruta: " + driver.getDriver());
            installApk(driver.getDriver(), checkAndCreateBatchFile());
            Thread.sleep(1000);
            this.manager.setPrintText("Estado : Iniciando....");
            
        }
            
        } catch (InterruptedException ex) {
           this.manager.setPrintText("error en thread : " + ex.getMessage());
        }
    }
    
    
    
    
    public String getInfo(String commando, String batchFile) {
                      
            String rutaBatch = checkAndCreateCommandFile();
            String line = "";
        try {
            // Ejecuta el comando pnputil
            ProcessBuilder processBuilder = new ProcessBuilder(rutaBatch, commando);
            System.out.println("archivos : " + commando);
            processBuilder.redirectErrorStream(true); // Combina la salida de error con la salida estándar
            Process process = processBuilder.start();
           
            // Lee la salida del proceso para obtener mensajes de instalación
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                
                if ((line = reader.readLine()) != null){
                    return line;
                }
                   
                    
            }
            
            Thread.sleep(500);      
            process.waitFor(5, TimeUnit.MINUTES);
           
        } catch (IOException e) {
           
          Thread.currentThread().interrupt();  // Restaura el estado de interrupción
            
        } catch (InterruptedException ex) {
            Logger.getLogger(ApkInstallerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
           return line;
    }
    
        
        
    public void installApk(Path infFilePath, String batchFile) {
          this.manager.setPrintText("proceso : Instalando, wait...");
            
            String rutaBatch = batchFile;
         

        try {
            // Ejecuta el comando pnputil
            ProcessBuilder processBuilder = new ProcessBuilder(rutaBatch, infFilePath.toAbsolutePath().toString());
            System.out.println("archivos : " + infFilePath.toAbsolutePath().toString());
            processBuilder.redirectErrorStream(true); // Combina la salida de error con la salida estándar
            Process process = processBuilder.start();
           
            // Lee la salida del proceso para obtener mensajes de instalación
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    this.manager.setPrintText(line);  // Imprime la salida (incluye errores)
                }
            }
            
            Thread.sleep(2000);      
            process.waitFor(15, TimeUnit.MINUTES);
            this.manager.setPrintText("Instalacion : Completa...\n");    
            System.out.println("");
                         
            

        } catch (IOException e) {
            this.manager.setPrintText("Error al ejecutar pnputil para el archivo: " + infFilePath.getFileName());
           
            Thread.currentThread().interrupt();  // Restaura el estado de interrupción
        } catch (InterruptedException ex) {
            Logger.getLogger(ApkInstallerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
    public void installApk(Path infFilePath, String batchFile) {
    this.manager.setPrintText("Proceso: Instalando, espera...");

    try {
        // Construye el proceso
        ProcessBuilder processBuilder = new ProcessBuilder(batchFile, infFilePath.toAbsolutePath().toString());
        processBuilder.redirectErrorStream(true); // Redirige errores a la salida estándar
        Process process = processBuilder.start();

        // Hilo separado para leer la salida del proceso y evitar bloqueos
        Thread outputThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    this.manager.setPrintText(line); // Muestra la salida en tiempo real
                }
            } catch (IOException e) {
                this.manager.setPrintText("Error leyendo la salida del proceso: " + e.getMessage());
            }
        });

        outputThread.start(); // Inicia el hilo de lectura

        // Espera a que el proceso termine completamente
        int exitCode = process.waitFor();
        outputThread.join(); // Asegura que el hilo de lectura termine antes de continuar

        if (exitCode == 0) {
            this.manager.setPrintText("Instalación completada con éxito.");
        } else {
            this.manager.setPrintText("Error en la instalación. Código de salida: " + exitCode);
        }
        
    } catch (IOException e) {
        this.manager.setPrintText("Error al ejecutar el instalador: " + e.getMessage());
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt(); // Restaura el estado de interrupción
        this.manager.setPrintText("Instalación interrumpida.");
    }
}
*/

   
}
