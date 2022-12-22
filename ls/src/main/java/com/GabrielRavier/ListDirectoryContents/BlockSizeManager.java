package com.GabrielRavier.ListDirectoryContents;

public class BlockSizeManager {

	private static String makeHumanReadableStringFromInteger(long formattedNum)
	{
		String result = "";
		
		do {
			result = (char)((char)(formattedNum % 10) + '0') + result;
			formattedNum /= 10;
		} while (formattedNum != 0);
		return result;
	}
	
	public static String makeHumanReadableString(long num, long origBlockSize, long outputBlockSize)
	{
		if (outputBlockSize <= origBlockSize) {
			if ((origBlockSize % outputBlockSize) == 0) {
				long mult = origBlockSize / outputBlockSize;
				long formattedNum = num * mult;
				if (formattedNum / mult == num)
					return BlockSizeManager.makeHumanReadableStringFromInteger(formattedNum);
			}
		}
		else if (origBlockSize != 0 && (outputBlockSize % origBlockSize == 0))
			return BlockSizeManager.makeHumanReadableStringFromInteger(num / (outputBlockSize / origBlockSize));
		System.err.println("TODO: makeHumanReadable non-integer case");
		return "";
	}
	
}
