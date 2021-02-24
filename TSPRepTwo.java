/******************************************************************************
*  A Teaching GA					  Developed by Hal Stringer & Annie Wu, UCF
*  Version 2, January 18, 2004
*******************************************************************************/

import java.io.*;
import java.util.*;
import java.text.*;

public class TSPRepTwo extends FitnessFunction{

/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/

/*******************************************************************************
*                            STATIC VARIABLES                                  *
*******************************************************************************/

/*******************************************************************************
*                              CONSTRUCTORS                                    *
*******************************************************************************/

	public TSPRepTwo(){
		name = "Traveling Salesman Problem RepTwo";
	}

/*******************************************************************************
*                                MEMBER METHODS                                *
*******************************************************************************/

//  COMPUTE A CHROMOSOME'S RAW FITNESS *************************************

	public void doRawFitness(Chromo X){

		// Set-up variables
		X.rawFitness = 0; //sets fitness at 0
		int[] chromogenes = new int[Parameters.numGenes];  //to store genes

		// Records the genes in a list as integers
		for (int z=0; z<Parameters.numGenes; z++){
			chromogenes[z] = X.getIntGeneValueTSP(z);
            //System.out.println("chromogenes[z]: " + chromogenes[z]);
		}

		// Sums distances
		for (int z=0; z<Parameters.numGenes-1; z++){
			X.rawFitness += CityDistCalc.getCityDistance(chromogenes[z]-1,chromogenes[z+1]-1);
            //System.out.println("Raw Fitness: " + X.rawFitness);
		}
        X.rawFitness += CityDistCalc.getCityDistance(chromogenes[Parameters.numGenes-1]-1,chromogenes[0]-1);
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

}   // End of TSPRepTwo.java ******************************************************