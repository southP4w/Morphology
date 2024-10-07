import java.io.BufferedReader;
import java.io.IOException;

public class Morphology
{
	/* Get from `inFile.txt` and `structFile`: */
	private int numImgRows, numImgCols, imgMin, imgMax;	// from `inFile.txt`
	private int numStructRows, numStructCols, structMin, structMax;	// from `structFile.txt`
	private int rowOrigin, colOrigin;	// from `structFile.txt`

	private int rowFrameSize, colFrameSize;    // set to (`numStructRows`/2), (`numStructCols`/2)
	private int extraRows, extraCols;    // set to (`rowFrameSize`*2), (`colFrameSize`*2)
	private int rowSize, colSize;    // set to (`numImgRows`+`extraRows`), (`numImgCols`+`extraCols`)
	private int[][] zeroFramedAry;    // a dynamically-allocated 2D array of size [`rowSize`]×[`colSize`]
	private int[][] morphAry;    // Same size as `zeroFramedAry`
	private int[][] tempAry;    // Same size as `zeroFramedAry`, to be used as the intermediate result within opening/closing operations
	private int[][] structAry;    // a dynamically-allocated 2D array of size [`numStructRows`]×[`numStructCols`]

	public Morphology() {

	}

	public static void main(String args[]) throws IOException {
		validateArguments(args);
	}

	private void initImgValuesFromHeader(BufferedReader inFile) throws IOException {
		String headerLine = inFile.readLine();
		if (headerLine != null) {
			String[] headerTokens = headerLine.split("\\s+");
			numImgRows = Integer.parseInt(headerTokens[0]);
			numImgCols = Integer.parseInt(headerTokens[1]);
			numStructRows = Integer.parseInt(headerTokens[2]);
			numStructCols = Integer.parseInt(headerTokens[3]);
		}
	}

	private void initStructValuesFromHeader(BufferedReader structFile) throws IOException {

	}

	private static void validateArguments(String[] args) {
		if (args.length!=4) {
			System.err.println("\nYou must have exactly 4 arguments: inFile.txt structFile.txt choice# prettyFile.txt");
			System.exit(1);
		}
	}
}