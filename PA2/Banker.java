import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Banker {
    //Attributes
    private static int n;
    private static int m;

    private static int[] resources;
    private static int[] available;
    private static boolean[] finish;

    private static int[][] allocation;
    private static int[][] maximum;
    private static int[][] need;


    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter the number of processes: ");
        n = s.nextInt();
        s.nextLine();
        System.out.println("Enter the number of resource types: ");
        m = s.nextInt();
        s.nextLine();
        resources = new int[m];
        available = new int[m];
        finish = new boolean[n];
        allocation = new int[n][m];
        maximum = new int[n][m];
        need = new int[n][m];

        System.out.println("Enter the maximum number of resources for each resource type: ");
        for (int i = 0; i < resources.length; i++) {
            resources[i] = s.nextInt();
        }
        s.nextLine();
        System.out.println();

        System.out.println("For each resource type of each process, enter the number of allocated resources.");
        inputMatrix(s, allocation);

        int[] alloc = new int[m];
        available = resources.clone();

        for (int[] ints : allocation) {
            Arrays.fill(alloc, 0);
            for (int j = 0; j < allocation[0].length; j++) {
                alloc[j] += ints[j];
                available[j] -= ints[j];
                if (alloc[j] > resources[j]) {
                    System.out.println("Error: More resources are allocated than the maximum allowed resources for at least one resource type!");
                    System.exit(1);
                }
                if (available[j] < 0) {
                    System.out.println("Error: Invalid amount of available resources for at least one resource type!");
                    System.exit(2);
                }
            }
        }

        System.out.println("For each resource type of each process, enter the maximum demand.");
        inputMatrix(s, maximum);

        for (int[] ints : maximum) {
            for (int j = 0; j < maximum[0].length; j++) {
                if (ints[j] > resources[j]) {
                    System.out.println("Error: More resources are demanded than the maximum allowed resources for at least one resource type of at least one process!");
                    System.exit(3);
                }
            }
        }

        for (int i = 0; i < need.length; i++) {
            for (int j = 0; j < need[0].length; j++) {
                need[i][j] = maximum[i][j] - allocation[i][j];
                if (need[i][j] < 0) {
                    System.out.println("Error: Invalid need for at least one resource type of at least one process!");
                    System.exit(4);
                }
            }
        }

        processInput();
        safety();

        while (true) {
            processInput();

            System.out.println("Write \"quit\" (without quotes) to exit the program. Write anything else to have a process make a request");
            String quit = s.nextLine();
            System.out.println();

            if (quit.toLowerCase().equals("quit")) {
                break;
            }

            request(s);
        }
        System.out.println("Goodbye!");
        System.out.println();
    }

    private static void inputMatrix(Scanner s, int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            System.out.println("Index " + i + ": ");
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = s.nextInt();
            }
            s.nextLine();
        }
        System.out.println();
    }

    private static void processInput() {
        String alabels = "";
        String mlabels = "";
        String nlabels = "";
        String aspaces = "";
        String mspaces = "";
        String nspaces = "";
        String adashes = "";
        String mdashes = "";
        String ndashes = "";
        String rstr = "[ ";
        String avstr = "[ ";
        String[] astr = new String[n];
        String[] mstr = new String[n];
        String[] nstr = new String[n];

        for (int i = 0; i < m; i++) {
            rstr = rstr.concat(resources[i] + " ");
            avstr = avstr.concat(available[i] + " ");
        }

        for (int i = 0; i < n; i++) {
            astr[i] = mstr[i] = nstr[i] = "[ ";
            for (int j = 0; j < m; j++) {
                astr[i] = astr[i].concat(allocation[i][j] + " ");
                mstr[i] = mstr[i].concat(maximum[i][j] + " ");
                nstr[i] = nstr[i].concat(need[i][j] + " ");
            }
            astr[i] = astr[i].concat("]");
            mstr[i] = mstr[i].concat("]");
            nstr[i] = nstr[i].concat("]");
        }

        rstr = rstr.concat("]");
        avstr = avstr.concat("]");

        int alen = 0;
        int mlen = 0;
        int nlen = 0;

        for (int i = 0; i < n; i++) {
            if (astr[i].length() > alen) {
                alen = astr[i].length();
            }
            if (mstr[i].length() > mlen) {
                mlen = mstr[i].length();
            }
            if (nstr[i].length() > nlen) {
                nlen = nstr[i].length();
            }
        }

        if (alen > 10) {
            for (int i = 0; i < (alen - 10); i++) {
                alabels = alabels.concat(" ");
            }
        } else {
            for (int i = 0; i < (10 - alen); i++) {
                aspaces = aspaces.concat(" ");
            }
        }

        if (mlen > 7) {
            for (int i = 0; i < (mlen - 7); i++) {
                mlabels = mlabels.concat(" ");
            }
        } else {
            for (int i = 0; i < (7 - mlen); i++) {
                mspaces = mspaces.concat(" ");
            }
        }

        if (nlen > 4) {
            for (int i = 0; i < (nlen - 4); i++) {
                nlabels = nlabels.concat(" ");
            }
        } else {
            for (int i = 0; i < (4 - mlen); i++) {
                nspaces = nspaces.concat(" ");
            }
        }

        for (int i = 0; i < alabels.length(); i++) {
            adashes = adashes.concat("-");
        }
        for (int i = 0; i < mlabels.length(); i++) {
            mdashes = mdashes.concat("-");
        }
        for (int i = 0; i < nlabels.length(); i++) {
            ndashes = ndashes.concat("-");
        }

        System.out.println();
        System.out.println("Max Resources : " + rstr);
        System.out.println("Available     : " + avstr);
        System.out.println();
        System.out.println("Process | Allocation " + alabels + "| Maximum " + mlabels + "| Need ");
        System.out.println("--------|------------" + adashes + "|---------" + mdashes + "|------" + ndashes);

        for (int i = 0; i < n; i++) {
            System.out.printf("P%-6d | %s%s | %s%s | %s\n", i, astr[i], aspaces, mstr[i], mspaces, nstr[i]);
        }
        System.out.println();
    }

    private static boolean safety() {
        int[] work = available.clone();
        ArrayList<Integer> sequence = new ArrayList<>();
        Arrays.fill(finish, false);
        boolean deadlock;
        boolean safetyCheck;

        System.out.println("----------Safety algorithm commenced----------");
        do {
            System.out.println("System is not in a safe state");
            System.out.println();
            deadlock = true;
            for (int i = 0; i < n; i++) {
                String[] nstr = new String[n];
                nstr[i] = "[ ";
                String wstr = "[ ";
                boolean flag = false;
                System.out.println("Evaluating index " + i + ": ");
                if (finish[i]) {
                    System.out.println("finish[" + i + "] is true, continuing");
                    System.out.println();
                    continue;
                }
                for (int j = 0; j < m; j++) {
                    wstr = wstr.concat(work[j] + " ");
                    nstr[i] = nstr[i].concat(need[i][j] + " ");
                    if (need[i][j] > work[j]) {
                        flag = true;
                    }
                }
                wstr = wstr.concat("]");
                nstr[i] = nstr[i].concat("]");
                System.out.println("need[" + i + "] = " + nstr[i] + "\t work = " + wstr);
                if (flag) {
                    System.out.println("need[" + i + "] is not less than or equal to work, continuing");
                    System.out.println();
                    continue;
                }
                wstr = "[ ";
                for (int j = 0; j < m; j++) {
                    work[j] += allocation[i][j];
                    wstr = wstr.concat(work[j] + " ");
                }
                wstr = wstr.concat("]");
                finish[i] = true;
                deadlock = false;
                sequence.add(i);
                System.out.println("finish[" + i + "] is now true");
                System.out.println("work is now " + wstr);
                System.out.println();
            }
            safetyCheck = true;
            for (boolean fin: finish) {
                safetyCheck &= fin;
            }
            if (safetyCheck) {
                deadlock = false;
            }
            if (deadlock) {
                break;
            }
        } while (!safetyCheck);

        if (!deadlock) {
            String sstr = sequence.toString();
            sstr = sstr.replace("[", "< ").replace("]", " >");
            System.out.println("System is now in a safe state");
            System.out.println("Safe sequence = " + sstr);
        } else {
            System.out.println("System will not be left in a safe state");
        }
        System.out.println("----------Safety algorithm completed----------");
        System.out.println();

        return !deadlock;
    }

    private static void request(Scanner s) {
        //TBD
        int pind;
        int[] preq = new int[m];

        System.out.println("Enter index of process for making a request: ");
        pind = s.nextInt();
        s.nextLine();

        if (pind >= n) {
            System.out.println("Error: index out of bounds!");
            System.out.println();
            return;
        }

        System.out.println("Enter a value for each resource type:");
        for (int i = 0; i < m; i++) {
            preq[i] = s.nextInt();
        }
        s.nextLine();

        for (int i = 0; i < m; i++) {
            if (preq[i] > need[pind][i]) {
                System.out.println("Request denied. For at least one resource type, the request of the process exceeds the need");
                System.out.println();
                return;
            }
            if (preq[i] > available[i]) {
                System.out.println("Request denied. For at least one resource type, the request of the process exceeds the available resources");
                System.out.println();
                return;
            }
        }

        int[] tempav = available.clone();
        int[] tempal = allocation[pind].clone();
        int[] tempn = need[pind].clone();

        for (int i = 0; i < m; i++) {
            available[i] -= preq[i];
            allocation[pind][i] += preq[i];
            need[pind][i] -= preq[i];
        }

        System.out.println("Request in consideration, now checking for system safety");
        System.out.println();

        if (!safety()) {
            available = tempav.clone();
            allocation[pind] = tempal.clone();
            need[pind] = tempn.clone();

            System.out.println("Request denied. System would not result in a safe state");
        } else {
            System.out.println("Request granted. Resources for chosen process as well as available resources have been updated");
        }
        System.out.println();
    }
}
