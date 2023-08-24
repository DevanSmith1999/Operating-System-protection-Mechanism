public class AccessMatrix extends AccessControlStructure {
    boolean[][][] AM;

    AccessMatrix() {
        super();
    }

    @Override
    void generate() {
        boolean[][][] temp = new boolean[N][C][];
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < M; ++j) temp[i][j] = new boolean[]{rand.nextBoolean(), rand.nextBoolean()};
            for (int j = M; j < C; ++j) {
                if (j - M == i) temp[i][j] = new boolean[]{true};
                else temp[i][j] = new boolean[]{rand.nextBoolean()};
            }
        }
        this.AM = temp;
    }

    @Override
    void print() {
        System.out.printf("Access control scheme:  Access Matrix\nDomain count:  %d\nObject count:  %d\n", N, M);
        System.out.print("       ");
        for (int i = 1; i < M + 1; ++i) System.out.printf("  F%d   ", i);
        for (int i = 1; i < N + 1; ++i) System.out.printf("  D%d   ", i);
        System.out.println();
        for (int i = 0; i < N; ++i) {
            System.out.printf("    D%d ", i + 1);
            for (int j = 0; j < M; ++j) {
                if (AM[i][j][0] && AM[i][j][1]) System.out.print("  R/W  ");
                else if (AM[i][j][0]) System.out.print("   R   ");
                else if (AM[i][j][1]) System.out.print("   W   ");
                else System.out.print("   -   ");
            }
            for (int j = M; j < C; ++j) {
                if (AM[i][j][0]) System.out.print(" allow ");
                else System.out.print("   -   ");
            }
            System.out.println();
        }
        System.out.println();
    }

    @Override
    boolean[] getPrivileges(int domainId, int objectId) {
        // zero-indexed matrix, subtract one.
        return AM[domainId - 1][objectId - 1];
    }
}
