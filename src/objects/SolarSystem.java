package objects;

import enums.Star;
import utils.DataNode;
import utils.DataObject;
import utils.DataWriter;
import utils.Format;

import java.util.*;

public class SolarSystem extends DataObject {
	private String name;
	private boolean hidden = false;
	private double x;
	private double y;
	private String government = "";
	private List<String> attributes = new ArrayList<>();
	private String music = "";
	private double hyperArrival;
	private double jumpArrival;
	private double hyperDeparture;
	private double jumpDeparture;
	private boolean ramscoopSettings = false;
	private boolean ramscoopUniversal = true;
	private double ramscoopAddend = 0.;
	private double ramscoopMultiplier = 1.;
	private double habitable;
	private List<Belt> belts = new ArrayList<>();
	private double jumpRange;
	private String haze = "";
	private List<String> links = new ArrayList<>();
	private List<Asteroid> asteroids = new ArrayList<>();
	private List<Asteroid> minables = new ArrayList<>();
	private List<NamedInt> trade = new ArrayList<>();
	private List<NamedInt> fleets = new ArrayList<>();
	private List<NamedInt> hazards = new ArrayList<>();
	private List<StellarObject> objects = new ArrayList<>();

	public SolarSystem(DataNode node) {
		Load(node);
	}

	@Override
	public void Load(DataNode node) {
		name = node.GetToken(1);
		for(DataNode child : node.GetChildren()) {
			String key = child.GetToken(0);
			switch(key) {
				case "hidden" -> hidden = true;
				case "pos" -> {
					x = child.GetDouble(1);
					y = child.GetDouble(2);
				}
				case "government" -> government = child.GetToken(1);
				case "attributes" -> attributes.addAll(child.GetTokensPast(1));
				case "music" -> music = child.GetToken(1);
				case "arrival" -> {
					if(child.Size() > 1) {
						hyperArrival = child.GetDouble(1);
						jumpArrival = child.GetDouble(1);
					}
					for(DataNode grand : child.GetChildren()) {
						if(grand.GetToken(0).equals("link"))
							hyperArrival = child.GetDouble(1);
						else if(grand.GetToken(0).equals("jump"))
							jumpArrival = child.GetDouble(1);
					}
				}
				case "departure" -> {
					if(child.Size() > 1) {
						hyperDeparture = child.GetDouble(1);
						jumpDeparture = child.GetDouble(1);
					}
					for(DataNode grand : child.GetChildren()) {
						if(grand.GetToken(0).equals("link"))
							hyperDeparture = child.GetDouble(1);
						else if(grand.GetToken(0).equals("jump"))
							jumpDeparture = child.GetDouble(1);
					}
				}
				case "ramscoop" -> {
					for(DataNode grand : child.GetChildren()) {
						String grandKey = grand.GetToken(0);
						if(grand.Size() < 2) {
							System.out.println("Skipping ramscoop key \"" + grandKey + "\" with no value.");
							continue;
						}
						double grandVal = grand.GetDouble(1);
						switch(grandKey) {
							case "universal" -> ramscoopUniversal = (grandVal == 1.);
							case "addend" -> ramscoopAddend = grandVal;
							case "multiplier" -> ramscoopMultiplier = grandVal;
							default -> System.out.println("Skipping unrecognized ramscoop node: " + key);
						}
						ramscoopSettings = true;
					}
				}
				case "habitable" -> habitable = child.GetDouble(1);
				case "belt" -> belts.add(new Belt(child.GetDouble(1), child.Size() > 2 ? child.GetInt(2) : 1));
				case "jump range" -> jumpRange = child.GetDouble(1);
				case "haze" -> haze = child.GetToken(1);
				case "link" -> links.add(child.GetToken(1));
				case "asteroids" -> asteroids.add(new Asteroid(child.GetToken(1), child.GetInt(2), child.GetDouble(3)));
				case "minables" -> minables.add(new Asteroid(child.GetToken(1), child.GetInt(2), child.GetDouble(3)));
				case "trade" -> trade.add(new NamedInt(child.GetToken(1), child.GetInt(2)));
				case "fleet" -> fleets.add(new NamedInt(child.GetToken(1), child.GetInt(2)));
				case "hazard" -> hazards.add(new NamedInt(child.GetToken(1), child.GetInt(2)));
				case "object" -> objects.add(new StellarObject(child));
				default -> System.out.println("Skipping unrecognized system node: " + key);
			}
		}
	}

	@Override
	public void Save(DataWriter out) {
		out.WriteTokens("system", name);
		out.BeginChild();
		{
			if(hidden)
				out.WriteTokens("hidden");
			out.WriteTokens("pos", Format.valueOf(x), Format.valueOf(y));
			if(!government.isEmpty())
				out.WriteTokens("government", government);
			if(!attributes.isEmpty()) {
				out.ForceQuote();
				out.WriteSortedTokens("attributes", attributes);
			}
			if(!music.isEmpty())
				out.WriteTokens("music", music);
			belts.sort(Comparator.comparingDouble(a -> a.radius));
			if(hyperArrival > 0 || jumpArrival > 0) {
				if(hyperArrival == jumpArrival)
					out.WriteTokens("arrival", Format.valueOf(hyperArrival));
				else {
					out.WriteTokens("arrival");
					out.BeginChild();
					{
						if(hyperArrival > 0)
							out.WriteTokens("link", Format.valueOf(hyperArrival));
						if(jumpArrival > 0)
							out.WriteTokens("jump", Format.valueOf(jumpArrival));
					}
					out.EndChild();
				}
			}
			if(hyperDeparture > 0 || jumpDeparture > 0) {
				if(hyperDeparture == jumpDeparture)
					out.WriteTokens("departure", Format.valueOf(hyperDeparture));
				else {
					out.WriteTokens("departure");
					out.BeginChild();
					{
						if(hyperDeparture > 0)
							out.WriteTokens("link", Format.valueOf(hyperDeparture));
						if(jumpDeparture > 0)
							out.WriteTokens("jump", Format.valueOf(jumpDeparture));
					}
					out.EndChild();
				}
			}
			if(ramscoopSettings) {
				out.WriteTokens("ramscoop");
				out.BeginChild();
				{
					out.WriteTokens("universal", Format.valueOf(ramscoopUniversal ? 1 : 0));
					out.WriteTokens("addend", Format.valueOf(ramscoopAddend));
					out.WriteTokens("multiplier", Format.valueOf(ramscoopMultiplier));
				}
				out.EndChild();
			}
			if(habitable > 0)
				out.WriteTokens("habitable", Format.valueOf(habitable));
			for(Belt belt : belts) {
				if(belts.size() > 1)
					out.WriteTokens("belt", Format.valueOf(belt.radius), Format.valueOf(belt.weight));
				else
					out.WriteTokens("belt", Format.valueOf(belt.radius));
			}
			if(jumpRange > 0)
				out.WriteTokens("jump range", Format.valueOf(jumpRange));
			if(!haze.isEmpty())
				out.WriteTokens("haze", haze);
			links.sort(String::compareTo);
			for(String link : links)
				out.WriteTokens("link", link);
			// TODO: Diff-minimizing change. Remove later, or decide if it should stay.
			//asteroids.sort(Comparator.comparing(a -> a.name));
			for(Asteroid asteroid : asteroids)
				out.WriteTokens("asteroids", asteroid.name, Format.valueOf(asteroid.count), Format.valueOf(asteroid.energy));
			// TODO: Diff-minimizing change. Remove later, or decide if it should stay.
			//minables.sort(Comparator.comparing(a -> a.name));
			for(Asteroid minable : minables)
				out.WriteTokens("minables", minable.name, Format.valueOf(minable.count), Format.valueOf(minable.energy));
			trade.sort(Comparator.comparing(a -> a.name));
			for(NamedInt commodity : trade)
				out.WriteTokens("trade", commodity.name, Format.valueOf(commodity.value));
			for(NamedInt fleet : fleets)
				out.WriteTokens("fleet", fleet.name, Format.valueOf(fleet.value));
			for(NamedInt hazard : hazards)
				out.WriteTokens("hazard", hazard.name, Format.valueOf(hazard.value));
			// Sort stellar objects by distance.
			objects.sort(Comparator.comparingDouble(StellarObject::Distance));
			for(StellarObject object : objects)
				object.Save(out);
		}
		out.EndChild();
		out.NewLine();
	}

	// Determine what this system's habitable range and planet orbital periods should be given the stars in the system.
	public void CalibrateStarInfluence() {
		// The sum of the habitable and mass of all stars in the system.
		double sumHabitable = 0.;
		double sumMass = 0.;
		for(StellarObject object : objects) {
			List<Optional<Star>> infoList = object.GetStarInfo();
			for(Optional<Star> info : infoList)
				if(info.isPresent()) {
					sumHabitable += info.get().Habitable();
					sumMass += info.get().Mass();
				}
		}
		// Warn about systems with no known stars.
		if(sumHabitable == 0. || sumMass == 0.) {
			System.out.println("System \"" + name + "\" has no known stars.");
			sumHabitable = Star.DEFAULT.Habitable();
			sumMass = Star.DEFAULT.Mass();
		}

		habitable = sumHabitable;
		for(StellarObject object : objects)
			if(!object.IsStar() && !object.IsSpecial())
				object.CalibratePeriod(sumMass);
	}

	public void CalibrateArrivalDistance() {
		hyperArrival = Math.max(500., Math.min(habitable, 5000.));
		jumpArrival = hyperArrival;
	}

	private class Belt {
		public double radius;
		public int weight;
		public Belt(double radius, int weight) {
			this.radius = radius;
			this.weight = weight;
		}
	}

	private class Asteroid {
		public String name;
		public int count;
		public double energy;
		public Asteroid(String name, int count, double energy) {
			this.name = name;
			this.count = count;
			this.energy = energy;
		}
	}

	private class NamedInt {
		public String name;
		public int value;
		public NamedInt(String name, int value) {
			this.name = name;
			this.value = value;
		}
	}
}
