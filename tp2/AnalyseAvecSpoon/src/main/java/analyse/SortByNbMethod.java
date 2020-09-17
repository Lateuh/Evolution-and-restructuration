package analyse;

import java.util.Comparator;

import spoon.reflect.declaration.CtClass;

public class SortByNbMethod implements Comparator<CtClass>{
// de la classe avec le plus de méthodes à la classe avec le moins de méthodes
	public int compare(CtClass a, CtClass b) { 
	        return b.getMethods().size() - a.getMethods().size(); 
	    } 
} 
	
