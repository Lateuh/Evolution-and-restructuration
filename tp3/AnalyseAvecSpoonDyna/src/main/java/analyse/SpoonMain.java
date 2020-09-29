package analyse;

import java.util.List;
import java.util.Set;

import spoon.Launcher;
import spoon.MavenLauncher;
import spoon.compiler.Environment;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCodeSnippetExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
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
		if(arguments.isMavenProject() ) {
			launcher = new MavenLauncher(experiment_source_code, MavenLauncher.SOURCE_TYPE.APP_SOURCE); // requires M2_HOME environment variable
		}else {
			launcher = new Launcher();
			launcher.addInputResource(experiment_source_code + "/src");
		}
		
		// Setting the environment for Spoon
		Environment environment = launcher.getEnvironment();
		environment.setCommentEnabled(true); // represent the comments from the source code in the AST
		environment.setAutoImports(true); // add the imports dynamically based on the typeReferences inside the AST nodes.
//		environment.setComplianceLevel(0); // sets the java compliance level.
		
		System.out.println("Run Launcher and fetch model.");
		launcher.run(); // creates model of project
		CtModel model = launcher.getModel(); // returns the model of the project


		
		
		
		
		
//		List<CtClass> classList = model.getElements(new TypeFilter<CtClass>(CtClass.class));
//
//        for(CtClass cls : classList) {
//            Set<CtMethod> methodesClasse = cls.getMethods();
//            for(CtMethod md : methodesClasse) {
//            	List<CtInvocation> invocList = md.getElements(new TypeFilter<CtInvocation>(CtInvocation.class));
//                CtBlock mdBlock = md.getBody();
//                System.out.println(mdBlock.toString()+"\n==================================================");
//                
//                //CtCodeSnippetExpression instruction =  launcher.getFactory().createCodeSnippetExpression("System.out.println("  GG   ");");
//                CtStatement NewInstruction = launcher.getFactory().Code().createCodeSnippetStatement("analyse.Analyse.printMe(\""+cls.getSimpleName()+"\",\""+md.getSimpleName()+"\")");
//                mdBlock.addStatement(0,NewInstruction);
//                md.setBody(mdBlock);
//
//                CtBlock mdBlock2 = md.getBody();
//                System.out.println(mdBlock2.toString()+"\n=================================================");
//            }
//        }
        
        
        
        
		List<CtClass> classList = model.getElements(new TypeFilter<CtClass>(CtClass.class));
		
		for(CtClass cls : classList) {
			Set<CtMethod> methodesClasse = cls.getMethods();
			for(CtMethod md : methodesClasse) {
				CtBlock mdBlock = md.getBody();
				System.out.println(md.getSimpleName()+"================================");
				
				List < CtInvocation > refs = md.getElements(new TypeFilter < CtInvocation > (CtInvocation.class));
				try {
					for(CtInvocation inv : refs) {
						Boolean cor = false;
						for (CtClass cls2 : classList) {
							if(cls2.getSimpleName().equals(inv.getTarget().getType().toString())) {
	
								
								CtCodeSnippetExpression instruction =  launcher.getFactory().createCodeSnippetExpression("System.out.println(\"  GG   \");");
								CtStatement NewInstruction = launcher.getFactory().Code().createCodeSnippetStatement("analyse.Analyse.printMe(\""+
								inv.getTarget().getType().toString()+"\",\""+cls.getSimpleName()+"\")");
								mdBlock.addStatement(0,NewInstruction);
								md.setBody(mdBlock);
								
								CtBlock mdBlock2 = md.getBody();
								System.out.println(mdBlock2.toString()+"\n=============");
							}
						}
					}
				}catch (Exception e) {
					System.out.println("Null METHODE");
				}
			}
		}
        
		
		launcher.setSourceOutputDirectory("D:\\docs\\cours\\M2\\hmin306\\Evolution-and-restructuration\\tp3\\RestSuite_new");
		launcher.prettyprint();
		System.out.println("THE EEEEEENNNNNNNDDDDDD");
		
		

		
		
		
		
	}
}
