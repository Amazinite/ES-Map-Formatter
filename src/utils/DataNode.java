package utils;

import java.util.ArrayList;
import java.util.List;

public class DataNode {
	private int depth;
	private List<String> tokens;
	private List<DataNode> children;

	public DataNode(int depth, List<String> tokens) {
		this.depth = depth;
		this.tokens = tokens;
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

	public String toString() {
		return tokens.toString();
	}
}
