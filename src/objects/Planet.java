package objects;

import utils.DataNode;
import utils.DataObject;
import utils.DataWriter;
import utils.Format;

import java.util.ArrayList;
import java.util.List;

public class Planet extends DataObject {
	private List<String> attributes = new ArrayList<>();
	private String name = "";
	private String landscape = "";
	private String music = "";
	private String government = "";
	private List<String> description = new ArrayList<>();
	private List<String> spaceport = new ArrayList<>();
	private List<String> shipyards = new ArrayList<>();
	private List<String> outfitters = new ArrayList<>();
	private int tribute = 0;
	private int threshold = 0;
	private int requiredRep = 0;
	private List<Fleet> fleets = new ArrayList<>();
	private double bribe = 0.01;
	private double security = 0.25;
	private String wormhole = "";
	private Port port;

	public Planet(DataNode node) {
		Load(node);
	}

	@Override
	public void Load(DataNode node) {
		name = node.GetToken(1);
		for(DataNode child : node.GetChildren()) {
			String key = child.GetToken(0);

			switch(key) {
				case "attributes" -> attributes.addAll(child.GetTokensPast(1));
				case "landscape" -> landscape = child.GetToken(1);
				case "music" -> music = child.GetToken(1);
				case "government" -> government = child.GetToken(1);
				case "description" -> description.add(child.GetToken(1));
				case "spaceport" -> spaceport.add(child.GetToken(1));
				case "port" -> {
					port = new Port();
					port.Load(child);
				}
				case "shipyard" -> shipyards.add(child.GetToken(1));
				case "outfitter" -> outfitters.add(child.GetToken(1));
				case "tribute" -> {
					tribute = child.GetInt(1);
					for(DataNode grand : child.GetChildren()) {
						switch(grand.GetToken(0)) {
							case "threshold" -> threshold = grand.GetInt(1);
							case "fleet" -> fleets.add(new Fleet(grand.GetToken(1), grand.GetInt(2)));
						}
					}
				}
				case "required reputation" -> requiredRep = child.GetInt(1);
				case "bribe" -> bribe = child.GetDouble(1);
				case "security" -> security = child.GetDouble(1);
				case "wormhole" -> wormhole = child.GetToken(1);
				default -> System.out.println("Skipping unrecognized planet node: " + key);
			}
		}
	}

	@Override
	public void Save(DataWriter out) {
		out.WriteTokens("planet", name);
		out.BeginChild();
		{
			if(!attributes.isEmpty()) {
				attributes.sort(String::compareTo);
				attributes.add(0, "attributes");
				out.WriteTokens(attributes);
			}
			if(!landscape.isEmpty())
				out.WriteTokens("landscape", landscape);
			if(!music.isEmpty())
				out.WriteTokens("music", music);
			for(String line : description) {
				out.ForceBacktick();
				out.WriteTokens("description", line);
			}
			for(String line : spaceport) {
				out.ForceBacktick();
				out.WriteTokens("spaceport", line);
			}
			if(port != null)
				port.Save(out);
			if(!government.isEmpty())
				out.WriteTokens("government", government);
			for(String shipyard : shipyards)
				out.WriteTokens("shipyard", shipyard);
			for(String outfitter : outfitters)
				out.WriteTokens("outfitter", outfitter);
			if(requiredRep != 0)
				out.WriteTokens("required reputation", Format.valueOf(requiredRep));
			if(bribe != 0.01)
				out.WriteTokens("bribe", Format.valueOf(bribe));
			if(security != 0.25)
				out.WriteTokens("security", Format.valueOf(security));
			if(tribute > 0)
			{
				out.WriteTokens("tribute", Format.valueOf(tribute));
				out.BeginChild();
				{
					out.WriteTokens("threshold", Format.valueOf(threshold));
					for(Fleet fleet : fleets)
						out.WriteTokens("fleet", fleet.name, Format.valueOf(fleet.period));
				}
				out.EndChild();
			}
			if(!wormhole.isEmpty())
				out.WriteTokens("wormhole", wormhole);
		}
		out.EndChild();
		out.NewLine();
	}

	private class Fleet {
		public String name;
		public int period;
		public Fleet(String name, int period) {
			this.name = name;
			this.period = period;
		}
	}
}
