package com.GabrielRavier.ListDirectoryContents;

public class FileInformation {
	enum Type {
		unknown,
		normal,
		directory,
		symbolicLink,
	};
	
	FileInformation.Type type;
	String name;
}