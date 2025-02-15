/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clone.logica_installador;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BatchFileCreator {
       // Función para verificar si el archivo batch existe, y crearlo si no es así
    public static String checkAndCreateBatchFile() {
        Path currentPath = Paths.get("").toAbsolutePath();
        File batchFile = new File(currentPath.toFile(), "Binaries/bin/internalBinary.bat");

        // Verificar si el archivo batch existe
        if (!batchFile.exists()) {
            System.out.println("El archivo internalBinary.bat no existe. Creando el archivo...");
             batchFile.getAbsolutePath();
            // Llamar a la función para crear el archivo batch
            createBatchFile();
            
            return checkAndCreateBatchFile();           
        }
         return  batchFile.getAbsolutePath();
    }
    public static void createBatchFile() {
        // Obtén el directorio de ejecución del programa y crea las carpetas necesarias
        Path currentPath = Paths.get("").toAbsolutePath();
        File binariesDir = new File(currentPath.toFile(), "Binaries/bin");

        if (!binariesDir.exists() && !binariesDir.mkdirs()) {
            System.err.println("No se pudo crear la carpeta Binaries/bin.");
            return;
        }

        // Ruta al archivo batch
        File batchFile = new File(binariesDir, "internalBinary.bat");

        // Contenido del archivo batch
        String batchContent = """
            @echo off
        
            rem Asigna el primer parámetro pasado al batch como la variable DIR_PATH
            set "DIR_PATH=%~1"
       echo ****************************************************************************************
       echo Waiting...
            adb.exe wait-for-device   
       echo instalando ...
            adb.exe install "%DIR_PATH%"  
       rem for %f in ("%DIR_PATH%"*.apk") do adb install "%f"
       echo *****************************************************************************************
            
            """;

        // Escribe el contenido en el archivo batch
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(batchFile))) {
            writer.write(batchContent);
            System.out.println("El archivo batch se ha creado correctamente en: " + batchFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error al crear el archivo batch: " + e.getMessage());
        }
    }
    
    public static void createBatchForInfoCommands() {
        // Obtén el directorio de ejecución del programa y crea las carpetas necesarias
        Path currentPath = Paths.get("").toAbsolutePath();
        File binariesDir = new File(currentPath.toFile(), "Binaries/bin");

        if (!binariesDir.exists() && !binariesDir.mkdirs()) {
            System.err.println("No se pudo crear la carpeta Binaries/bin.");
            return;
        }

        // Ruta al archivo batch
        File batchFile = new File(binariesDir, "adbcommand.bat");

        // Contenido del archivo batch
        String batchContent = """
            @echo off
        
            rem Asigna el primer parámetro pasado al batch como la variable DIR_PATH
            set "COMMAD=%~1"
            adb.exe wait-for-device                           
            adb.exe shell getprop "%COMMAD%"  
        
            """;

        // Escribe el contenido en el archivo batch
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(batchFile))) {
            writer.write(batchContent);
            System.out.println("El archivo batch se ha creado correctamente en: " + batchFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error al crear el archivo batch: " + e.getMessage());
        }
    }
    
    public static String checkAndCreateCommandFile() {
        Path currentPath = Paths.get("").toAbsolutePath();
        File batchFile = new File(currentPath.toFile(), "Binaries/bin/adbcommand.bat");

        // Verificar si el archivo batch existe
        if (!batchFile.exists()) {
            System.out.println("El archivo adbcommand.bat no existe. Creando el archivo...");
             batchFile.getAbsolutePath();
            // Llamar a la función para crear el archivo batch
            createBatchForInfoCommands();
            
            return checkAndCreateCommandFile();           
        }
         return  batchFile.getAbsolutePath();
    }

 
    public static void createBatchForInfoCommandsUninstall() {
        // Obtén el directorio de ejecución del programa y crea las carpetas necesarias
        Path currentPath = Paths.get("").toAbsolutePath();
        File binariesDir = new File(currentPath.toFile(), "Binaries/bin");

        if (!binariesDir.exists() && !binariesDir.mkdirs()) {
            System.err.println("No se pudo crear la carpeta Binaries/bin.");
            return;
        }

        // Ruta al archivo batch
        File batchFile = new File(binariesDir, "adbcommandUninstall.bat");

        // Contenido del archivo batch
        String batchContent = """
            @echo off
        
            rem Asigna el primer parámetro pasado al batch como la variable DIR_PATH
           
            adb.exe wait-for-device                           
            echo Configurando para desintalacion, Por favor espere...
            echo no desconecte el equipo
            adb.exe shell pm uninstall com.google.android.gsf.login
            adb.exe shell pm uninstall com.google.android.gsf
            adb.exe shell pm uninstall com.google.android.gms
            echo desintalando Google play Store
            adb.exe shell pm uninstall com.android.vending
            echo reinciando..
            adb.exe reboot
            echo espere...
            adb.exe wait-for-device
            echo Reinicio exitoso!!
                              
            """;

        // Escribe el contenido en el archivo batch
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(batchFile))) {
            writer.write(batchContent);
            System.out.println("El archivo batch se ha creado correctamente en: " + batchFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error al crear el archivo batch: " + e.getMessage());
        }
    }
    
    public static String checkAndCreateCommandFileUninstall() {
        Path currentPath = Paths.get("").toAbsolutePath();
        File batchFile = new File(currentPath.toFile(), "Binaries/bin/adbcommandUninstall.bat");

        // Verificar si el archivo batch existe
        if (!batchFile.exists()) {
            System.out.println("El archivo adbcommand.bat no existe. Creando el archivo...");
             batchFile.getAbsolutePath();
            // Llamar a la función para crear el archivo batch
            createBatchForInfoCommandsUninstall();
            
            return checkAndCreateCommandFileUninstall();           
        }
         return  batchFile.getAbsolutePath();
    }

}

class Simplificando{
    
      public static String checkAndCreateBatchFile() {
        Path currentPath = Paths.get("").toAbsolutePath();
        File batchFile = new File(currentPath.toFile(), "Binaries/bin/simple.bat");

        // Verificar si el archivo batch existe
        if (!batchFile.exists()) {
            System.out.println("El archivo simple.bat no existe. Creando el archivo...");
             batchFile.getAbsolutePath();
            // Llamar a la función para crear el archivo batch
            Simplificando.createBatchFile();
            
            return Simplificando.checkAndCreateBatchFile();           
        }
         return  batchFile.getAbsolutePath();
    }
    public static void createBatchFile() {
        // Obtén el directorio de ejecución del programa y crea las carpetas necesarias
        Path currentPath = Paths.get("").toAbsolutePath();
        File binariesDir = new File(currentPath.toFile(), "Binaries/bin");

        if (!binariesDir.exists() && !binariesDir.mkdirs()) {
            System.err.println("No se pudo crear la carpeta Binaries/bin.");
            return;
        }

        // Ruta al archivo batch
        File batchFile = new File(binariesDir, "simple.bat");

        // Contenido del archivo batch
        String batchContent = """
     @echo off
     setlocal enabledelayedexpansion
     
     rem Asigna el primer parámetro pasado al batch como la variable DIR_PATH
     set "DIR_PATH=%~1"
     set "mivar=%DIR_PATH%"
     
     echo ****************************************************************************************
     echo Waiting...
     adb.exe wait-for-device   
     
     echo preprando instalacion .............
           
        for %%f in ("!mivar!\\*.apk") do (
            echo Instalando  ................
            adb.exe install "%%f"
        )
     
     echo ****************************************************************************************
     echo instalacion completa... 
     echo reiniciando, favor espere....
     adb.exe reboot
     echo no desconectes la tablet...
     adb.exe wait-for-device
     echo completado!
     endlocal
            """;

        // Escribe el contenido en el archivo batch
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(batchFile))) {
            writer.write(batchContent);
            System.out.println("El archivo batch se ha creado correctamente en: " + batchFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error al crear el archivo batch: " + e.getMessage());
        }
    }
    
    
}