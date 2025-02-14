
package clone.logica_installador;

import static clone.logica_installador.BatchFileCreator.checkAndCreateBatchFile;
import static clone.logica_installador.BatchFileCreator.checkAndCreateCommandFile;
import static clone.logica_installador.BatchFileCreator.checkAndCreateCommandFileUninstall;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileSystemView;

public class ApkInstallerThread implements Runnable {
    private final ApkManager manager;
    //private final String installerName;
    private final JProgressBar progress;
    

    public ApkInstallerThread(ApkManager manager, JProgressBar progress) {
        this.manager = manager;
        //this.installerName = installerName;
        this.progress = progress;
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
           // installApk(driver.getDriver(), checkAndCreateBatchFile());
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
    
  
   /* public void installApk(Path infFilePath, String batchFile) {
          this.manager.setPrintText("proceso : Instalando, wait...");
            
            String rutaBatch = batchFile;
         

        try {
            // Ejecuta el comando pnputil
           
            ProcessBuilder processBuilder = new ProcessBuilder(rutaBatch, infFilePath.toAbsolutePath().toString());
            //System.out.println("archivos : " + infFilePath.toAbsolutePath().toString());
            processBuilder.redirectErrorStream(true); // Combina la salida de error con la salida estándar
            Process process = processBuilder.start();
            new ProgressUpdater(manager).execute();
            // Lee la salida del proceso para obtener mensajes de instalación
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    progress.setValue(80);
                    System.out.println("Empiezo a leer");
                    this.manager.setPrintText(line);  // Imprime la salida (incluye errores)
                    System.out.println("Lanzo lo que encontre: "+ line);
                    
                    
                }
                progress.setValue(100);
                System.out.println("Ya termino el buffer..  ");
               
            }
                    
            //Thread.sleep(2000);      
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
*/
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
    
    
     // SwingWorker para actualizar la barra de progreso en la UI
    public class ProgressUpdater extends SwingWorker<Void, Integer> {
        private final ApkManager manager;
        private final int totalDrivers;
     
    
        public ProgressUpdater(ApkManager manager) {
            this.manager = manager;
           
            this.totalDrivers = manager.getQueueSize(); // Guardar el total de drivers
        }

        @Override
        protected Void doInBackground() {
            // Instant inicio = Instant.now();
                   
            while (!manager.isQueueEmpty()) {
                int processedDrivers = totalDrivers - manager.getQueueSize();
                int progress = (int) ((processedDrivers / (double) totalDrivers) * 80);
                
                publish(progress); // Publicar el progreso para actualizar en la UI
                try {
                                                        
                    Thread.sleep(500);
                            
                     // Intervalo de actualización
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
                 //   Instant fin = Instant.now();
                   // long duracionMinutos = Duration.between(inicio, fin).toSeconds();
                  //  this.manager.setPrintText("\nDuracion : " +duracionMinutos+ " Segundos\n");
            
                    publish(100); 
            
            return null;
        }

        @Override
        protected void process(List<Integer> chunks) {
            int lastProgress = chunks.get(chunks.size() - 1);
            progress.setValue(lastProgress); // Actualizar la barra de progreso
        }

        @Override
        protected void done() {
          publish(100); 
          
           
        }
    }
    
    public  String seleccionarCarpeta() {
        // Crear un JFileChooser con el modo de selección de directorios
        JFileChooser selectorCarpeta = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        selectorCarpeta.setDialogTitle("Selecciona una carpeta");
        selectorCarpeta.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        // Mostrar el diálogo y capturar la selección
        int resultado = selectorCarpeta.showOpenDialog(null);

        // Verificar si el usuario seleccionó una carpeta
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File carpetaSeleccionada = selectorCarpeta.getSelectedFile();
            return carpetaSeleccionada.getAbsolutePath(); // Devolver la ruta de la carpeta seleccionada
        } else {
            System.out.println("No se seleccionó ninguna carpeta.");
            return null;
        }
    }

}



   

