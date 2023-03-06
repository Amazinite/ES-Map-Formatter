package objects;

import utils.DataNode;
import utils.DataObject;
import utils.DataWriter;
import utils.Format;

public class Sprite extends DataObject {
	private String name;
	private String path;
	private double scale;
	private double frameRate;
	private boolean randomStartFrame;

	public Sprite(DataNode node) {
		Load(node);
	}

	@Override
	public void Load(DataNode node) {
		path = node.GetToken(1);
		name = path.substring(path.indexOf("/") + 1);
		for(DataNode child : node.GetChildren()) {
			String key = child.GetToken(0);
			switch(key) {
				case "random start frame" -> randomStartFrame = true;
				case "frame rate" -> frameRate = child.GetDouble(1);
				case "scale" -> scale = child.GetDouble(1);
				default -> System.out.println("Skipping unrecognized sprite node: " + key);
			}
		}
	}

	@Override
	public void Save(DataWriter out) {
		out.WriteTokens("sprite", path);
		out.BeginChild();
		{
			if(randomStartFrame)
				out.WriteTokens("random start frame");
			if(frameRate != 0)
				out.WriteTokens("frame rate", Format.valueOf(scale));
			if(scale > 0)
				out.WriteTokens("scale", Format.valueOf(scale));
		}
		out.EndChild();
	}

	public String GetPath() {
		return path;
	}

	public String GetName() {
		return name;
	}
}
