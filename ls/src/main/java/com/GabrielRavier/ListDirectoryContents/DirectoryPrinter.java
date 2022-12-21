package com.GabrielRavier.ListDirectoryContents;

public class DirectoryPrinter {
	public static boolean printNames = true;
	private static boolean isFirstDirectory = true;
	
	private static void prepareOnePath(String containingDirectoryName, String nextPathString)
	{
		if (!FileIgnoreManager.isIgnored(nextPathString))
			CurrentDirectoryFilesManager.addFile(nextPathString, false, containingDirectoryName);
	}
	
	public static void print(String name, boolean isCommandLineArgument)
	{
		java.nio.file.DirectoryStream<java.nio.file.Path> directory;
		try {
			directory = java.nio.file.Files.newDirectoryStream(java.nio.file.Paths.get(name));
		} catch (java.io.IOException exception) {
			ErrorManager.printFileError(isCommandLineArgument, "cannot open directory %s", name);
			return;
		}
		
		CurrentDirectoryFilesManager.clear();
		
		if (DirectoryPrinter.printNames) {
			if (!DirectoryPrinter.isFirstDirectory)
				System.out.println();
			DirectoryPrinter.isFirstDirectory = false;
			System.out.print(name);
			System.out.println(":");
		}

		DirectoryPrinter.prepareOnePath(name, ".");
		DirectoryPrinter.prepareOnePath(name, "..");
		for (var nextPath : directory)
			DirectoryPrinter.prepareOnePath(name, nextPath.getFileName().toString());
		
		try {
			directory.close();
		} catch (java.io.IOException exception) {
			ErrorManager.printFileError(isCommandLineArgument, "closing directory %s", name);
		}
		
		CurrentDirectoryFilesManager.sort();
		
		if (CurrentDirectoryFilesManager.fileCount != 0)
			CurrentDirectoryFilesManager.print();
	}
}
