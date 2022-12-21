package com.GabrielRavier.ListDirectoryContents;

public class DirectoryPrinter {
	public static boolean printNames = true;
	private static boolean isFirstDirectory = true;
	
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
		
		for (var nextPath : directory) {
			String nextPathString = nextPath.getFileName().toString();
			if (!FileIgnoreManager.isIgnored(nextPathString)) {
				CurrentDirectoryFilesManager.addFile(nextPathString, false, name);
			}
		}
		
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
