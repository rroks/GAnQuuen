package population;

import java.util.*;

/**
 * Created by Rroks on 2016/11/14.
 */
public class GeneticAlgorithm {
    private static final double MUTATION_RATE = 1;
    private int populationSize;
    private int offspringSize;
    private static final int MAX_GENERATION = 500;
    private int boardSize;
    private boolean flag = true;
    private static ArrayList<Individual> population = new ArrayList<Individual>();//hashset存，再转换list
    //或者hashmap(int[], int)嗯，总之不太懂
    private HashMap<int[], Individual> map = new HashMap<int[], Individual>();

    GeneticAlgorithm(){}

    public void GeneticAlgorithm(int boardSize){
        this.boardSize = boardSize;
        populationSize = 4 * boardSize;
        offspringSize = 2 * boardSize;
        generatePopulation();
        int generationCounter = 1;
//        for (int i = 0; i < population.size(); i++){
//            System.out.println(population.get(i).getFitness());
//        }
        while (generationCounter <= MAX_GENERATION && flag) {
            reproduct();
            generationCounter++;
            System.out.println(generationCounter);
//            for (int i = 0; i < population.size(); i++){
//                for (int j = 0; j < population.get(i).getChromosome().length; j++){
//                    System.out.print(population.get(i).getChromosome()[j]);
//                }
//                System.out.print('\n');
//            }
            nQueenSolution();
        }
    }

    public void setBoardSize(int boardSize){
        this.boardSize = boardSize;
    }
    public int getBoardSize(){
        return boardSize;
    }
    public void setPopulationSize(){}
    public void setOffspringSize(){}

    public void generatePopulation(){
        Individual newIndi = null;
        boolean isLoop = true;

        while (population.size() < populationSize){
            newIndi = new Individual(generateArray(boardSize), boardSize);
            addNewIndividual(newIndi);
            nQueenSolution();
        }
    }
    public void addNewIndividual(Individual individual){
        if (checkIdentical(individual)){
            population.add(individual);
        }
    }
    public boolean checkIdentical(Individual individual){
        for (int i = 0; i < population.size(); i++){
            if (Arrays.equals(individual.getChromosome(), population.get(i).getChromosome())){
                return false;
            }
        }
        return true;
    }

    public void sortPopulation(){
        Collections.sort(population, comparator);
    }

    public void reproduct(){
//        for (int i = 0; i < populationSize; i++){
//            mutation(population.get(i));
//        }
        while (population.size() <= (populationSize + offspringSize)) {
            mating();
        }
        selection();
        System.out.println(population.size());
    }
    public void mating(){
        Random random = new Random();
        int ranNum1 = 0;
        int ranNum2 = 0;
        int ranNum3 = 0;
        while (ranNum1 == ranNum2 || ranNum1 == ranNum3 || ranNum2 == ranNum3){
            ranNum1 = Math.abs(random.nextInt() % populationSize);
            ranNum2 = Math.abs(random.nextInt() % populationSize);
            ranNum3 = Math.abs(random.nextInt() % populationSize);
        }
        Individual candidate1 = population.get(ranNum1);
        Individual candidate2 = population.get(ranNum2);
        Individual candidate3 = population.get(ranNum3);
        Individual parent1;
        Individual parent2;
        if (candidate1.getFitness() > candidate2.getFitness()){
            parent1 = candidate1;
            if (candidate2.getFitness() > candidate3.getFitness()){
                parent2 = candidate2;
            } else {
                parent2 = candidate3;
            }
        } else {
            parent1 = candidate2;
            if (candidate1.getFitness() > candidate3.getFitness()){
                parent2 = candidate1;
            } else {
                parent2 = candidate3;
            }
        }
        crossover(parent1, parent2);

    }
    public void crossover(Individual indivadual1, Individual indivadual2){
        Random crosPosition = new Random();
        int startPoint = 0;
        int endPoint = 0;
        while (startPoint == endPoint || startPoint < 0 || endPoint < 0){
            startPoint = Math.abs((crosPosition.nextInt()) % boardSize);
            endPoint = Math.abs((crosPosition.nextInt()) % boardSize);
        }
        int temp;
        if (startPoint > endPoint){
            temp = startPoint;
            startPoint = endPoint;
            endPoint =temp;
        }
        addNewIndividual(pmxCrossover(indivadual1, indivadual2, startPoint, endPoint));
        addNewIndividual(pmxCrossover(indivadual2, indivadual1, startPoint, endPoint));
    }
    public Individual pmxCrossover(Individual individual1, Individual individual2, int startPoint, int endPoint){
        int[] pmxChild = new int[boardSize];
        int temp;
        for (int i = 0; i < boardSize; i++){
            pmxChild[i] = individual2.getChromosome()[i];
        }
        for (int i = startPoint; i <= endPoint; i++){
            for (int j = 0; j < boardSize; j++){
                if (individual1.getChromosome()[i] == individual2.getChromosome()[j]){
                    temp = pmxChild[j];
                    pmxChild[j] = pmxChild[i];
                    pmxChild[i] = temp;
                    break;
                }
            }
        }
        Individual offspring = new Individual(pmxChild, boardSize);
        return offspring;
    }
    public void mutation(Individual individual){
        if ((Math.abs(new Random().nextInt()) % 100) < MUTATION_RATE){
            int a, b;
            Random random = new Random();
            a = random.nextInt() % boardSize;
            b = random.nextInt() % boardSize;
            while (b == a){
                a = random.nextInt() % boardSize;
                b = random.nextInt() % boardSize;
            }

            int temp = individual.getChromosome()[a];
            System.out.println(temp);
            System.out.println(individual.getChromosome()[a]);
            individual.setChromosome(a ,individual.getChromosome()[b]);
            individual.setChromosome(b, temp);
            addNewIndividual(individual);
        }
    }
    public void selection(){
        while (population.size() > populationSize) {
            Random random = new Random();
            int candiNum1 = 0;
            int candiNum2 = 0;
            int candiNum3 = 0;
            while (candiNum1 == candiNum2 || candiNum1 == candiNum3 || candiNum2 == candiNum3){
                candiNum1 = Math.abs((random.nextInt() % population.size()));
                candiNum2 = Math.abs((random.nextInt() % population.size()));
                candiNum3 = Math.abs((random.nextInt() % population.size()));
            }
            Individual candidate1 = population.get(candiNum1);
            Individual candidate2 = population.get(candiNum2);
            Individual candidate3 = population.get(candiNum3);
            if (candidate1.getFitness() > candidate3.getFitness()
                    && candidate2.getFitness() > candidate3.getFitness()) {
                population.remove(population.indexOf(candidate3));
            }
            if (candidate1.getFitness() > candidate2.getFitness()
                    && candidate3.getFitness() > candidate2.getFitness()) {
                population.remove(population.indexOf(candidate2));
            }
            if (candidate3.getFitness() > candidate1.getFitness()
                    && candidate2.getFitness() > candidate1.getFitness()) {
                population.remove(population.indexOf(candidate1));
            }
        }
    }
    public void nQueenSolution(){
        for (int i = 0; i < population.size(); i++){
            if (population.get(i).getFitness() == population.get(i).getPairs()){
                for (int j = 0; j < boardSize; j++){
                    System.out.print(population.get(i).getChromosome()[j] + "|");
                }
                System.out.print("\nsolution");
                flag = false;
            }
        }
    }

    public int[] generateArray(int boardSize){
        int[] newArray = new int[boardSize];
        int i;
        ArrayList<Integer> newList = new ArrayList<Integer>();
        for (i = 0; i < boardSize; i++){
            newArray[i] = i+1;
            newList.add(newArray[i]);
        }
        Collections.shuffle(newList);
        for (i = 0; i < boardSize; i++){
            newArray[i] = newList.get(i);
        }
        return newArray;
    }

    Comparator<Individual> comparator = new Comparator<Individual>() {
        @Override
        public int compare(Individual indi1, Individual indi2){
            if (indi1.getFitness() != indi2.getFitness()){
                return indi1.getFitness() - indi2.getFitness();
            }
            else if (indi1.getChromosome() != indi2.getChromosome()){
                return indi1.getChromosome().toString().toString().compareTo(indi2.getChromosome().toString());
            }
            else return 0;
        }
    };
}
