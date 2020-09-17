#include <iostream>
#include <fstream>
#include <string>
#include <regex>
#include <stdexcept>
#include <stdio.h>
#include <locale>
#include <codecvt>
#include <sstream>
#include <Windows.h>
#include <chrono>
#include <thread>
#include <ctime>

using namespace std;

string getCpuName();
string getGpuName();
float getGpuWatt();
string getCpuPercentage();
float getCpuWatt(int);
int getCpuMaxConso();
void exportConfig();
void exportConso(int cpuMaxWatt,int * previusGpuWatt);
bool is_readable(const string &file);
void determineCpuMaxConso();
string getConsoFix();
float getConsoFixInt();

int main() {
    //HWND hWnd = GetConsoleWindow();
    //ShowWindow( hWnd, SW_HIDE );

    determineCpuMaxConso();

    int cpuMaxWatt = getCpuMaxConso();
    int timeGap = 10;

    int previousGpuWatt = 0;

    
    exportConfig();

    while(true){
        exportConso(cpuMaxWatt,& previousGpuWatt);
        Sleep(timeGap*1000);
    }

    return 0;   
}



string getCpuName(){
    string line;
    string res;
    smatch match;
    regex patern {"Name=(.*\\b)"};


    system("wmic cpu get name /value>vrac\\source.txt");
    system("powershell.exe \"Get-Content vrac\\source.txt -Encoding Unicode | Set-Content -Encoding UTF8 vrac\\destination.txt\"");

    

    ifstream file("vrac\\destination.txt");

    while(getline(file,line)){
        
        if (regex_search(line, match, patern)){
            res = match[1];
        }
    }
    file.close();

    return res;
}

string getGpuName(){
    string line;
    string res;
    smatch match;
    regex patern {"Name=(.*\\b)"};


    system("wmic path Win32_VideoController get Name /value>vrac\\source.txt");
    system("powershell.exe \"Get-Content vrac\\source.txt -Encoding Unicode | Set-Content -Encoding UTF8 vrac\\destination.txt\"");

    

    ifstream file("vrac/vrac\\destination.txt");

    while(getline(file,line)){
        
        if (regex_search(line, match, patern)){
            res = match[1];
        }
    }
    file.close();

    return res;
}

float getGpuWatt(int * previousGpuWatt){
    string line;
    string res;
    smatch match;
    regex patern {"(.*) W"};

    if (is_readable("vrac\\wattsgpu.txt")){

        ifstream file("vrac\\wattsgpu.txt");

        while(getline(file,line)){

            if (regex_search(line, match, patern)){
                res = match[1];
            }
        }
        float resFinal = stof(res);
        file.close();
        *previousGpuWatt = resFinal;
        return resFinal;

    } else{
        return (*previousGpuWatt);
    }

    
}

string getCpuPercentage() {
    string percentage;

    system("wmic cpu get loadpercentage > vrac\\source.txt");
    system("powershell.exe \"Get-Content vrac\\source.txt -Encoding Unicode | Set-Content -Encoding UTF8 vrac\\destination.txt\"");
    ifstream cpuPercentageFile("vrac\\destination.txt");

    if (cpuPercentageFile.is_open())
    {
        string line;
        while (getline(cpuPercentageFile,line))
        {
            regex const patternCpuPercentage { "[0-9]\\b"};
            if (regex_search(line, patternCpuPercentage))
                percentage=line;  
        }
    }
    else {
        cerr<< "Impossible d'ouvrir le fichier !" << endl;
    }
    cpuPercentageFile.close();

    return percentage;
}

float getCpuWatt(int maxconso){
    string strPercentage = getCpuPercentage();
    int intPercentage = stoi(strPercentage);
    float conso = (intPercentage*maxconso) / 100.0;
    return conso;
}

int getCpuMaxConso(){
    string line = "0";

    ifstream file("vrac\\cpuconso.txt");
    getline(file,line);
    int res = stoi(line);
    file.close();

    return res;
}

void exportConfig(){
    ofstream file("vrac\\info.conf");
    file<<"cpu:"<<getCpuName()<<endl;
    file<<"gpu:"<<getGpuName();

    file.close();
}

void exportConso(int cpuMaxWatt,int* previousGpuWatt){
    string nom = "";

    time_t now = time(0);


    tm *ltm = localtime(&now);


    nom = "log\\"+
        (ltm->tm_mday<10 ? "0"+to_string(ltm->tm_mday) : to_string(ltm->tm_mday))+"_"+
        (1 + ltm->tm_mon<10 ? "0"+to_string(1 + ltm->tm_mon) : to_string(1 + ltm->tm_mon))+"_"+
        to_string(1900 + ltm->tm_year)+".log";


    ofstream file(nom,ios::app);
    file<<time(nullptr)<<",total,"<<getCpuWatt(cpuMaxWatt)+getGpuWatt(previousGpuWatt)+getConsoFixInt()<<",cpu,"<<getCpuWatt(cpuMaxWatt)<<",gpu,"<<getGpuWatt(previousGpuWatt)<<getConsoFix()<<endl;
    file.close();
}



bool is_readable(const string &file) //indique si un fichier est lisible (et donc si il existe)
{
  ifstream fichier(file.c_str());
  bool res = !fichier.fail();
  fichier.close();
  return res;
}


void determineCpuMaxConso(){

    if (! is_readable("vrac\\cpuconso.txt")){
        ofstream conso("vrac\\cpuconso.txt");
        ifstream dataBase("data\\bddCPU.txt");

        string nom = getCpuName();
        string line;
        string customReg;
        string res;

        regex const extractRegex { "(.*)="};
        regex const extractConso { "=(.*)"};

        smatch match;

        while (getline(dataBase,line))
        {

            if(regex_search(line, match ,extractRegex))
            {
                customReg = match[1];

                regex const verify {customReg};

                if (regex_search(nom,match, verify)){
                    regex_search(line, match ,extractConso);
                    cout<<"Nom du processeur donne par windows : "<<nom<<endl
                    <<"Corespondance dans la base de donnee : "<<line<<endl
                    <<"Consommation deduite : "<<match[1]<<endl;
                    res = match[1];
                    
                }
            }
        }

        conso<<res; 
        conso.close();
        dataBase.close();
    }
}


string getConsoFix(){
    string configFileName = "data/config.csv";
    string res = "";

    if (is_readable(configFileName)){

        ifstream file(configFileName);
        
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

float getConsoFixInt(){
    string configFileName = "data/config.csv";
    float res = 0;

    if (is_readable(configFileName)){

        ifstream file(configFileName);
        
        string line;

        regex const extractConso { ",(.*)"};

        smatch match;


        while (getline(file,line))
        {

            if(regex_search(line, match ,extractConso))
            {
                res += stof(match[1]);
            }
            
        }

        file.close();
    }

    return res;
}