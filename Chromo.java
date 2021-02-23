/******************************************************************************
*  A Teaching GA					  Developed by Hal Stringer & Annie Wu, UCF
*  Version 2, January 18, 2004
*******************************************************************************/

import java.io.*;
import java.util.*;
import java.text.*;

public class Chromo
{
/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/

	public String chromo;
	public double rawFitness;
	public double sclFitness;
	public double proFitness;

/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/

	private static double randnum;

/*******************************************************************************
*                              CONSTRUCTORS                                    *
*******************************************************************************/

	public Chromo(){

		//  Set gene values to a randum sequence of 1's and 0's
		char geneBit;
		chromo = "";
		this.chromo = "";
		for (int i=0; i<Parameters.numGenes; i++){
			for (int j=0; j<Parameters.geneSize; j++){
				randnum = Search.r.nextDouble();
				if (randnum > 0.5) geneBit = '0';
				else geneBit = '1';
				this.chromo = chromo + geneBit;
			}
		}

		this.rawFitness = -1;   //  Fitness not yet evaluated
		this.sclFitness = -1;   //  Fitness not yet scaled
		this.proFitness = -1;   //  Fitness not yet proportionalized
	}

	public Chromo(int tspRepTwo){
		ArrayList<Integer> cities = new ArrayList<Integer>();
		this.chromo = "";

		// Initialize list of cities
		for (int j = 0; j < Parameters.numCity; j++)
			cities.add(j+1);

		// Randomize order of cities
		Collections.shuffle(cities);
		
		if (Parameters.numCity >= 10 && Parameters.numCity <= 99){
			for (int curCity : cities){
				if (curCity <= 9){
					this.chromo = this.chromo + "0" + String.valueOf(curCity);
				}
				else{
					this.chromo = this.chromo + "" + String.valueOf(curCity);
				}
			}
		}
		else{
			for (int curCity : cities){
				if (curCity <= 9){
					this.chromo = this.chromo + "000" + String.valueOf(curCity);
				}
				else if (curCity <= 99){
					this.chromo = this.chromo + "00" + String.valueOf(curCity);
				}
				else if (curCity <= 999){
					this.chromo = this.chromo + "0" + String.valueOf(curCity);
				}
				else{
					this.chromo = this.chromo + "" + String.valueOf(curCity);
				}
			}
		}

		System.out.println("Chromo: " + this.chromo);

		this.rawFitness = -1;   //  Fitness not yet evaluated
		this.sclFitness = -1;   //  Fitness not yet scaled
		this.proFitness = -1;   //  Fitness not yet proportionalized
	}


/*******************************************************************************
*                                MEMBER METHODS                                *
*******************************************************************************/

	//  Get Alpha Represenation of a Gene **************************************

	public String getGeneAlpha(int geneID){
		int start = geneID * Parameters.geneSize;
		int end = (geneID+1) * Parameters.geneSize;
		String geneAlpha = this.chromo.substring(start, end);
		return (geneAlpha);
	}

	//  Get Integer Value of a Gene (Positive or Negative, 2's Compliment) ****

	public int getIntGeneValue(int geneID){
		String geneAlpha = "";
		int geneValue;
		char geneSign;
		char geneBit;
		geneValue = 0;
		geneAlpha = getGeneAlpha(geneID);
		for (int i=Parameters.geneSize-1; i>=1; i--){
			geneBit = geneAlpha.charAt(i);
			if (geneBit == '1') geneValue = geneValue + (int) Math.pow(2.0, Parameters.geneSize-i-1);
		}
		geneSign = geneAlpha.charAt(0);
		if (geneSign == '1') geneValue = geneValue - (int)Math.pow(2.0, Parameters.geneSize-1);
		return (geneValue);
	}

	public int getIntGeneValueTSP(int geneID){

		if (Parameters.numCity >= 10 && Parameters.numCity <= 99){
			return Integer.parseInt(this.chromo.substring(geneID*2,(geneID*2)+2));
		}
		else{
			return Integer.parseInt(this.chromo.substring(geneID*4,(geneID*4)+4));
		}
		
	}

	//  Get Integer Value of a Gene (Positive only) ****************************

	public int getPosIntGeneValue(int geneID){
		String geneAlpha = "";
		int geneValue;
		char geneBit;
		geneValue = 0;
		geneAlpha = getGeneAlpha(geneID);
		for (int i=Parameters.geneSize-1; i>=0; i--){
			geneBit = geneAlpha.charAt(i);
			if (geneBit == '1') geneValue = geneValue + (int) Math.pow(2.0, Parameters.geneSize-i-1);
		}
		return (geneValue);
	}

	//  Mutate a Chromosome Based on Mutation Type *****************************

	public void doMutation(){

		String mutChromo = "";
		char x;

		switch (Parameters.mutationType){

		case 1:     //  Replace with new random number

			for (int j=0; j<(Parameters.geneSize * Parameters.numGenes); j++){
				x = this.chromo.charAt(j);
				randnum = Search.r.nextDouble();
				if (randnum < Parameters.mutationRate){
					if (x == '1') x = '0';
					else x = '1';
				}
				mutChromo = mutChromo + x;
			}
			this.chromo = mutChromo;
			break;
		
		case 2:     //  For TSP: swap with random city
			randnum = Search.r.nextDouble();
			ArrayList<Integer> citiesMut = new ArrayList<Integer>();

			System.out.println("this.chromo: " + this.chromo);

			if (Parameters.numCity >= 10 && Parameters.numCity <= 99){
				
				// Convert string to ArrayList<Integet>
				for (int i=0; i<Parameters.numCity; i++){
					citiesMut.add(getIntGeneValueTSP(i));
				}

				// swap
				for (int i=0; i<Parameters.numCity; i++){
					randnum = Search.r.nextDouble();
					if (randnum < Parameters.mutationRate){
						int swappedIndex = Search.r.nextInt(Parameters.numCity);
						Collections.swap(citiesMut, i, swappedIndex);
					}
				}
			
				// convert chromo back to string
				for (int curCity : citiesMut){
					if (curCity <= 9){
						this.chromo = this.chromo + "0" + String.valueOf(curCity);
					}
					else{
						this.chromo = this.chromo + "" + String.valueOf(curCity);
					}
				}

			}
			else{

				// Convert string to ArrayList<Integet>
				for (int i=0; i<Parameters.numCity; i++){
					citiesMut.add(getIntGeneValueTSP(i));
				}

				// swap
				for (int i=0; i<Parameters.numCity; i++){
					randnum = Search.r.nextDouble();
					if (randnum < Parameters.mutationRate){
						int swappedIndex = Search.r.nextInt(Parameters.numCity);
						Collections.swap(citiesMut, i, swappedIndex);
					}
				}
			
				// convert chromo back to string
				for (int curCity : citiesMut){
					if (curCity <= 9){
						this.chromo = this.chromo + "000" + String.valueOf(curCity);
					}
					else if (curCity <= 99){
						this.chromo = this.chromo + "00" + String.valueOf(curCity);
					}
					else if (curCity <= 999){
						this.chromo = this.chromo + "0" + String.valueOf(curCity);
					}
					else{
						this.chromo = this.chromo + "" + String.valueOf(curCity);
					}
				}
			}
			break;

		default:
			System.out.println("ERROR - No mutation method selected");
		}
	}

/*******************************************************************************
*                             STATIC METHODS                                   *
*******************************************************************************/

	//  Select a parent for crossover ******************************************

	public static int selectParent(){

		double rWheel = 0;
		int j = 0;
		int k = 0;

		switch (Parameters.selectType){

		case 1:     // Proportional Selection
			randnum = Search.r.nextDouble();
			for (j=0; j<Parameters.popSize; j++){
				rWheel = rWheel + Search.member[j].proFitness;
				if (randnum < rWheel) return(j);
			}
			break;

		case 3:     // Random Selection
			randnum = Search.r.nextDouble();
			j = (int) (randnum * Parameters.popSize);
			return(j);

		case 2:     //  Tournament Selection
			randnum = Search.r.nextDouble();
			j = (int) (randnum * Parameters.popSize);
			randnum = Search.r.nextDouble();
			k = (int) (randnum * Parameters.popSize);
			if (Search.member[j].proFitness > Search.member[k].proFitness)
				return(j);
			else
				return(k);

		default:
			System.out.println("ERROR - No selection method selected");
		}
	return(-1);
	}

	//  Produce a new child from two parents  **********************************

	public static void mateParents(int pnum1, int pnum2, Chromo parent1, Chromo parent2, Chromo child1, Chromo child2){

		int xoverPoint1;
		int xoverPoint2;

		switch (Parameters.xoverType){

		case 1:     //  Single Point Crossover

			//  Select crossover point
			xoverPoint1 = 1 + (int)(Search.r.nextDouble() * (Parameters.numGenes * Parameters.geneSize-1));

			//  Create child chromosome from parental material
			child1.chromo = parent1.chromo.substring(0,xoverPoint1) + parent2.chromo.substring(xoverPoint1);
			child2.chromo = parent2.chromo.substring(0,xoverPoint1) + parent1.chromo.substring(xoverPoint1);
			break;

		case 2:     //  Single Point Crossover for TSP
			
			ArrayList<Integer> citiesXover = new ArrayList<Integer>();
			//public sta

			// Convert string to ArrayList<Integet>
			//for (int i=0; i<Parameters.numCity; i++){
			//	citiesXover.add(getIntGeneValueTSP(i));
			//}
					
			xoverPoint1 = 1 + Search.r.nextInt(Parameters.numCity-1);

			//for (int curCity : citiesXover.subList(xoverPoint1)){
			//for (int i=0; i<Parameters.numCity; i++){
			//	if (
			//}

			//child1.chromo = parent1.chromo.substring(0,xoverPoint1);
			
			//for (int i=0; i < Parameters.numCity-xoverPoint1) 
			
			//child1.chromo = parent1.chromo.substring(0,xoverPoint1) + parent2.chromo.substring(xoverPoint1);
			


		case 3:     //  Uniform Crossover

		default:
			System.out.println("ERROR - Bad crossover method selected");
		}

		//  Set fitness values back to zero
		child1.rawFitness = -1;   //  Fitness not yet evaluated
		child1.sclFitness = -1;   //  Fitness not yet scaled
		child1.proFitness = -1;   //  Fitness not yet proportionalized
		child2.rawFitness = -1;   //  Fitness not yet evaluated
		child2.sclFitness = -1;   //  Fitness not yet scaled
		child2.proFitness = -1;   //  Fitness not yet proportionalized
	}

	//  Produce a new child from a single parent  ******************************

	public static void mateParents(int pnum, Chromo parent, Chromo child){

		//  Create child chromosome from parental material
		child.chromo = parent.chromo;

		//  Set fitness values back to zero
		child.rawFitness = -1;   //  Fitness not yet evaluated
		child.sclFitness = -1;   //  Fitness not yet scaled
		child.proFitness = -1;   //  Fitness not yet proportionalized
	}

	//  Copy one chromosome to another  ***************************************

	public static void copyB2A (Chromo targetA, Chromo sourceB){

		targetA.chromo = sourceB.chromo;

		targetA.rawFitness = sourceB.rawFitness;
		targetA.sclFitness = sourceB.sclFitness;
		targetA.proFitness = sourceB.proFitness;
		return;
	}

}   // End of Chromo.java ******************************************************
