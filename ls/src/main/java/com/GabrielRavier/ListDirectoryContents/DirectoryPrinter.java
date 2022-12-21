package com.GabrielRavier.ListDirectoryContents;

public class DirectoryPrinter {
	public static boolean printNames = true;
	
	public static void print(String name, boolean isCommandLineArgument)
	{
		java.nio.file.DirectoryStream<java.nio.file.Path> directory;
		try {
			directory = java.nio.file.Files.newDirectoryStream(java.nio.file.Paths.get(name));
		} catch (java.io.IOException exception) {
			ErrorManager.printFileError(exception, isCommandLineArgument, "cannot open directory %s", name);
			return;
		}
		
		if (DirectoryPrinter.printNames) {
			System.err.println("TODO: printNames");
		}
		
		for (var nextPath : directory) {
			String nextPathString = nextPath.getFileName().toString();
			if (!FileIgnoreManager.isIgnored(nextPathString)) {
				CurrentDirectoryFilesManager.addFile(nextPathString, false, name);
			}
		}
		
		try {
			directory.close();
		} catch (java.io.IOException exception) {
			ErrorManager.printFileError(exception, isCommandLineArgument, "closing directory %s", name);
		}
		
		CurrentDirectoryFilesManager.sort();
		
		if (CurrentDirectoryFilesManager.isNonEmpty())
			CurrentDirectoryFilesManager.print();
	}
}
