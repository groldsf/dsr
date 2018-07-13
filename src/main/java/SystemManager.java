import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.file.AsyncFile;
import io.vertx.rxjava.core.file.FileProps;
import io.vertx.rxjava.core.file.FileSystem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SystemManager {

	private String mainPath = "D:\\victor\\ideaTest\\WORK\\dsr\\dsr\\";
	private Vertx vertx;
	private FileSystem fileSystem;
	public SystemManager(Vertx vertx) {
		this.vertx = vertx;
		fileSystem = new FileSystem(vertx
				.fileSystem());
	}

	public void jsonPackage(String path, HttpServerRequest request) {
		path = path.replace('/','\\');
		String fullPath = mainPath + path;
		fileSystem.readDir(fullPath, dir->{
			if(dir.failed())
				ErrorResponse.returnError(request);
			else{
				createJsonDirectory(dir.result(),request);
			}
		});
	}

	private void createJsonDirectory(List<String> dir, HttpServerRequest request) {

		JsonArray array = new JsonArray();
		fileSystem = new FileSystem(vertx.fileSystem());
		ArrayList<Future> list = new ArrayList<>();
		for(String path : dir)
		{
			Future<FileProps> fut = Future.future();
			list.add(fut);
			//fut.setHandler(
			fileSystem.props(path, res->{
				if(res.succeeded()) {
					FileProps prop = res.result();
					JsonObject file = new JsonObject();

					file.put("type", prop.isDirectory() ? "directory" : "file");
					file.put("name", path.substring(path.lastIndexOf("\\")+1));
					file.put("fullDirectory", path.substring(mainPath.length()));
					file.put("size", prop.size());
					file.put("lastModifiedTime", prop.lastModifiedTime());
					file.put("creationTime", prop.creationTime());
					file.put("lastAccessTime", prop.lastAccessTime());
					array.add(file);
					fut.complete();
				}else{
					fut.fail("");
				}

			});
			//System.out.println();
		}
		CompositeFuture.all(list).setHandler(ar -> {
			if (ar.succeeded()) {

				JsonObject out = new JsonObject();
				out.put("status", true);
				out.put("answer", array);
				request.response().end(out.toString());
			} else {
				ErrorResponse.returnError(request);
			}
		});
	}

	private boolean isCorrect(String path) {
		if(path.contains(".."))
			return false;
		return true;
	}

	public void getPackage(HttpServerRequest request, JsonObject in) {
		if(!in.containsKey("path"))
			ErrorResponse.returnError(request);
		else {
			String path = in.getString("path");
			if (!isCorrect(path))
				ErrorResponse.returnError(request);
			else
				jsonPackage(path, request);
		}
	}

	public  void getFile(HttpServerRequest request, JsonObject in){
		if(!in.containsKey("path"))
			ErrorResponse.returnError(request);
		else {
			String path = in.getString("path");
			if (!isCorrect(path))
				ErrorResponse.returnError(request);
			else
				jsonTxt(in.getString("path"), request);
		}
	}

	private void jsonTxt(String path, HttpServerRequest request) {
		String fullPath = mainPath+path;

		fileSystem.readFile(fullPath, handler -> {
			if (handler.succeeded()) {
				JsonObject out = new JsonObject();
				out.put("status", true);
				out.put("text", handler.result().toString());
				request.response().end(out.toString());

			} else {
				ErrorResponse.returnError(request);
			}
		});
	}

}
