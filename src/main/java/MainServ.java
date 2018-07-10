import io.vertx.core.Vertx;

import java.util.Scanner;

public class MainServ {


	public static void main(String str[]) {


		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new MyVertex());
		//vertx.deployVerticle(new ServAndRouter());

		Scanner in = new Scanner(System.in);
		in.next();

		vertx.close();
	}
}
