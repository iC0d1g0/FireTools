
package clone;

import clone.logica_installador.ApkInstallerThread;
import clone.logica_installador.ApkManager;
import static clone.logica_installador.BatchFileCreator.checkAndCreateCommandFile;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
/**
 *
 * @author adder
 */
public final class LogicaDriversInterfaz implements LogicaDrivers {
    
    private final Funciones_windows helper ;
    private InfoEntity thisPC;
    private final JTextArea console;
    private final JProgressBar progress;
    public LogicaDriversInterfaz(JProgressBar progress, JTextArea console){
       
        this.console = console;
        helper = new Funciones_windows(progress,console);
        this.progress = progress;
        
       
    }

    @Override
    public synchronized InfoEntity getInfo() {
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
    public synchronized void instalarDrivers() {
        System.out.println("este es mi pc " +  this.thisPC.getVersion());
        helper.installDrivers(this.thisPC.getVersion());
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
        info.put("Modelo", getOutput("ro.product.vendor.model"));
        info.put("SystemType", getOutput("ro.boot.selinux"));
        info.put("Procesador", getOutput("ro.board.platform"));
        info.put("SDK", getOutput("ro.build.version.sdk"));
        info.put("Security_Patch", getOutput("ro.build.version.security_patch"));
        return info;
       }
      
        // Mostrar la información
      //  info.forEach((key, value) -> System.out.println(key + ": " + value.trim()));
        return null;
    }

    private  String getOutput(String command) {
       ApkManager algo = new ApkManager("", console, this.progress);
       ApkInstallerThread leer = new ApkInstallerThread(algo,this.progress);
       
        return leer.getInfo(command, checkAndCreateCommandFile()).trim(); // Remover espacios y saltos de línssea finales
    }


    @Override
    public void unInstallGapps() {
        this.helper.unInstallGpk();
    }

 
}

