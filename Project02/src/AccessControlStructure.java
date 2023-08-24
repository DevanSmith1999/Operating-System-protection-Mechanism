import java.util.concurrent.ThreadLocalRandom;

// this is the abstract base class for all three data structures
public abstract class AccessControlStructure {
    ThreadLocalRandom rand = ThreadLocalRandom.current();

    // M = number of file objects (resources) [3,7]
    int M = rand.nextInt(3,8);

    // N = number of domains [3,7], number of rows for matrix
    int N = rand.nextInt(3,8);

    // C = total number of objects (files and domains), number of columns for matrix
    int C = N + M;

    // array of file objects, called resources, for reading and writing
    Resource[] resources = new Resource[M];

    // this constructor standardizes the construction for all subclasses.
    // subclass constructors need only call super()
    AccessControlStructure() {
        // when instantiated, build the appropriate number of resources
        for (int i = 0; i < M; ++i) resources[i] = new Resource();

        // randomly generate privileges (build the datastructure)
        generate();

        // print the data structure
        print();
    }

    // this method randomly generates the data structure (privileges)
    // each intersection (domain-resource or domain-domain) should hold
    // a boolean array
    abstract void generate();

    // this method should print the data structure and
    // the necessary statements preceding it as in the
    // example output
    abstract void print();

    // this method should return the boolean array held at the
    // intersection of the specified domain and object (resource or domain)
    // KEEP IN MIND:  domains and resources are indexed from 1
    abstract boolean[] getPrivileges(int domainId, int objectId);

    // this method is called by a user thread when checking a read or write
    // resource request.  no subclass implementation is needed.  DO NOT OVERRIDE.
    public boolean requestResource(User user, int resourceId, int action) {
        return getPrivileges(user.domain, resourceId)[action];
    }

    // this method is called by a user thread when checking a domain change
    // request.  no subclass implementation is needed.  DO NOT OVERRIDE.
    public boolean requestDomain(User user, int domainId) {
        return getPrivileges(user.domain, domainId)[0];
    }
}
