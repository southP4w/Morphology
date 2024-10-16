import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;

public class Morphology
{
	private int numImgRows, numImgCols, imgMin, imgMax;              // from `inFile.txt`
	private int numStructRows, numStructCols, structMin, structMax;  // from `structFile.txt`
	private int rowOrigin, colOrigin;                                // from `structFile.txt`

	private int rowFrameSize, colFrameSize; // set to (`numStructRows`/2), (`numStructCols`/2)
	private int extraRows, extraCols;       // set to (`rowFrameSize`*2), (`colFrameSize`*2)
	private int rowSize, colSize;           // set to (`numImgRows`+`extraRows`), (`numImgCols`+`extraCols`)
	int[][] zeroFramedAry;                  // a dynamically-allocated 2D array of size [`rowSize`]×[`colSize`]
	private int[][] morphAry;               // Same size as `zeroFramedAry`
	private int[][] tempAry;                // Same size as `zeroFramedAry`, to be used as the intermediate result within opening/closing operations
	private int[][] structAry;              // a dynamically-allocated 2D array of size [`numStructRows`]×[`numStructCols`]

	public Morphology(BufferedReader inFile, BufferedReader structFile) throws IOException {
		initImgValuesFromHeader(inFile);
		initStructValuesFromHeader(structFile);
		rowFrameSize = (numStructRows/2);
		colFrameSize = (numStructCols/2);
		extraRows = (rowFrameSize*2);
		extraCols = (colFrameSize*2);
		rowSize = (numImgRows + extraRows);
		colSize = (numImgCols + extraCols);
		zeroFramedAry = new int[rowSize][colSize];
		morphAry = new int[rowSize][colSize];
		tempAry = new int[rowSize][colSize];
		structAry = new int[numStructRows][numStructCols];
	}

	public int getNumImgRows() {return numImgRows;}
	public void setNumImgRows(int numImgRows) {this.numImgRows = numImgRows;}
	public int getNumImgCols() {return numImgCols;}
	public void setNumImgCols(int numImgCols) {this.numImgCols = numImgCols;}
	public int getImgMin() {return imgMin;}
	public void setImgMin(int imgMin) {this.imgMin = imgMin;}
	public int getImgMax() {return imgMax;}
	public void setImgMax(int imgMax) {this.imgMax = imgMax;}
	public int getNumStructRows() {return numStructRows;}
	public void setNumStructRows(int numStructRows) {this.numStructRows = numStructRows;}
	public int getNumStructCols() {return numStructCols;}
	public void setNumStructCols(int numStructCols) {this.numStructCols = numStructCols;}
	public int getStructMin() {return structMin;}
	public void setStructMin(int structMin) {this.structMin = structMin;}
	public int getStructMax() {return structMax;}
	public void setStructMax(int structMax) {this.structMax = structMax;}
	public int getRowOrigin() {return rowOrigin;}
	public void setRowOrigin(int rowOrigin) {this.rowOrigin = rowOrigin;}
	public int getColOrigin() {return colOrigin;}
	public void setColOrigin(int colOrigin) {this.colOrigin = colOrigin;}
	public int getRowFrameSize() {return rowFrameSize;}
	public void setRowFrameSize(int rowFrameSize) {this.rowFrameSize = rowFrameSize;}
	public int getColFrameSize() {return colFrameSize;}
	public void setColFrameSize(int colFrameSize) {this.colFrameSize = colFrameSize;}
	public int getExtraRows() {return extraRows;}
	public void setExtraRows(int extraRows) {this.extraRows = extraRows;}
	public int getExtraCols() {return extraCols;}
	public void setExtraCols(int extraCols) {this.extraCols = extraCols;}
	public int getRowSize() {return rowSize;}
	public int getColSize() {return colSize;}
	public int[][] getZeroFramedAry() {return zeroFramedAry;}
	public void setZeroFramedAry(int[][] zeroFramedAry) {this.zeroFramedAry = zeroFramedAry;}
	public int[][] getMorphAry() {return morphAry;}
	public void setMorphAry(int[][] morphAry) {this.morphAry = morphAry;}
	public int[][] getTempAry() {return tempAry;}
	public void setTempAry(int[][] tempAry) {this.tempAry = tempAry;}
	public int[][] getStructAry() {return structAry;}
	public void setStructAry(int[][] structAry) {this.structAry = structAry;}

	public int[][] zero2DAry(int[][] ary, int nRows, int nCols) {
		for (int i = 0; i < nRows; i++)
			for (int j = 0; j < nCols; j++)
				ary[i][j] = 0;

		return ary;
	}

	public void loadImg(String inFile) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(inFile));

		bufferedReader.readLine();    // skip header
		for (int i = 0; i < numImgRows; i++) {
			String line = bufferedReader.readLine();
			String[] pixelValues = line.split("\\s+");
			for (int j = 0; j < numImgCols; j++)
				zeroFramedAry[i + rowFrameSize][j + colFrameSize] = Integer.parseInt(pixelValues[j]);
		}

		bufferedReader.close();
	}

	public void loadStruct(String structFile) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(structFile));

		for (int i = 0; i < 2; i++) bufferedReader.readLine(); // skip header and origin values
		for (int i = 0; i < numStructRows; i++) {              // load `structFile` into `structAry`
			String line = bufferedReader.readLine();
			String[] pixelValues = line.split("\\s+");
			for (int j = 0; j < numStructCols; j++)
				structAry[i][j] = Integer.parseInt(pixelValues[j]);
		}

		bufferedReader.close();
	}

	public void binaryPrettyPrint(int[][] inAry, BufferedWriter outFile) throws IOException {
		outFile.write('\n');
		for (int i = 0; i < inAry.length; i++) {
			for (int j = 0; j < inAry[0].length; j++)
				if (inAry[i][j] == 0)
					outFile.write(". ");
				else
					outFile.write("1 ");
			outFile.write('\n');
		}
	}

	public void aryToFile(int[][] inAry, BufferedWriter fileOut) throws IOException {
		fileOut.write(numImgRows + " " + numImgCols + " " + imgMin + " " + imgMax + "\n");
		for (int i = 0; i < rowSize; i++) {
			for (int j = 0; j < colSize; j++)
				fileOut.write(inAry[i][j] + " ");
			fileOut.write("\n");
		}
	}

	public void computeOpening() {
		computeErosion(zeroFramedAry, tempAry);
		computeDilation(tempAry, morphAry);
	}

	public void computeClosing() {
		computeDilation(zeroFramedAry, tempAry);
		computeErosion(tempAry, morphAry);
	}

	public void computeDilation(int[][] inAry, int[][] outAry) {
		for (int i = rowFrameSize; i < rowSize - rowFrameSize; i++)
			for (int j = colFrameSize; j < colSize - colFrameSize; j++)
				if (inAry[i][j] > 0)
					onePixelDilation(i, j, inAry, outAry);
	}

	public void computeErosion(int[][] inAry, int[][] outAry) {
		for (int i = rowFrameSize; i < rowSize - rowFrameSize; i++)
			for (int j = colFrameSize; j < colSize - colFrameSize; j++)
				if (inAry[i][j] > 0)
					onePixelErosion(i, j, inAry, outAry);
	}

	private void onePixelDilation(int i, int j, int[][] inAry, int[][] outAry) {
		int iOffset = i - rowOrigin;
		int jOffset = j - colOrigin;
		for (int r = 0; r < numStructRows; r++)
			for (int c = 0; c < numStructCols; c++)
				if (structAry[r][c] > 0)
					outAry[iOffset + r][jOffset + c] = 1;
	}

	private void onePixelErosion(int i, int j, int[][] inAry, int[][] outAry) {
		int iOffset = i - rowOrigin;
		int jOffset = j - colOrigin;
		boolean matchFlag = true;
		for (int r = 0; r < numStructRows && matchFlag; r++)
			for (int c = 0; c < numStructCols && matchFlag; c++)
				if (structAry[r][c] > 0 && inAry[iOffset + r][jOffset + c] <= 0)
					matchFlag = false;
		if (matchFlag)
			outAry[i][j] = 1;
		else
			outAry[i][j] = 0;
	}

	private void initImgValuesFromHeader(BufferedReader inFile) throws IOException {
		String header = inFile.readLine();
		if (header != null) {
			String[] headerTokens = header.split("\\s+");
			numImgRows = Integer.parseInt(headerTokens[0]);
			numImgCols = Integer.parseInt(headerTokens[1]);
			imgMin = Integer.parseInt(headerTokens[2]);
			imgMax = Integer.parseInt(headerTokens[3]);
		}
	}

	private void initStructValuesFromHeader(BufferedReader structFile) throws IOException {
		String header = structFile.readLine();
		if (header != null) {        // get header values (line 1)
			String[] headerTokens = header.split("\\s+");
			numStructRows = Integer.parseInt(headerTokens[0]);
			numStructCols = Integer.parseInt(headerTokens[1]);
			structMin = Integer.parseInt(headerTokens[2]);
			structMax = Integer.parseInt(headerTokens[3]);
		}
		String origin = structFile.readLine();
		if (origin != null) {        // get origin values (line 2)
			String[] originTokens = origin.split("\\s+");
			rowOrigin = Integer.parseInt(originTokens[0]);
			colOrigin = Integer.parseInt(originTokens[1]);
		}
	}
}