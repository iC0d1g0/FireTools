/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clone.logica_installador;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author elcue
 */
public class AntesInstalar {
     public void mergeFiles(String outputFilePath, String partPrefix) throws IOException {
        File outputFile = new File(outputFilePath);
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            int partNumber = 1;
            File partFile;
            while ((partFile = new File(partPrefix + ".part" + partNumber)).exists()) {
                try (FileInputStream fis = new FileInputStream(partFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) > 0) {
                        fos.write(buffer, 0, bytesRead);
                    }
                }
                System.out.println("Unido: " + partFile.getName());
                partNumber++;
            }
        }
        System.out.println("Archivo restaurado correctamente: " + outputFile.getName());
    }
     public void unzipFile(String zipFilePath) throws IOException {
        
        Path zipPath = Paths.get(zipFilePath);
        Path outputDir = zipPath.getParent();
        
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Path entryPath = outputDir.resolve(entry.getName());
                
                if (entry.isDirectory()) {
                    Files.createDirectories(entryPath);
                } else {
                    Files.createDirectories(entryPath.getParent());
                    try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(entryPath.toFile()))) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = zis.read(buffer)) != -1) {
                            bos.write(buffer, 0, bytesRead);
                        }
                    }
                }
                zis.closeEntry();
            }
         
        }
        File zipFile = new File(zipFilePath);
        if (zipFile.exists()) {
            try {
                Files.delete(zipFile.toPath()); // Borra el archivo ZIP
                System.out.println("Archivo ZIP eliminado: " + zipFilePath);
            } catch (IOException e) {
                System.err.println("Error al eliminar el archivo ZIP: " + e.getMessage());
            }
        } else {
            System.out.println("El archivo ZIP no existe.");
        }
    
    }
    
      public boolean existeG(){
          Path currentPath = Paths.get("").toAbsolutePath();
          File gDir = new File(currentPath.toFile(), "Binaries/g");
          File ideDir = new File(currentPath.toFile(), "Binaries/ide");
          if(!gDir.exists() && ideDir.exists()){
             try {
                 gDir.mkdirs();
                  System.out.println("Resultados: !gDir.exists() = " + !gDir.exists());
                  System.out.println("Resultados: ideDir.exists() = " + ideDir.exists());
                  mergeFiles(gDir.getPath()+"/g.zip", ideDir+"/g.zip");
                  unzipFile(gDir.getPath()+"/g.zip");
                  
                  
                  
                  
                } catch (IOException e) {
                    e.printStackTrace();
                }
          }else if(!ideDir.exists() && !gDir.exists()){
              
              System.out.println("ubo una alteracion en las carpetas ide y g");
              return false;
          }
          return true;
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

 
}
