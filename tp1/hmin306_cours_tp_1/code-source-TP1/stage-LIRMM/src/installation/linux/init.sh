#!/bin/bash
echo "creation du daemon consommation dans /etc/systemd/system";
cp -r /etc/systemd /etc/systemd_old;
cp consommation.service /etc/systemd/system/consommation.service;
echo "creation des dossier de log dans /etc/consommation";
mkdir /etc/consommation;
mkdir /etc/consommation/collect;
mkdir /etc/consommation/collect/data;
touch /etc/consommation/collect/data/config.csv
mkdir /etc/consommation/collect/log;
chmod +rw /etc/consommation/collect/data/config.csv
echo "compilation du binaire pour la consommation";
g++ main.cpp -o joule;
sleep 1;
mv joule /usr/bin/joule;
echo "copie de conso.jar et powerMonitor dans /usr/bin";
cp conso.jar /usr/bin/conso.jar
cp powerMonitor /usr/bin/powerMonitor
echo "creation de l'application powerMonitor";
cp powerMonitor.desktop /usr/share/applications/powerMonitor.desktop
echo "demarage du service";
systemctl enable consommation.service
systemctl start consommation.service


