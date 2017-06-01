import java.util.*;
import java.io.*;

public class Player{
    private int max_fitness;
    private int fitness;
    private double[] genome;

    public Player(int max_fitness){
        this.max_fitness = max_fitness;
        this.fitness = 0;
        initGenome();
    }

    // get genome
    public double[] getGenome(){
        return this.genome;
    }

    // get fitness
    public double getFitness(){
        return this.fitness;
    }

    // set genome
    public void setGenome(double[] g){
        for(int i = 0; i < g.length; i++){
            this.genome[i] = g[i];
        }
    }

    // simulate a player's genome until it dies
    public double simulate(String visualize){
        int width = 10;
        int height = 22;
        Piece pieces = new Piece();

		int sim = 10;
		double avg_fitness = 0;

		for(int i = 0; i < sim; i++){
			Board board = new Board(width, height);
			avg_fitness += simulatePlayer(pieces, board, visualize);
		}
		avg_fitness = avg_fitness/sim;
		return avg_fitness;
    }

    // get a random tetramino
    public ArrayList<int[][]> getRandomPiece(Piece pieces){
        Random random = new Random();
        int piece_index = random.nextInt(7);
        return pieces.getPiece(piece_index);
    }

    // pick one random component in a vector and scale it by +- mutation_range
    public void mutate(double mutation_range){
        double random = Math.random();

        // randomly choose sign (+ or -)
        int sign = 1;
        if(random > 0.5){
            sign = -1;
        }
        double mutation_factor = sign * mutation_range;

        // find a random index
        Random rand = new Random();
        int randIndex = rand.nextInt(4);

        // mutate a randomly selected component
        this.genome[randIndex] = this.genome[randIndex] * (1 + mutation_factor);
        normalize();
    }

    // randomly decide how many components to swap and swap them
    public void crossover(Player p){

        // randomly decide how many components to swap
        Random random = new Random();
        int n = random.nextInt(4) + 1;

        // shuffle index array
        int[] index_arr = {0,1,2,3};
        int index, temp;
        for(int i = index_arr.length-1; i > 0; i--){
            index = random.nextInt(i+1);
            temp = index_arr[index];
            index_arr[index] = index_arr[i];
            index_arr[i] = temp;
        }

        // pick the first n component indices from the shuffled list and swap
        double[] p2_genome = p.getGenome();
        for(int i = 0; i < n; i++){
            int crossover_index = index_arr[i];
            double temp2 = this.genome[crossover_index];
            this.genome[crossover_index] = p2_genome[crossover_index];
            p2_genome[crossover_index] = temp2;
        }
        p.setGenome(p2_genome);
        normalize();
        p.normalize();
    }

    // normalize the genome vector
    public void normalize(){
        double len = 0;
        for(int i = 0; i < 4; i++){
            len += Math.pow(genome[i],2);
        }
        len = Math.sqrt(len);
        for(int i = 0; i < 4; i++){
            this.genome[i] = this.genome[i]/len;
        }
    }

    private int simulatePlayer(Piece pieces, Board board, String visualize){
        // generate current & lookahead pieces
        ArrayList<int[][]> current = null;
		ArrayList<int[][]> lookahead = null;

        this.fitness = 0;

        TetrisGraphic tg = new TetrisGraphic();

        // while the game isn't over
        int gameOver = -1;
        while(gameOver != 1){

            // get a new piece
			if(current == null){
				current = getRandomPiece(pieces);
				lookahead = getRandomPiece(pieces);
			}else{
				current = lookahead;
				lookahead = getRandomPiece(pieces);
			}

            // simulate all posible configurations for current piece for all columns
            board.initSimulation();

            for(int[][] c : current){
				for(int[][] l : lookahead){
					for(int i = 0; i < 10; i++){
						for(int j = 0; j < 10; j++){
							board.simulateDrop(c, l, i, j);
						}
					}
				}
            }


            ArrayList<BoardState> board_states = board.getBoardStates();

            // the game isn't over, retrieve the board state with the best heuristics
            if(board_states.size() > 0){
                BoardState best_player = evaluateHeuristics(board_states);
                board.drop_piece(best_player.getPiece(), best_player.getCol());

				if(visualize.equals("on")){
					tg.updateGrid(board.getBoard());
				}

                int rows_cleared = board.clear_rows();
                switch(rows_cleared){
					case 1:
						fitness += 2;
						break;
					case 2:
						fitness += 5;
						break;
					case 3:
						fitness += 15;
						break;
					case 4:
						fitness += 60;
						break;
					default:
						fitness += 0;
						break;
				}
            }
            // the game is over
            else{
                gameOver = 1;
            }

            if(fitness >= max_fitness){
                gameOver = 1;
            }
        }
        tg.dispose();
        return fitness;
    }

    // randomly initialize chromosomes
    private void initGenome(){

        this.genome = new double[4];
        Random rand = new Random();

        // random double between -1 and 1
        for(int i = 0; i < 4; i++){
            this.genome[i] = rand.nextDouble() * 2 - 1;
        }
    }

    private BoardState evaluateHeuristics(ArrayList<BoardState> board_states){
        BoardState best = board_states.get(0);
        double best_score = evalHeuristic(best.getBoard());
        for(int i = 1; i < board_states.size(); i++){
            BoardState b = board_states.get(i);
            double h = evalHeuristic(b.getBoard());
            if(h > best_score){
                best_score = h;
                best = b;
            }
        }
        return best;
    }

    private double evalHeuristic(int[][] b){
        return genome[0]*aggregateHeight(b) + genome[1]*completeRows(b) + genome[2]*holes(b) + genome[3]*bumpiness(b);
    }

    // compute the sum of heights for all columns in the board
    private int aggregateHeight(int[][] board){
        int aggregate = 0;
        int[] heights = getHeights(board);
        for(int i = 0; i < heights.length; i++){
            aggregate += heights[i];
        }
        return aggregate;
    }

    // find the number of complete rows on the board
    private int completeRows(int[][] board){
        int completeRows = 0;
        for(int i = 0; i < board.length; i++){
            boolean complete = true;
            for(int j = 0; j < board[i].length; j++){
                if(board[i][j] != 1){
                    complete = false;
                }
                if(!complete){
                    break;
                }
            }
            if(complete){
                completeRows++;
            }
        }
        return completeRows;
    }

    // find the number of holes on the board
    private int holes(int[][] board){
        int holes = 0;
        int cols = board[0].length;
        int rows = board.length;
        for(int i = 0; i < cols; i++){
            boolean holes_counting = false;
            for(int j = 0; j < rows; j++){
                if(holes_counting == false && board[j][i] == 1){
                    holes_counting = true;
                }
                if(holes_counting == true && board[j][i] == 0){
                    holes++;
                }
            }
        }
        return holes;
    }

    // aggregate the different in height for all adjacent columns
    private int bumpiness(int[][] board){
        int bump = 0;
        int[] heights = getHeights(board);
        for(int i = 0; i < heights.length-1; i++){
            bump += Math.abs(heights[i]-heights[i+1]);
        }
        return bump;
    }

    private int[] getHeights(int[][] board){
        int[] heights = new int[board[0].length];
        int cols = board[0].length;
        int rows = board.length;
        int max_height = board[0].length;
        for(int i = 0 ; i < cols; i++){
            int current_height = max_height;
            for(int j = 0; j < rows; j++){
                if(board[j][i] == 1){
                    heights[i] = current_height;
                    break;
                }
                current_height--;
            }
        }
        return heights;
    }

    public static void main(String[] args){
        Player p = new Player(50000);
        p.simulate("off");
    }
}
