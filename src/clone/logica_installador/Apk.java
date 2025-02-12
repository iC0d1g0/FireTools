/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clone.logica_installador;

import java.nio.file.Path;


public class Apk {
    
    private Path driver = null;
    private String name = "";
    private int sdk = 0;
    private int androidVersion = 0;
    
    public Apk(Path driver){
           this.driver = driver;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSdk() {
        return sdk;
    }

    public void setSdk(int sdk) {
        this.sdk = sdk;
    }

    public int getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(int androidVersion) {
        this.androidVersion = androidVersion;
    }

      
    public Path getDriver(){
        return this.driver;
    }
   public void setDriver(Path path){
         this.driver = path;
         
    }
 
}
