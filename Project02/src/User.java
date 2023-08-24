import java.util.concurrent.ThreadLocalRandom;

public class User implements Runnable{
    static int nextId = 1;
    int domain, id, M, N, C;
    AccessControlStructure accessControl;
    Resource[] resources;


    User(AccessControlStructure accessControl) {
        this.id = nextId++;
        this.domain = id;
        this.accessControl = accessControl;
        this.M = accessControl.M;
        this.N = accessControl.N;
        this.C = accessControl.C;
        this.resources = accessControl.resources;
    }

    // print and yield [3,7] times
    void yieldLoop() {
        int yields = ThreadLocalRandom.current().nextInt(3,8);
        System.out.printf("%s yields %d times.\n", this, yields);
        for (int i = 0; i < yields; ++i) Thread.yield();
    }

    // method that is called for a write request.
    void writeResource(Resource resource, boolean granted) throws InterruptedException{
        // if denied
        if (!granted) {
            System.out.printf("%s is denied write access to Resource %d.\n", this, resource.id);
            return;
        }

        System.out.printf("%s is granted write access to Resource %d.\n", this, resource.id);

        // only one permit, writer waits for exclusive access
        resource.access.acquire();

        resource.buffer = ThreadLocalRandom.current().nextLong();
        System.out.printf("%s writes to Resource %d:  %d\n", this, resource.id, resource.buffer);

        // per project description, user should yield while holding semaphore
        yieldLoop();

        resource.access.release();
    }

    // method that is called for a read request.
    void readResource(Resource resource, boolean granted) throws InterruptedException {
        // if denied
        if (!granted) {
            System.out.printf("%s is denied read access to Resource %d.\n", this, resource.id);
            return;
        }

        System.out.printf("%s is granted read access to Resource %d.\n", this, resource.id);

        // first reader waits until writer is done, then unlimited readers may access concurrently
        resource.mutex.acquire();
        if (++resource.readCount == 1) resource.access.acquire();
        resource.mutex.release();

        System.out.printf("%s reads from Resource %d:  %d\n", this, resource.id, resource.buffer);

        // per project description, user should yield while holding semaphore
        yieldLoop();

        resource.mutex.acquire();
        if (--resource.readCount == 0) resource.access.release();
        resource.mutex.release();
    }

    // method that is called for a domain request.
    void changeDomain(int domain, boolean granted) {
        // if denied
        if (!granted) {
            System.out.printf("%s is denied access to Domain %d.\n", this, domain);
            return;
        }

        System.out.printf("%s is granted access to Domain %d.\n", this, domain);

        // semaphores are unnecessary here.
        this.domain = domain;
        System.out.printf("%s changes to Domain %d.\n", this, domain);

        yieldLoop();
    }

    // convenience method for all of the printing that occurs.
    @Override
    public String toString() {
        return String.format("[User %d (D%d)]", id, domain);
    }

    @Override
    public void run() {
        try {
            ThreadLocalRandom rand = ThreadLocalRandom.current();
            int obj, action;
            for (int i = 0; i < 5; ++i) {
                // get random int in [1,C-1]
                obj = rand.nextInt(1, C);

                // if random number is >= number of resources + current domain, add one
                // this ensures that the random value is never the user's current domain
                if (obj >= M + domain) obj++;

                // if random number is > number of resources, it's a domain request
                // otherwise, it's a resource request
                if (obj > M) {
                    System.out.printf("%s requests access to Domain %d.\n", this, obj - M);
                    changeDomain(obj - M, accessControl.requestDomain(this, obj));
                }
                else {
                    // get random int in [0,1] to pick read (0) or write (1)
                    action = rand.nextInt(2);
                    if (action < 1) {
                        System.out.printf("%s requests read access to Resource %d.\n", this, obj);

                        // requestResource method of AccessControlStructure takes a user and an action (0 or 1)
                        // and returns true (granted) or false (denied).
                        // this is passed to the readResource method of the User.
                        // resource array is zero-indexed, so subtract one.
                        readResource(resources[obj - 1], accessControl.requestResource(this, obj, 0));
                    }
                    else {
                        System.out.printf("%s requests write access to Resource %d.\n", this, obj);

                        // requestResource method of AccessControlStructure takes a user and an action (0 or 1)
                        // and returns true (granted) or false (denied).
                        // this is passed to the writeResource method of the User.
                        // resource array is zero-indexed, so subtract one.
                        writeResource(resources[obj - 1], accessControl.requestResource(this, obj, 1));
                    }
                }

                // per project description, yield [3,7] times after request, regardless of outcome.
                yieldLoop();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
