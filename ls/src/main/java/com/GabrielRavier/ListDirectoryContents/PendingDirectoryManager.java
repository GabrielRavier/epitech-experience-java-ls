package com.GabrielRavier.ListDirectoryContents;

public class PendingDirectoryManager {
	
	private static java.util.ArrayList<PendingDirectory> pending = new java.util.ArrayList<PendingDirectory>();
	
	public static void queue(String name, boolean isCommandLineArgument)
	{
		PendingDirectoryManager.pending.add(new PendingDirectory(name, isCommandLineArgument));
	}
	
	public static int entryCount()
	{
		return PendingDirectoryManager.pending.size();
	}
	
	public static PendingDirectory getNext()
	{
		PendingDirectory result = PendingDirectoryManager.pending.get(PendingDirectoryManager.pending.size() - 1);
		PendingDirectoryManager.pending.remove(PendingDirectoryManager.pending.size() - 1);
		return result;
	}
}
