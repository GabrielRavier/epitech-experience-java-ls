package com.GabrielRavier.ListDirectoryContents;

public class ArgumentsParser {
	
	enum IgnoreMode {
		NORMAL,
		MINIMAL,
	};
	
	static IgnoreMode ignoreMode = IgnoreMode.NORMAL;
	
	enum FormatMode {
		UNKNOWN,
		ONE_PER_LINE,
		LONG,
	};
	
	static FormatMode formatMode = FormatMode.LONG;
	
	enum DeferenceSymlinkMode {
		UNKNOWN,
		NEVER,
		COMMAND_LINE_SYMLINK_TO_DIR,
	}
	
	static DeferenceSymlinkMode dereferenceSymlinkMode = DeferenceSymlinkMode.UNKNOWN;
	
	static boolean formatNeedsStat = false;
	
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
			System.out.println("  -l                         use a long listing format");
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
		
		var getOptHandler = new gnu.getopt.Getopt("ls", args, "al", longOptions);
		var formatOption = ArgumentsParser.FormatMode.UNKNOWN;
		
		while (true) {
			int c = getOptHandler.getopt();
			
			if (c == -1)
				break;
			
			switch (c) {
			case 'a':
				ArgumentsParser.ignoreMode = IgnoreMode.MINIMAL;
				break;
				
			case 'l':
				formatOption = FormatMode.LONG;
				break;
				
			default:
				printUsageAndExit(ErrorManager.exitStatusFailure);
			}
		}
		
		ArgumentsParser.formatMode = formatOption != ArgumentsParser.FormatMode.UNKNOWN ? formatOption : ArgumentsParser.FormatMode.ONE_PER_LINE;

		return getOptHandler.getOptind();
	}
}
