package population;

import java.lang.reflect.Array;

import static java.lang.Math.abs;

/**
 * Created by Rroks on 2016/11/14.
 */
public class Individual {
    private int[] chromosome;
    private int fitness;
    private int crmslength;
    private int conflict;
    private int pairs;

    Individual(){
        calculatePairs();
        calculateConflict();
        calculateFitness();
    }
    Individual(int[] chromosome, int boardSize){
        this.chromosome = chromosome;
        this.crmslength = boardSize;
        calculatePairs();
        calculateConflict();
        calculateFitness();
    }

    public void setChromosome(int row, int column){
        chromosome[row] = column;
    }
    public int[] getChromosome(){return chromosome;}

    public int getFitness(){
        return fitness;
    }
    public void calculateFitness(){
        fitness = pairs - conflict;
    }

    public void calculateConflict(){
        conflict = 0;
        for (int i = 0; i < crmslength - 1; i ++){
            for (int j = i + 1; j < crmslength; j ++){
                if ((j - i) == abs(chromosome[i] - chromosome[j]) || chromosome[i] == chromosome[j]){
                    conflict++;
                }
            }
        }
    }
    public void calculatePairs(){
        pairs = 0;
        for (int i = crmslength - 1; i > 0; i--){
            pairs += i;
        }
    }
    public int getPairs(){
        return pairs;
    }
}
