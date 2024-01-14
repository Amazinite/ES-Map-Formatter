package utils;

import java.io.PrintWriter;
import java.util.List;

public class DataWriter {
	private StringBuilder builder = new StringBuilder();
	private String path;
	private String indent = "";
	private boolean forceQuote = false;
	private boolean forceBacktick = false;

	public DataWriter(String path, String header) {
		// An output folder must exist to write to.
		this.path = "output\\" + path;
		// Comments don't get parsed, so add the copyright header if it was provided.
		if(header != null)
			builder.append(header);
	}

	/**
	 * End the current line and start a new one.
	 */
	public void NewLine() {
		builder.append("\r\n");
	}

	/**
	 * Write the input strings in order of appearance.
	 * @param tokens An arbitrary number of string inputs.
	 */
	public void WriteTokens(String... tokens) {
		WriteTokens(List.of(tokens));
	}

	/**
	 * Alphabetically sort the input tokens list, then write the sorted tokens after the root string.
	 * @param root The root token that comes before the tokens list.
	 * @param tokens A list of tokens to be written in alphabetical order.
	 */
	public void WriteSortedTokens(String root, List<String> tokens) {
		tokens.sort(String::compareTo);
		tokens.add(0, root);
		WriteTokens(tokens);
	}

	/**
	 * Write a list of tokens to the file. Starts a new line after the tokens are written.
	 * @param tokens A list of tokens to be written in order.
	 */
	public void WriteTokens(List<String> tokens) {
		String whitespace = indent;
		for(String token : tokens) {
			builder.append(whitespace).append(Quote(token, !whitespace.equals(" ")));
			whitespace = " ";
		}
		NewLine();
		forceQuote = false;
		forceBacktick = false;
	}

	/**
	 * Increment the number of tabs used to indent new lines.
	 */
	public void BeginChild() {
		indent += '\t';
	}

	/**
	 * Decrement the number of tabs used to indent new lines.
	 */
	public void EndChild() {
		indent = indent.substring(0, indent.length() - 1);
	}

	/**
	 *
	 * @throws Exception Throws an exception if the file writer fails to be created.
	 */
	public void PrintSave() throws Exception {
		PrintWriter out = new PrintWriter(path);
		out.println(builder.toString().trim());
		out.close();
	}

	/**
	 * Force the non-root tokens of the next write to be quoted.
	 */
	public void ForceQuote() {
		forceQuote = true;
	}

	/**
	 * Force the non-root tokens of the next write to be backticked.
	 */
	public void ForceBacktick() {
		forceBacktick = true;
	}

	/**
	 * Determine whether the input token needs to be quoted.
	 * @param input The input token to potentially quote.
	 * @param first Whether the given input is the first token in a line. The first token in a line won't be force
	 *              quoted if force quoting is active, as force quoting is for user defined values.
	 * @return The input token, quoted if it needs to be.
	 */
	private String Quote(String input, boolean first) {
		// Backticks take precedence over quotes.
		if((!first && forceBacktick) || (input.contains(" ") && input.contains("\"")))
			return "`" + input + "`";
		if((!first && forceQuote) || input.contains(" "))
			return "\"" + input + "\"";
		return input;
	}
}
