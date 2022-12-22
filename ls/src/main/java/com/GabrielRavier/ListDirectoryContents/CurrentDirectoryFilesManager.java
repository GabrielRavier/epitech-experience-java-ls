package com.GabrielRavier.ListDirectoryContents;

public class CurrentDirectoryFilesManager {
	public static java.util.ArrayList<FileInformation> elements = new java.util.ArrayList<FileInformation>();
	public static java.util.ArrayList<Integer> sortedElementIndexes = new java.util.ArrayList<Integer>();
	public static int fileCount = 0;
	private static int ownerWidth = 0;
	private static int groupWidth = 0;
	private static int numLinksWidth = 0;
	private static int fileSizeWidth = 0;
	
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
	
	public static long addFile(String fileName, boolean isCommandLineArgument, String containingDirectoryName)
	{
		long blocks = 0;
		var newElement = new FileInformation();
		newElement.type = FileInformation.Type.UNKNOWN;
		
		if (isCommandLineArgument || ArgumentsParser.formatNeedsStat) {
			String fullName;
			
			if (fileName.charAt(0) == '/' || containingDirectoryName.length() == 0)
				fullName = fileName;
			else
				fullName = CurrentDirectoryFilesManager.addUpNamesForFullName(containingDirectoryName, fileName);
			
			var namePath = java.nio.file.Paths.get(fullName);
			Exception exception = null;

			switch (ArgumentsParser.dereferenceSymlinkMode) {
			case COMMAND_LINE_SYMLINK_TO_DIR:
				if (isCommandLineArgument) {
					boolean gotError = false;
					boolean isErrorFileNotFound = false;
					try {
						newElement.posixAttributes = java.nio.file.Files.readAttributes(namePath, java.nio.file.attribute.PosixFileAttributes.class);
						newElement.numLinks = (int)java.nio.file.Files.getAttribute(namePath, "unix:nlink");
					} catch (java.io.FileNotFoundException caughtException) {
						gotError = true;
						isErrorFileNotFound = true;
						exception = caughtException;
					} catch (java.io.IOException caughtException) {
						gotError = true;
						exception = caughtException;
					}
					
					boolean needToCheckSymlink = (gotError ? isErrorFileNotFound : !newElement.posixAttributes.isDirectory());
	
					if (!needToCheckSymlink)
						break;
				}
				// fall-through

			default:
				try {
					newElement.posixAttributes = java.nio.file.Files.readAttributes(namePath, java.nio.file.attribute.PosixFileAttributes.class, java.nio.file.LinkOption.NOFOLLOW_LINKS);
					newElement.numLinks = (int)java.nio.file.Files.getAttribute(namePath, "unix:nlink", java.nio.file.LinkOption.NOFOLLOW_LINKS);
				} catch (java.io.IOException caughtException) {
					exception = caughtException;
				}
			}
				
			if (exception != null) {
				ErrorManager.printFileError(isCommandLineArgument, "cannot access %s", fileName);
				if (isCommandLineArgument)
					return 0;
				newElement.name = fileName;
				CurrentDirectoryFilesManager.elements.add(newElement);
				++CurrentDirectoryFilesManager.fileCount;
				return 0;
			}
			
			newElement.areAttributesOk = true;
			
			if (newElement.posixAttributes.isSymbolicLink() &&
				ArgumentsParser.formatMode == ArgumentsParser.FormatMode.LONG)
				newElement.retrieveLinkName(fullName, isCommandLineArgument);

			if (newElement.posixAttributes.isSymbolicLink())
				newElement.type = FileInformation.Type.SYMBOLIC_LINK;
			else if (newElement.posixAttributes.isDirectory()) {
				if (isCommandLineArgument)
					newElement.type = FileInformation.Type.COMMAND_LINE_ARGUMENT_DIRECTORY;
				else
					newElement.type = FileInformation.Type.DIRECTORY;
			} else
				newElement.type = FileInformation.Type.NORMAL;

			blocks = newElement.posixAttributes.size() / 511;
			
			if (ArgumentsParser.formatMode == ArgumentsParser.FormatMode.LONG) {
				int len = newElement.posixAttributes.owner().getName().length();
				if (CurrentDirectoryFilesManager.ownerWidth < len)
					CurrentDirectoryFilesManager.ownerWidth = len;
				
				len = newElement.posixAttributes.group().getName().length();
				if (CurrentDirectoryFilesManager.groupWidth < len)
					CurrentDirectoryFilesManager.groupWidth = len;
				
				len = ("" + newElement.numLinks).length();
				if (CurrentDirectoryFilesManager.numLinksWidth < len)
					CurrentDirectoryFilesManager.numLinksWidth = len;
				
				len = BlockSizeManager.makeHumanReadableString(newElement.posixAttributes.size(), 1, 1).length();
				if (CurrentDirectoryFilesManager.fileSizeWidth < len)
					CurrentDirectoryFilesManager.fileSizeWidth = len;
			}
		}
		
		newElement.name = fileName;
		CurrentDirectoryFilesManager.elements.add(newElement);
		++CurrentDirectoryFilesManager.fileCount;
		return blocks;
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
	
	private static void printSingleFileName(FileInformation file)
	{
		System.out.print(file.name);
	}
	
	private static void printSingleLongFormat(FileInformation file)
	{
		char modeType;
		
		if (file.type == FileInformation.Type.NORMAL)
			modeType = '-';
		else if (file.isDirectory())
			modeType = 'd';
		else if (file.type == FileInformation.Type.SYMBOLIC_LINK)
			modeType = 'l';
		else
			modeType = '?';
		
		String permissions;
		if (file.areAttributesOk)
			permissions = java.nio.file.attribute.PosixFilePermissions.toString(file.posixAttributes.permissions()) + '.';
		else
			permissions = "??????????";
		
		System.out.print(modeType + permissions + ' ');
		
		String numLinksStr = file.areAttributesOk ? "" + file.numLinks : "?";
		for (int i = 0; i < (CurrentDirectoryFilesManager.numLinksWidth - numLinksStr.length()); ++i)
			System.out.print(' ');
		System.out.print(numLinksStr + ' ');
		
		String ownerStr = file.areAttributesOk ? file.posixAttributes.owner().getName() : "?";
		System.out.print(ownerStr);
		for (int i = 0; i < (CurrentDirectoryFilesManager.ownerWidth - ownerStr.length() + 1); ++i)
			System.out.print(' ');
		
		String groupStr = file.areAttributesOk ? file.posixAttributes.group().getName() : "?";
		System.out.print(groupStr);
		for (int i = 0; i < (CurrentDirectoryFilesManager.ownerWidth - groupStr.length() + 1); ++i)
			System.out.print(' ');
		
		String fileSizeStr = (!file.areAttributesOk ? "?" : BlockSizeManager.makeHumanReadableString(file.posixAttributes.size(), 1, 1));
		for (int i = 0; i < (CurrentDirectoryFilesManager.fileSizeWidth - fileSizeStr.length()); ++i)
			System.out.print(' ');
		System.out.print(fileSizeStr + ' ');
		
		System.out.print(file.name);
		if (file.type == FileInformation.Type.SYMBOLIC_LINK && file.linkName != "")
			System.out.print(" -> " + file.linkName);
	}
	
	public static void print()
	{
		switch (ArgumentsParser.formatMode) {
		case ONE_PER_LINE:
			for (int i = 0; i < CurrentDirectoryFilesManager.fileCount; ++i) {
				CurrentDirectoryFilesManager.printSingleFileName(CurrentDirectoryFilesManager.elements.get(CurrentDirectoryFilesManager.sortedElementIndexes.get(i)));
				System.out.println();
			}
			break;
			
		case LONG:
			for (int i = 0; i < CurrentDirectoryFilesManager.fileCount; ++i) {
				CurrentDirectoryFilesManager.printSingleLongFormat(CurrentDirectoryFilesManager.elements.get(CurrentDirectoryFilesManager.sortedElementIndexes.get(i)));
				System.out.println();
			}
			break;
		
		default:
			System.err.println("UNKNOWN should never occur !!!!!");
		}
	}
	
	public static void clear()
	{
		CurrentDirectoryFilesManager.elements.clear();
		CurrentDirectoryFilesManager.sortedElementIndexes.clear();
		CurrentDirectoryFilesManager.fileCount = 0;
		CurrentDirectoryFilesManager.ownerWidth = 0;
		CurrentDirectoryFilesManager.groupWidth = 0;
		CurrentDirectoryFilesManager.numLinksWidth = 0;
		CurrentDirectoryFilesManager.fileSizeWidth = 0;
	}
}
