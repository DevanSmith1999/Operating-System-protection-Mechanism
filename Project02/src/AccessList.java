import java.util.ArrayList;
import java.util.HashMap;

public class AccessList extends AccessControlStructure {
    // declare base data structure
    ArrayList<HashMap<Integer, boolean[]>> aclList;

    // constructor just calls super()
    AccessList() { super(); }

    @Override
    void generate() {

        //randomly generate base data structure
        ArrayList<HashMap<Integer, boolean[]>> tempList = new ArrayList<>();
        for (int i = 0; i < C; i++){
            HashMap <Integer, boolean[]> acl = new HashMap<>();
            for (int j = 0; j < N; j++) {
                boolean[] permissions;
                // each resource, domain pair will have 2 elements: {read, write}
                if (i < M) {
                    permissions = new boolean[]{rand.nextBoolean(), rand.nextBoolean()};
                }
                // each domain, domain pair will have 1 element: {allow}
                else{
                    if ((i - M) == j){
                        permissions = new boolean[]{true};
                    }
                    else{
                        permissions = new boolean[]{rand.nextBoolean()};
                    }
                }
                // if all false, then exclude.
                boolean hasPermissions = false;
                for (boolean permission : permissions) {
                    if (permission) {
                        hasPermissions = true;
                        break;
                    }
                }
                if (hasPermissions) {
                    acl.put(j, permissions);
                }
            }
            tempList.add(acl);
        }
        this.aclList = tempList;
    }

    @Override
    void print() {

        //New print
        // print header and base data structure
        System.out.printf("Access control scheme:  Access List\nDomain count:  %d\nObject count:  %d\n", N, M);
        for (int i = 0; i < M; i++){
            System.out.printf("    F%d --> ", i + 1);
            int count = 0;
            for (int j = 0; j < N; j++){
                if (aclList.get(i).get(j) != null) {
                    count++;
                    System.out.printf(" D%d:", j+1);
                    if (aclList.get(i).get(j)[0] && aclList.get(i).get(j)[1]) System.out.print("R/W");
                    else if (aclList.get(i).get(j)[0]) System.out.print("R");
                    else if (aclList.get(i).get(j)[1]) System.out.print("W");
                    if (count != aclList.get(i).size()){
                        System.out.print(", ");
                    }
                }

            }
            System.out.println();
        }
        for (int i = M; i < C; i++) {
            System.out.printf("    D%d --> ", i + 1 - M);
            int count = 0;
            for (int j = 0; j < N; j++){
                if (aclList.get(i).get(j) != null) {
                    count++;
                    System.out.printf("D%d:allow", j + 1);
                    if (count != aclList.get(i).size()){
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
        // returns boolean array for the indicated object, domain pair
        return aclList.get(objectId - 1).getOrDefault(domainId - 1, new boolean[]{false, false});
    }
}
