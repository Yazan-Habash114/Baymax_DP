package newVersion;

import java.util.Scanner;
import static java.lang.Math.*;

class Position {
    private double x, y;
    
    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public double getX() {
        return this.x;
    }
    public double getY() {
        return this.y;
    }
    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
}

public class Project {
    private static final int MINUS_INFINITY = -999999;
    private static int [][] decision;
    
    private static double distance(Position a, Position b) {
        //System.out.println("Distance between: " + a.toString() + "  and  " + b.toString());
        double xDiff = pow(a.getX() - b.getX(), 2);
        double yDiff = pow(a.getY() - b.getY(), 2);
        return sqrt(xDiff + yDiff);
    }
    
    private static void sort(int [] arr) {   // Bubble sort
        int n = arr.length;
        int temp;
        for(int i=0; i < n; i++)
            for(int j=1; j < (n-i); j++)
               if(arr[j-1] > arr[j]) {
                 temp = arr[j-1];
                 arr[j-1] = arr[j];
                 arr[j] = temp;
              }
    }
    
    private static void sortMap(int [] val, int [] sol) {
        int n = val.length;
        int temp1, temp2;
        for(int i=0; i < n; i++)
            for(int j=1; j < (n-i); j++)
               if(val[j-1] > val[j]) {
                 temp1 = val[j-1];
                 val[j-1] = val[j];
                 val[j] = temp1;
                 temp2 = sol[j-1];
                 sol[j-1] = sol[j];
                 sol[j] = temp2;
              }
    }
    
    private static void printPath(Position [] A, Position [] B, int m, int n) {
        int start = decision[m][n];
        if(start == 2)   // We will move to decision[m-1][n-1]
            printPath(A, B, m-1, n-1);
        else if(start == 1)   // Go to decision[m][n-1]
            printPath(A, B, m, n-1);
        else if(start == 3)   // Go to decision[m-1][n]
            printPath(A, B, m-1, n);
        System.out.println("Person at point: " + A[m].toString() + ", Robot at point: " + B[n].toString());
    }
    
    private static int minLength(Position [] A, Position [] B, int [] lengths, int i, int j) {
        int sub_sol = MINUS_INFINITY;
        int sub_solA, sub_solB, sub_solC;
        if(i > 0 && j > 0) {
            sub_solA = minLength(A, B, lengths, i-1, j-1);
            sub_solB = minLength(A, B, lengths, i, j-1);
            sub_solC = minLength(A, B, lengths, i-1, j);
            int [] arr = new int[3];
            arr[0] = sub_solA;
            arr[1] = sub_solB;
            arr[2] = sub_solC;
            sort(arr);
            if(arr[2] == -1)
                sub_sol = -1;  //  All elements are (-1)
            else {
                for(int q=0; q<arr.length; ++q)
                    if(arr[q] != -1) {
                        sub_sol = arr[q];
                        break;
                    }
            }
        }
        else if(i == 0 && j > 0)
            sub_sol = minLength(A, B, lengths, 0, j-1);
        else if(j == 0 && i > 0)
            sub_sol = minLength(A, B, lengths, i-1, 0);
        
        double dist = distance(A[i], B[j]);
        //System.out.println("dist = " + dist);
        
        int len = -1;
        for(int r = 0; r < lengths.length; ++r) {
            //System.out.println(lengths[r]);
            if(lengths[r] >= dist) {
                len = lengths[r];
                break;
            }
        }
        
        //System.out.println("Suitable length = " + len);
        //System.out.println("Sub-Sol = " + sub_sol);
        
        if(len != -1 && sub_sol != -1)
            return max(len, sub_sol);
        return -1;
    }
    
    private static int minLengthDP(Position [] A, Position [] B, int [] lengths, int i, int j) {
        int sub_sol = MINUS_INFINITY;
        
        int [][] dp = new int [i+1][j+1];
        decision = new int [i+1][j+1];
        
        int len = -1;
        double dist = distance(A[0], B[0]);
        for(int r = 0; r < lengths.length; ++r)
            if(lengths[r] >= dist) {
                len = lengths[r];
                break;
            }
        //System.out.println("Suitable length = " + len);
        
        if(len != -1 && sub_sol != -1)
            dp[0][0] = max(len, sub_sol);
        else
            dp[0][0] = -1;
        decision[0][0] = -1;
        //System.out.println("first element = " + dp[0][0]);
        
        // After fill first index dp[0][0], then fill the table
        
        int sub_solA, sub_solB, sub_solC;
        for(int s=0; s<=i; ++s)
            for(int t=0; t<=j; ++t) {
                if(s == 0 && t == 0)
                    continue;
                if(s == 0 && t > 0) {
                    sub_sol = dp[0][t-1];
                    decision[0][t] = 1;     // From left
                }
                else if(t == 0 && s > 0) {
                    sub_sol = dp[s-1][0];
                    decision[s][0] = 3;    // From above
                }
                
                else if(s > 0 && t > 0) {
                    sub_solA = dp[s-1][t-1];
                    sub_solB = dp[s][t-1];
                    sub_solC = dp[s-1][t];
                    int [] val = new int[3];
                    int [] sol = new int[3];
                    val[0] = sub_solA;
                    val[1] = sub_solB;
                    val[2] = sub_solC;
                    sol[0] = 2;
                    sol[1] = 1;
                    sol[2] = 3;
                    sortMap(val, sol);
                    if(val[2] == -1)
                        sub_sol = -1;  //  All elements are (-1)
                    else {
                        int q;
                        for(q=0; q<val.length; ++q)
                            if(val[q] != -1) {
                                sub_sol = val[q];
                                break;
                            }
                        decision[s][t] = sol[q];
                    }
                }
                
                dist = distance(A[s], B[t]);
                //System.out.println("dist = " + dist);
                len = -1;
                for(int r = 0; r < lengths.length; ++r)
                    if(lengths[r] >= dist) {
                        len = lengths[r];
                        break;
                    }
                //System.out.println("Suitable length = " + len);
                //System.out.println("Sub-Sol = " + sub_sol);

                if(len != -1 && sub_sol != -1) {
                    dp[s][t] = max(len, sub_sol);
                    //System.out.println("Saved value dp[" + s + "][" + t + "] = " + dp[s][t]);
                }
                else {
                    dp[s][t] = -1;
                    decision[s][t] = -1;
                    //System.out.println("Saved value dp[" + s + "][" + t + "] = " + dp[s][t]);
                }
            }
        
        return dp[i][j];
    }
    
    public static void main(String[] args) {
        //Point [] A = { new Position(0, 1), new Position(1, 1), new Position(1, 2) };
        //Point [] B = { new Position(0, 0), new Position(1, 0) };                                     // Gives 2
        //Point [] A = { new Position(3, -4), new Position(5, 0), new Position(5, 2), new Position(3, 2), new Position(5, 4) };
        //Point [] B = { new Position(-3, 0), new Position(-5, -4), new Position(-2, -4), new Position(-2, 4) };                // Gives 9
        /*Position [] A = { new Position(2, 4), new Position(-3, 4), new Position(1, 6), new Position(5, 4), new Position(3, 0) };
        Position [] B = { new Position(-5, 6), new Position(-4, 2), new Position(-5, 0), new Position(-1, 2), new Position(-4, -1)
                            , new Position(-3, 3), new Position(-1, -4)};                             // Gives 9*/
        
        Scanner scan = new Scanner(System.in);
        int size = Integer.parseInt(scan.nextLine());
        String line1 = scan.nextLine();
        String [] seqA = line1.split(",");
        
        int index = 0;
        Position [] A = new Position[size];
        for(int i=0; i<size*2; i=2+i) {
            Position p = new Position(Integer.parseInt(seqA[i]), Integer.parseInt(seqA[i+1]));
            A[index++] = p;
        }
        
        
        size = Integer.parseInt(scan.nextLine());
        String line2 = scan.nextLine();
        String [] seqB = line2.split(",");
        
        index = 0;
        Position [] B = new Position[size];
        for(int i=0; i<size*2; i=2+i) {
            Position p = new Position(Integer.parseInt(seqB[i]), Integer.parseInt(seqB[i+1]));
            B[index++] = p;
        }
        
        size = Integer.parseInt(scan.nextLine());
        String line3 = scan.nextLine();
        String [] lens = line3.split(",");
        
        int [] lengths = new int [size];
        for(int i=0; i<size; ++i)
            lengths[i] = Integer.parseInt(lens[i]);
        
        //int [] lengths = {3, 4, 2, 1, 8, 7, 9};
        //int [] lengths = {2, 1, 3};
        sort(lengths);
        
        //System.out.println("Returned value = " + minLength(A, B, lengths, A.length - 1, B.length - 1));
        System.out.println("Returned value = " + minLengthDP(A, B, lengths, A.length - 1, B.length - 1));
        
        /*for(int r=0; r<A.length; ++r) {
            for(int s=0; s<B.length; ++s)
                System.out.print(decision[r][s] + " ");
            System.out.println();
        }*/
        
        printPath(A, B, A.length - 1, B.length - 1);
        
        System.gc();
        System.exit(0);
    }
    
}