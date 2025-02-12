/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package firetools;

import javax.swing.JTextArea;

/**
 *
 * @author elcue
 */
/*
Versiones de android  que usan las tablet amazon: 
La version 2.3.3 api 10, esta en la Kindle Fire 2011, pero ya no es soportada para acceder a los servicios de google.
4.0.3  --> 15
4.4.2 --> 19
5.1 --> 22
9 --> 28
11 --> 30
*/
public class AdbManagement {
    /*
        private int version; //getprop ro.build.version.release 
    private String 
            name, //getprop ro.build.mktg.fireos
            os,//ro.build.version.fireos
            securitypatch,//getprop ro.build.version.security_patch
            model,// getprop ro.product.model, 
            modelo_alt, //ro.camera.model 
            mfacture; //getprop ro.product.brand
    */
    private String wait = "wait-for-device";
    private String shell = "shell ";
    private String uninstall = "uninstall ";
    private String packagelist = "pm package list";
    private String getInfo = "";
    /*
    DeviceInfo info(){
        
        return new DeviceInfo();
    }
    
    void installGapps(JTextArea console){
        
    }
    
    void installSocial(JTextArea console){
        
    }
    
    void uninstallGapps(JTextArea console){
        
    }
*/
    
}
