/************************************************************************************************************************
* Developer: Minhas Kamal(BSSE-0509, IIT, DU)																			*
* Date: 08-Sep-2014																										*
*************************************************************************************************************************/

package com.minhaskamal.alphabetRecognizer.weightedPixel;

import java.io.FileWriter;
import java.io.IOException;

import com.minhaskamal.egami.matrix.Matrix;

public class WeightedStandardImage {
	private int types;
	private int[] size;
	private int[] ids;
	private int[] weights;
	private Matrix[] standardImages;
	
	
	public WeightedStandardImage(int types, int[] size) {
		this.types = types;
		this.size = size;
		this.ids = new int[types];
		this.weights = new int[types];
		this.standardImages = new Matrix[types];
		for(int i=0; i<types; i++){
			standardImages[i] = new Matrix(size[0], size[1], Matrix.BLACK_WHITE);
		}
	}
	
	public WeightedStandardImage() {
		this.types = 0;
		this.size = new int[]{0, 0};
		this.ids = new int[types];
		this.weights = new int[types];
		this.standardImages = new Matrix[types];
		for(int i=0; i<types; i++){
			standardImages[i] = new Matrix(size[0], size[1], Matrix.BLACK_WHITE);
		}
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String dump(){
		int rows = this.size[0], cols = this.size[1];
		String content = "<WeightedStandardImage/>\n<types>" + types + "</types>\n<size>" +
				rows + "," + cols + "</size>\n<data>";
		
		String imageType = "";
		String line = "";
		for(int i=0; i<types; i++){
			imageType = "\n<id>" + ids[i] + "</id>\n" +
						"<weight>" + weights[i] + "</weight>\n" +
						"<stdImage>\n";
			
			for(int row=0; row<rows; row++){
				line = "";
				for(int col=0; col<cols; col++){
					line = line + (short)standardImages[i].pixels[row][col][0] + ",";
				}
				imageType = imageType + line + "\n";
			}
			
			content = content + imageType + "</stdImage>\n";
		}
		
		content = content + "</data>";
		
		return content;
	}
	
	
	public void saveKnowledge(String filePath){
		System.out.println("Saving Knowledge...");	//notification
		
		try {
			FileWriter fileWriter = new FileWriter(filePath);
			fileWriter.write(dump());
			fileWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	//getter//setters//
	
	public int getTypes(){
		return types;
	}
	
	public int[] getSize(){
		return this.size.clone();
	}
	
	public int getId(int index){
		if(index<types){
			return ids[index];
		}else{
			return -1;
		}
	}
	
	public void setId(int index, int id){
		if(index<types){
			ids[index] = id;
		}
	}
	
	public int getWeight(int index){
		if(index<types){
			return weights[index];
		}else{
			return -1;
		}
	}
	
	public void setWeight(int index, int weight){
		if(index<types){
			weights[index] = weight;
		}
	}
	
	public void incrementWeight(int index){
		if(index<types){
			weights[index]++;
		}
	}
	
	public void setStandardImages(int index, int row, int col, short pixel){
		if(index<types){
			standardImages[index].pixels[row][col][0] = pixel;
		}
	}
	
	public void setStandardImages(Matrix newMat, int index){
		if(index<types){
			standardImages[index] = newMat;
		}
	}
	
	public short getStandardImages(int index, int row, int col){
		if(index<types && row<size[0] && col<size[1]){
			return (short) standardImages[index].pixels[row][col][0];
		}else{
			return 0;
		}
	}
	
	public Matrix getStandardImages(int index){
		if(index<types){
			return standardImages[index];
		}else{
			return null;
		}
	}
	
	
	//test only
	public static void main(String[] args) {
		WeightedStandardImage weightedStandardImage = new WeightedStandardImage(5, new int[]{40, 40});
		
		System.out.println(weightedStandardImage.dump());
	}
}
