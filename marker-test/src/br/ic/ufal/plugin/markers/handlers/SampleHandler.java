package br.ic.ufal.plugin.markers.handlers;

import java.util.Scanner;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;

public class SampleHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		createMarkerAtMethod("myPublicMethod", "removeField", "Farinha");

		/*
		 * IStructuredSelection selection = (IStructuredSelection) HandlerUtil
		 * .getActiveSite(event).getSelectionProvider().getSelection(); if
		 * (selection == null) { return null; }
		 * 
		 * 
		 * Object firstElement = selection.getFirstElement();
		 * System.out.println("First Element: " + firstElement); if
		 * (firstElement instanceof IJavaProject) { IJavaProject type =
		 * (IJavaProject) firstElement; System.out.println("Java Project: " +
		 * type.toString()); writeMarkers(type);
		 * 
		 * }
		 */
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
							// System.out.println("\n" + "Pacote: " +
							// mypackage.getElementName());
							for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
								IType[] allTypes = unit.getAllTypes();
								for (IType type : allTypes) {

									for (IJavaElement ije : type.getChildren()) {
										// System.out.println(ije.getElementName());
										if (ije.getElementType() == IJavaElement.METHOD) {
											// writeMarkersAtLine(type, 2);
											IMethod m = (IMethod) ije;

											// System.out.println(m.getElementName());
											// System.out.println(m.getParameters().length);
											// System.out.println(m.getCompilationUnit().getSource());
											// System.out.println(m.getSource());
											// System.out.println(m.getCompilationUnit().getSource());

											// System.out.println(m.getCompilationUnit().getChildren().length);

											for (IJavaElement mC : m.getChildren()) {
												// System.out.println(mC.getElementName());
											}
										} else if (ije.getElementType() == IJavaElement.FIELD) {
											// System.out.println(ije.getElementName());
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
		return null;
	}

	/**
	 * Cria o QuickFix
	 * 
	 * @param type
	 */
	private void writeMarkersAtLine(IType type, Refactor r, String message, int line) {
		try {

			IResource resource = type.getUnderlyingResource();
			// Setando os atributos para transformar um IMarker em um QuickFix,
			// QuickFix é um Marker
			IMarker marker = resource.createMarker(IMarker.PROBLEM);
			marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
			// Mensagem que aparece ao colocar o ponteiro do mouse no QuickFix
			marker.setAttribute(IMarker.MESSAGE, message);
			// Linha da classes em que aparece o QuickFix
			marker.setAttribute(IMarker.LINE_NUMBER, line);

			marker.setAttribute("Class", r.getClassToRefactor());
			marker.setAttribute("Method", r.getMethodToRefactor());
			marker.setAttribute("Field", r.getFieldToRefactor());

			if (r.isClazz()) {
				marker.setAttribute("Description", "CLASS");
			} else if (r.isMethod()) {
				marker.setAttribute("Description", "METHOD");
			} else if (r.isField()) {
				marker.setAttribute("Description", "FIELD");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createMarkerAtMethod(final String sourceMethod, String description, String message) {

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
									//String[] lines = type.getCompilationUnit().getSource()
										//	.split(System.getProperty("line.separator"));
									for (final IJavaElement ije : type.getChildren()) {
										if (ije.getElementType() == IJavaElement.METHOD) {
											IMethod m = (IMethod) ije;
											if (sourceMethod.contains(m.getElementName())) {
												int l = 0;

											/*	for (int i = 0; i < lines.length; i++) {
													if (lines[i].contains(m.getElementName())) {
														l = i;
														l++;
														break;
													}
												}*/
												
												Scanner scanner = new Scanner(type.getCompilationUnit().getSource());
												while (scanner.hasNextLine()) {
												  String line = scanner.nextLine();
												  l++;
												  if(line.contains(m.getElementName())){
													  break;
												  }
												  // process the line
												}
												scanner.close();
												
												if (l != 0) {
													Refactor r = new Refactor(ije.getElementName(), m.getElementName(),
															"");
													r.setMethod(true);
													writeMarkersAtLine(type, r, message, l);
													break;
												}
												break;
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

	}

}