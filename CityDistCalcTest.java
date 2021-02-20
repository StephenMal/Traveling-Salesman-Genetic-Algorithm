import java.io.*;
import java.util.*;
import java.text.*;

public class CityDistCalcTest {

    public static CityDistCalc cities = new CityDistCalc();

    public static void main(String[] args) {
      System.out.println(Double.toString(cities.getCityCoordsX(1)));
      System.out.println(Double.toString(cities.getCityDistance(1,2)));
    }
}
