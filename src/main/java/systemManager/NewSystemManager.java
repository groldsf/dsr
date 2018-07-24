package systemManager;


import dirElements.Jsonoble;
import dirElements.MyFile;
import dirElements.MyPackage;
import dirElements.TextFile;
import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.file.FileProps;
import io.vertx.core.file.FileSystem;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class NewSystemManager implements InterfaceSystemManager {



	private String mainPath = "C:/";
	private FileSystem fileSystem;

	public NewSystemManager(FileSystem fs) {
		fileSystem = fs;
	}

	public String getMainPath() {
		return mainPath;
	}

	public void setMainPath(String mainPath) {
		this.mainPath = mainPath;
	}



	public void readFile(String path, Handler<AsyncResult<Jsonoble>> handler) {

		path = mainPath + path;

		if(!path.endsWith(".txt")){
			handler.handle(Future.failedFuture("type error"));//rewrite
			return;
		}

		fileSystem.readFile(path, res -> {
			if (res.succeeded()) {
				TextFile textFile = new TextFile(res.result().toString());
				handler.handle(Future.succeededFuture(textFile));

			} else {
				handler.handle(Future.failedFuture(res.cause()));
			}
		});
	}


	public void open(String path, Handler<AsyncResult<Jsonoble>> handler) {

		try {
			path = new URI(path).normalize().toString();
			String fullPath = mainPath + path;
			String finalPath = path;

			fileSystem.props(fullPath, res->{
				if(res.failed()){
					handler.handle(Future.failedFuture(res.cause()));
					return;
				}
				FileProps props = res.result();
				if(props.isDirectory()){
					readPackage(finalPath, handler);
				}else{
					readFile(finalPath, handler);
				}
			});
		} catch (URISyntaxException e) {
			handler.handle(Future.failedFuture(e));
		}
	}


	public void readPackage(String path, Handler<AsyncResult<Jsonoble>> handler) {
		String finalPath = path;
		path = mainPath + path;
		fileSystem.readDir(path, dir->{
			if(dir.failed()) {
				handler.handle(Future.failedFuture(dir.cause()));
				return;
			}
			createJsonDirectory(dir.result(), res ->{
				if(res.succeeded()){
					MyPackage answer = new MyPackage(res.result(), finalPath);
					handler.handle(Future.succeededFuture(answer));
				}else{
					handler.handle(Future.failedFuture(res.cause()));
				}
			});
		});
	}

	private void createJsonDirectory(List<String> dir, Handler<AsyncResult<ArrayList<MyFile>>> handler) {
		ArrayList<MyFile> array = new ArrayList<>();
		ArrayList<Future> list = new ArrayList<>();
		for(String path : dir)
		{
			Future<Void> fut = Future.future();
			list.add(fut);
			fileSystem.props(path, res->{
				if(res.succeeded()) {
					String localPath =  path.substring(mainPath.length());
					array.add(new MyFile(res.result(),localPath));
					fut.complete();
				}else{
					fut.fail(res.cause());
				}
			});
		}
		CompositeFuture.all(list).setHandler(ar -> {
			if (ar.succeeded()) {
				handler.handle(Future.succeededFuture(array));
			} else {
				handler.handle(Future.failedFuture(ar.cause()));
			}
		});
	}
}
