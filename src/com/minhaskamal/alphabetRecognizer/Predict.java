/**
 * Developer: Minhas Kamal (BSSE: 0509, IID, DU)
 * Date: 09.Sep.2014
**/

package com.minhaskamal.alphabetRecognizer;


import java.io.*;
import org.opencv.core.*;
import org.opencv.highgui.Highgui;

import com.minhaskamal.alphabetRecognizer.weightedPixel.WeightedStandardPixelTrainer;


public class Predict {

	
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		String sampleImage = "src/res/sample/test.png";
		String Experience = "src/res/knowledge/KnowledgeAlphabet.log";
		
		String str = ImageToContentString(sampleImage, Experience);
		
		System.out.println(str);
		
		//store the result in hard disk
		try {
			FileWriter writer = new FileWriter(new File(sampleImage + "Content.txt"));
			writer.write(str);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Operation Successful!!!");
	}
	
	
	/**
	 * 
	 * @param path
	 * @param Experience
	 * @return
	 */
	public static String ImageToContentString(String path, String Experience){
		String string = "";
		
		System.out.println("Loading Knowledge...");
		WeightedStandardPixelTrainer weightedStandardPixelTrainer = new WeightedStandardPixelTrainer();
		weightedStandardPixelTrainer.load(Experience);
		/*WeightedStandardImage weightedStandardImage = weightedStandardPixelTrainer.getWeightedStandardImage();
		System.out.println(weightedStandardImage.dump());*/
		
//		CvGBTrees gbt = new CvGBTrees();
//		gbt.load("src/alphabetCollection/resource/Alphabets/AlphabetDetectorGBT.xml");
		
		Mat matImage = Highgui.imread(path, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		
		Mat matTemp;
		int[] a = new int[2];
		
		
		int verticalSpace=3;
		int i=1;
		while(true){
			
			a = ObjectEdgeDetector.upDownRange(matImage, a[1]);
			if(a[1]==0){
				break;
			}
			
			matTemp = matImage.submat(a[0]-verticalSpace, a[1]+verticalSpace, 0, matImage.width());
			
			
			Mat matTemp2;
			int x[] = new int[2];
			int y[] = new int[2];
			
			while(true){
				x = ObjectEdgeDetector.leftRightRange(matTemp, x[1]);
				if(x[1]==0){
					break;
				}
				
				matTemp2 = matTemp.submat(0, matTemp.height(), x[0], x[1]);
				
				
				y = ObjectEdgeDetector.upRangeDownRange(matTemp2, 0, matTemp2.height()-1);
				if(y[1]==0){
					break;
				}
				matTemp2 = matTemp2.submat(y[0]-verticalSpace, y[1]+verticalSpace, 0, matTemp2.width());
				
				//detect
				int f = weightedStandardPixelTrainer.predict(matTemp2);
				
				//Highgui.imwrite("C:\\Users\\admin\\Desktop\\" + i + ".png" , matTemp2);	///test
				
				string = string + getValueOf(f);
				System.out.println("Working on alphabet >> " + i++);	///show progress
			}
		}
		
		return string;
	}
	
	/**
	 * 
	 * @param f
	 * @return
	 */
 	public static char getValueOf(int f){
		char ch;
		
		switch(f){
		case 1:
			ch='a';
			break;
		case 2:
			ch='b';
			break;
		case 3:
			ch='c';
			break;
		case 4:
			ch='d';
			break;
		case 5:
			ch='e';
			break;
		case 6:
			ch='f';
			break;
		case 7:
			ch='g';
			break;
		case 8:
			ch='h';
			break;
		case 9:
			ch='i';
			break;
		case 10:
			ch='j';
			break;
		case 11:
			ch='k';
			break;
		case 12:
			ch='l';
			break;
		case 13:
			ch='m';
			break;
		case 14:
			ch='n';
			break;
		case 15:
			ch='o';
			break;
		case 16:
			ch='p';
			break;
		case 17:
			ch='q';
			break;
		case 18:
			ch='r';
			break;
		case 19:
			ch='s';
			break;
		case 20:
			ch='t';
			break;
		case 21:
			ch='u';
			break;
		case 22:
			ch='v';
			break;
		case 23:
			ch='w';
			break;
		case 24:
			ch='x';
			break;
		case 25:
			ch='y';
			break;
		case 26:
			ch='z';
			break;
		case 27:
			ch='A';
			break;
		case 28:
			ch='B';
			break;
		case 29:
			ch='C';
			break;
		case 30:
			ch='D';
			break;
		case 31:
			ch='E';
			break;
		case 32:
			ch='F';
			break;
		case 33:
			ch='G';
			break;
		case 34:
			ch='H';
			break;
		case 35:
			ch='I';
			break;
		case 36:
			ch='J';
			break;
		case 37:
			ch='K';
			break;
		case 38:
			ch='L';
			break;
		case 39:
			ch='M';
			break;
		case 40:
			ch='N';
			break;
		case 41:
			ch='O';
			break;
		case 42:
			ch='P';
			break;
		case 43:
			ch='Q';
			break;
		case 44:
			ch='R';
			break;
		case 45:
			ch='S';
			break;
		case 46:
			ch='T';
			break;
		case 47:
			ch='U';
			break;
		case 48:
			ch='V';
			break;
		case 49:
			ch='W';
			break;
		case 50:
			ch='X';
			break;
		case 51:
			ch='Y';
			break;
		case 52:
			ch='Z';
			break;
		case 53:
			ch='1';
			break;
		case 54:
			ch='2';
			break;
		case 55:
			ch='3';
			break;
		case 56:
			ch='4';
			break;
		case 57:
			ch='5';
			break;
		case 58:
			ch='6';
			break;
		case 59:
			ch='7';
			break;
		case 60:
			ch='8';
			break;
		case 61:
			ch='9';
			break;
		case 62:
			ch='0';
			break;
		case 63:
			ch=',';
			break;
		case 64:
			ch=';';
			break;
		case 65:
			ch=':';
			break;
		case 66:
			ch='?';
			break;
		case 67:
			ch='!';
			break;
		case 68:
			ch='.';
			break;
		case 69:
			ch='@';
			break;
		case 70:
			ch='#';
			break;
		case 71:
			ch='$';
			break;
		case 72:
			ch='%';
			break;
		case 73:
			ch='&';
			break;
		case 74:
			ch='(';
			break;
		case 75:
			ch=')';
			break;
		case 76:
			ch='{';
			break;
		case 77:
			ch='}';
			break;
		case 78:
			ch='[';
			break;
		case 79:
			ch=']';
			break;
			
		default:
			ch='~';
		}
		
		return ch;
	}

}
