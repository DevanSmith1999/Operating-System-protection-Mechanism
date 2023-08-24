import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class Resource {
    static int nextId = 1;

    int id;
    long buffer = ThreadLocalRandom.current().nextLong();

    // the following three field are used for synchronization of this resource
    // the synchronization is implemented by the user
    int readCount = 0;
    Semaphore access = new Semaphore(1);
    Semaphore mutex = new Semaphore(1);

    Resource() {
        this.id = nextId++;
    }
}
