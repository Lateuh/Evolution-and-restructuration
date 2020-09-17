nvidia-smi.exe -i 0 --format=csv,noheader --query-gpu=power.draw>vrac/wattsgpu.txt
if %ERRORLEVEL% neq 0 goto boucle2
goto boucle1



:boucle1
nvidia-smi.exe -i 0 --format=csv,noheader --query-gpu=power.draw>vrac/wattsgpu.txt
timeout /t 10
goto boucle1


:boucle2
"C:\Program Files\NVIDIA Corporation\NVSMI\nvidia-smi.exe" -i 0 --format=csv,noheader --query-gpu=power.draw>vrac/wattsgpu.txt
timeout /t 10
goto boucle2