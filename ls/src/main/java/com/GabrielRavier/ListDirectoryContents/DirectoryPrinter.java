package com.GabrielRavier.ListDirectoryContents;

public class DirectoryPrinter {
	public static boolean printNames = true;
	private static boolean isFirstDirectory = true;
	
	private static long prepareOnePath(String containingDirectoryName, String nextPathString)
	{
		long blocks = 0;
		
		if (!FileIgnoreManager.isIgnored(nextPathString))
			blocks = CurrentDirectoryFilesManager.addFile(nextPathString, false, containingDirectoryName);
		return blocks;
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

		long totalBlocks = 0;
		totalBlocks += DirectoryPrinter.prepareOnePath(name, ".");
		totalBlocks += DirectoryPrinter.prepareOnePath(name, "..");
		for (var nextPath : directory)
			totalBlocks += DirectoryPrinter.prepareOnePath(name, nextPath.getFileName().toString());
		
		try {
			directory.close();
		} catch (java.io.IOException exception) {
			ErrorManager.printFileError(isCommandLineArgument, "closing directory %s", name);
		}
		
		CurrentDirectoryFilesManager.sort();
		
		if (ArgumentsParser.formatMode == ArgumentsParser.FormatMode.LONG) {
			var output = BlockSizeManager.makeHumanReadableString(totalBlocks, 512, 1024);
			System.out.print("total " + output + '\n');
		}
		
		if (CurrentDirectoryFilesManager.fileCount != 0)
			CurrentDirectoryFilesManager.print();
	}
}
