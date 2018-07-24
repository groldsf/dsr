package dirElements;

import io.vertx.core.file.FileProps;
import io.vertx.core.json.JsonObject;

public class MyFile implements dirElements.Jsonoble {

	private String type;
	private String name;
	private String path;
	private long size;
	private long lastModifiedTime;
	private long creationTime;
	private long lastAccessTime;

	public MyFile(FileProps props, String path){
		type = props.isDirectory()? "package" : "file";
		name = path.substring(path.lastIndexOf("\\")+1);
		this.path = path;
		size = props.size();
		lastModifiedTime = props.lastModifiedTime();
		creationTime = props.creationTime();
		lastAccessTime = props.lastAccessTime();
	}

	public JsonObject toJson(){
		JsonObject file = new JsonObject();
		file.put("type", type);
		file.put("name", name);
		file.put("fullDirectory", path);
		file.put("size", size);
		file.put("lastModifiedTime", lastModifiedTime);
		file.put("creationTime", creationTime);
		file.put("lastAccessTime", lastAccessTime);
		return file;
	}
}
