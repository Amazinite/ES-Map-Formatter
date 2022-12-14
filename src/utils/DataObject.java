package utils;

public abstract class DataObject {
	public abstract void Load(DataNode node);
	public abstract void Save(DataWriter out);
}
