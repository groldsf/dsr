
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.File;

public class SystemManager {

	public String mainPath = "";

	public JsonArray getPackage(String path) {
		if(!isCorrect(path)){
			return null;
		}
		String fullPath = mainPath+path;


		//File file = new File(fullPath);
		FileSystem file = new FileSystem
		if(!file.exists() || !file.isDirectory()){
			return null;
		}

		return createJsonDirectory(file);
	}

	private JsonArray createJsonDirectory(File dir) {
		if(!dir.exists() || !dir.isDirectory()){
			return null;
		}
		JsonArray array = new JsonArray();
		for(File item : dir.listFiles())
		{
			JsonObject file = new JsonObject();
			file.put("type",item.isDirectory());
			file.put("name",item.getName());
			file.put("size",item.length());
			file.put("time",item.lastModified());
			array.add(file);
		}

		return array;
	}

	private boolean isCorrect(String path) {
		if(path.contains(".."))
			return false;
		return true;
	}
}
