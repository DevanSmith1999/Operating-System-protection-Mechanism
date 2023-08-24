

public class Main {

    public static void main(String[] args) {
        // Test if proper number of program arguments are provided
        if (args.length != 2) {
            System.out.println("PROGRAM TERMINATED");
            System.out.println("Invalid number of program arguments. Use -S # where # is a number between 1 and 3.");
            System.exit(0);
        }
        // Test validity of first program argument
        if (!(args[0].equals("-S"))) {
            System.out.println("PROGRAM TERMINATED");
            System.out.println("Invalid program argument: " + args[0] + ". Use -S # where # is a number between 1 and 3.");
            System.exit(0);
        }
        // Test validity of second program argument & begin correct task
        switch (args[1]) {
            case "1" -> {
                // instantiate AccessControlStructure
                AccessMatrix AM = new AccessMatrix();

                // thread array is currently unnecessary and can be removed.
                // if we need to time the execution, like in project one,
                // we can use this to call Thread.Join() on all threads before getting end time.
                Thread[] threads = new Thread[AM.N];
                for (int i = 0; i < AM.N; ++i) {
                    threads[i] = new Thread(new User(AM));
                    threads[i].start();
                }
            }
            case "2" -> {
                // instantiate AccessControlStructure
                AccessList AL = new AccessList();

                // thread array is currently unnecessary and can be removed.
                // if we need to time the execution, like in project one,
                // we can use this to call Thread.Join() on all threads before getting end time.
                Thread[] threads = new Thread[AL.N];
                for (int i = 0; i < AL.N; ++i) {
                    threads[i] = new Thread(new User(AL));
                    threads[i].start();
                }
            }
            case "3" -> {
                // instantiate AccessControlStructure

                // thread array is currently unnecessary and can be removed.
                // if we need to time the execution, like in project one,
                // we can use this to call Thread.Join() on all threads before getting end time.
                CapabilityList CL = new CapabilityList();
                Thread[] threads = new Thread[CL.N];
                for (int i = 0; i < CL.N; ++i) {
                    threads[i] = new Thread(new User(CL));
                    threads[i].start();
                }
            }
            default -> {
                System.out.println("PROGRAM TERMINATED");
                System.out.println("Invalid program argument: " + args[1] + ". Use -S # where # is a number between 1 and 3.");
            }
        }


    }

}
