package github.com.cp149.netty.client;

public class MystackElement {
	// Normally initialized by VM (public constructor added in 1.5)
    private String declaringClass;
    private String methodName;
    private String fileName;
    private int    lineNumber;
	public MystackElement(StackTraceElement stackTraceElement) {
		this.declaringClass=stackTraceElement.getClassName();
		this.methodName=stackTraceElement.getMethodName();
		this.fileName=stackTraceElement.getFileName();
		this.lineNumber=stackTraceElement.getLineNumber();
	}
	public String getDeclaringClass() {
		return declaringClass;
	}
	public void setDeclaringClass(String declaringClass) {
		this.declaringClass = declaringClass;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	

}
