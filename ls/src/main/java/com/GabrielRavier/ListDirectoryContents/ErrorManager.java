package com.GabrielRavier.ListDirectoryContents;

public class ErrorManager {
	final static int exitStatusMinorProblem = 1;
	final static int exitStatusFailure = 2;
	static int exitStatus;
	
	public static void setExitStatus(boolean isSerious)
	{
		if (isSerious)
			ErrorManager.exitStatus = ErrorManager.exitStatusFailure;
		else if (ErrorManager.exitStatus == 0)
			ErrorManager.exitStatus = ErrorManager.exitStatusMinorProblem;
	}
	
	public static void printFileError(boolean isSerious, String message, String fileName)
	{
		System.out.flush();
		System.err.print(System.getProperty("sun.java.command"));
		System.err.print(": ");
		System.err.printf(message, fileName);
		System.err.println();
		ErrorManager.setExitStatus(isSerious);
	}
}
