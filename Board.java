import java.util.*;
import java.io.*;

public class Board {
    private int width;
    private int height;
    private int[][] board;
    private ArrayList<BoardState> board_states;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        init_board();
    }

    public int[][] getBoard(){
        return this.board;
    }

    public void initSimulation(){
        this.board_states = new ArrayList<BoardState>();
    }

    public ArrayList<BoardState> getBoardStates(){
        return this.board_states;
    }

    public void simulateDrop(int[][] piece, int col){
        // create a copy of board
        int[][] copy = new int[height][width];
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                copy[i][j] = this.board[i][j];
            }
        }

        // simulate drop
        int flag = drop_piece(piece, col);
        if(flag == 1){
            board_states.add(new BoardState(board, piece, col, width, height));
        }

        // set copy to the original board
        this.board = copy;
    }

    // 1 -> valid drop, -1 -> game over, 0 -> invalid move
    public int drop_piece(int[][] piece, int column) {
        int flag;
        if (is_valid_move(piece, 0, column)) {
            return add_piece(piece, column);
        }
        else{
            return 0;
        }
    }

    // print out array flipped accrose x axis so it looks like a tetris board
    public void print_tetris(int[][] arr){
        for(int i = 0; i < arr.length; i++){
            for(int j = 0; j < arr[i].length; j++){
                System.out.print(arr[i][j] + " ");
            }System.out.println();
        }
    }

    // clear complete rows in the board and return the number of rows cleared
    public int clear_rows(){
        int cleared = 0;
        int row_index = 0;
        while(row_index < height){
            int[] row = board[row_index];
            if(isComplete(row)){
                cleared++;
                for(int i = row_index; i > 0; i--){
                    board[i] = board[i-1];
                }
            }
            row_index++;
        }
        return cleared;
    }

    private boolean isComplete(int[] row){
        for(int i = 0; i < row.length; i++){
            if(row[i] == 0){
                return false;
            }
        }return true;
    }

    private void init_board(){
        this.board = new int[height][width];
    }

    private int add_piece(int[][] piece, int col){
        int piece_width = piece[0].length;
        int piece_height = piece.length;
        
        
        int cur_row = 0;
        int max_row = height - piece_height;
        while(cur_row < max_row){
            int[][] board_copy = getBoardCopy();
            int collision = simDrop(board_copy, piece, cur_row, col);
            if(collision == 1){
                cur_row = cur_row-1;
                break;
            }else{
                cur_row++;
            }
        }
        
        if(cur_row > 0){
            int[][] b_copy = getBoardCopy();
            int collision = simDrop(b_copy, piece, cur_row, col);
            if(collision == 1){
                return -1;
            }else{
                simDrop(board, piece, cur_row, col);
            }
        }else{
            return -1;
        }
        return 1;
    }
    
    private int simDrop(int[][] b, int[][] p, int r, int c){
        for(int i = 0; i < p.length; i++){
            for(int j = 0; j < p[i].length; j++){
                b[r+i][c+j] = b[r+i][c+j] + p[i][j];
            }
        }
        
        for(int i = 0; i < b.length; i++){
            for(int j = 0; j < b[i].length; j++){
                if(b[i][j] > 1){
                    return 1;
                }
            }
        }
        return -1;
    }
    
    private int[][] getBoardCopy(){
        int[][] b = new int[height][width];
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                b[i][j] = board[i][j];
            }
        }
        return b;
    }

    private int max(int x, int y) {
        return (x > y) ? x : y;
    }

    //returns if a move is legal,
    private boolean is_valid_move(int[][] piece, int x, int y){
        boolean bool = true;

        //invalid move
        if (piece[0].length + y > 10) {
            bool = false;
        }
        return bool;
    }
}
