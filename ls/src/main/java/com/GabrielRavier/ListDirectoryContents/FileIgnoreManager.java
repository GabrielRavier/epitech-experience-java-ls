package com.GabrielRavier.ListDirectoryContents;

public class FileIgnoreManager {
	public static boolean isIgnored(String fileName)
	{
		return ((ArgumentsParser.ignoreMode != ArgumentsParser.IgnoreMode.MINIMAL 
				&& fileName.length() != 0 && fileName.charAt(0) == '.'
				&& (ArgumentsParser.ignoreMode == ArgumentsParser.IgnoreMode.NORMAL || fileName == "." || fileName == "..")));
	}
}
