package SortingCars;

//Main.java

import java.io.*;
import java.util.*;

/*
* Main program:
*
* 1) Creates lists of cars
* 2) Writes unsorted lists to files
* 3) Sorts them simultaneously
* 4) Writes sorted lists to files
* 5) Prints execution time
* 6) Terminates engine
*/
public class Main {

 static int NUMBER_OF_LISTS = 3;
 static int CARS_PER_LIST = 100000;
 static int MAX_THREADS = 8;

 static String[] destinations = {
         "Los Angeles",
         "Houston",
         "New Orleans",
         "Miami",
         "New York"
 };

 public static void main(String[] args) throws Exception {

     List<LinkedList<Car>> carLists = new ArrayList<>();
     Random random = new Random();

     /*
      * STEP 1:
      * Create lists and write them to files.
      * (NOT multithreaded)
      */
     for (int i = 1; i <= NUMBER_OF_LISTS; i++) {

         LinkedList<Car> list = new LinkedList<>();
         Set<String> serialSet = new HashSet<>();

         for (int j = 1; j <= CARS_PER_LIST; j++) {

             // Ensure serial is unique within list
             String serial;
             do {
                 serial = generateSerial(random);
             } while (!serialSet.add(serial));

             Car car = new Car(
                     j,
                     serial,
                     Car.Color.values()[random.nextInt(4)],
                     destinations[random.nextInt(destinations.length)]
             );

             list.add(car);
         }

         carLists.add(list);

         writeToFile("cars-" + i + ".txt", list);
     }

     /*
      * STEP 2:
      * Initialize QuickSort Engine
      */
     QuicksortEngine engine = new QuicksortEngine(MAX_THREADS);

     /*
      * STEP 3:
      * Sort all lists simultaneously
      */
     int index = 1;

     for (LinkedList<Car> list : carLists) {

         long start = System.currentTimeMillis();

         engine.sort(list);

         long end = System.currentTimeMillis();

         System.out.println("List " + index +
                 " sorting took: " + (end - start) + " ms");

         writeToFile("cars-" + index + "-sorted.txt", list);

         index++;
     }

     /*
      * STEP 5:
      * Terminate engine gracefully
      */
     engine.terminate();
 }

 /*
  * Generates random 12-character serial
  */
 static String generateSerial(Random random) {

     String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
     StringBuilder sb = new StringBuilder();

     for (int i = 0; i < 12; i++) {
         sb.append(chars.charAt(random.nextInt(chars.length())));
     }

     return sb.toString();
 }

 /*
  * Writes list to file (TAB separated)
  */
 static void writeToFile(String filename, LinkedList<Car> list)
         throws IOException {

     BufferedWriter writer = new BufferedWriter(
             new FileWriter(filename));

     for (Car car : list) {
         writer.write(car.toString());
         writer.newLine();
     }

     writer.close();
 }
}