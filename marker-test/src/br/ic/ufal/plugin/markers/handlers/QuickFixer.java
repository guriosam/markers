package br.ic.ufal.plugin.markers.handlers;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;

public class QuickFixer implements IMarkerResolutionGenerator {

	@Override
	public IMarkerResolution[] getResolutions(IMarker mk) {
		String refactor = mk.getAttribute("Description", "");

		String className = mk.getAttribute("Class", "");
		String methodName = mk.getAttribute("Method", "");
		String fieldName = mk.getAttribute("Field", "");

		if (refactor == null) {
			return null;
		}

		if (refactor.equals("CLASS")) {
			return new IMarkerResolution[] { new QuickFix("Fix #1 for Clazz", className), };
		} else if (refactor.equals("METHOD")) {

			return new IMarkerResolution[] { new QuickFix("Remove Unused Method", className, methodName), };
		} else if (refactor.equals("FIELD")) {
			return new IMarkerResolution[] { new QuickFix("Fix #1 for Field", className, methodName, fieldName), };
		}
		return new IMarkerResolution[] {};
	}

}
