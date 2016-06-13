/************************************************************************************************************************
* Developer: Minhas Kamal(BSSE-0509, IIT, DU)																			*
* Date: 08-Sep-2014																										*
*************************************************************************************************************************/

package com.minhaskamal.alphabetRecognizer.weightedPixel;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;

import com.minhaskamal.egami.matrix.Matrix;

public class WeightedStandardPixelTrainer {
	public static final int[] imageDataSize60 = new int[]{60, 60};
	public static final int[] imageDataSize90 = new int[]{60, 60};
	public static final int[] imageDataSize120 = new int[]{60, 60};
	
	private int[] imageSize;
	
	private WeightedStandardImage weightedStandardImage;
	
	
	public WeightedStandardPixelTrainer(){
		this.imageSize = imageDataSize90;
		this.weightedStandardImage = new WeightedStandardImage();
	}
	
	public WeightedStandardPixelTrainer(int[] imageSize){
		this.imageSize = imageSize;
		this.weightedStandardImage = new WeightedStandardImage();
	}
	
	
	//TRAIN/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	 * @param imageFilePaths
	 * @param ids
	 * @return
	 * @throws Exception 
	 */
	public void train(String[] imageFilePaths, Integer[] ids) throws Exception{
		if(imageFilePaths.length!=ids.length){	//data check
			System.out.println("Incompatible data.");
			return ;
		}
		
		int[] variety = varietyIn(ids);
		int types = variety[variety.length-1];
		int standardImageRow = imageSize[0], standardImageCol = imageSize[1];
		WeightedStandardImage weightedStandardImage = new WeightedStandardImage(types, imageSize);
		
		
		for(int i=0; i<types; i++){
			weightedStandardImage.setId(i, variety[i]);
		}
		
		
		Matrix mat;
		int typeNo=0, index=0;
		for(String imageFilePath : imageFilePaths){
			mat = new Matrix(imageFilePath, Matrix.BLACK_WHITE);
			mat = resize(mat, imageSize[1], imageSize[0]);
			mat = toMedial(mat);
			
			double sumValue = 0;
			int value=0;
			
			for(int i=0; i<types; i++){
				if(weightedStandardImage.getId(i)==ids[index]){
					typeNo=i;
					break;
				}
			}
			
			for(int row=0; row<standardImageRow; row++){
				for(int col=0; col<standardImageCol; col++){
					sumValue = (weightedStandardImage.getStandardImages(typeNo, row, col) *
							weightedStandardImage.getWeight(typeNo)) +
							mat.pixels[row][col][0];
					
					value = (int) sumValue / (weightedStandardImage.getWeight(typeNo)+1);
					
					weightedStandardImage.setStandardImages(typeNo, row, col, (short) value);
				}
			}
			
			weightedStandardImage.incrementWeight(typeNo);
			System.out.println(index + ": image No: " + weightedStandardImage.getWeight(typeNo));	//show progress
			
			index++;
		}
		
		this.weightedStandardImage = weightedStandardImage;
		
		return ;
	}
	
	/**
	 * 
	 * @param integers
	 * @return
	 */
	private int[] varietyIn(Integer[] integers){
		int length = integers.length;
		int[] member = new int[length+1];
		int variety = 0;
		boolean flagMatched = false;
		for(int i=0; i<length; i++){
			member[i]=integers[i];
			flagMatched=false;
			
			for(int j=0; j<variety; j++){
				if(member[j]==member[i]){
					flagMatched=true;
					break;
				}
			}
			
			if(!flagMatched){
				member[variety]=member[i];
				variety++;
			}
		}
		
		member[length] = variety;
		
		return member;
	}
	
	/**
	 * 
	 * @param mat
	 * @return
	 */
	private Matrix toMedial(Matrix mat){
		Matrix mat2 = new Matrix(mat.getRows(), mat.getCols(), mat.getType());
		
		double mediumPixel = 0;
		double sumOfPixelByRow = 0;
		double sumOfPixelByColInRow = 0;
		
		int rows = mat.getRows();
		int cols = mat.getCols();
		
		for(int x=0; x<rows; x++){
			sumOfPixelByRow=0;
			for(int y=0; y<cols; y++){
				sumOfPixelByRow = sumOfPixelByRow + mat.pixels[x][y][0];
			}
			
			sumOfPixelByRow = sumOfPixelByRow/cols;
			sumOfPixelByColInRow = sumOfPixelByColInRow + sumOfPixelByRow;
		}
		
		mediumPixel = sumOfPixelByColInRow/rows;
		
		int perfectMediumPixel = 255/2;
		
		
		int mediumValue = 0;
		int pixelValue = 0;
		for(int x=0; x<rows; x++){
			for(int y=0; y<cols; y++){
				pixelValue = (int) mat.pixels[x][y][0];
				
				mediumValue = (int) (pixelValue*perfectMediumPixel/mediumPixel);
				
				if(mediumValue>255){
					mediumValue=255;
				}
				
				mat2.pixels[x][y][0] = mediumValue;
			}
		}
		
		return mat2;
	}
	
	
	//LOAD/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	 * @param filePath
	 */
	public void load(String filePath){
		
		String mainString = readFile(filePath);
		
		if(!mainString.substring(0, 24).equals("<WeightedStandardImage/>")){
			//file is corrupted
			return ;
		}
		
		int startIndex=0;
		int stopIndex=0;
		
		//total image types
		startIndex = mainString.indexOf("<types>", 24);
		stopIndex = mainString.indexOf("</types>", 24);
		int types = Integer.parseInt(mainString.substring(startIndex+7, stopIndex));
		
		//all image size
		startIndex = mainString.indexOf("<size>", stopIndex);
		stopIndex = mainString.indexOf(",", stopIndex);
		int width = Integer.parseInt(mainString.substring(startIndex+6, stopIndex));
		
		startIndex = stopIndex+1;
		stopIndex = mainString.indexOf("</size>", stopIndex);	
		int height = Integer.parseInt(mainString.substring(startIndex, stopIndex));

		
		imageSize = new int[]{height, width};
		
		
		WeightedStandardImage weightedStandardImage = new WeightedStandardImage(types, imageSize);
		
		stopIndex = mainString.indexOf("<data>", stopIndex);
		for(int i=0; i<types; i++){
			//image id
			startIndex = mainString.indexOf("<id>", stopIndex);
			stopIndex = mainString.indexOf("</id>", stopIndex);
			int id = Integer.parseInt(mainString.substring(startIndex+4, stopIndex));;
			weightedStandardImage.setId(i, id);
			
			//image number or weight
			startIndex = mainString.indexOf("<weight>", stopIndex);
			stopIndex = mainString.indexOf("</weight>", stopIndex);
			int weight = Integer.parseInt(mainString.substring(startIndex+8, stopIndex));;
			weightedStandardImage.setWeight(i, weight);
			
			startIndex = mainString.indexOf("<stdImage>", stopIndex);
			stopIndex = startIndex+10;
//			stopIndex = mainString.indexOf("</stdImage>", stopIndex);
//			System.out.println(mainString.substring(startIndex+11, stopIndex-1));
			
			short pixel;
			for(int row=0, col; row<width; row++){
				for(col=0; col<height; col++){
					//standard image data
					startIndex = stopIndex + 1;
					stopIndex = mainString.indexOf(',', startIndex);
//					System.out.println(mainString.substring(startIndex, stopIndex));
					pixel = Short.parseShort(mainString.substring(startIndex, stopIndex));
					weightedStandardImage.setStandardImages(i, row, col, pixel);
				}
				
				stopIndex++;
			}
		}
		
		
		/*///test only
		System.out.println("type: " + types + "\nsize: " + width + "," + height + "\n ");
		for(int i=0; i<types; i++){
			System.out.println(i + "id: " + weightedStandardImage.getId(i));
			System.out.println(i + "weight: " + weightedStandardImage.getWeight(i));
			System.out.println(i + "data: " + weightedStandardImage.getStandardImages(i).dump());
		}
		/**/
		
		this.weightedStandardImage = weightedStandardImage;
		
		return ;
	}
	
	/**
	 * 
	 * @param filePath
	 * @return
	 */
	private String readFile(String filePath) {
		String tempString = new String(); // for temporary data store
		String Information = new String(); // #contains the full file

		try {
			BufferedReader mainBR = new BufferedReader(new FileReader(filePath)); 

			tempString = mainBR.readLine();
			while (tempString!=null) { // reading step by step
				Information = Information + tempString + "\n";
				tempString = mainBR.readLine();
			}

			mainBR.close(); // closing the file

		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return Information;
	}
	
	
	//PREDICT//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	 * @param matSample
	 * @return
	 */
	public int predict(Matrix matSample){
		int id = 0;
		float similarity = 0;
		
		matSample = resize(matSample, imageSize[1], imageSize[0]);
//		matSample = toMedial(matSample);
		
		int types = weightedStandardImage.getTypes();
		for(int i=0; i<types; i++){
			float currentSimilarity = compareMatDiv(weightedStandardImage.getStandardImages(i), matSample) + 
					compareMatDif(weightedStandardImage.getStandardImages(i), matSample);
			
			//System.out.println(currentSimilarity);	///test
			
			if(currentSimilarity > similarity){
				similarity = currentSimilarity;
				id = weightedStandardImage.getId(i);
			}
		}
		
		if(similarity<20){	//if image is not recognized
			return -1;
		}
		
		return id;
	}
	
	/**
	 * 
	 * @param mat1
	 * @param mat2
	 * @return
	 */
	private float compareMatDiv(Matrix mat1, Matrix mat2){
		if(mat1.getRows()!=mat2.getRows() || mat1.getCols()!=mat2.getCols()){
			System.out.println("Incompatible Data!");
			return -1;
		}
		
		float similarity = 0;
		
		int rows = mat1.getRows();
		int cols = mat1.getCols();
		
		float pixel1 = 0;
		float pixel2 = 0;
		float pixelSimilarity = 0;
		float sumOfSimilarityByRow = 0;
		float sumOfSimilarityByColInRow = 0;
		
		for(int row=0; row<rows; row++){
			sumOfSimilarityByRow = 0;
			
			for(int col=0; col<cols; col++){
				pixel1 = (float) mat1.pixels[row][col][0];
				pixel2 = (float) mat2.pixels[row][col][0];
				
				if(pixel1==pixel2){
					pixelSimilarity = 100;
				}else if(pixel1>pixel2){
					pixelSimilarity = ( (255+pixel2) / (255+pixel1) ) * 100;
				}else{
					pixelSimilarity = (pixel1/pixel2) * 100;
				}
				
				sumOfSimilarityByRow = sumOfSimilarityByRow + pixelSimilarity;
			}
			
			sumOfSimilarityByRow = sumOfSimilarityByRow/cols;
			sumOfSimilarityByColInRow = sumOfSimilarityByColInRow + sumOfSimilarityByRow;
		}
		
		similarity = sumOfSimilarityByColInRow/rows;
		
		
		if(similarity>50){
			similarity = similarity-50;
		}else{
			similarity = 50-similarity;
		}
		
		
		return similarity;
	}
	
	/**
	 * 
	 * @param mat1
	 * @param mat2
	 * @return
	 */
	private float compareMatDif(Matrix mat1, Matrix mat2){
		if(mat1.getRows()!=mat2.getRows() || mat1.getCols()!=mat2.getCols()){
			System.out.println("Incompatible Data!");
			return -1;
		}
		
		float similarity = 0;
		
		int rows = mat1.getRows();
		int cols = mat1.getCols();
		
		float pixel1 = 0;
		float pixel2 = 0;
		float pixelSimilarity = 0;
		float sumOfSimilarityByRow = 0;
		float sumOfSimilarityByColInRow = 0;
		
		for(int row=0; row<rows; row++){
			sumOfSimilarityByRow = 0;
			
			for(int col=0; col<cols; col++){
				pixel1 = (float) mat1.pixels[row][col][0];
				pixel2 = (float) mat2.pixels[row][col][0];
				
				if(pixel1==pixel2){
					pixelSimilarity = 100;
				}else if(pixel1<pixel2){
					pixelSimilarity = (255-pixel2+pixel1)/255 * 100;
				}else{
					pixelSimilarity = (255-pixel1+pixel2)/255 * 100;
				}
				
				sumOfSimilarityByRow = sumOfSimilarityByRow + pixelSimilarity;
			}
			
			sumOfSimilarityByRow = sumOfSimilarityByRow/cols;
			sumOfSimilarityByColInRow = sumOfSimilarityByColInRow + sumOfSimilarityByRow;
		}
		
		similarity = sumOfSimilarityByColInRow/rows;
		
		
		if(similarity>50){
			similarity = similarity-50;
		}else{
			similarity = 50-similarity;
		}
		
		
		return similarity;
	}
	
	
	//MATRIX_RESIZER////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * @return may not be the same type as the input <code>Matrix</code>
	 */
	public Matrix resize(Matrix matrix, int scaledWidth, int scaledHeight){
		BufferedImage originalBufferedImage = Matrix.matrixToBufferedImage(matrix);
		BufferedImage scaledBufferedImage = new BufferedImage(scaledWidth, scaledHeight, originalBufferedImage.getType());
	
		Graphics2D g = scaledBufferedImage.createGraphics();
		g.drawImage(originalBufferedImage, 0, 0, scaledWidth, scaledHeight, null); 
		g.dispose();
		
		return Matrix.bufferedImageToMatrix(scaledBufferedImage, matrix.getType());
	}
	
	//GETTER_SETTER////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public WeightedStandardImage getWeightedStandardImage(){
		return weightedStandardImage;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**///test only
	public static void main(String[] args) {
		
		//list of image files//////////////////////////////////////////////////////////////////////////////////////////
		/*String[] stringFilePaths = {"resource/Face_Male_Female/images1", "resource/Face_Male_Female/images2"};
		
		ArrayList<String> filePathList = new ArrayList<String>();
		ArrayList<Integer> idList = new ArrayList<Integer>();
		
		int id=0;
		for(String stringFilePath: stringFilePaths){
			
			File[] files = new File(stringFilePath).listFiles();
			
			for(File file: files){
				filePathList.add(file.getAbsolutePath());
				idList.add(id);
			}
			
			id++;
		}
		
		String[] filePaths = new String[filePathList.size()];
		filePathList.toArray(filePaths);
		Integer[] ids = new Integer[idList.size()];
		idList.toArray(ids);
		*/
		
		///test
		/*for(int i=filePaths.length-1; i>=0; i--){
			System.out.println("filePaths: " + filePaths[i]);
			System.out.println("ids: " + ids[i]);
		}*/
		
		
		//train and predict////////////////////////////////////////////////////////////////////////////////////////////
		WeightedStandardPixelTrainer weightedStandardPixelTrainer = new WeightedStandardPixelTrainer();
		/*weightedStandardPixelTrainer.train(filePaths, ids);
		WeightedStandardImage weightedStandardImage = weightedStandardPixelTrainer.getWeightedStandardImage();
		weightedStandardImage.saveKnowledge("resource/Face_Male_Female/Knowledge.log");*/
		//experience file
		weightedStandardPixelTrainer.load("res/knowledge/KnowledgeAlphabet.log");
		/*WeightedStandardImage weightedStandardImage = weightedStandardPixelTrainer.getWeightedStandardImage();
		System.out.println(weightedStandardImage.dump());*/
		
		//sample file
		String imageFilePath = "res/sample/test.png";
		try {
			Matrix mat = new Matrix(imageFilePath, Matrix.BLACK_WHITE);
			int prediction = weightedStandardPixelTrainer.predict(mat);
			System.out.println("Prediction is: " + prediction);
			
			
			/*Mat mat = weightedStandardImage.getStandardImages(0);
			Highgui.imwrite("resource/Face_Male_Female/stdImage4.png" , mat);*/
			
			System.out.println("Operation Successful!!!");
		} catch (IOException e) {
			System.out.println("Error in File Path!!!");
		}
		
	}
	/**/
}
