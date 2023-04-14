public class problem2_runner 
{
    private static int[] highestTemps = new int[5];
    private static int[] lowestTemps = new int[5];
    
    public static void main(String[] args) throws Exception 
    {
        SharedMemorySpace memorySpace = new SharedMemorySpace(highestTemps, lowestTemps);
        multiThread[] threadArr = new multiThread[8];
        for (int i = 0; i < 5; i++) {
            highestTemps[i] = -101;
        }

        long start = System.nanoTime();
        for(int i = 0; i < 8; i++)  // initializes each thread and tells it to start 
        {
            threadArr[i] = new multiThread((i+1),memorySpace,start);
            threadArr[i].start();
        }
        for(int i = 1; i <= 60; i++) 
        {
            Thread.sleep(1000);
            if(i%10 == 0)
                System.out.println("10 Minute Report, The biggest temperature change occured after "+memorySpace.timeOfChange+" minutes had passed");
            
            if(i != 60) // we dont want to wake up on the last iteration because they're already done
            {
                // notify all threads to wake up
                synchronized (multiThread.class) {
                    multiThread.class.notifyAll();
                    System.out.println("------------------------------------------------");
                }
            }
        }
        for(int i = 0; i < 8; i++)
        {
            if(threadArr[i].isAlive()) // kills all the threads
                threadArr[i].stop();
        }

        System.out.println("Top 5 hottest temperatures recorded");
        for (int i = 0; i < 5; i++) {
            System.out.println(i + " = " + highestTemps[i]);
        }
        System.out.println("Top 5 lowest temperatures recorded");
        for (int i = 0; i < 5; i++) {
            System.out.println(i + " = " + lowestTemps[i]);
        }
        
    }
}
