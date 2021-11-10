package algorithmproject;

import java.util.Scanner;
import java.util.Queue;
import java.util.LinkedList;
import static java.lang.Math.*;

class Point {
    private double x, y;
    
    public Point(double x, double y) {
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

public class AlgorithmProject {
    private static final int MINUS_INFINITY = -999999;
    private static Queue<Integer> q;
    
    private static double distance(Point a, Point b) {
        System.out.println("Distance between: " + a.toString() + "  and  " + b.toString());
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
    
    private static int minLength(Point [] A, Point [] B, int [] lengths, int i, int j) {
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
        System.out.println("dist = " + dist);
        
        int len = -1;
        for(int r = 0; r < lengths.length; ++r) {
            //System.out.println(lengths[r]);
            if(lengths[r] >= dist) {
                len = lengths[r];
                break;
            }
        }
        
        System.out.println("Suitable length = " + len);
        System.out.println("Sub-Sol = " + sub_sol);
        
        if(len != -1 && sub_sol != -1)
            return max(len, sub_sol);
        return -1;
    }
    
    private static int minLengthDP(Point [] A, Point [] B, int [] lengths, int i, int j) {
        int sub_sol = MINUS_INFINITY;
        
        int [][] dp = new int [i+1][j+1];
        int len = -1;
        double dist = distance(A[0], B[0]);
        for(int r = 0; r < lengths.length; ++r) {
                    //System.out.println(lengths[r]);
                    if(lengths[r] >= dist) {
                        len = lengths[r];
                        break;
                    }
                }
        System.out.println("Suitable length = " + len);
        
        if(len != -1 && sub_sol != -1)
            dp[0][0] = max(len, sub_sol);
        else
            dp[0][0] = -1;
        System.out.println("first element = " + dp[0][0]);
        
        // After fill first index dp[0][0], then fill the table
        
        int sub_solA, sub_solB, sub_solC;
        for(int s=0; s<=i; ++s)
            for(int t=0; t<=j; ++t) {
                if(s == 0 && t == 0)
                    continue;
                if(s == 0 && t > 0)
                    sub_sol = dp[0][t-1];
                else if(t == 0 && s > 0)
                    sub_sol = dp[s-1][0];
                
                else if(s > 0 && t > 0) {
                    sub_solA = dp[s-1][t-1];
                    sub_solB = dp[s][t-1];
                    sub_solC = dp[s-1][t];
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
                
                dist = distance(A[s], B[t]);
                System.out.println("dist = " + dist);
                len = -1;
                for(int r = 0; r < lengths.length; ++r) {
                    //System.out.println(lengths[r]);
                    if(lengths[r] >= dist) {
                        len = lengths[r];
                        break;
                    }
                }
                System.out.println("Suitable length = " + len);
                System.out.println("Sub-Sol = " + sub_sol);

                if(len != -1 && sub_sol != -1) {
                    dp[s][t] = max(len, sub_sol);
                    System.out.println("Saved value dp[" + s + "][" + t + "] = " + dp[s][t]);
                }
                else {
                    dp[s][t] = -1;
                    System.out.println("Saved value dp[" + s + "][" + t + "] = " + dp[s][t]);
                }
            }
        
        return dp[i][j];
    }
    
    // Old version
    private static int minLength(Point [] A, Point [] B, int i, int j) {
        int sub_sol = MINUS_INFINITY;
        //************************************* Ignore this
        /*if(i > 0 && j > 0)
            sub_sol = minLength(A, B, i-1, j-1);
        else if(i == 0 && j > 0)
            sub_sol = minLength(A, B, 0, j-1);
        else if(i > 0 && j == 0)
            sub_sol = minLength(A, B, i-1, 0);*/
        
        //*************************************** Nice solution backward
        /*if(i == j && i > 0)
            sub_sol = minLength(A, B, i-1, j-1);
        else if(i > j)
            sub_sol = minLength(A, B, i-1, j);
        else if(j > i)
            sub_sol = minLength(A, B, i, j-1);*/
        
        //*************************************** If (i==j) => get three solutions
        /*if(i == j && i > 0) {
            int sub_solA, sub_solB, sub_solC;
            sub_solA = minLength(A, B, i-1, j-1);
            sub_solB = minLength(A, B, i-1, j);
            sub_solC = minLength(A, B, i, j-1);
            sub_sol = min(sub_solA, sub_solB);
            sub_sol = min(sub_sol, sub_solC);
        }
        else if(i > j)
            sub_sol = minLength(A, B, i-1, j);
        else if(j > i)
            sub_sol = minLength(A, B, i, j-1);*/
        
        
        //****************************************  Three possible solutions
        int sub_solA, sub_solB, sub_solC;
        if(i > 0 && j > 0) {
            sub_solA = minLength(A, B, i-1, j-1);
            sub_solB = minLength(A, B, i, j-1);
            sub_solC = minLength(A, B, i-1, j);
            sub_sol = min(sub_solA, sub_solB);
            sub_sol = min(sub_sol, sub_solC);
        }
        else if(i == 0 && j > 0)
            sub_sol = minLength(A, B, 0, j-1);
        else if(j == 0 && i > 0)
            sub_sol = minLength(A, B, i-1, 0);
        
        int len;
        double dist = distance(A[i], B[j]);
        System.out.println("dist = " + dist);
        while(true) {
            if(q.isEmpty() || dist <= q.peek())
                break;
            else if(dist > q.peek())
                q.remove();
        }
        System.out.println("Suitable length = " + q.peek());
        
        if(!q.isEmpty())
            len = q.peek();
        else
            len = -1;
        
        if(len != -1)
            return max(len, sub_sol);
        return -1;
    }
    
    private static int minLengthDP(Point [] A, Point [] B, int i, int j) {
        int sub_sol = MINUS_INFINITY;
        
        int [][] dp = new int [i+1][j+1];
        int len;
        double dist = distance(A[0], B[0]);
        while(true) {
            if(q.isEmpty() || dist <= q.peek())
                break;
            else if(dist > q.peek())
                q.remove();
        }
         if(!q.isEmpty())
            len = q.peek();
        else
            len = -1;
        
        if(len != -1)
            dp[0][0] = max(len, sub_sol);
        else
            dp[0][0] = -1;
        System.out.println("first element = " + dp[0][0]);
        
        // After fill first index dp[0][0], then fill the table
        
        int sub_solA, sub_solB, sub_solC;
        for(int s=0; s<=i; ++s)
            for(int t=0; t<=j; ++t) {
                if(s == 0 && t == 0)
                    continue;
                if(s == 0 && t > 0)
                    sub_sol = dp[0][t-1];
                else if(t == 0 && s > 0)
                    sub_sol = dp[s-1][0];
                
                else if(s > 0 && t > 0) {
                    sub_solA = dp[s-1][t-1];
                    sub_solB = dp[s][t-1];
                    sub_solC = dp[s-1][t];
                    sub_sol = min(sub_solA, sub_solB);
                    sub_sol = min(sub_sol, sub_solC);
                }
                
                dist = distance(A[s], B[t]);
                System.out.println("dist = " + dist);
                while(true) {
                    if(q.isEmpty() || dist <= q.peek())
                        break;
                    else if(dist > q.peek())
                        q.remove();
                }
                System.out.println("Suitable length = " + q.peek());

                if(!q.isEmpty())
                    len = q.peek();
                else
                    len = -1;

                if(len != -1)
                    dp[s][t] = max(len, sub_sol);
                else
                    dp[s][t] = -1;
            }
        
        return dp[i][j];
    }
    
    public static void main(String[] args) {
        Point [] A = { new Point(0, 1), new Point(1, 1), new Point(1, 2) };
        Point [] B = { new Point(0, 0), new Point(1, 0) };                                   // Gives 2
        /*Point [] A = { new Point(3, -4), new Point(5, 0), new Point(5, 2), new Point(3, 2), new Point(5, 4) };
        Point [] B = { new Point(-3, 0), new Point(-5, -4), new Point(-2, -4), new Point(-2, 4) };*/      // Gives 9
        q = new LinkedList<>();
        
        /*Scanner scan = new Scanner(System.in);
        String line1 = scan.nextLine();
        String [] seqA = line1.split(",");
        String line2 = scan.nextLine();
        String [] seqB = line2.split(",");
        String line3 = scan.nextLine();
        String [] lens = line3.split(",");
        
        int size = seqA.length;
        int index = 0;
        Point [] A = new Point[size / 2];
        for(int i=0; i<size; i=2+i) {
            Point p = new Point(Integer.parseInt(seqA[i]), Integer.parseInt(seqA[i+1]));
            A[index++] = p;
        }
        
        size = seqB.length;
        index = 0;
        Point [] B = new Point[size / 2];
        for(int i=0; i<size; i=2+i) {
            Point p = new Point(Integer.parseInt(seqB[i]), Integer.parseInt(seqB[i+1]));
            B[index++] = p;
        }
        
        size = lens.length;
        int [] lengths = new int [size];
        for(int i=0; i<size; ++i)
            lengths[i] = Integer.parseInt(lens[i]);*/
        
        //int [] lengths = {4, 8, 14, 7, 2, 5, 9, 3, 12, 10, 11, 1, 6};
        //int [] lengths = {3, 4, 2, 1, 7, 9};
        int [] lengths = {2, 1};
        sort(lengths);
        
        /*String s = "1,1,4,5,6,7";
        String [] ss = s.split(",");
        System.out.println("Length = " + ss.length);
        for(int i=0; i<ss.length; i=i+2)
            System.out.println(ss[i] + " " + ss[i+1]);*/
        
        //for(int i: lengths)
        //    q.add(i);     // Push elements to queue
        
        /*  OLD VERSION  */
        //System.out.println("Returned value = " + minLength(A, B, A.length - 1, B.length - 1));
        //System.out.println("Returned value = " + minLengthDP(A, B, A.length - 1, B.length - 1));
        
        /*  NEW VERSION  */
        //System.out.println("Returned value = " + minLength(A, B, lengths, A.length - 1, B.length - 1));
        System.out.println("Returned value = " + minLengthDP(A, B, lengths, A.length - 1, B.length - 1));
        
        System.gc();
        System.exit(0);
    }
    
}


/*
private static int minLength(Point [] A, Point [] B, int [] lengths, int i, int j) {
        int sub_sol = MINUS_INFINITY;
        int sub_solA, sub_solB, sub_solC;
        if(i > 0 && j > 0) {
            sub_solA = minLength(A, B, lengths, i-1, j-1);
            sub_solB = minLength(A, B, lengths, i, j-1);
            sub_solC = minLength(A, B, lengths, i-1, j);
            sub_sol = min(sub_solA, sub_solB);
            sub_sol = min(sub_sol, sub_solC);
        }
        else if(i == 0 && j > 0)
            sub_sol = minLength(A, B, lengths, 0, j-1);
        else if(j == 0 && i > 0)
            sub_sol = minLength(A, B, lengths, i-1, 0);
        
        double dist = distance(A[i], B[j]);
        System.out.println("dist = " + dist);
        
        int len = -1;
        for(int r = 0; r < lengths.length; ++r) {
            //System.out.println(lengths[r]);
            if(lengths[r] >= dist) {
                len = lengths[r];
                break;
            }
        }
        
        System.out.println("Suitable length = " + len);
        
        if(len != -1 && sub_sol != -1)
            return max(len, sub_sol);
        return -1;
    }
*/

/*
private static int minLengthDP(Point [] A, Point [] B, int [] lengths, int i, int j) {
        int sub_sol = MINUS_INFINITY;
        
        int [][] dp = new int [i+1][j+1];
        int len = -1;
        double dist = distance(A[0], B[0]);
        for(int r = 0; r < lengths.length; ++r) {
                    //System.out.println(lengths[r]);
                    if(lengths[r] >= dist) {
                        len = lengths[r];
                        break;
                    }
                }
        System.out.println("Suitable length = " + len);
        
        if(len != -1 && sub_sol != -1)
            dp[0][0] = max(len, sub_sol);
        else
            dp[0][0] = -1;
        System.out.println("first element = " + dp[0][0]);
        
        // After fill first index dp[0][0], then fill the table
        
        int sub_solA, sub_solB, sub_solC;
        for(int s=0; s<=i; ++s)
            for(int t=0; t<=j; ++t) {
                if(s == 0 && t == 0)
                    continue;
                if(s == 0 && t > 0)
                    sub_sol = dp[0][t-1];
                else if(t == 0 && s > 0)
                    sub_sol = dp[s-1][0];
                
                else if(s > 0 && t > 0) {
                    sub_solA = dp[s-1][t-1];
                    sub_solB = dp[s][t-1];
                    sub_solC = dp[s-1][t];
                    sub_sol = min(sub_solA, sub_solB);
                    sub_sol = min(sub_sol, sub_solC);
                }
                
                dist = distance(A[s], B[t]);
                System.out.println("dist = " + dist);
                len = -1;
                for(int r = 0; r < lengths.length; ++r) {
                    //System.out.println(lengths[r]);
                    if(lengths[r] >= dist) {
                        len = lengths[r];
                        break;
                    }
                }
                System.out.println("Suitable length = " + len);

                if(len != -1 && sub_sol != -1) {
                    dp[s][t] = max(len, sub_sol);
                    System.out.println("Saved value dp[" + s + "][" + t + "] = " + dp[s][t]);
                }
                else {
                    dp[s][t] = -1;
                    System.out.println("Saved value dp[" + s + "][" + t + "] = " + dp[s][t]);
                }
            }
        
        return dp[i][j];
    }
*/