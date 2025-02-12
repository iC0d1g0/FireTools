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
       echo Waiting for youd device... please enable debuging options under developer options.
            adb.exe wait-for-device   
       echo device found.. 
       echo attempting to install ...                                 
            adb.exe install "%DIR_PATH%"  
       echo installation completed.. 
       echo please, confirmed if not install try again.
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
            rem adb.exe wait-for-device                           
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

 
}
