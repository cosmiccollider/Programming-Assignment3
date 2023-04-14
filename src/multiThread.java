import java.util.Random;

public class multiThread extends Thread
{
    Random rand = new Random();
    int max=70,min=-100,num;
    SharedMemorySpace memory;
    long start;

    public multiThread(int num, SharedMemorySpace memorySpace, long start)
    {
        this.memory = memorySpace;
        this.num = num;
        this.start = start;
    }
    // 
    public void copyItOverHigh(int x, int newTemp)
    {
        for (int i = x; i < 5; i++) 
        {
            int oldTemp = memory.highestTemps[i];
            memory.highestTemps[i] = newTemp;
            if(i == 4)
                break;
            if(memory.highestTemps[i+1] != -101)
                newTemp = oldTemp;
            else
            {
                memory.highestTemps[i+1] = oldTemp;
                break;
            }
        }
    }
    
    public void copyItOverLow(int x, int newTemp)
    {
        for (int i = x; i < 5; i++) 
        {
            int oldTemp = memory.lowestTemps[i];
            memory.lowestTemps[i] = newTemp;
            if(i == 4)
                break;
            if(memory.lowestTemps[i+1] != -101)
                newTemp = oldTemp;
            else
            {
                memory.lowestTemps[i+1] = oldTemp;
                break;
            }
        }
    }

    @Override
    public void run()
    {
        while(true)
        {
            synchronized(memory) // only one thread has access to the shared memory
            {
                long tempTime = System.nanoTime(); // the time when the temperature was read in
                int newTemp = rand.nextInt(max - min + 1) + min;
                System.out.println("Thread "+num+" recorded a temperature of "+newTemp);
                // as soon as we generate the number we need to see if its one of the hottest or coldest temps
                for (int i = 0; i < 5; i++) 
                {
                    int currentValue = memory.highestTemps[i];
                    if(newTemp == currentValue)
                        break;
                    if (newTemp > currentValue) 
                    {
                        copyItOverHigh(i,newTemp);
                        break;
                    }
                }
                for (int i = 0; i < 5; i++) 
                {
                    int currentValue = memory.lowestTemps[i];
                    if(newTemp == currentValue)
                        break;
                    if (newTemp < currentValue) 
                    {
                        copyItOverLow(i,newTemp);
                        break;
                    }
                }
                if(memory.lastTemp != -101) // if their is a previous temperature to work with
                {
                    int tempChange = Math.abs(memory.lastTemp - newTemp);
                    if(tempChange > memory.biggestChange) // if the biggest change in temperature is our current temperature
                    {
                        memory.biggestChange = tempChange;
                        memory.timeOfChange = (int) ((tempTime - start) / 1000000000);
                    }
                }
                memory.lastTemp = newTemp;
                
            }
            synchronized (multiThread.class) 
            {
                try {
                    multiThread.class.wait(); // this thread has to wait until main says "time has passed" and he can get another temperature
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
            }
        }
    }
}