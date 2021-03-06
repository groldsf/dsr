package dirElements;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;

public class Package implements Jsonoble {

	private ArrayList<File> array;
	private boolean isMainDir;
	private String fatherDir;
	private String path;

	public Package(ArrayList<File> arrayList, String path) {
		this.path = path;
		array = arrayList;
		isMainDir = path.equals("") || path.equals("/");
		if (!isMainDir) {
			int ind = path.lastIndexOf('/');
			if (ind != -1) {
				fatherDir = path.substring(0, ind);
			} else {
				fatherDir = "";
			}
		}
	}


	public JsonObject toJson() {
		JsonObject answer = new JsonObject();
		JsonArray jsonArray = new JsonArray();
		for (File file : array) {
			jsonArray.add(file.toJson());
		}
		answer.put("array", jsonArray);
		answer.put("path", path);
		answer.put("isMainDir", isMainDir);
		if (!isMainDir) {
			answer.put("fatherDir", fatherDir);
		}
		return answer;
	}
}
