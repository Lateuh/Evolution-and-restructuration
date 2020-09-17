#include <iostream>
#include <dirent.h>
#include <fstream>
#include <sys/types.h>
#include <vector>
#include <sstream>
using namespace std;

vector<string> split(const string& s, char delimiter)
{
   vector<string> tokens;
   string token;
   istringstream tokenStream(s);
   while (getline(tokenStream, token, delimiter))
   {
      tokens.push_back(token);
   }
   return tokens;
}

struct dirent *entry;

struct data{
  int year,month,day;
  unsigned long total, nombre;
};

class TabData
{
private :
  size_t n,max;
  data* tab;

public :
  TabData():n(0),max(5){
    tab=new data[max];
  }
  void realloc(){
    data t[n];
    for (size_t i=0;i<n;i++){
      t[i]=tab[i];
    }
    delete [] tab;
    max*=2;
    tab = new data[max];
    for (size_t i=0;i<n;i++){
      tab[i]=t[i];
    }
  }

  void add(data d){
    if (n==max){
      realloc();
    }
    size_t i=0;
    while (i<n && (d.year>tab[i].year || (d.year==tab[i].year && d.month>tab[i].month ) || (d.year==tab[i].year && d.month==tab[i].month && d.day>=tab[i].day))){
      i++;
    }

    if (i==n){
      tab[n++]=d;
    }
    else {
      data prec=tab[i];
      tab[i]=d;
      n++;
      for (size_t j=n+1;j>i+1;j--){
	tab[j]=tab[j-1];
      }
      tab[i+1]=prec;
    }
  }

  data get(size_t i){
    return tab[i];
  }

  size_t taille(){ return n;}
  
};

  
int main() {
  
  string path="/home/florent/git/collect/log";
  DIR *dir = opendir(path.c_str());
  if (dir == NULL) {
    return 1;
  }
  int cpt=0;
  while ((entry = readdir(dir)) != NULL) {
    cpt++;
  }
  closedir(dir);

  string namesFile[cpt];
  int i=0;
  dir = opendir(path.c_str());
  
  while ((entry = readdir(dir)) != NULL) {
    namesFile[i++]=entry->d_name;
  }
  closedir(dir);

  time_t now = time(0);
  tm *ltm = localtime(&now);
  TabData tab;
  for (i=0;i<cpt;i++){
    data d;
    string name=namesFile[i];
    cout<<name<<endl;
    if (name.length()==14){
      int year=atoi(name.substr(6,10).c_str()),
	month=atoi(name.substr(3,5).c_str()),
	day=atoi(name.substr(0,2).c_str()),
	ltmyear=1900+ltm->tm_year,
	ltmmon=1+ltm->tm_mon,
	ltmday=ltm->tm_mday;
      d.year=year; d.month=month;d.day=day;
      //cout<<name<<" "<<day<<" "<<month<<" "<<year<<endl;
      //cout<<ltmday<<" "<<ltmmon<<" "<<ltmyear<<endl;
      if (ltmyear!=year || month<ltmmon || (month==ltmmon && ltmday-day>3) || (month==ltmmon-1 && ltmday-(31-day)>3))
	{
	  //cout<<"open "<<name<<endl;
	  string line;
	  string pathfile=path+"/"+name;
	  ifstream file(pathfile.c_str());
	  unsigned long total=0,nombre=0;
	  if (file.is_open())
	    {
	      while ( getline (file,line) )
		{
		  vector<string> vect=split(line,','); 
		  //cout<<vect.at(0)<<" ";
		  nombre++;
		  for (unsigned int it=1;it<vect.size();it+=2){
		    //cout<<vect.at(it)<<" "<<vect.at(it+1);
		    total+=atoi(vect.at(it+1).c_str());
		  }
		  //cout<<endl;
		}
	      //cout<<name.substr(0,10)<<" total : "<<total<<" | moyenne : "<<total*1.0/nombre<<" | uptime : "<<nombre*10<<endl;
	      d.total=total/360;d.nombre=nombre;
	      tab.add(d);
	      file.close();	  
	    }
	}
    }
  }
  ofstream outF("/home/florent/git/collect/log/old.log");
  for (size_t i=0;i<tab.taille();i++){
    data d=tab.get(i);
    cout<<(d.day<10?"0":"")<<d.day<<"_"<<(d.month<10?"0":"")<<d.month<<"_"<<d.year<<",total,"<<d.total<<",nombre,"<<d.nombre<<endl;
    outF<<(d.day<10?"0":"")<<d.day<<"_"<<(d.month<10?"0":"")<<d.month<<"_"<<d.year<<",total,"<<d.total<<",nombre,"<<d.nombre<<endl;
  }
  outF.close();
  return 0;
}

