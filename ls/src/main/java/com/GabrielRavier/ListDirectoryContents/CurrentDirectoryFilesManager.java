package com.GabrielRavier.ListDirectoryContents;

public class CurrentDirectoryFilesManager {
	public static java.util.ArrayList<FileInformation> elements = new java.util.ArrayList<FileInformation>();
	public static java.util.ArrayList<Integer> sortedElementIndexes = new java.util.ArrayList<Integer>();
	public static int fileCount = 0;
	
	private static String addUpNamesForFullName(String containingDirectoryName, String fileName)
	{
		String result = "";

		if (!containingDirectoryName.equals(".")) {
			result = containingDirectoryName;
			if (!result.isEmpty() && result.charAt(result.length() - 1) != '/')
				result = result + '/';
		}
		return result + fileName;
	}
	
	public static void addFile(String fileName, boolean isCommandLineArgument, String containingDirectoryName)
	{
		var newElement = new FileInformation();
		newElement.type = FileInformation.Type.unknown;
		
		if (isCommandLineArgument) {
			String fullName;
			
			if (fileName.charAt(0) == '/' || containingDirectoryName.length() == 0)
				fullName = fileName;
			else
				fullName = CurrentDirectoryFilesManager.addUpNamesForFullName(containingDirectoryName, fileName);
			
			var namePath = java.nio.file.Paths.get(fullName);
			
			if (isCommandLineArgument) {
				boolean gotError = false;
				boolean isErrorFileNotFound = false;
				Exception exception = null;
				try {
					newElement.posixAttributes = java.nio.file.Files.readAttributes(namePath, java.nio.file.attribute.PosixFileAttributes.class);
				} catch (java.io.FileNotFoundException caughtException) {
					gotError = true;
					isErrorFileNotFound = true;
					exception = caughtException;
				} catch (java.io.IOException caughtException) {
					gotError = true;
					exception = caughtException;
				}
				
				boolean needToCheckSymlink = (gotError ? isErrorFileNotFound : !newElement.posixAttributes.isDirectory());
				
				if (needToCheckSymlink) {
					try {
						newElement.posixAttributes = java.nio.file.Files.readAttributes(namePath, java.nio.file.attribute.PosixFileAttributes.class, java.nio.file.LinkOption.NOFOLLOW_LINKS);
					} catch (java.io.IOException caughtException) {
						exception = caughtException;
					}
				}
				
				if (exception != null) {
					ErrorManager.printFileError(isCommandLineArgument, "cannot access %s", fileName);
					if (isCommandLineArgument)
						return;
					newElement.name = fileName;
					CurrentDirectoryFilesManager.elements.add(newElement);
					++CurrentDirectoryFilesManager.fileCount;
					return;
				}
				
				newElement.areAttributesOk = true;
				
				if (newElement.posixAttributes.isSymbolicLink())
					newElement.type = FileInformation.Type.symbolicLink;
				else if (newElement.posixAttributes.isDirectory()) {
					if (isCommandLineArgument)
						newElement.type = FileInformation.Type.commandLineArgumentDirectory;
					else
						newElement.type = FileInformation.Type.directory;
				} else
					newElement.type = FileInformation.Type.normal;
			}
		}
		
		newElement.name = fileName;
		CurrentDirectoryFilesManager.elements.add(newElement);
		++CurrentDirectoryFilesManager.fileCount;
	}
	
	public static int compareNames(FileInformation i1, FileInformation i2)
	{
		return java.text.Collator.getInstance().compare(i1.name, i2.name);
	}
	
	private static void initSortedElementIndexes()
	{
		CurrentDirectoryFilesManager.sortedElementIndexes.clear();
		for (int i = 0; i < CurrentDirectoryFilesManager.fileCount; ++i)
			CurrentDirectoryFilesManager.sortedElementIndexes.add(i);
	}
	
	public static void sort()
	{
		CurrentDirectoryFilesManager.initSortedElementIndexes();
		CurrentDirectoryFilesManager.sortedElementIndexes.sort((i1, i2) -> CurrentDirectoryFilesManager.compareNames(CurrentDirectoryFilesManager.elements.get(i1), CurrentDirectoryFilesManager.elements.get(i2)));
	}
	
	public static void printSingleFileName(FileInformation file)
	{
		System.out.print(file.name);
	}
	
	public static void print()
	{
		for (int i = 0; i < CurrentDirectoryFilesManager.fileCount; ++i) {
			CurrentDirectoryFilesManager.printSingleFileName(CurrentDirectoryFilesManager.elements.get(CurrentDirectoryFilesManager.sortedElementIndexes.get(i)));
			System.out.println();
		}
	}
	
	public static void clear()
	{
		CurrentDirectoryFilesManager.elements.clear();
		CurrentDirectoryFilesManager.sortedElementIndexes.clear();
		CurrentDirectoryFilesManager.fileCount = 0;
	}
}
