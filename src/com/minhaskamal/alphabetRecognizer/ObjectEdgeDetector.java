/**
 * Developer: Minhas Kamal (BSSE: 0509, IID, DU)
 * Date: 29.Aug.2014
**/

package com.minhaskamal.alphabetRecognizer;

import com.minhaskamal.egami.matrix.Matrix;


public class ObjectEdgeDetector {
	private static int darkValue = 150;
	private static int buffer = 1;
	
	/**
	 * Finds the left and right boundary of an object (object darker than background).  [-> O ->]
	 * @param mat
	 * @param startPosition
	 * @return
	 */
	public static int[] leftRightRange(Matrix mat, int startPosition){
		int lenght = mat.getCols(), height = mat.getRows();
		int[] x = new int[2];
		
		boolean blackDotFound = false;
		int position, i;
		for(position=startPosition+1; position<lenght && !blackDotFound; position++){
			for(i=0; i<height; i++){
				if(mat.pixels[i][position][0]<darkValue){
					blackDotFound=true;
					break;
				}
			}
		}
		
		if(!blackDotFound){
			return x;
		}
		
		x[0] = position-1-buffer;
		
		for(; position<lenght && blackDotFound; position++){
			blackDotFound=false;
			for(i=0; i<height; i++){
				if(mat.pixels[i][position][0]<darkValue){
					blackDotFound=true;
					break;
				}
			}
		}
		
		x[1] = position-1+buffer;
		
		return x;
	}
	
	
	/**
	 * 
	 * @param mat
	 * @param startPosition
	 * @return
	 */
	public static int[] upDownRange(Matrix mat, int startPosition){
		int length = mat.getCols(), height = mat.getRows();
		int[] y = new int[2];
		
		boolean blackDotFound = false;
		int position, i;
		for(position=startPosition+1; position<height && !blackDotFound; position++){
			for(i=0; i<length; i++){
				if(mat.pixels[position][i][0]<darkValue){
					blackDotFound=true;
					break;
				}
			}
		}
		
		if(!blackDotFound){
			return y;
		}
		
		y[0] = position-1-buffer;
		
		for(; position<height && blackDotFound; position++){
			blackDotFound=false;
			for(i=0; i<length; i++){
				if(mat.pixels[position][i][0]<darkValue){
					blackDotFound=true;
					break;
				}
			}
		}
		
		y[1] = position-1+buffer;
		
		return y;
	}
	
	
	/**
	 * 
	 * @param mat
	 * @param startPosition
	 * @param stopPosition
	 * @return
	 */
	public static int[] upRangeDownRange(Matrix mat, int startPosition, int stopPosition){
		int width = mat.getCols(), height = mat.getRows();
		int[] y = new int[2];
		
		if(stopPosition>height){
			return y;
		}
		
		boolean blackDotFound = false;
		int position, i;
		for(position=startPosition+1; position<stopPosition && !blackDotFound; position++){
			for(i=0; i<width; i++){
				if(mat.pixels[position][i][0]<darkValue){
					blackDotFound=true;
					break;
				}
			}
		}
		
		if(!blackDotFound){
			return y;
		}
		
		y[0] = position-1-buffer;
		
		blackDotFound=false;
		for(position=stopPosition; position>y[0] && !blackDotFound; position--){
			for(i=0; i<width; i++){
				if(mat.pixels[position][i][0]<darkValue){
					blackDotFound=true;
					break;
				}
			}
		}
		
		y[1] = position+1+buffer;
		
		return y;
	}
}
