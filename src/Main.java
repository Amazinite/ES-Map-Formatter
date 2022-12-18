import objects.*;
import utils.*;

import java.util.*;

public class Main {
	public static void main(String[] args) throws Exception {
		// Parse the input file into data nodes.
		DataFile file = new DataFile("input.txt");

		// Tree maps sort the keys without any extra effort needed.
		Map<String, DataObject> galaxies = new TreeMap<>();
		Map<String, DataObject> systems = new TreeMap<>();
		// We want to interweave the list of wormholes and planets, so use the same map for each.
		// Wormholes typically share the name of the planet that uses them, so pair them together as to avoid
		// one overwriting the other. I could also use a multimap for this, but that isn't in java.util and I don't
		// and to fight dependencies right now, so I'm just copying over a Pair class I've used in other projects.
		// first = planet, second = wormhole.
		Map<String, Couple<DataObject>> planets = new TreeMap<>();

		// Sort the root nodes into the above maps while constructing them into objects that can be saved out.
		for(DataNode node : file.GetRootNodes()) {
			if(node.Size() == 0) continue;
			String key = node.GetToken(0);
			String value = node.GetToken(1);
			switch(key) {
				case "galaxy" -> galaxies.put(value, new Galaxy(node));
				case "system" -> systems.put(value, new SolarSystem(node));
				case "planet" -> planets.computeIfAbsent(value, k -> new Couple<>()).first = new Planet(node);
				case "wormhole" -> planets.computeIfAbsent(value, k -> new Couple<>()).second = new Wormhole(node);
				default -> System.out.println("Skipping unrecognized root: " + key);
			}
		}

		// Each class organizes object attributes when saving, but some attributes may need their values formatted.
		// For example, the stars in a system influence the habitable range and arrival distance of the system, as
		// well as the orbital periods of any objects. These values should therefore be set in stone.
		for(DataObject system : systems.values()) {
			((SolarSystem)system).CalibrateStarInfluence();
			((SolarSystem)system).CalibrateArrivalDistance();
		}

		// Save out all the data objects. The maps containing the objects sorted the objects by name,
		// and each object sorts and formats its own internals.
		DataWriter out = new DataWriter("map.txt",
        """
				# Copyright (c) 2014 by Michael Zahniser
				#
				# Endless Sky is free software: you can redistribute it and/or modify it under the
				# terms of the GNU General Public License as published by the Free Software
				# Foundation, either version 3 of the License, or (at your option) any later version.
				#
				# Endless Sky is distributed in the hope that it will be useful, but WITHOUT ANY
				# WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
				# PARTICULAR PURPOSE. See the GNU General Public License for more details.
				#
				# You should have received a copy of the GNU General Public License along with
				# this program. If not, see <https://www.gnu.org/licenses/>.

				""");
		for(DataObject galaxy : galaxies.values())
			galaxy.Save(out);
		for(DataObject system : systems.values())
			system.Save(out);
		for(Couple<DataObject> planet : planets.values()) {
			if(planet.first != null)
				planet.first.Save(out);
			if(planet.second != null)
				planet.second.Save(out);
		}
		out.PrintSave();
	}
}