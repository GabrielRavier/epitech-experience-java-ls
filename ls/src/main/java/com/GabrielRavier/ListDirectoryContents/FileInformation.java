package com.GabrielRavier.ListDirectoryContents;

public class FileInformation {
	enum Type {
		UNKNOWN,
		NORMAL,
		DIRECTORY,
		COMMAND_LINE_ARGUMENT_DIRECTORY,
		SYMBOLIC_LINK,
	};
	
	FileInformation.Type type = FileInformation.Type.UNKNOWN;
	String name = "";
	java.nio.file.attribute.PosixFileAttributes posixAttributes;
	boolean areAttributesOk = false;
	String linkName = "";
	int numLinks;
	
	public boolean isDirectory()
	{
		return this.type == Type.DIRECTORY ||
			   this.type == Type.COMMAND_LINE_ARGUMENT_DIRECTORY;
	}
	
	public void retrieveLinkName(String fileName, boolean isCommandLineArgument)
	{
		try {
			this.linkName = java.nio.file.Files.readSymbolicLink(java.nio.file.Paths.get(fileName)).toString();
		} catch (java.io.IOException exception) {
			ErrorManager.printFileError(isCommandLineArgument, "cannot read symbolic link %s", fileName);
		}
	}
}