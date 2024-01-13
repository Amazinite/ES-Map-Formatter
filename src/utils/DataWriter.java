package utils;

import java.io.PrintWriter;
import java.util.List;

public class DataWriter {
	private StringBuilder builder = new StringBuilder();
	private String path;
	private String indent = "";
	private String whitespace = "";
	private boolean forceQuote = false;
	private boolean forceBacktick = false;

	public DataWriter(String path, String header) {
		this.path = "output\\" + path;
		// Comments don't get parsed, so add the copyright header if it was provided.
		if(header != null)
			builder.append(header);
	}

	public void NewLine() {
		builder.append("\n");
	}

	public void WriteTokens(String... tokens) {
		WriteTokens(List.of(tokens));
	}

	public void WriteTokens(List<String> tokens) {
		whitespace = indent;
		for(String token : tokens) {
			builder.append(whitespace).append(Quote(token, !whitespace.equals(" ")));
			whitespace = " ";
		}
		NewLine();
		forceQuote = false;
		forceBacktick = false;
	}

	public void BeginChild() {
		indent += '\t';
	}

	public void EndChild() {
		indent = indent.substring(0, indent.length() - 1);
	}

	public void PrintSave() throws Exception {
		PrintWriter out = new PrintWriter(path);
		out.println(builder.toString().trim());
		out.close();
	}

	// Force the non-root tokens of the next write to be quoted.
	public void ForceQuote() {
		forceQuote = true;
	}

	// Force the non-root tokens of the next write to be backticked.
	public void ForceBacktick() {
		forceBacktick = true;
	}

	private String Quote(String input, boolean first) {
		// Backticks take precedence over quotes.
		if((!first && forceBacktick) || (input.contains(" ") && input.contains("\"")))
			return "`" + input + "`";
		if((!first && forceQuote) || input.contains(" "))
			return "\"" + input + "\"";
		return input;
	}
}
