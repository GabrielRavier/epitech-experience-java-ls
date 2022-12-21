package com.GabrielRavier.ListDirectoryContents;

public class CurrentDirectoryFilesManager {
	private static java.util.ArrayList<FileInformation> elements = new java.util.ArrayList<FileInformation>();
	private static java.util.ArrayList<Integer> sortedElementIndexes = new java.util.ArrayList<Integer>();
	
	public static void addFile(String fileName, boolean isCommandLineArgument, String containingDirectoryName)
	{
		var newElement = new FileInformation();
		newElement.type = FileInformation.Type.unknown;
		
		if (isCommandLineArgument) {
			System.err.println("TODO: addFile command line argument");
		}
		
		newElement.name = fileName;
		CurrentDirectoryFilesManager.elements.add(newElement);
	}
	
	public static int compareNames(FileInformation i1, FileInformation i2)
	{
		return java.text.Collator.getInstance().compare(i1.name, i2.name);
	}
	
	public static void sort()
	{
		CurrentDirectoryFilesManager.sortedElementIndexes.clear();
		for (int i = 0; i < CurrentDirectoryFilesManager.elements.size(); ++i)
			CurrentDirectoryFilesManager.sortedElementIndexes.add(i);
		CurrentDirectoryFilesManager.sortedElementIndexes.sort((i1, i2) -> CurrentDirectoryFilesManager.compareNames(CurrentDirectoryFilesManager.elements.get(i1), CurrentDirectoryFilesManager.elements.get(i2)));
	}
	
	public static boolean isNonEmpty()
	{
		return CurrentDirectoryFilesManager.elements.size() != 0;
	}
	
	public static void printSingleFileName(FileInformation file)
	{
		System.out.print(file.name);
	}
	
	public static void print()
	{
		for (int i = 0; i < CurrentDirectoryFilesManager.elements.size(); ++i) {
			CurrentDirectoryFilesManager.printSingleFileName(CurrentDirectoryFilesManager.elements.get(CurrentDirectoryFilesManager.sortedElementIndexes.get(i)));
			System.out.println();
		}
	}
}
