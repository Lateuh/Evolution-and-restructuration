package analyse;

import java.util.Comparator;

import spoon.reflect.declaration.CtClass;

public class SortByNbAtt implements Comparator<CtClass>{

	// de la classe avec le plus de attributs à la classe avec le moins d'attributs
		public int compare(CtClass a, CtClass b) { 
		        return b.getFields().size() - a.getFields().size(); 
		    }
}
