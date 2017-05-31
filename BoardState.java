public class BoardState{
    private int[][] board;
    private int[][] piece;
    private int col;
    
    public BoardState(int[][] b, int[][] p, int c, int w, int h){
        this.col = c;
        this.piece = p;
        this.board = new int[h][w];
        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                this.board[i][j] = b[i][j];
            }
        }
    }
    public int[][] getBoard(){
        return this.board;
    }
    
    public int getCol(){
        return this.col;
    }
    
    public int[][] getPiece(){
        return this.piece;
    }
    
    public void print(){
        System.out.println("Col: " + this.col);
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                System.out.print(board[i][j] + " ");
            }System.out.println();
        }
    }
}