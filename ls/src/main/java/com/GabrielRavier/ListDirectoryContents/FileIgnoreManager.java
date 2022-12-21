package com.GabrielRavier.ListDirectoryContents;

public class FileIgnoreManager {
	public static boolean isIgnored(String fileName)
	{
		return ((fileName.length() != 0 && fileName.charAt(0) == '.'));
	}
}
