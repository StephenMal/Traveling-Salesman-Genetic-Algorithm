/******************************************************************************
*  A Teaching GA					  Developed by Hal Stringer & Annie Wu, UCF
*  Version 2, January 18, 2004
*******************************************************************************/

import java.io.*;
import java.util.*;
import java.text.*;

public class TSP_Rep extends FitnessFunction{

/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/
	public double [][] coordinates;
	public String info;
	public static Set<Integer> distance;
	public int length;

/*******************************************************************************
*                            STATIC VARIABLES                                  *
*******************************************************************************/


/*******************************************************************************
*                              CONSTRUCTORS                                    *
*******************************************************************************/

	public TSP_Rep(int length) throws java.io.IOException
	{
		name = "TSP Representation";
		distance = new HashSet<Integer>();
		this.length = length;
		cityInfo();
	}

/*******************************************************************************
*                                MEMBER METHODS                                *
*******************************************************************************/

	private void cityInfo() throws java.io.IOException
	{//pull data from TSP file
		String data = "";
		BufferedReader dataIn= new BufferedReader(new FileReader(Parameters.dataInputFileName) ); 
		 //determine which TSP
		this.info = dataIn.readLine().substring(6);
		if (this.info.equals("att48"))
			coordinates = new double [49][2];
		if (this.info.equals("berlin52"))
			coordinates = new double [53][2];
		else if (this.info.equals("rl1323"))
			coordinates = new double [1324][2];
		
		for (int i = 0; i < 5; i++)
			dataIn.readLine();
		//Store data
		do{
			data = dataIn.readLine();
			if (data.equals("EOF")) break;
			System.out.println(data);
			String index = data.split(" ")[0];
			String x = data.split(" ")[1];
			String y = data.split(" ")[2];
			
			this.distance.add(Integer.parseInt(index));
			this.coordinates[Integer.parseInt(index)][0] = Double.parseDouble(x);
			this.coordinates[Integer.parseInt(index)][1] = Double.parseDouble(y);
			
		}while(!data.equals("EOF"));
	}

//  COMPUTE A CHROMOSOME'S RAW FITNESS *************************************

	public void doRawFitness(Chromo X){

		X.rawFitness = 0;
		Set<Integer> chromoDistance = new HashSet<Integer>();
		try{

			for (int z=0; z<Parameters.numGenes * Parameters.geneSize; z+=3){
				int presentCity = Integer.parseInt(X.chromo.substring(z, z+3), 16);
				int futureCity = 0;
				if (z+3 < Parameters.numGenes * Parameters.geneSize)
					futureCity = Integer.parseInt(X.chromo.substring(z+3, z+3+3), 16);
				else
					futureCity = Integer.parseInt(X.chromo.substring(0, 3), 16);
				
				chromoDistance.add(presentCity); 
				
				if(this.length == 0) {
					X.rawFitness += Math.sqrt( Math.pow((this.coordinates[presentCity][0]-this.coordinates[futureCity][0]), 2) + Math.pow((this.coordinates[presentCity][1]-this.coordinates[futureCity][1]), 2));
				} 
			}
			
			Set<Integer> delta = new HashSet<Integer>();
			delta.addAll(TSP_Rep.distance);
			delta.removeAll(chromoDistance);
			
			if (!delta.isEmpty()) {
				X.rawFitness = Integer.MAX_VALUE;
			}
		}
		catch(IndexOutOfBoundsException e)
		{
			X.rawFitness = Integer.MAX_VALUE;
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

}   // End of TSP_Rep.java ******************************************************

