import io.vertx.core.Vertx;

import java.util.Scanner;

public class Main {
	public static void main(String str[]) {

		Vertx vertx = Vertx.vertx();
		Scanner in = new Scanner(System.in);
		//String dir = TestSupport.getTestDir();
		String dir = in.nextLine();

		vertx.deployVerticle(new VertexRouter().setPathMainDir(dir));


		in.next();
		vertx.close();
	}
}
