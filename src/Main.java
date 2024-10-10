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
			BufferedWriter prettyFile = new BufferedWriter(new FileWriter(args[3]))
		) {
			Morphology m = new Morphology(inFile, structFile);

			m.zero2DAry(m.getZeroFramedAry(), m.getZeroFramedAry().length, m.getZeroFramedAry()[0].length);
			m.loadImg(args[0]);
			prettyFile.write("\nBelow is the output of the `binaryPrettyPrint` method using `zeroFramedAry`:");
			prettyFile.write("\n" + m.getNumImgRows() + " " + m.getNumImgCols() + " " + m.getImgMin() + " " + m.getImgMax());
			m.binaryPrettyPrint(m.getZeroFramedAry(), prettyFile);
			m.zero2DAry(m.getStructAry(), m.getNumStructRows(), m.getNumStructCols());
			m.loadStruct(args[1]);
			prettyFile.write("\nBelow is the output of the `binaryPrettyPrint` method using `structAry`:");
			m.binaryPrettyPrint(m.getStructAry(), prettyFile);
			int choice = Integer.parseInt(args[2]);
			if (choice < 1 || choice > 5) {
				System.err.println("\n`args[2]` must be an integer > 0 and < 6");
				System.exit(1);
			} else if (choice == 1)
				process1(m, prettyFile);
			else if (choice == 2)
				process2(m, prettyFile);
			else if (choice == 3)
				process3(m, prettyFile);
			else if (choice == 4)
				process4(m, prettyFile);
			else
				process5(m, prettyFile);
		} catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private static void process1(Morphology m, BufferedWriter prettyFile) {
		try (BufferedWriter outFile = new BufferedWriter(new FileWriter("dilationOutFile.txt"))) {
			m.zero2DAry(m.getMorphAry(), m.getRowSize(), m.getColSize());
			m.computeDilation(m.zeroFramedAry, m.getMorphAry());
			m.aryToFile(m.getMorphAry(), outFile);
			m.binaryPrettyPrint(m.getMorphAry(), prettyFile);
		} catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private static void process2(Morphology m, BufferedWriter prettyFile) {
		try (BufferedWriter outFile = new BufferedWriter(new FileWriter("erosionOutFile.txt"))) {
			m.zero2DAry(m.getMorphAry(), m.getRowSize(), m.getColSize());
			m.computeErosion(m.zeroFramedAry, m.getMorphAry());
			m.aryToFile(m.getMorphAry(), outFile);
			m.binaryPrettyPrint(m.getMorphAry(), prettyFile);
		} catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private static void process3(Morphology m, BufferedWriter prettyFile) {
		try (BufferedWriter outFile = new BufferedWriter(new FileWriter("openingOutFile.txt"))) {
			m.zero2DAry(m.getMorphAry(), m.getRowSize(), m.getColSize());
			m.computeOpening();
			m.aryToFile(m.getMorphAry(), outFile);
			m.binaryPrettyPrint(m.getMorphAry(), prettyFile);
		} catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private static void process4(Morphology m, BufferedWriter prettyFile) {
		try (BufferedWriter outFile = new BufferedWriter(new FileWriter("closingOutFile.txt"))) {
			m.zero2DAry(m.getMorphAry(), m.getRowSize(), m.getColSize());
			m.computeClosing();
			m.aryToFile(m.getMorphAry(), outFile);
			m.binaryPrettyPrint(m.getMorphAry(), prettyFile);
		} catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private static void process5(Morphology m, BufferedWriter prettyFile) {
		try (BufferedWriter dilationFile = new BufferedWriter(new FileWriter("dilationOutFile.txt"));
			 BufferedWriter erosionFile = new BufferedWriter(new FileWriter("erosionOutFile.txt"));
			 BufferedWriter openingFile = new BufferedWriter(new FileWriter("openingOutFile.txt"));
			 BufferedWriter closingFile = new BufferedWriter(new FileWriter("closingOutFile.txt"))
		) {
			prettyFile.write("\n** Part of `process5` (all operations) **:");
			prettyFile.write("\n** Below is a call to `process1` (the Dilation operation) **:");
			process1(m, prettyFile);
			prettyFile.write("\n** Below is a call to `process2` (the Erosion operation) **:");
			process2(m, prettyFile);
			prettyFile.write("\n** Below is a call to `process3` (the Opening operation) **:");
			process3(m, prettyFile);
			prettyFile.write("\n** Below is a call to `process4` (the Closing operation) **:");
			process4(m, prettyFile);

		} catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}

	}

	private static void validateArguments(String[] args) {
		if (args.length != 4) {
			System.err.println("\nYou must have exactly 4 arguments: `inFile.txt` `structFile.txt` `choice#` `prettyFile.txt`.");
			System.exit(1);
		}
		int choice = Integer.parseInt(args[2]);
		if (choice < 1 || choice > 5) {
			System.err.println("\n`args[2]` must be be > 0 and < 6.");
			System.exit(1);
		}
	}
}