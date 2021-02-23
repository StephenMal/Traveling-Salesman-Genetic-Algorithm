// Stephen Maldonado
// File made to read in the input and cache all the distances

import java.io.*;
import java.util.*;
import java.text.*;
import java.lang.Math;
import java.lang.Math;

public class CityDistCalc
{
  public String name;
  public String comment;
  public String type;
  public int dim;
  public String edge_weight_type;
  public static double[][] cityCoords;
  public static double[][] cityDistArr;

  public CityDistCalc(){
    System.out.println("Calculating city dist");
    System.out.println("Input file: " + Parameters.dataInputFileName);
    // Reads in the input and saves it into the files
    readInput(Parameters.dataInputFileName);

    // Calculate and cache the distance between cities
    double tempDbl;
    for(int i = 0; i < dim; i++){
      for(int j = 0; j < dim; j++){

        // Euclidian btwn i and j
        double x1 = cityCoords[i][0];
        double y1 = cityCoords[i][1];
        double x2 = cityCoords[j][0];
        double y2 = cityCoords[j][1];
        tempDbl = Math.sqrt(((x2-x1)*(x2-x1))+((y2-y1)*(y2-y1)));

        // Caches for both i to j and j to i
        cityDistArr[i][j] = tempDbl;
        cityDistArr[j][i] = tempDbl;
      }
    }
  }

  // Returns cached distance between two cities
  public static double getCityDistance(int city1, int city2){
    return cityDistArr[city1][city2];
  }

  public static double getCityCoordsX(int city){
    return cityCoords[city][0];
  }

  public static double getCityCoordsY(int city){
    return cityCoords[city][1];
  }

  private void readInput(String fileLoc){
    try
    {
      File inputFile = new File(fileLoc);
      Scanner inputReader = new Scanner(inputFile);
      String data; //For temp saving in
      String[] dataSplit; //for splitting to get only important info

      // Saves name from file
      data = inputReader.nextLine();
      System.out.println(data);
      dataSplit = data.split(":",0);
      name = dataSplit[1];

      // Saves Comment from file
      data = inputReader.nextLine();
      System.out.println(data);
      dataSplit = data.split(":",0);
      comment = dataSplit[1];

      // Saves type from file
      data = inputReader.nextLine();
      System.out.println(data);
      dataSplit = data.split(":",0);
      type = dataSplit[1];

      // Saves dimension number from file
      data = inputReader.nextLine();
      System.out.println(data);
      dataSplit = data.split(" : ",0);
      dim = Integer.parseInt(dataSplit[1]);

      // Saves edge_weight_type from file
      data = inputReader.nextLine();
      System.out.println(data);
      dataSplit = data.split(":",0);
      edge_weight_type = dataSplit[1];

      // Generate city array and distance array
      cityCoords = new double[dim][2];
      cityDistArr = new double[dim][dim];

      // Reads in the coordinates
      data = inputReader.nextLine(); //scans in next line, will be disregarded
      data = inputReader.nextLine(); // Should be first coord
      for(int i = 0; i < dim; i++){
        System.out.println(data);
        dataSplit = data.split(" ",0);
        cityCoords[i][0] = Double.parseDouble(dataSplit[1]);
        cityCoords[i][1] = Double.parseDouble(dataSplit[2]);
        data = inputReader.nextLine();
      }
    }
    catch (Exception e){
      System.out.println("Error inputting file");
      e.printStackTrace();
    }
  }

}
