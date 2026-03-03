package SortingCars;

/*
* Car class represents one car produced in the factory.
* It implements Comparable<Car> so it can be sorted using QuickSort.
*/
public class Car implements Comparable<Car> {

 /*
  * Enum for car colors.
  * The order of enum values defines color priority:
  * RED < BLUE < BLACK < WHITE
  */
 public enum Color {
     RED, BLUE, BLACK, WHITE
 }

 // Car attributes
 private long REC_ID;        // Production number
 private String serial;      // Unique serial (12 characters)
 private Color color;        // Car color
 private String destination; // Dealer city

 // Constructor
 public Car(long REC_ID, String serial, Color color, String destination) {
     this.REC_ID = REC_ID;
     this.serial = serial;
     this.color = color;
     this.destination = destination;
 }

 // Getters
 public long getREC_ID() { return REC_ID; }
 public String getSerial() { return serial; }
 public Color getColor() { return color; }
 public String getDestination() { return destination; }

 /*
  * Converts destination to an integer priority
  * because sorting requires a specific custom order.
  */
 private static int destinationOrder(String dest) {
     switch (dest) {
         case "Los Angeles": return 0;
         case "Houston": return 1;
         case "New Orleans": return 2;
         case "Miami": return 3;
         case "New York": return 4;
         default: return 5;
     }
 }

 /*
  * compareTo() defines how two cars are compared.
  *
  * Sort criteria (in order):
  * 1) Destination
  * 2) Color
  * 3) Serial (alphabetical)
  */
 @Override
 public int compareTo(Car other) {

     // 1️⃣ Compare destination first
     int destCompare = Integer.compare(
             destinationOrder(this.destination),
             destinationOrder(other.destination)
     );
     if (destCompare != 0)
         return destCompare;

     // 2️⃣ Compare color
     int colorCompare = this.color.compareTo(other.color);
     if (colorCompare != 0)
         return colorCompare;

     // 3️⃣ Compare serial alphabetically
     return this.serial.compareTo(other.serial);
 }

 /*
  * Defines how car is written to file.
  * Format:
  * Destination -> Color -> Serial -> REC_ID
  */
 @Override
 public String toString() {
     return destination + "\t" + color + "\t" + serial + "\t" + REC_ID;
 }
}