import java.io.*;
import java.util.*;

public class TetrisEA{

    // parameter used for evolutionary agorithm
    private static int pop_size;
    private static int num_generations;
    private static int T_size;
    private static int elites;
    private static int max_fitness;
    private static double mutation_prob;
    private static double mutation_range;
    private static double crossover_prob;
    private static String selection_type;   // Tournament or Porportional w/ Universal Stochastic

    public static void main(String[] args){
        // read parameters
        readParams("params.txt");

        // initialize population
        Player[] pop = new Player[pop_size];
        for(int i = 0; i < pop_size; i++){
            pop[i] = new Player(max_fitness);
        }

        // for as many as number of generations or we find a solution that infinitely plays the tetris
        Player bestPlayer = null;
        boolean best_found = false;
        double[] fitnesses = new double[pop_size];
        for(int i = 0; i < num_generations; i++){
            fitnesses = new double[pop_size];
            int fitness_index = 0;

            // simulate each player
            for(Player p : pop){
                double fitness = p.simulate("off");
                fitnesses[fitness_index++] = fitness;
                if(fitness >= max_fitness){
                    System.out.println("Best Found");
                    bestPlayer = p;
                    best_found = true;
                    break;
                }
            }

            // if best individual found, terminate evolution
            if(best_found){
                break;
            }

            // selection
            Player[] nextGeneration;
			nextGeneration = tournamentSelection(pop, fitnesses);
			
			// sort pop by fitnesses & select elites
			quickSort(pop, fitnesses);
			for(int k = 0; k < elites; k++){
				nextGeneration[k] = pop[k];
			}
			
            // mutation
            mutatePop(nextGeneration);

            // crossover
            crossoverPop(nextGeneration);

            // set new population
            pop = nextGeneration;

            double max = fitnesses[0];
			int max_index = 0;
            for(int j = 1; j < fitnesses.length; j++){
              if(fitnesses[j] > max){
                max = fitnesses[j];
				max_index = i;
              }
            }
			Player best_so_far = pop[max_index];
			best_so_far.simulate("on");
			double[] g = best_so_far.getGenome();
			
            double avg = 0;
            for(int s = 0; s < fitnesses.length; s++){
                avg += fitnesses[s];
            }
            avg = avg / fitnesses.length;
			
			System.out.println(i + "," + max + "," + avg + "," + g[0] + "," + g[1] + "," + g[2] + "," + g[3]);
			System.gc();
        }

        // if individual whose fitness doesn't exceed the max_fitness, select the one that is by far the best
        if(!best_found){
            int bestIndex = 0;
            bestPlayer = pop[0];
            for(int i = 1; i < pop_size; i++){
                if(fitnesses[i] > fitnesses[bestIndex]){
                    bestIndex = i;
                    bestPlayer = pop[i];
                }
            }
		}
		
        double[] b = bestPlayer.getGenome();
		System.out.println("Best Genome: [" + b[0] + "," + b[1] + "," + b[2] + "," + b[3] + "]");
		System.out.println("Best Fitness: " + bestPlayer.getFitness());
        bestPlayer.simulate("on");
    }
	
	// sort pop by fitnesses
	private static void quickSort(Player[] pop, double[] fitnesses){
		quickSortHelper(pop, fitnesses, 0, pop.length-1);
	}
	
	private static void quickSortHelper(Player[] pop, double[] fitnesses, int lo, int hi){
		if(lo < hi){
			int p = partition(pop, fitnesses, lo, hi);
			quickSortHelper(pop, fitnesses, lo, p-1);
			quickSortHelper(pop, fitnesses, p+1, hi);
		}
	}
	
	private static int partition(Player[] pop, double[] fitnesses, int lo, int hi){
		double pivot = fitnesses[lo];
		int wall = lo+1;
		for(int i = lo+1; i <= hi; i++){
			if(pivot < fitnesses[i]){
				Player p_temp = pop[i];
				pop[i] = pop[wall];
				pop[wall] = p_temp;
				
				double f_temp = fitnesses[i];
				fitnesses[i] = fitnesses[wall];
				fitnesses[wall] = f_temp;
				wall++;
			}
		}
		--wall;
		Player p_temp = pop[lo];
		pop[lo] = pop[wall];
		pop[wall] = p_temp;
		
		double f_temp = fitnesses[lo];
		fitnesses[lo] = fitnesses[wall];
		fitnesses[wall] = f_temp;
		
		return wall;
	}
	
    // crossover operator
    private static void crossoverPop(Player[] pop){
        ArrayDeque<Player> queue = new ArrayDeque<Player>();
		for(int i = elites; i < pop.length; i++){
			Player p = pop[i];
			double prob = Math.random();
            if(prob <= crossover_prob){
                queue.offer(p);
            }
            if(queue.size() == 2){
                Player p1 = queue.poll();
                Player p2 = queue.poll();
                p1.crossover(p2);
            }
		}
    }

    // mutation operator
    private static void mutatePop(Player[] pop){
		for(int i = elites; i < pop.length; i++){
			Player p = pop[i];
			double random = Math.random();
            if(random <= mutation_prob){
                p.mutate(mutation_range);
            }
		}
    }

    private static Player[] tournamentSelection(Player[] pop, double[] fitnesses){
        Player[] nextGeneration = new Player[pop_size];
        int index = 0;

        for(int i = 0; i < pop_size; i++){
            // shuffle population & their fitnesses
            shuffle(pop, fitnesses);

            // pick first T_size individuals
            Player best_p = pop[0];
            double best_f = fitnesses[0];
            for(int j = 1; j < T_size; j++){
                if(fitnesses[j] > best_f){
                    best_f = fitnesses[j];
                    best_p = pop[j];
                }
            }

            // store the best indidivudla from a tournament
            nextGeneration[index++] = best_p;
        }
        return nextGeneration;
    }

    // randomly shuffle an array
    private static void shuffle(Player[] p, double[] f){
        int index;
        Player temp_p;
        double temp_f;
        Random random = new Random();
        for(int i = p.length-1; i > 0; i--){
            index = random.nextInt(i+1);

            // swap player
            temp_p = p[index];
            p[index] = p[i];
            p[i] = temp_p;

            // swap corresponding fitnesses
            temp_f = f[index];
            f[index] = f[i];
            f[i] = temp_f;
        }
    }

    // read parameters necessary for evolution
    private static void readParams(String fileName){
        try{
            File file = new File(fileName);
            Scanner scan = new Scanner(file);
            int row = 0;
            while(scan.hasNextLine()){
                String[] line = scan.nextLine().split(" ");
                switch(row){
                    case 0:
                        pop_size = Integer.parseInt(line[1]);
                        break;
                    case 1:
                        num_generations = Integer.parseInt(line[1]);
                        break;
                    case 2:
                        selection_type = line[1].toLowerCase();
                        break;
                    case 3:
                        T_size = Integer.parseInt(line[1]);
                        break;
                    case 4:
                        elites = Integer.parseInt(line[1]);
                        break;
                    case 5:
                        mutation_prob = Double.parseDouble(line[1]);
                        break;
                    case 6:
                        mutation_range = Double.parseDouble(line[1]);
                        break;
                    case 7:
                        crossover_prob = Double.parseDouble(line[1]);
                        break;
                    case 8:
                        max_fitness = Integer.parseInt(line[1]);
                    default:
                        break;
                }
                row++;
            }
        }catch(IOException e){
            System.out.println("Error reading in parameters...");
        }
    }
}
