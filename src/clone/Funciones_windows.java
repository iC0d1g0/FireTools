
package clone;
import clone.logica_installador.ApkInstallerThread;
import clone.logica_installador.ApkManager;
import java.awt.TextArea;
import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileSystemView;



public class Funciones_windows {
  
     private final JProgressBar progress;
     private final JTextArea console;
     private Thread installador;
     public Funciones_windows (JProgressBar progress, JTextArea console){
          this.progress = progress;
        this.console = console;
     }
 
    // Método para iniciar la instalación de drivers desde la carpeta Binaries
    public void installDrivers(String folderName) {
     
        ApkManager manager = new ApkManager(folderName, this.console, this.progress);
        installador = manager.getInstaller();
        System.out.println("Voy a iniciar la insalacion de drivers");
        manager.iniciar();
      
        //new ProgressUpdater(manager, installador).execute();
    }
    
    public void installCustomDrivers(String folderName){
        ApkManager manager = new ApkManager(folderName, this.console, this.progress);
        System.out.println("Voy a iniciar la insalacion de drivers");
        manager.installCustomDriver(folderName);
      
        new ProgressUpdater(manager, installador).execute();
        
    }
    public void println(String texto){
         this.console.append(texto + "\n");
    }
    public void copyDriverStore(String folderName) {
     
            ApkManager manager = new ApkManager(folderName, this.console, this.progress);
            System.out.println("Voy a iniciar la insalacion de drivers");
            manager.extraer();

            new ProgressUpdater(manager, installador).execute();
         }

 
     // SwingWorker para actualizar la barra de progreso en la UI
    private class ProgressUpdater extends SwingWorker<Void, Integer> {
        private final ApkManager manager;
        private final int totalDrivers;
        private Thread installador;
    
        public ProgressUpdater(ApkManager manager,Thread installador ) {
            this.manager = manager;
            this.installador = installador;
           
            this.totalDrivers = manager.getQueueSize(); // Guardar el total de drivers
        }

        @Override
        protected Void doInBackground() {
             Instant inicio = Instant.now();
        
            while (!manager.isQueueEmpty()) {
                int processedDrivers = totalDrivers - manager.getQueueSize();
                int progress = (int) ((processedDrivers / (double) totalDrivers) * 100);
                
                publish(progress); // Publicar el progreso para actualizar en la UI
                try {
                                                        
                    Thread.sleep(500);
                            
                     // Intervalo de actualización
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
                    Instant fin = Instant.now();
                    long duracionMinutos = Duration.between(inicio, fin).toSeconds();
                    println("\nDuracion : " +duracionMinutos+ " Segundos\n");
            
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
           // JOptionPane.showMessageDialog(null, "Instalación completada.");
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

