package objects;

import utils.DataNode;
import utils.DataObject;
import utils.DataWriter;

import java.util.ArrayList;
import java.util.List;

public class Wormhole extends DataObject {
	private String name;
	private boolean mappable = false;
	private List<String> links = new ArrayList<>();
	private String color = "";

	public Wormhole(DataNode node) {
		Load(node);
	}

	@Override
	public void Load(DataNode node) {
		name = node.GetToken(1);
		for(DataNode child : node.GetChildren()) {
			String key = child.GetToken(0);
			switch(key) {
				case "mappable" -> mappable = true;
				case "color" -> {
					color = child.GetToken(1);
					if(child.Size() > 2)
						System.out.println("TODO: Support RGB color input.");
				}
				case "link" -> {
					links.add(child.GetToken(1));
					links.add(child.GetToken(2));
				}
				default -> System.out.println("Skipping unrecognized wormhole node: " + key);
			}
		}
	}

	@Override
	public void Save(DataWriter out) {
		out.WriteTokens("wormhole", name);
		out.BeginChild();
		{
			if(mappable)
				out.WriteTokens("mappable");
			for(int i = 0; i < links.size(); )
				out.WriteTokens("link", links.get(i++), links.get(i++));
			if(!color.isEmpty())
				out.WriteTokens("color", color);
		}
		out.EndChild();
		out.NewLine();
	}
}
