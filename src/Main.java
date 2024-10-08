import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main
{
	public static void main(String args[]) throws IOException {
		validateArguments(args);
		try (
			BufferedReader inFile = new BufferedReader(new FileReader(args[0]));
			BufferedReader structFile = new BufferedReader(new FileReader(args[1]));
			BufferedReader prettyFile = new BufferedReader(new FileReader(args[3]));
		) {
			Morphology morphology = new Morphology(inFile, structFile);
		} catch (IOException ioException) {

		}
	}

	private static void validateArguments(String[] args) {
		if (args.length != 4) {
			System.err.println("\nYou must have exactly 4 arguments: inFile.txt structFile.txt choice# prettyFile.txt");
			System.exit(1);
		}
	}
}