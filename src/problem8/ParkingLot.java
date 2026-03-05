package problem8;

import java.util.*;

class ParkingSpot {


    String licensePlate;
    long entryTime;
    boolean occupied;

    public ParkingSpot() {
        occupied = false;
    }


}

public class ParkingLot {


    private ParkingSpot[] table;
    private int capacity;
    private int occupiedSpots = 0;
    private int totalProbes = 0;
    private int totalParks = 0;

    public ParkingLot(int capacity) {
        this.capacity = capacity;
        table = new ParkingSpot[capacity];

        for (int i = 0; i < capacity; i++) {
            table[i] = new ParkingSpot();
        }
    }

    // Hash function
    private int hash(String plate) {
        return Math.abs(plate.hashCode()) % capacity;
    }

    // Park vehicle
    public void parkVehicle(String plate) {

        int index = hash(plate);
        int probes = 0;

        while (table[index].occupied) {
            index = (index + 1) % capacity;
            probes++;
        }

        table[index].licensePlate = plate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].occupied = true;

        occupiedSpots++;
        totalProbes += probes;
        totalParks++;

        System.out.println(
                "parkVehicle(\"" + plate + "\") → Assigned spot #" +
                        index + " (" + probes + " probes)"
        );
    }

    // Exit vehicle
    public void exitVehicle(String plate) {

        int index = hash(plate);
        int start = index;

        while (table[index].occupied) {

            if (plate.equals(table[index].licensePlate)) {

                long durationMillis =
                        System.currentTimeMillis() - table[index].entryTime;

                double hours = durationMillis / 3600000.0;
                double fee = hours * 5.0; // $5 per hour

                table[index].occupied = false;
                occupiedSpots--;

                System.out.printf(
                        "exitVehicle(\"%s\") → Spot #%d freed, Duration: %.2f hours, Fee: $%.2f\n",
                        plate, index, hours, fee
                );

                return;
            }

            index = (index + 1) % capacity;

            if (index == start) break;
        }

        System.out.println("Vehicle not found.");
    }

    // Find nearest free spot from entrance
    public void findNearestSpot() {

        for (int i = 0; i < capacity; i++) {
            if (!table[i].occupied) {
                System.out.println("Nearest available spot: #" + i);
                return;
            }
        }

        System.out.println("Parking full.");
    }

    // Statistics
    public void getStatistics() {

        double occupancy = (occupiedSpots * 100.0) / capacity;
        double avgProbes = totalParks == 0 ? 0 :
                (double) totalProbes / totalParks;

        System.out.println("\nParking Statistics:");
        System.out.printf("Occupancy: %.2f%%\n", occupancy);
        System.out.printf("Average Probes: %.2f\n", avgProbes);
    }

    public static void main(String[] args) {

        ParkingLot lot = new ParkingLot(500);

        lot.parkVehicle("ABC-1234");
        lot.parkVehicle("ABC-1235");
        lot.parkVehicle("XYZ-9999");

        lot.findNearestSpot();

        lot.exitVehicle("ABC-1234");

        lot.getStatistics();
    }


}
