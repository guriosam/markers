package br.ic.ufal.plugin.markers.handlers;

public class Refactor {
	
	private boolean field;
	private boolean method;
	private boolean clazz;
	
	private String classToRefactor;
	private String methodToRefactor;
	private String fieldToRefactor;

	public Refactor(String classToRefactor, String methodToRefactor, String fieldToRefactor) {
		super();
		this.classToRefactor = classToRefactor;
		this.methodToRefactor = methodToRefactor;
		this.fieldToRefactor = fieldToRefactor;
		field = false;
		method = false;
		clazz = false;
	}

	public boolean isField() {
		return field;
	}

	public void setField(boolean field) {
		this.field = field;
	}

	public boolean isMethod() {
		return method;
	}

	public void setMethod(boolean method) {
		this.method = method;
	}

	public boolean isClazz() {
		return clazz;
	}

	public void setClazz(boolean clazz) {
		this.clazz = clazz;
	}

	public String getClassToRefactor() {
		return classToRefactor;
	}

	public void setClassToRefactor(String classToRefactor) {
		this.classToRefactor = classToRefactor;
	}

	public String getMethodToRefactor() {
		return methodToRefactor;
	}

	public void setMethodToRefactor(String methodToRefactor) {
		this.methodToRefactor = methodToRefactor;
	}

	public String getFieldToRefactor() {
		return fieldToRefactor;
	}

	public void setFieldToRefactor(String fieldToRefactor) {
		this.fieldToRefactor = fieldToRefactor;
	}
	
	
	
	

}
