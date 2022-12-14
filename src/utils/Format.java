package utils;

public class Format {
	public static String valueOf(int i) {
		return String.valueOf(i);
	}

	public static String valueOf(double d) {
		String string = String.valueOf(d);
		boolean negative = d < 0.;
		if(string.endsWith(".0"))
			return string.substring(0, string.length() - 2);
		return string.substring(0, Math.min(string.length(), 7 + (negative ? 1 : 0)));
	}
}
