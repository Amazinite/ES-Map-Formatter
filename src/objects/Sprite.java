package objects;

import utils.DataNode;
import utils.DataObject;
import utils.DataWriter;
import utils.Format;

public class Sprite extends DataObject {
	private String path;
	private double scale;
	public Sprite(DataNode node) {
		Load(node);
	}

	@Override
	public void Load(DataNode node) {
		path = node.GetToken(1);
		for(DataNode child : node.GetChildren()) {
			String key = child.GetToken(0);
			if(key.equals("scale"))
				scale = child.GetDouble(1);
		}
	}

	@Override
	public void Save(DataWriter out) {
		out.WriteTokens("sprite", path);
		out.BeginChild();
		{
			if(scale > 0)
				out.WriteTokens("scale", Format.valueOf(scale));
		}
		out.EndChild();
	}
}
