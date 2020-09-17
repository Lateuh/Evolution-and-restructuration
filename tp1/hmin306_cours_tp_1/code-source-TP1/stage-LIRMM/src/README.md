Dévellopé par Sylvain, Esteban et Florent
Lanagage utilisé : c++/bash pour script système et java avec swing pour interfacegraphique
Conseil pour dévelloper : utiliser Eclipse avec Egit

--------------------------------------------------------------------------------

Installation : contient les scripts c++ et les fichiers d'installations respectivement sous Linux et Windows
script : contient d'autres script qui peuvent être utile
MainPackage : contient toute les classes java qui ont permis l'interface graphique
test : contient le main du graphique Java

--------------------------------------------------------------------------------

pour linux ne pas oublier de changer :
1. Dans Test.java :
l10 : Controller control = new Controller(true);

2. Dans Controller.java :
l18 : private static String collectFolderPath = "/etc/consommation/";

3. Dans Day.java :
l22 : private static boolean linux=true;

Bon courage !
