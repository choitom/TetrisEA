import java.util.*;
import java.io.*;

public class Piece{
    private ArrayList<ArrayList<int[][]>> tetraminos;
    
    public Piece(){
        this.tetraminos = new ArrayList<ArrayList<int[][]>>();
        init_tetraminos();
    }
    
    public ArrayList<int[][]> getPiece(int ID){
        return this.tetraminos.get(ID);
    }
    
    private void init_tetraminos(){
        ArrayList<int[][]> L_set = new ArrayList<int[][]>();
        ArrayList<int[][]> R_set = new ArrayList<int[][]>();
        ArrayList<int[][]> Square_set = new ArrayList<int[][]>();
        ArrayList<int[][]> T_set = new ArrayList<int[][]>();
        ArrayList<int[][]> I_set = new ArrayList<int[][]>();
        ArrayList<int[][]> S_set = new ArrayList<int[][]>();
        ArrayList<int[][]> Z_set = new ArrayList<int[][]>();
        
        // L
        int[][] L_1 = {{0,0,1},
                       {1,1,1}};
        int[][] L_2 = {{1,0},
                       {1,0},
                       {1,1}};
        int[][] L_3 = {{1,1,1},
                       {1,0,0}};
        int[][] L_4 = {{1,1},
                       {0,1},
                       {0,1}};
        L_set.add(L_1);
        L_set.add(L_2);
        L_set.add(L_2);
        L_set.add(L_3);
        
        // R
        int[][] R_1 = {{1,0,0},
                       {1,1,1}};
        int[][] R_2 = {{1,1},
                       {1,0},
                       {1,0}};
        int[][] R_3 = {{1,1,1},
                       {0,0,1}};
        int[][] R_4 = {{0,1},
                       {0,1},
                       {1,1}};
        R_set.add(R_1);
        R_set.add(R_2);
        R_set.add(R_2);
        R_set.add(R_3);
        
        // Square
        int[][] square_1 = {{1,1},
                            {1,1}};
        int[][] square_2 = {{1,1},
                            {1,1}};
        int[][] square_3 = {{1,1},
                            {1,1}};
        int[][] square_4 = {{1,1},
                            {1,1}};
        
        Square_set.add(square_1);
        Square_set.add(square_2);
        Square_set.add(square_2);
        Square_set.add(square_3);
        
        // T
        int[][] T_1 = {{0,1,0},
                       {1,1,1}};
        int[][] T_2 = {{1,0},
                       {1,1},
                       {1,0}};
        int[][] T_3 = {{1,1,1},
                       {0,1,0}};
        int[][] T_4 = {{0,1},
                       {1,1},
                       {0,1}};
        T_set.add(T_1);
        T_set.add(T_2);
        T_set.add(T_2);
        T_set.add(T_3);
        
        // I
        int[][] I_1 = {{1,1,1,1}};
        int[][] I_2 = {{1},
                       {1},
                       {1},
                       {1}};
        int[][] I_3 = {{1,1,1,1}};
        int[][] I_4 = {{1},
                       {1},
                       {1},
                       {1}};
        I_set.add(I_1);
        I_set.add(I_2);
        I_set.add(I_2);
        I_set.add(I_3);
        
        // S
        int[][] S_1 = {{1,0},
                       {1,1},
                       {0,1}};
        int[][] S_2 = {{0,1,1},
                       {1,1,0}};
        int[][] S_3 = {{1,0},
                       {1,1},
                       {0,1}};
        int[][] S_4 = {{0,1,1},
                       {1,1,0}};
        S_set.add(S_1);
        S_set.add(S_2);
        S_set.add(S_2);
        S_set.add(S_3);
        
        // Z
        int[][] Z_1 = {{0,1},
                       {1,1},
                       {1,0}};
        int[][] Z_2 = {{1,1,0},
                       {0,1,1}};
        int[][] Z_3 = {{0,1},
                       {1,1},
                       {1,0}};
        int[][] Z_4 = {{1,1,0},
                       {0,1,1}};
        Z_set.add(Z_1);
        Z_set.add(Z_2);
        Z_set.add(Z_2);
        Z_set.add(Z_3);
        
        this.tetraminos.add(L_set);
        this.tetraminos.add(R_set);
        this.tetraminos.add(Square_set);
        this.tetraminos.add(T_set);
        this.tetraminos.add(I_set);
        this.tetraminos.add(S_set);
        this.tetraminos.add(Z_set);
    }
}