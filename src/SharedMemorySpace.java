public class SharedMemorySpace {
    int[] highestTemps;
    int[] lowestTemps;
    int lastTemp = -101;   // The last temperature recorded by any thread. Since their is no temperature at the beginning we will use -101 as the default.
    int biggestChange = 0; // the biggest temperature change
    int timeOfChange;   // the time of the biggest temperature change
    
    public SharedMemorySpace(int[] highestTemps, int[] lowestTemps) {
        this.highestTemps = highestTemps;
        this.lowestTemps = lowestTemps;
    }
}