public class Morphology
{
	private int numImgRows, numImgCols, imgMin, imgMax;
	private int numStructRows, numStructCols, structMin, structMax;
	private int rowOrigin, colOrigin;
	private int rowFrameSize, colFrameSize;    // set to (`numStructRows`/2), (`numStructCols`/2)
	private int extraRows, extraCols;    // set to (`rowFrameSize`*2), (`colFrameSize`*2)
	private int rowSize, colSize;    // set to (`numImgRows`+`extraRows`), (`numImgCols`+`extraCols`)
	private int[][] zeroFramedAry;    // a dynamically-allocated 2D array of size [`rowSize`]×[`colSize`]
	private int[][] morphAry;    // Same size as `zeroFramedAry`
	private int[][] tempAry;    // Same size as `zeroFramedAry`, to be used as the intermediate result within opening/closing operations
	private int[][] structAry;    // a dynamically-allocated 2D array of size [`numStructRows`]×[`numStructCols`]

	
}