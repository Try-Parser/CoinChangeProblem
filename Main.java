import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
    	Deno deno = new Deno();

        System.out.println("Waiting for the Command: ");
        System.out.println("-[Show] Show");
        System.out.println("-[Put] Put");
        System.out.println("-[Take] Take");
        System.out.println("-[Change] Change");
        System.out.println("-[Quit] Quit");

  		deno.start();
    }
}