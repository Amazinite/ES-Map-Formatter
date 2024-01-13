package objects;

import utils.DataNode;
import utils.DataObject;
import utils.DataWriter;

import java.util.ArrayList;
import java.util.List;

public class Port extends DataObject {
	private String name;
	private List<String> recharges = new ArrayList<>();
	private List<String> services = new ArrayList<>();
	private boolean news = false;
	private List<String> description = new ArrayList<>();

	@Override
	public void Load(DataNode node) {
		if(node.Size() > 1)
			name = node.GetToken(1);
		for(DataNode child : node.GetChildren()) {
			String key = child.GetToken(0);

			switch(key) {
				case "recharges" -> recharges.addAll(child.GetTokensPast(1));
				case "services" -> services.addAll(child.GetTokensPast(1));
				case "news" -> news = true;
				case "description" -> description.add(child.GetToken(1));
				default -> System.out.println("Skipping unrecognized port node: " + key);
			}
		}
	}

	@Override
	public void Save(DataWriter out) {
		if(name != null)
			out.WriteTokens("port", name);
		else
			out.WriteTokens("port");
		out.BeginChild();
		{
			if(!recharges.isEmpty()) {
				recharges.sort(String::compareTo);
				recharges.add(0, "recharges");
				out.WriteTokens(recharges);
			}
			if(!services.isEmpty()) {
				services.sort(String::compareTo);
				services.add(0, "services");
				out.WriteTokens(services);
			}
			if(news)
				out.WriteTokens("news");
			for(String line : description) {
				out.ForceBacktick();
				out.WriteTokens("description", line);
			}
		}
		out.EndChild();
	}
}
