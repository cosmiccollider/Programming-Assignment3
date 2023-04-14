import java.util.*;
import java.util.concurrent.*;

class p1_threadClass extends Thread 
{
    LazyList deque;
    LinkedList<Integer> bag;
    int max=100, min=1;

    public p1_threadClass(LinkedList<Integer> bag, LazyList deque) // all threads need to have access to the same bag
    {
        this.bag = bag;
        this.deque = deque;
    }
    
    public void run() 
    {
        while(true)
        {
            // search but only 10% of the time
            int action = ThreadLocalRandom.current().nextInt(min, max);
            if(action > 90)
            {
                System.out.println("Searching for guest...");
                if(deque.contains(action)) // the actual guest tag does not matter
                    System.out.println("Found");
                else
                    System.out.println("Couldn't find");
            }

            // Add to chain from bag
            synchronized(bag)
            {
                if(bag.size() != 0)
                {
                    int gift = bag.remove();
                    if (gift != -1) 
                        deque.add(gift);
                    System.out.println("Adding guest present to chain, Tag: "+gift);
                }
            }

            // alternate to remove now
            int gift = deque.remove();
            if (gift != -1) 
                System.out.println("Writing a Thank You card for " + gift);
            if(deque.size == 0 && bag.size() == 0)
            {
                System.out.println("Congratulations! The bag and chain are both empty and all guests have recieved \"Thank You\" cards.");
                break;
            }
        }
    }
}

public class problem1_runner 
{
    public static void main(String args[])
    {
        // create the unordered bag
        LinkedList<Integer> bag = new LinkedList<Integer>();
        LazyList deque = new LazyList();
        p1_threadClass[] threadArr = new p1_threadClass[4];

        for (int i = 1; i <= 500000; i++) {
            bag.add(i);
        }
        Collections.shuffle(bag);
       
        for(int i = 0; i < 4; i++)
        {
            threadArr[i] = new p1_threadClass(bag, deque);
            threadArr[i].start();
        }

        for(int i = 0; i < 4; i++)
            try {
                threadArr[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }
    
}
