/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clone;

/**
 *
 * @author adder
 */
public class InfoEntity {
   private String   
    name, //getprop ro.build.mktg.fireos
    OsName, //ro.build.version.fireos
    Version,//getprop ro.build.version.release 
    Manufacture, //getprop ro.product.brand
    Modelo, //ro.camera.model 
    sdk, // getprop ro.build.version.sdk
    SystemType, //getprop ro.build.version.codename  Nombre de la version(nougat, Masmellow, Oreo, etc..): 
    Procesador, //ro.board.platform
    security_patch; //ro.build.version.security_patch

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSdk() {
        return sdk;
    }

    public void setSdk(String sdk) {
        this.sdk = sdk;
    }
  

    public String getOsName() {
        return OsName;
    }

    public void setOsName(String OsName) {
        this.OsName = OsName;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String Version) {
        this.Version = Version;
    }

    public String getManufacture() {
        return Manufacture;
    }

    public void setManufacture(String Manufacture) {
        this.Manufacture = Manufacture;
    }

    public String getModelo() {
        return Modelo;
    }

    public void setModelo(String Modelo) {
        this.Modelo = Modelo;
    }

    public String getSystemType() {
        return SystemType;
    }

    public void setSystemType(String SystemType) {
        this.SystemType = SystemType;
    }

    public String getProcesador() {
        return Procesador;
    }

    public void setProcesador(String Procesador) {
        this.Procesador = Procesador;
    }

    public String getSecurity_patch() {
        return security_patch;
    }

    public void setSecurity_patch(String security_patch) {
        this.security_patch = security_patch;
    }


}
