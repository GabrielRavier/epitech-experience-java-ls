package com.GabrielRavier.ListDirectoryContents;

public class DirectoryLister {
	
	public void execute(String[] args)
	{
    	PendingDirectoryManager.queue(".", true);
    	DirectoryPrinter.printNames = false;
    	
    	while (PendingDirectoryManager.isNonEmpty()) {
    		var nextDir = PendingDirectoryManager.getNext();
    		
    		DirectoryPrinter.print(nextDir.name, nextDir.isCommandLineArgument);
    	}
    	
    	System.exit(ErrorManager.exitStatus);
	}
}
