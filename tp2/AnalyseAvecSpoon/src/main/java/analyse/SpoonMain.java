package analyse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import spoon.Launcher;
import spoon.MavenLauncher;
import spoon.compiler.Environment;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.*;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.visitor.filter.TypeFilter;




public class SpoonMain {

	public static void main(String[] args) {
		
		System.out.println("Begin Analysis");

		// Parsing arguments using JCommander
		Arguments arguments = new Arguments();
		boolean isParsed = arguments.parseArguments(args);

		// if there was a problem parsing the arguments then the program is terminated.
		if(!isParsed)
			return;
		
		// Parsed Arguments
		String experiment_source_code = arguments.getSource();
		String experiment_output_filepath = arguments.getTarget();
		
		// Load project (APP_SOURCE only, no TEST_SOURCE for now)
		Launcher launcher = null;
//		if(arguments.isMavenProject() ) {
//			launcher = new MavenLauncher(experiment_source_code, MavenLauncher.SOURCE_TYPE.APP_SOURCE); // requires M2_HOME environment variable
//		}else {
//			launcher = new Launcher();
//			launcher.addInputResource(experiment_source_code + "/src");
//		}
		
		launcher = new Launcher();
		launcher.addInputResource(experiment_source_code + "/src");
		
		// Setting the environment for Spoon
		Environment environment = launcher.getEnvironment();
		environment.setCommentEnabled(true); // represent the comments from the source code in the AST
		environment.setAutoImports(true); // add the imports dynamically based on the typeReferences inside the AST nodes.
//		environment.setComplianceLevel(0); // sets the java compliance level.
		
		System.out.println("Run Launcher and fetch model.");
		launcher.run(); // creates model of project
		CtModel model = launcher.getModel(); // returns the model of the project
		
		

		// basic type filter to retrive all methods in your model
		List<CtMethod> methodList = model.getElements(new TypeFilter<CtMethod>(CtMethod.class));
		/* Liste de toutes les méthodes
		 * for(CtMethod method : methodList) {
			System.out.println("method: " + method.getSimpleName());
		}*/
		
		
		
		
		System.out.println("Il y a "+ methodList.size()+ " méthodes dans le projet");
		
		
		List<CtClass> classList = model.getElements(new TypeFilter<CtClass>(CtClass.class));
		System.out.println("Il y a "+ classList.size() +" classes dans le projet");
		
		
		List<CtImport> importList = model.getElements(new TypeFilter<CtImport>(CtImport.class));
		System.out.println("Il y a "+ importList.size()+ " import dans le projet");
		
		
		System.out.println("Il y a " + ( classList.toString().split("\n").length + importList.size() ) + " lignes de code dans le projet");
		
		
		List<CtPackage> packList = model.getElements(new TypeFilter<CtPackage>(CtPackage.class));
		System.out.println("Il y a "+ packList.size() +" packages dans le projet");
		
		
		System.out.println("Il y a "+ ( (float)methodList.size() / (float)classList.size() ) +" méthodes par classe dans le projet");
		
		
		System.out.println("Il y a " + ( (float)methodList.toString().split("\n").length / (float)methodList.size() ) + " lignes de code par méthode dans le projet");
		
		
		
		int nbAtt = 0;
		for(CtClass classs : classList) {
			nbAtt += classs.getFields().size();
		}
		System.out.println("Il y a " + nbAtt + " attributs au total");
		System.out.println("Il y a " + ( (float)nbAtt / (float)classList.size() ) + " attributs par classe");
		
		
		
		ArrayList<CtClass> classListSorted = new ArrayList<CtClass>(classList);	
		Collections.sort(classListSorted, new SortByNbMethod());
		System.out.println("Liste des classes avec le plus grand nombre de méthodes par ordre décroissant : ");
		for (int i=0; i < (0.1*classListSorted.size()); i++) System.out.println((i+1) + " : " + classListSorted.get(i).getSimpleName() + " avec " + classListSorted.get(i).getMethods().size() + " méthodes"); 
		


		ArrayList<CtClass> classListSorted2 = new ArrayList<CtClass>(classList);	
		Collections.sort(classListSorted2, new SortByNbAtt());
		System.out.println("Liste des classes avec le plus grand nombre d'attributs par ordre décroissant : ");
		for (int i=0; i < (0.1*classListSorted2.size()); i++) System.out.println((i+1) + " : " + classListSorted2.get(i).getSimpleName() + " avec " + classListSorted2.get(i).getFields().size() + " attributs");
	
		
		
		
		System.out.println("Dans les classements précédents on peut retrouver des classes en commun, les voici :");
		for(int i = 0;i<(0.1*classListSorted.size());i++) {
			for(int j = 0;j<(0.1*classListSorted2.size());j++) {
				if(classListSorted.get(i).getSimpleName().equals(classListSorted2.get(j).getSimpleName())) {
					System.out.println(classListSorted.get(i).getSimpleName());
				}
			}
		}
		
		
		
		System.out.println("Les classes qui ont strictement plus de X méthodes.");
		Scanner sc = new Scanner(System.in);
		System.out.println("Que vaut X ?");
		String nbM = sc.nextLine();
		System.out.println("Ok, voici les classes qui ont strictement plus de "+ nbM + " méthodes :");
		int i = 1;
		for(CtClass classs : classListSorted) {
			if(classs.getMethods().size() > Integer.parseInt(nbM)) {System.out.println(i + " : " + classs.getSimpleName() + " avec " + classs.getMethods().size() + " méthodes."); i++;}
		}
		
		
		


		
		
		
		
		
		
		
		
		
		
		
		String methode = "";
		int nbParamMax = 0;
		for(CtMethod m : methodList) {
			if(m.getParameters().size() > nbParamMax) {
				nbParamMax = m.getParameters().size();
				methode = m.getSimpleName();
			}
		}
		System.out.println("Il y a " + nbParamMax + " paramètres dans la méthode " + methode);
		
		
		
		
	}
}
