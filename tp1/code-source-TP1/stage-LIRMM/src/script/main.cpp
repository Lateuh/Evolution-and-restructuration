#include <iostream>
#include <cerrno>
#include <unistd.h>
#include <ctime>
#include <sys/types.h>
#include <sys/stat.h> 
#include <exception>
#include <fstream>
#include <chrono>
#include <thread>
#include <stdio.h>
#include <string.h>
#include <regex>

using namespace std;
//recupere la consoFix depuis le fichier config.csv
string getConsoFix(){
    string configFileName = "/etc/consommation/collect/data/config.csv";
    string res = "";
    ifstream file(configFileName);
    if (file.is_open()){
        string line;

        regex const extractName { "(.*),"};
        regex const extractConso { ",(.*)"};

        smatch match;


        while (getline(file,line))
        {
            res+=",";

            if(regex_search(line, match ,extractName))
            {
                res += match[1];
            } else{
                return "";
            }


            res+=",";

            if(regex_search(line, match ,extractConso))
            {
                res += match[1];
            } else{
                return "";
            }
        }

        file.close();
    }

    return res;
}


int main(){
  
  //verifie droit root
  if (geteuid()!=0)
    {
      cerr<<"erreur lancer avec les droits root !"<<endl;
      return 1;
    }
  
  ifstream jouleFile;

  string nomFileLog="";
   time_t now = time(0);


    tm *ltm = localtime(&now);

    //nom fichier log du jour 
    nomFileLog = "/etc/consommation/collect/log/"+
        (ltm->tm_mday<10 ? "0"+to_string(ltm->tm_mday) : to_string(ltm->tm_mday))+"_"+
        (1 + ltm->tm_mon<10 ? "0"+to_string(1 + ltm->tm_mon) : to_string(1 + ltm->tm_mon))+"_"+
        to_string(1900 + ltm->tm_year)+".log";
    ofstream outF(nomFileLog, ios::app);
    if (!outF.is_open()){ //si aucun problème
    cerr<<"impossible d'ouvrir en écriture : "<<nomFileLog<<endl;
    return 3;
  }
  
  long W;
  jouleFile.open("/sys/kernel/debug/dri/0/i915_energy_uJ");//adresss contenant la conso en uJ
  if (jouleFile.is_open())
    {
      string line;
      string::size_type sz; 
      getline(jouleFile, line);
      long j1 = stol(line,&sz),j2;
      jouleFile.close();
      
      while(true){
	this_thread::sleep_for(10s); //10secondes entre chaque mesure
	jouleFile.open("/sys/kernel/debug/dri/0/i915_energy_uJ");
	getline(jouleFile, line); 
	j2 = stol(line,&sz);
	jouleFile.close();
	//cout<<j1<<" "<<j2<<" ";
	W = (j2-j1)/100000;//divise par 10^6 pour Joule et Watt 
	j1=j2;
	time_t timestamp = time(nullptr);
	//cout<<timestamp<<"\t"<<W/20.0<<endl;
	//ecrit la conso dans fichier de sortie
	outF<<timestamp<<",total,"<<W/10<<getConsoFix()<<endl;
	now = time(0);
	ltm = localtime(&now);
	//met les droit de lecture au fichiers pour tous le monde
	if (chmod(nomFileLog.c_str(), S_IRUSR|S_IWUSR|S_IRGRP|S_IWGRP|S_IROTH)==-1)
	  cerr<<"erreur numero : "<<errno<<endl<<strerror(errno)<<endl;
	//verifier qu'on a pas changer de jour
	if (ltm->tm_mday != localtime(&now)->tm_mday)
	  {
	    outF.close();
	    nomFileLog = "/home/florent/git/collect/log/"+
	      (ltm->tm_mday<10 ? "0"+to_string(ltm->tm_mday) : to_string(ltm->tm_mday))+"_"+
	      (1 + ltm->tm_mon<10 ? "0"+to_string(1 + ltm->tm_mon) : to_string(1 + ltm->tm_mon))+"_"+
	      to_string(1900 + ltm->tm_year)+".log";
	    cout<<"changement de jour : "<<nomFileLog;
	    outF.open(nomFileLog, ios::app);
	  }
	    
      }
    }
  else {
    cerr<<"impossible d'ouvrire le fichier i915_energy_uJ";
    return 2;
  }
  outF.close();
  return 0;
}
