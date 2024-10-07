import java.io.IOException;

public class Main
{
	public static void main(String args[]) throws IOException {
		validateArguments(args);
		Morphology morphology = new Morphology();


	}

	private static void validateArguments(String[] args) {
		if (args.length!=4) {
			System.err.println("\nYou must have exactly 4 arguments: inFile.txt structFile.txt choice# prettyFile.txt");
			System.exit(1);
		}
	}
}