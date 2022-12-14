package objects;

import utils.DataNode;
import utils.DataObject;
import utils.DataWriter;
import utils.Format;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StellarObject extends DataObject {
	private String name = "";
	private Sprite sprite;
	private double distance;
	private double period;
	private double offset;
	private List<StellarObject> objects = new ArrayList<>();

	public StellarObject(DataNode node) {
		Load(node);
	}

	@Override
	public void Load(DataNode node) {
		if(node.Size() > 1)
			name = node.GetToken(1);
		for(DataNode child : node.GetChildren()) {
			String key = child.GetToken(0);
			switch(key) {
				case "sprite" -> sprite = new Sprite(child);
				case "distance" -> distance = child.GetDouble(1);
				case "period" -> period = child.GetDouble(1);
				case "offset" -> offset = child.GetDouble(1);
				case "object" -> objects.add(new StellarObject(child));
				default -> System.out.println("Skipping unrecognized node: " + key);
			}
		}
	}

	@Override
	public void Save(DataWriter out) {
		if(name.isEmpty())
			out.WriteTokens("object");
		else
			out.WriteTokens("object", name);
		out.BeginChild();
		{
			if(sprite != null)
				sprite.Save(out);
			if(distance > 0)
				out.WriteTokens("distance", Format.valueOf(distance));
			if(period > 0)
				out.WriteTokens("period", Format.valueOf(period));
			if(offset != 0)
				out.WriteTokens("offset", Format.valueOf(offset));
			for(StellarObject object : objects)
				object.Save(out);
		}
		out.EndChild();
	}
}
