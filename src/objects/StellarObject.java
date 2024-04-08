package objects;

import enums.Star;
import utils.DataNode;
import utils.DataObject;
import utils.DataWriter;
import utils.Format;

import java.util.*;

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
				default -> System.out.println("Skipping unrecognized object node: " + key);
			}
		}
		// Stellar objects should ideally have a sprite.
		if(sprite == null)
			node.PrintTrace("Encountered a StellarObject with no sprite:");
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
			// Sort moons by distance.
			objects.sort(Comparator.comparingDouble(StellarObject::Distance));
			for(StellarObject object : objects)
				object.Save(out);
		}
		out.EndChild();
	}

	public List<Optional<Star>> GetStarInfo() {
		List<Optional<Star>> info = new ArrayList<>();
		if(sprite != null)
			info.add(Star.GetStar(sprite.GetName()));
		for(StellarObject object : objects)
			info.addAll(object.GetStarInfo());
		return info;
	}

	public void CalibratePeriod(double mass) {
		// Solar panels should act as though they have the same distance as a ringworld.
		double d = sprite != null && sprite.GetName().contains("panel") ? 812. : distance;
		period = Math.sqrt(Math.pow(d, 3) / mass);
	}

	public boolean IsStar() {
		// Rouge brown dwarves are in the planet folder but act as stars.
		return sprite != null && (sprite.GetPath().contains("star/") || sprite.GetPath().contains("rogue"));
	}

	/**
	 * Special objects don't have their orbital periods recalibrated.
	 */
	public boolean IsSpecial() {
		return sprite != null && sprite.GetPath().contains("vajra/vajra");
	}

	public double Distance() {
		return distance;
	}
}
