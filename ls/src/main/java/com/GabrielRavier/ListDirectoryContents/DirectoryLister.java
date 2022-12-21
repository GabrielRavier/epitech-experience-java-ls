package com.GabrielRavier.ListDirectoryContents;

public class DirectoryLister {
	
	public static void getDirectoriesFromFilesManager()
	{
		for (int i = CurrentDirectoryFilesManager.fileCount; i-- != 0;) {
			var fileInfo = CurrentDirectoryFilesManager.elements.get(CurrentDirectoryFilesManager.sortedElementIndexes.get(i));
			
			if (fileInfo.isDirectory())
				PendingDirectoryManager.queue(fileInfo.name, true);
		}
		for (int i = 0; i < CurrentDirectoryFilesManager.sortedElementIndexes.size(); ++i) {
			var fileInfo = CurrentDirectoryFilesManager.elements.get(CurrentDirectoryFilesManager.sortedElementIndexes.get(i));
			if (fileInfo.type == FileInformation.Type.commandLineArgumentDirectory) {
				CurrentDirectoryFilesManager.sortedElementIndexes.remove(i);
				--i;
			}
		}
		CurrentDirectoryFilesManager.fileCount = CurrentDirectoryFilesManager.sortedElementIndexes.size();
	}
	
	public static void execute(String[] args)
	{
		int i = 0;

		CurrentDirectoryFilesManager.clear();
		
		int numFiles = args.length;

		if (numFiles <= 0)
			PendingDirectoryManager.queue(".", true);
		else
			do
				CurrentDirectoryFilesManager.addFile(args[i++], true, "");
			while (i < args.length);
		
		if (CurrentDirectoryFilesManager.fileCount != 0) {
			CurrentDirectoryFilesManager.sort();
			DirectoryLister.getDirectoriesFromFilesManager();
		}
		if (CurrentDirectoryFilesManager.fileCount != 0) {
			CurrentDirectoryFilesManager.print();
			if (PendingDirectoryManager.entryCount() != 0)
				System.out.println();
		} else if (numFiles <= 1 && PendingDirectoryManager.entryCount() == 1)
			DirectoryPrinter.printNames = false;
    	
    	while (PendingDirectoryManager.entryCount() != 0) {
    		var nextDir = PendingDirectoryManager.getNext();
    		
    		DirectoryPrinter.print(nextDir.name, nextDir.isCommandLineArgument);
    	}
    	
    	System.exit(ErrorManager.exitStatus);
	}
}
