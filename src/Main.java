import objects.*;
import utils.DataFile;
import utils.DataNode;
import utils.DataObject;
import utils.DataWriter;

import java.io.File;
import java.util.*;

public class Main {
	public static void main(String[] args) throws Exception {
		// Parse the input file into data nodes.
		Scanner reader = new Scanner(new File("input.txt"));
		DataFile file = new DataFile(reader);
		reader.close();

		// Tree maps sort the keys without any extra effort needed.
		Map<String, DataObject> galaxies = new TreeMap<>();
		Map<String, DataObject> systems = new TreeMap<>();
		Map<String, DataObject> planets = new TreeMap<>();
		Map<String, DataObject> wormholes = new TreeMap<>();

		// Sort the root nodes into the above maps while constructing them into objects that can be saved out.
		for(DataNode node : file.GetRootNodes()) {
			if(node.Size() == 0) continue;
			String key = node.GetToken(0);
			String value = node.GetToken(1);
			switch(key) {
				case "galaxy" -> galaxies.put(value, new Galaxy(node));
				case "system" -> systems.put(value, new SolarSystem(node));
				case "planet" -> planets.put(value, new Planet(node));
				case "wormhole" -> wormholes.put(value, new Wormhole(node));
				default -> System.out.println("Skipping unrecognized root: " + key);
			}
		}

		// TODO: Add additional formatting options, like setting arrival distances or orbital periods on systems.

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
		for(DataObject planet : planets.values())
			planet.Save(out);
		for(DataObject wormhole : wormholes.values())
			wormhole.Save(out);
		out.PrintSave();
	}
}