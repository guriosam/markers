package mypackage;


public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String s = "public class Refactor { "
				+ " private boolean field;"
				+ "private boolean method;"
				+ "private boolean clazz;"
				+ "private String classToRefactor;"
				+ "private String methodToRefactor;"
				+ "private String fieldToRefactor;"
				+ "public Refactor(String classToRefactor, String methodToRefactor, String fieldToRefactor) {"
				+ "super(); "
				+ "this.classToRefactor = classToRefactor;"
				+ "this.methodToRefactor = methodToRefactor;"
				+ "this.fieldToRefactor = fieldToRefactor;"
				+ "field = false;"
				+ "method = false;"
				+ "clazz = false;"
				+ "}"
				+ "public boolean isField() {"
				+ "return field;"
				+ "}"
				+ "public void setField(boolean field) {"
				+ "this.field = field;"
				+ "}"
				+ "}";
		
		BalenParenteses b = new BalenParenteses();
		b.verificarParen(s);

	}

}
