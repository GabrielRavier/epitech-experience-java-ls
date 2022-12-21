package com.GabrielRavier.ListDirectoryContents;

public class PendingDirectory {
	public String name;
	public boolean isCommandLineArgument;
	
	PendingDirectory(String name, boolean isCommandLineArgument)
	{
		this.name = name;
		this.isCommandLineArgument = isCommandLineArgument;
	}
}