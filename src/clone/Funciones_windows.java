
package clone;
import clone.logica_installador.ApkManager;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;



public class Funciones_windows {
  
     private final JProgressBar progress;
     private final JTextArea console;
     
    public Funciones_windows (JProgressBar progress, JTextArea console){
          this.progress = progress;
        this.console = console;
     }
    public void unInstallGpk(){
        ApkManager manager = new ApkManager("", this.console, this.progress);
        manager.unInstallGApps();
    }
    // Método para iniciar la instalación de drivers desde la carpeta Binaries
    public synchronized void installDrivers(String folderName) {
        ApkManager manager = new ApkManager(folderName, this.console, this.progress);
        manager.InstalacionSimple();
      
    }
    public void println(String texto){
         this.console.append(texto + "\n");
    }
}

