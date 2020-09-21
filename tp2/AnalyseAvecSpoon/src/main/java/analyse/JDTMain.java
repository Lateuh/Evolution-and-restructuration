package analyse;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.*;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.dom.*;


public class JDTMain {

	public static void main(String[] args) {
        
        ASTParser parser = ASTParser.newParser(AST.JLS3); 
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource("public class A { int i = 9;  \n int j; \n ArrayList<Integer> al = new ArrayList<Integer>();j=1000; }".toCharArray()); // set source
//		parser.setSource(ClassAsString.classAsString.toCharArray()); // set source
		parser.setResolveBindings(true); // we need bindings later on
		CompilationUnit cu = (CompilationUnit) parser.createAST(null /* IProgressMonitor */); // parse
		
		cu.accept(new ASTVisitor() {
			Set names = new HashSet();
			
			public boolean visit(VariableDeclarationFragment node) {
				SimpleName name = node.getName();
				this.names.add(name.getIdentifier());
				System.out.println("Declaration of '"+name+"' at line"+cu.getLineNumber(name.getStartPosition()));
				return false; // do not continue to avoid usage info
			}
			
			
			public boolean visit(SimpleName node) {
				if (this.names.contains(node.getIdentifier())) {
				System.out.println("Usage of '" + node + "' at line " +	cu.getLineNumber(node.getStartPosition()));
				}
				return true;
			}
			
			/*public boolean visit(MethodDeclaration node) {
				node.setReturnType(type);;
				node.setName(new SimpleName(getAge));
				return false;
				
			}*/
			
		});
		
		System.out.println(cu.getParent());
		System.out.println(cu.getRoot());
		System.out.println(cu.getTypeRoot());
		
		cu.getAST().newMethodDeclaration();
		
		
		
		//System.out.println("Fini");
		
	
	}
}
