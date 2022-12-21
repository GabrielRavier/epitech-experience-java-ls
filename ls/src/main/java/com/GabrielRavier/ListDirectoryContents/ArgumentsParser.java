package com.GabrielRavier.ListDirectoryContents;

public class ArgumentsParser {
	
	enum IgnoreMode {
		normal,
		minimal,
	};
	
	static IgnoreMode ignoreMode = IgnoreMode.normal;
	
	private static void printUsageAndExit(int exitStatus)
	{
		if (exitStatus != 0)
			System.err.println("Try 'ls --help' for more information.");
		else {
			System.out.println("Usage: ls [OPTION]... [FILE]...");
			System.out.println("List information about the FILEs (the current directory by default).");
			System.out.println("Sort entries alphabetically.");
			System.out.println();
			System.out.println("Mandatory options to long options are mandatory for short options too.");
			System.out.println("  -a, --all                  do not ignore entries starting with .");
			System.out.println();
			System.out.println("Exit status:");
			System.out.println(" 0  if OK,");
			System.out.println(" 1  if minor problems (e.g., cannot access subdirectory),");
			System.out.println(" 2  if serious trouble (e.g., cannot access command-line argument).");
		}
		System.exit(exitStatus);
	}
	
	public static int parse(String[] args)
	{
		var longOptions = new gnu.getopt.LongOpt[1];
		longOptions[0] = new gnu.getopt.LongOpt("all", gnu.getopt.LongOpt.NO_ARGUMENT, null, 'a');
		
		var getOptHandler = new gnu.getopt.Getopt("ls", args, "a", longOptions);
		
		while (true) {
			int c = getOptHandler.getopt();
			
			if (c == -1)
				break;
			
			switch (c) {
			case 'a':
				ArgumentsParser.ignoreMode = IgnoreMode.minimal;
				break;
			default:
				printUsageAndExit(ErrorManager.exitStatusFailure);
			}
		}
		
		return getOptHandler.getOptind();
	}
}
