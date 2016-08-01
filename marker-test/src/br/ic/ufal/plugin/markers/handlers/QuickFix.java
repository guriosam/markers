package br.ic.ufal.plugin.markers.handlers;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.WorkingCopyOwner;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.IMarkerResolution;

/**
 * Classe responsável por definir um determinado tipo de QuickFix e o que ele
 * faz, para cada tipo de problema se define um QuickFix
 * 
 * @author anderson
 * 
 */
public class QuickFix implements IMarkerResolution {

	String label;
	String className;
	String methodName;
	String fieldName;

	QuickFix(String label) {
		this.label = label;
	}

	QuickFix(String label, String className) {
		this.label = label;
		this.className = className;
	}

	QuickFix(String label, String className, String methodName) {
		this.label = label;
		this.className = className;
		this.methodName = methodName;
	}

	QuickFix(String label, String className, String methodName, String fieldName) {
		this.label = label;
		this.className = className;
		this.methodName = methodName;
		this.fieldName = fieldName;
	}

	@Override
	public String getLabel() {
		return label;
	}

	/**
	 * Define o que o QuickFix faz
	 */
	@Override
	public void run(IMarker marker) {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();

		IProject[] projects = root.getProjects();

		// Loop para ler todas as classes de todos os projeto do Workspace
		for (IProject project : projects) {
			try {
				if (project.isNatureEnabled("org.eclipse.jdt.core.javanature")) {
					IJavaProject javaProject = JavaCore.create(project);

					IPackageFragment[] packages = javaProject.getPackageFragments();
					for (IPackageFragment mypackage : packages) {
						if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
							for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
								IType[] allTypes = unit.getAllTypes();
								for (IType type : allTypes) {
									String[] lines = type.getCompilationUnit().getSource()
											.split(System.getProperty("line.separator"));
									
									for (IJavaElement ije : type.getChildren()) {
										if (className.equals(ije.getElementName())) {
											if (ije.getElementType() == IJavaElement.METHOD) {
												IMethod m = (IMethod) ije;
												if (methodName.contains(m.getElementName())) {
													ICompilationUnit originalUnit = type.getCompilationUnit();
												    WorkingCopyOwner owner = type.getCompilationUnit().getOwner();
												    ICompilationUnit workingCopy = originalUnit.getWorkingCopy(null);

												    ASTParser parser = ASTParser.newParser(AST.JLS8);
												    parser.setSource(type.getCompilationUnit());
												    parser.setKind(ASTParser.K_COMPILATION_UNIT);
												    parser.setResolveBindings(true);

												    final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
												    
												    final ASTRewrite rewrite = ASTRewrite.create(cu.getAST());
												    
												    
												    try {
												    	Document doc = new Document(originalUnit.getSource());
												    	String d = doc.get();
													    int start = d.indexOf(m.getSource());
													    doc.replace(start, m.getSource().length(), " ");
														cu.recordModifications();
														TextEdit edit = rewrite.rewriteAST(doc, null);
													    edit.apply(doc);	
													    originalUnit.getBuffer().setContents(doc.get());
													    workingCopy.applyTextEdit(edit, null);
													    workingCopy.reconcile(ICompilationUnit.NO_AST, false, null, null);
													    workingCopy.discardWorkingCopy();
													    
													} catch (Exception e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}
												    

												    																										
												}
											}
										}
									}

								}
							}
						}

					}

				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		// MessageDialog.openInformation(null, "QuickFix Demo",
		// "This quick-fix is not yet implemented");
	}

}
