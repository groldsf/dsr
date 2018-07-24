import io.vertx.core.Vertx;
import newServer.TestVertexRouter;

import java.util.Scanner;

public class MainServ {


	public static void main(String str[]) {


		Vertx vertx = Vertx.vertx();
		//vertx.deployVerticle(new oldServer.MyVertex());
		vertx.deployVerticle(new TestVertexRouter().setPathMainDir(MyTestSupport.getTestDir()));

		Scanner in = new Scanner(System.in);
		in.next();

		vertx.close();
	}
}
