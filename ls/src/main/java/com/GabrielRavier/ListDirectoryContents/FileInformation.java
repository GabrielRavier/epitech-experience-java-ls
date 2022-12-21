package com.GabrielRavier.ListDirectoryContents;

public class FileInformation {
	enum Type {
		unknown,
		normal,
		directory,
		commandLineArgumentDirectory,
		symbolicLink,
	};
	
	FileInformation.Type type = FileInformation.Type.unknown;
	String name = "";
	java.nio.file.attribute.PosixFileAttributes posixAttributes;
	boolean areAttributesOk = false;
	
	public boolean isDirectory()
	{
		return this.type == Type.directory ||
			   this.type == Type.commandLineArgumentDirectory;
	}
}