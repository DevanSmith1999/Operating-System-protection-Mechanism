import java.util.*;

public class CapabilityList extends AccessControlStructure {
    // some type of base data structure should be declared as a field here

    // constructor just calls super()
    CapabilityList() { super(); }
    List<HashMap<String,boolean[]>> headers;


    @Override
    void generate() {
        // randomly generate the base data structure
        // each domain, resource pair will have 2 elements: {read, write}
        // each domain, domain pair will have 1 element: {allow}
        // if all false, then exclude.
        boolean rand1;
        boolean rand2;
        List<HashMap<String,boolean[]>> temp= new ArrayList<>();
        for(int i=1;i<N+1;i++){
            HashMap<String,boolean[]> domain= new HashMap<>();
            for(int j=1;j<M+1;j++){
                rand1=rand.nextBoolean();//Assigning a random boolean to a variable to avoid nulls
                rand2=rand.nextBoolean();
                //If either of the random booleans is true add them as the permissions, if both false add nothing because
                //that would be a file with no permissions(a null in the list).
                if(rand1||rand2){
                    domain.put("F"+j,new boolean[]{rand1,rand2});
                }
            }
            for(int j=1;j<N+1;j++) {
                rand1 = rand.nextBoolean();//Assigning a random boolean for the domain access
                //Making sure each domain will have access to itself
                if (j == i) domain.put("D" + j, new boolean[]{true});
                //If the domain is not equal to itself and the random boolean is true add it to the domains permissions
                //if false do nothing to avoid null spaces in the CL
                else if (rand1) domain.put("D" + j, new boolean[]{true});
            }
            temp.add(domain);
        }
        this.headers=temp;
    }

    @Override
    void print() {
        // print header and base data structure
        // see example output from moodle and AccessMatrix for guidance
        System.out.printf("Access control scheme:  Capability List\nDomain count:  %d\nObject count:  %d\n", N, M);

        for (int i = 0; i < N; i++) {
            System.out.printf("    D%d --> ", i + 1);
            int count=0;
            for(int j=1;j<M+1;j++){
                String key="F"+j;
                if(headers.get(i).containsKey(key)){
                    count++;
                    System.out.printf(" F%d:", j);
                    if(headers.get(i).get(key)[0]&& headers.get(i).get(key)[1]) System.out.print("R/W");
                    else if(headers.get(i).get(key)[0]) System.out.print("R");
                    else if(headers.get(i).get(key)[1]) System.out.print("W");
                    if(count!=headers.get(i).size()){
                        System.out.print(", ");
                    }
                }
            }

            for(int j=1;j<N+1;j++){
                String key="D"+j;
                if(headers.get(i).containsKey(key)){
                    count++;
                    System.out.printf(" D%d:",j);
                    System.out.print(" allow");
                    if(count!=headers.get(i).size()){
                        System.out.print(", ");
                    }
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    @Override
    boolean[] getPrivileges(int domainId, int objectId) {
        // returns boolean array for the indicated domain, object pair
        // return statement below will need to change
        String key;
        if(objectId>M){
            key="D"+(objectId - M);
        }
        else{
            key="F"+objectId;
        }
        return headers.get(domainId - 1).getOrDefault(key, new boolean[]{false,false});
    }
}