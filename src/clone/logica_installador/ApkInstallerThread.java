
package clone.logica_installador;

import static clone.logica_installador.BatchFileCreator.checkAndCreateCommandFile;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApkInstallerThread  {

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
}