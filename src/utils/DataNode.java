package utils;

import java.util.ArrayList;
import java.util.List;

public class DataNode {
	private int depth;
	private List<String> tokens;
	private DataNode parent;
	private List<DataNode> children;
	private String location;

	/**
	 * Root node constructor.
	 */
	public DataNode() {
		this.depth = -1;
		this.tokens = new ArrayList<>();
		children = new ArrayList<>();
	}

	public DataNode(DataNode parent, int depth, List<String> tokens, String fileName, int lineNum) {
		this.parent = parent;
		this.depth = depth;
		this.tokens = tokens;
		this.location = fileName + " at line " + lineNum;
		children = new ArrayList<>();
	}

	public int GetDepth() {
		return depth;
	}

	public List<String> GetTokens() {
		return tokens;
	}

	public int Size() {
		return tokens.size();
	}

	public String GetToken(int i) {
		return tokens.get(i);
	}

	public List<String> GetTokensPast(int i) {
		return tokens.subList(i, tokens.size());
	}

	public int GetInt(int i) {
		return Integer.parseInt(tokens.get(i));
	}

	public double GetDouble(int i) {
		return Double.parseDouble(tokens.get(i));
	}

	public List<DataNode> GetChildren() {
		return children;
	}

	public void PrintTrace(String error) {
		List<String> lines = new ArrayList<>();
		DataNode current = this;
		while(current.depth != -1) {
			String line = "\t".repeat(current.depth) + String.join(" ", current.tokens);
			lines.add(0, line);
			current = current.parent;
		}
		System.out.println(error + " " + location);
		for(String line : lines)
			System.out.println(line);
	}

	public String toString() {
		return tokens.toString();
	}
}
