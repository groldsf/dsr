package systemManager;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import io.vertx.rxjava.core.file.FileProps;
import io.vertx.rxjava.core.file.FileSystem;

import java.util.ArrayList;
import java.util.List;

public class SystemManager {

	protected String mainPath = "C:\\";//"D:\\victor\\ideaTest\\WORK\\dsr\\dsr\\";
	private Vertx vertx;
	protected FileSystem fileSystem;

	public SystemManager(Vertx vertx) {
		this.vertx = vertx;
		fileSystem = new FileSystem(vertx.fileSystem());
	}



	public  void getFile(HttpServerRequest request, JsonObject in){
		if(!in.containsKey("path")){}
			//ErrorResponse.returnError(request);
		else {
			String path = in.getString("path");
			if (!isCorrect(path)){}
				//ErrorResponse.returnError(request);
			else
				jsonTxt(in.getString("path"), request);
		}
	}

	private void jsonTxt(String path, HttpServerRequest request) {
		String fullPath = mainPath+path;


		//(path.endsWith(".txt")){
		fileSystem.readFile(fullPath, handler -> {
			if (handler.succeeded()) {
				JsonObject out = new JsonObject();
				out.put("status", true);
				out.put("text", handler.result().toString());
				request.response().end(out.toString());

			} else {
				//ErrorResponse.returnBadFile(request);
			}
		});
		/*}else{
			responses.ErrorResponse.returnBadFile(request);
		}*/


	}

	private boolean isCorrect(String path) {
		if(path.contains(".."))
			return false;
		return true;
	}
}
