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

