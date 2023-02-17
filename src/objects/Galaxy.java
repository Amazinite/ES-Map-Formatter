package objects;

import utils.DataNode;
import utils.DataObject;
import utils.DataWriter;
import utils.Format;

public class Galaxy extends DataObject {
	private String name;
	private double x;
	private double y;
	private Sprite sprite;

	public Galaxy(DataNode node) {
		Load(node);
	}

	@Override
	public void Load(DataNode node) {
		name = node.GetToken(1);
		for(DataNode child : node.GetChildren()) {
			String key = child.GetToken(0);
			if(key.equals("pos")) {
				x = child.GetDouble(1);
				y = child.GetDouble(2);
			} else if(key.equals("sprite")) {
				sprite = new Sprite(child);
			} else
				System.out.println("Skipping unrecognized node: " + key);
		}
	}

	@Override
	public void Save(DataWriter out) {
		out.WriteTokens("galaxy", name);
		out.BeginChild();
		{
			out.WriteTokens("pos", Format.valueOf(x), Format.valueOf(y));
			if(sprite != null)
				sprite.Save(out);
		}
		out.EndChild();
		out.NewLine();
	}
}
