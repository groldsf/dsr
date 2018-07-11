import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.file.FileProps;
import io.vertx.rxjava.core.file.FileSystem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SystemManager {

	public String mainPath = "D:\\victor\\ideaTest\\WORK\\dsr\\dsr";
	private Vertx vertx;

	public SystemManager(Vertx vertx) {
		this.vertx = vertx;

	}

	public void jsonPackage(String path, HttpServerRequest request) {
		if(!isCorrect(path)){
			return;
		}
		String fullPath = mainPath+path;

		FileSystem file = new FileSystem(vertx.fileSystem());


		//////////////////

		file.exists(fullPath, existsResult -> {
			//проверка на существование
			if(existsResult.failed() || !existsResult.result()){
				ErrorResponse.returnError(request);
			}else{
				file.lprops(fullPath, propsResult -> {
					//проверка на директорию
					if(propsResult.failed() || !propsResult.result().isDirectory()){
						ErrorResponse.returnError(request);
					}else{
						file.readDir(fullPath, dir->{
							if(dir.failed())
								ErrorResponse.returnError(request);
							else{
								createJsonDirectory(dir.result(),request);
							}
						});
					}
				});
			}
		});
	}

	private void createJsonDirectory(List<String> dir, HttpServerRequest request) {

		JsonArray array = new JsonArray();
		FileSystem fileSystem = new FileSystem(vertx.fileSystem());
		ArrayList<Future> list = new ArrayList<>();
		for(String path : dir)
		{
			Future<FileProps> fut = Future.future();
			list.add(fut);
			fut.setHandler(res->{
				if(res.succeeded()){
					FileProps prop = res.result();

					JsonObject file = new JsonObject();

					file.put("type",prop.isDirectory()? "directory":"file");
					file.put("creationTime",prop.creationTime());
					file.put("size",prop.size());
					file.put("lastModifiedTime",prop.lastModifiedTime());
					file.put("lastAccessTime", prop.lastAccessTime());
					file.put("fullDirectory", path);
					///////////// у vertx не нашёл, свой парсер мб работает дольше, протестить
					file.put("name",new File(path).getName());
					array.add(file);
				}
			});
			fileSystem.props(path,fut.completer());
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

		jsonPackage(in.getString("path"),request);
	}



}
