package utils;

import java.io.File;
import java.util.*;

public class DataFile {
	// Create a node stack with a root node that has a depth of -1, which no actual node could have.
	// This ensures that the root node never gets popped off the stack.
	private final DataNode root = new DataNode(-1, new ArrayList<>());

	public DataFile(File file) throws Exception {
		Scanner fileReader = new Scanner(file);
		// Add the root node to the bottom of the stack.
		Stack<DataNode> nodeStack = new Stack<>();
		nodeStack.push(root);

		while(fileReader.hasNext()) {
			String line = fileReader.nextLine();
			// Skip blank lines, as they contain no tokens.
			if(line.isBlank())
				continue;

			// Pop the stack until the node at the top of the stack would be the parent of the current line.
			int depth = IndentationDepth(line);
			while(nodeStack.peek().GetDepth() >= depth)
				nodeStack.pop();

			// Create a new node, add it as a child of the node at the top of the stack, then add it to the stack.
			DataNode node = new DataNode(depth, TokenizeLine(line));
			nodeStack.peek().GetChildren().add(node);
			nodeStack.add(node);
		}
		fileReader.close();
	}

	public List<DataNode> GetRootNodes() {
		return root.GetChildren();
	}

	private int IndentationDepth(String line) {
		int pos = 0;
		char c = line.charAt(pos);
		while(c <= ' ')
			c = line.charAt(++pos);
		return pos;
	}

	private static List<String> TokenizeLine(String line) {
		// If the line contains a comment, trim the comment off.
		if(line.contains("#"))
			line = line.substring(0, line.indexOf('#'));
		// Trim whitespace.
		line = line.trim();
		// Store tokens here.
		List<String> tokens = new ArrayList<>();
		// Keep track of the start and end of each token.
		int start = 0;
		int end = 0;
		while(end < line.length()) {
			// Get the first available character. Determine if it is a quote, backtick, or any other character.
			char c = line.charAt(start);
			boolean quoted = (c == '"');
			boolean backticked = (c == '`');
			// If the character is a quote or backtick, we don't want to include that in the token, so move
			// the token start forward once.
			start += (quoted || backticked ? 1 : 0);
			// If the character is a quote or backtick, look for the next quote or backtick. Otherwise,
			// look for the next space.
			end = line.indexOf((quoted ? '"' : backticked ? '`' : ' '), start);
			// If no quote, backtick, or space could be found, then this is the last token in the line.
			if(end == -1) {
				tokens.add(line.substring(start));
				break;
			}
			// The token is located between start and end.
			tokens.add(line.substring(start, end));
			// Move to the start of the next token, which is either after a space if this token was unquoted,
			// or after a quote and a space if this token was quoted.
			start = end + (quoted || backticked ? 2 : 1);
			end = start;
		}
		return tokens;
	}
}
