
package clone;

import clone.logica_installador.ApkInstallerThread;
import clone.logica_installador.ApkManager;
import static clone.logica_installador.BatchFileCreator.checkAndCreateCommandFile;
import clone.logica_installador.CardReaderInstaller;
import java.awt.TextArea;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
/**
 *
 * @author adder
 */
public final class LogicaDriversInterfaz implements LogicaDrivers {
    
    private final Funciones_windows helper ;
    private InfoEntity thisPC;
    private final JProgressBar progress;
    private final JTextArea console;
    
    public LogicaDriversInterfaz(JProgressBar progress, JTextArea console){
       
        this.progress = progress;
        this.console = console;
        helper = new Funciones_windows(progress,console);
    }

    @Override
    public InfoEntity getInfo() {
        InfoEntity info= new InfoEntity();
      
         var local = getLocalInfo();
        if(local != null){
         info.setName(local.get("Name"));
         info.setOsName(local.get("OsName"));
         info.setVersion(local.get("Version"));
         
         info.setManufacture(local.get("Manufacture"));
         info.setModelo(local.get("Modelo"));
         info.setSystemType(local.get("SystemType"));
         info.setProcesador(local.get("Procesador"));
         info.setSdk(local.get("SDK"));
         info.setSecurity_patch(local.get("Security_Patch"));
        
         this.thisPC = info;
          return info;
        }
        
         
       return null;
    }

    @Override
    public void instalarDrivers() {
        helper.installDrivers(this.thisPC.getSdk());
    }

    @Override
    public void extraerDrivers() {
        helper.copyDriverStore(this.thisPC.getModelo());
    }

    @Override
    public void customDrivers() {
      String carpeta = helper.seleccionarCarpeta();
      if(carpeta != null){
          this.console.append("Instalando drivers de: " + carpeta);
          helper.installCustomDrivers(carpeta);
      }else{
          this.console.append("Favor seleconar algun archivo..");
          
      }
    }

    @Override
    public void manualDriver(String path) {
        
    }
        
    private  Map<String, String> getLocalInfo(){
       
      
       if(!"error: no devices/emulators found".equals(getOutput("ro.product.brand"))){
             Map<String, String> info = new HashMap<>();                 
        // Información del sistema básico usando System.getProperty
        info.put("Name",getOutput("ro.build.mktg.fireos") );
        info.put("OsName",getOutput("ro.build.version.fireos") );
        info.put("Version", getOutput("ro.build.version.release "));
       
        // Información avanzada usando comandos de Windows
        info.put("Manufacture", getOutput("ro.product.brand"));
        info.put("Modelo", getOutput("ro.camera.model"));
        info.put("SystemType", getOutput("ro.build.version.codename"));
        info.put("Procesador", getOutput("ro.board.platform"));
        info.put("SDK", "ro.board.platform");
        info.put("Security_Patch", getOutput("ro.build.version.security_patch"));
        return info;
       }
      
        // Mostrar la información
      //  info.forEach((key, value) -> System.out.println(key + ": " + value.trim()));
        return null;
    }

    private  String getOutput(String command) {
       ApkManager algo = new ApkManager("", console);
       ApkInstallerThread leer = new ApkInstallerThread(algo, "");
       
        return leer.getInfo(command, checkAndCreateCommandFile()).trim(); // Remover espacios y saltos de línssea finales
    }

    @Override
    public void installarCardReader() {
     
        try {
         this.console.append("\n Preparando instalacion de CardReader\n");
        this.console.append("\n por favor espere ....\n");
        this.console.append("\n Espere terminar el asistente.. luego finalize");
        Thread.sleep(1000);
        CardReaderInstaller ca = new CardReaderInstaller ();
        this.console.append("\nEstado: "+ ca.checkAndCreateBatchFile() + "\n");
        this.console.append("Exito...\n");
        
        } catch (InterruptedException ex) {
            Logger.getLogger(LogicaDriversInterfaz.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
}

