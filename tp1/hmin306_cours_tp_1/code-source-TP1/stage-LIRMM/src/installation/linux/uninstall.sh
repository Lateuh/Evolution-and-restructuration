#!/bin/bash
echo "arret du service"
systemctl disable consommation.service;
systemctl stop consommation.service;
echo "suppression du fichier du service /etc/systemd/system/consommation.service";
rm /etc/systemd/system/consommation.service;
echo "voulez-vous supprimer les fichiers de log (y/n)";
read supprimer;
while [supprimer!="y"] || [supprimer!="n"]
do
    echo "y/n";
    read supprimer;
done
if [supprimer=="y"]
then
    echo "suppression des fichiers de log";
    rm -r /etc/consommation
fi

rm /usr/bin/joule;
echo "sppression des binaires et de conso.jar dans /usr/bin";
rm /usr/bin/conso.jar
rm /usr/bin/powerMonitor
echo "suppression de l'application Power Monitor";
rm /usr/share/applications/powerMonitor.desktop
