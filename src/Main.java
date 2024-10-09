import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main
{
	public static void main(String[] args) {
		validateArguments(args);
		try (
			BufferedReader inFile = new BufferedReader(new FileReader(args[0]));
			BufferedReader structFile = new BufferedReader(new FileReader(args[1]));
			BufferedWriter prettyFile = new BufferedWriter(new FileWriter(args[3]));
			BufferedWriter outFile = new BufferedWriter(new FileWriter("dilationOutFile.txt"))
		) {
			Morphology m = new Morphology(inFile, structFile);

			m.zero2DAry(m.getZeroFramedAry(), m.getZeroFramedAry().length, m.getZeroFramedAry()[0].length);
			m.loadImg(args[0]);
			prettyFile.write("Below is the output of the `binaryPrettyPrint` method using `zeroFramedAry`:\n");
			m.binaryPrettyPrint(m.getZeroFramedAry(), prettyFile);
			m.zero2DAry(m.getStructAry(), m.getNumStructRows(), m.getNumStructCols());
			m.loadStruct(args[1]);
			prettyFile.write("Below is the output of the `binaryPrettyPrint` method using `structAry`:\n");
			m.binaryPrettyPrint(m.getStructAry(), prettyFile);
		} catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private static void validateArguments(String[] args) {
		if (args.length != 4) {
			System.err.println("\nYou must have exactly 4 arguments: inFile.txt structFile.txt choice# prettyFile.txt");
			System.exit(1);
		}
	}
}