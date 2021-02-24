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
	public int tspRepresentative;
	public int cityNumber;

/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/

	private static double randnum;

/*******************************************************************************
*                              CONSTRUCTORS                                    *
*******************************************************************************/

	public Chromo(){

		//  Scleet gene values to a randum sequence of 1's and 0's
		char geneBit;
		chromo = "";
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

public Chromo(boolean viewTSP, int cityNumber)
	{			
		chromo = "";
		String gene = "";
		int initialCity = 0;
		Set<Integer> seenCity = new HashSet<Integer>();

		ArrayList<Integer> randnums = new ArrayList<Integer>();
		
		for (int i = 1; i <= cityNumber; i++) 
		{
			randnums.add(i);
		}

		Collections.shuffle(randnums);
		
		for (int points : randnums)
		{		
			if (points <= 15) 
			{
				gene = "00" + Integer.toHexString(points);
				this.chromo += gene;
			}
			else if(points <= 255) 
			{
				gene = "0" + Integer.toHexString(points);
				this.chromo += gene;
			}
			else 
			{
				gene = Integer.toHexString(points);
				this.chromo += gene;
			}
		} 
		
		this.tspRepresentative = 1;
		this.cityNumber = cityNumber;
		this.rawFitness = -1;   //  Fitness not yet evaluated
		this.sclFitness = -1;   //  Fitness not yet scaled
		this.proFitness = -1;   //  Fitness not yet proportionalized
		
	}

	public Chromo(boolean viewTSP, int cityNumber, boolean representative2)
	{
		
		chromo = "";
		String gene = "";
		int initialCity = 0;

		Set<Integer> seenCity = new HashSet<Integer>();
		ArrayList<Integer> randnums = new ArrayList<Integer>();
		
		for (int i = 1; i <= cityNumber; i++) 
		{
			randnums.add(i);
		}		
		Collections.shuffle(randnums);
		
		for (int points : randnums)
		{
			this.chromo += (char)(points + '0');
		}
		this.tspRepresentative = 2;
		this.cityNumber = cityNumber;
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
		case 2:

		for (int j=0; j<(Parameters.geneSize * Parameters.numGenes); j++){
				x = this.chromo.charAt(j);
				randnum = Search.r.nextInt(cityNumber);
				if (randnum < Parameters.mutationRate){
					x = (char)(randnum + '0');
				}
				mutChromo = mutChromo + x;
			}
			this.chromo = mutChromo;
			 TSP_RepChild2(this);
			break; 

		default:
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
		
		    int firstparameter = Math.abs(Search.r.nextInt());
			int secondparameter = Math.abs(Search.r.nextInt()); 
			double slice = 0.5;
			
			if (slice > Search.r.nextDouble())
				return (firstparameter % Parameters.popSize);
			else
				return (secondparameter % Parameters.popSize);
		case 4: //Rank Selection

			randnum = Search.r.nextDouble();
			
			for (j=0; j<Parameters.popSize; j++){
				rWheel = rWheel + Search.member[j].proFitness;
				if (randnum < rWheel) return(j);
			}
		default:

			//System.out.println("ERROR - No selection method selected");
		}
	return(-1);
	}

	//  Produce a new child from two parents  **********************************
	private static void TSP_RepChild(Chromo X)
	{
		Set<Integer> chromoDistance = new HashSet<Integer>();
		ArrayList<Integer> adjust = new ArrayList<Integer>();
		for (int z=0; z<Parameters.numGenes * Parameters.geneSize; z+=3)
		{
			int presentCity = Integer.parseInt(X.chromo.substring(z, z+3), 16);
			if (presentCity > Parameters.numGenes || presentCity <= 0)
				adjust.add(z);
			else if (chromoDistance.contains(presentCity))
				adjust.add(z);
			else
				chromoDistance.add(presentCity);			
		}
		Set<Integer> delta = new HashSet<Integer>();
		delta.addAll(TSP_Rep.distance);
		delta.removeAll(chromoDistance);
		if(!delta.isEmpty())
		{
			Iterator<Integer> execute = delta.iterator();
			StringBuffer standby = new StringBuffer(X.chromo);
			String gene = "";
			for (int city : adjust)
			{
				int goodCity = execute.next();
				if (goodCity <= 15) 
				{
					gene = "00" + Integer.toHexString(goodCity);
					standby.replace(city, city+3, gene);
				}
				else if(goodCity <= 255) 
				{
					gene = "0" + Integer.toHexString(goodCity);
					standby.replace(city, city+3, gene);
				}
				else 
				{
					gene = Integer.toHexString(goodCity);
					standby.replace(city, city+3, gene);
				}	
			}
			X.chromo = standby.toString();
		}
	}

	private static void TSP_RepChild2(Chromo X)
	{
		Set<Integer> chromoDistance = new HashSet<Integer>();
		ArrayList<Integer> adjust = new ArrayList<Integer>();
		for (int z=0; z< Parameters.numGenes; z++)
		{
			int presentCity = X.chromo.charAt(z) - '0';
			if (presentCity > Parameters.numGenes || presentCity <= 0)
				adjust.add(z);
			else if (chromoDistance.contains(presentCity)) {
				adjust.add(z);
			}
			else
				chromoDistance.add(presentCity);			
		}
		Set<Integer> delta = new HashSet<Integer>();
		delta.addAll(TSP_Rep2.distance);
		delta.removeAll(chromoDistance);
		if(!delta.isEmpty())
		{
			Iterator<Integer> execute = delta.iterator();
			StringBuffer standby = new StringBuffer(X.chromo);
			String gene = "";
			for (int city : adjust)
			{
				if (!execute.hasNext())
					break;
				
				int goodCity = execute.next();
				char convert = (char)(goodCity + '0');
				standby.setCharAt(city, convert);
			}
			X.chromo = standby.toString();
		}

	}
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
			
			if (Parameters.problemType.equals("TSP_Rep"))
			{
				TSP_RepChild(child1);
				TSP_RepChild(child2);
			}
			else if (Parameters.problemType.equals("TSP_Rep2"))
			{
				TSP_RepChild2(child1);
				TSP_RepChild2(child2);
			}

			break;

		case 2:     //  Two Point Crossover:
			
			xoverPoint1 = 1 + (int)(Search.r.nextDouble() * (Parameters.numGenes * Parameters.geneSize-1));
			xoverPoint2 = 1 + (int)(Search.r.nextDouble() * (Parameters.numGenes * Parameters.geneSize-1));

			if(xoverPoint1 < xoverPoint2)
			{
			child1.chromo = parent1.chromo.substring(0, xoverPoint1) + parent2.chromo.substring(xoverPoint1, xoverPoint2) + parent1.chromo.substring(xoverPoint2);
			child2.chromo = parent2.chromo.substring(0, xoverPoint1) + parent1.chromo.substring(xoverPoint1, xoverPoint2) + parent2.chromo.substring(xoverPoint2);
			}
			else if(xoverPoint1 >= xoverPoint2)
			{
			child1.chromo = parent1.chromo.substring(0, xoverPoint2) + parent2.chromo.substring(xoverPoint2, xoverPoint1) + parent1.chromo.substring(xoverPoint1);
			child2.chromo = parent2.chromo.substring(0, xoverPoint2) + parent1.chromo.substring(xoverPoint2, xoverPoint1) + parent2.chromo.substring(xoverPoint1);
			}
			else
			{
				System.out.println("ERROR - Bad crossover points selected");
				break;
			}

			if (Parameters.problemType.equals("TSP_Rep"))
			{
				TSP_RepChild(child1);
				TSP_RepChild(child2);
			}
			else if (Parameters.problemType.equals("TSP_Rep2"))
			{
				TSP_RepChild2(child1);
				TSP_RepChild2(child2);
			}

			break;

			
		case 3:     //  Uniform Crossover 

			StringBuffer standbyChild1 = new StringBuffer();
			StringBuffer standbyChild2 = new StringBuffer();

			for(int i = 0; i < (Parameters.numGenes * Parameters.geneSize); i++)
			{
				randnum = Search.r.nextDouble();
				if(randnum > Parameters.xoverRate)
				{
					standbyChild1.append(parent2.chromo.charAt(i));
					standbyChild2.append(parent1.chromo.charAt(i));
				}
				else
				{
					standbyChild1.append(parent1.chromo.charAt(i));
					standbyChild2.append(parent2.chromo.charAt(i));					
				}
			}

			child1.chromo = standbyChild1.toString();
			child2.chromo = standbyChild2.toString();

			if (Parameters.problemType.equals("TSP_Rep"))
			{
				TSP_RepChild(child1);
				TSP_RepChild(child2);
			}
			else if (Parameters.problemType.equals("TSP_Rep2"))
			{
				TSP_RepChild2(child1);
				TSP_RepChild2(child2);
			}
			break;
			
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