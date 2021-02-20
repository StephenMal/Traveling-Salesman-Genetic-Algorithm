/******************************************************************************
*  A Teaching GA					  Developed by Hal Stringer & Annie Wu, UCF
*  Version 2, January 18, 2004
*******************************************************************************/

import java.io.*;
import java.util.*;
import java.text.*;

public class OneMax extends FitnessFunction{

/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/


/*******************************************************************************
*                            STATIC VARIABLES                                  *
*******************************************************************************/


/*******************************************************************************
*                              CONSTRUCTORS                                    *
*******************************************************************************/

	public TSPRepOne(){
		name = "Traveling Salesman Problem RepOne";
	}

/*******************************************************************************
*                                MEMBER METHODS                                *
*******************************************************************************/

//  COMPUTE A CHROMOSOME'S RAW FITNESS *************************************

	public void doRawFitness(Chromo X){

		// Set-up variables
		X.rawFitness = 0; //sets fitness at 0
		int[] chromogenes = new int[Parameters.numGenes];  //to store genes
		int[] cityOrder = new int[Parameters.numGenes+1]; //to store city order

		// Records the genes in a list as integers
		for (int z=0; z<Parameters.numGenes; z++){
			chromogenes[z] = X.chromo.getIntGeneValue(z);
		}

		// Records city order to follow the lowest valued chromosomes
		for (int z=0; z<Parameter.numGenes; z++){
			int minValue = Integer.MAX_VALUE;
			int minValueIndex = -1;
			for (int i=0; i<Parameteres.numGenes;i++){
				if(chromogenes[i] < minValue){
					minValue = chromogenes[i];
					minValueIndex = i;
				}
			}
			cityOrder[minValueIndex] = z;
			chromogenes[minValueIndex] = Integer.MAX_VALUE;
		}
		cityOrder[Parameters.numGenes] = cityOrder[0]; //ends where started

		// Sums distances
		for (int z=0; z<Parameter.numGenes + 1; z++){
			X.rawFitness += CityDistCalc.getCityDistance(cityOrder[z][z+1]);
		}
	}

//  PRINT OUT AN INDIVIDUAL GENE TO THE SUMMARY FILE *********************************

	public void doPrintGenes(Chromo X, FileWriter output) throws java.io.IOException{

		for (int i=0; i<Parameters.numGenes; i++){
			Hwrite.right(X.getGeneAlpha(i),11,output);
		}
		output.write("   RawFitness");
		output.write("\n        ");
		for (int i=0; i<Parameters.numGenes; i++){
			Hwrite.right(X.getPosIntGeneValue(i),11,output);
		}
		Hwrite.right((int) X.rawFitness,13,output);
		output.write("\n\n");
		return;
	}

/*******************************************************************************
*                             STATIC METHODS                                   *
*******************************************************************************/

}   // End of OneMax.java ******************************************************
