/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GA;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.logging.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author dukun
 */
public class GeneticAlgorithm {
    
    private List<Chromosome> oldpopulation;
    private List<Chromosome> newpopulation;
    private int popSize;
    private int cityNum;
    private int MAX_GEN; // max generation 运行代数  
    private float len_p;
    private float price_p;
    private int[][] distance; // distance matrix距离矩阵  
    private int bestT;// generation where chromosome shows最佳出现代数 
    private Chromosome bestChromosome;
    private float eachGBestChromosomefitness;
    private double[] Pi;// 种群中各个个体的累计概率  
    private float Pc;// 交叉概率  
    private float Pm;// 变异概率  
    private int t;// 当前代数  
    private Random random;
     
    public static Logger logger1 = LogManager.getLogger(GeneticAlgorithm.class.getName());
   
    public GeneticAlgorithm(int popSize, float Pc, float Pm, int MAX_GEN, int cityNum, String filename,
        float len_p, float price_p) throws IOException {
        logger1.info("Build GeneticAlgorithm");
        this.Pc = Pc;
        this.MAX_GEN = MAX_GEN;
        this.popSize = popSize;
        this.Pm = Pm;
        this.cityNum = cityNum;
        this.len_p = len_p;
        this.price_p = price_p;
        distance = new int[cityNum][2];
        random = new Random(System.currentTimeMillis());
        Pi = new double[popSize];
        oldpopulation = new ArrayList<>();
        newpopulation = new ArrayList<>();
        eachGBestChromosomefitness = 0;
        bestT = 0;
        initGA(filename);
      }

    private void initGA(String filename) throws FileNotFoundException, IOException {
        logger1.info("Initialize distance matrix");
        String strbuff;
        BufferedReader data = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));

        for (int i = 0; i < cityNum; i++) {
            strbuff = data.readLine();
            String[] strcol = strbuff.split(",");
            distance[i][0] = Integer.valueOf(strcol[0]);// x coordinate  
            distance[i][1] = Integer.valueOf(strcol[1]);// y coordinate  
        }
    }

//init population
    public void initPop() {
       logger1.info("Initialize the population. The population is: "+popSize);
        for (int i = 0; i < popSize; i++) {
            Chromosome chromosome = new Chromosome();
            chromosome.calculatefitness(distance, len_p, price_p); 
            oldpopulation.add(chromosome);
        }
        bestChromosome = oldpopulation.get(0);

    }

//find each generation best chromosome, sort all the chromosome
    public void selectBestchromosoem() {
       
        PriorityQueue<Chromosome> chromosomes = new PriorityQueue<>();
        for (Chromosome c : oldpopulation) {
            chromosomes.add(c);
        }
        Chromosome chromosome = chromosomes.remove();
        if (bestChromosome.getFitness() > chromosome.getFitness()) {
            bestChromosome = chromosome;
            bestT =t;
        }
        eachGBestChromosomefitness = chromosome.getFitness();
        Chromosome bstchromosome = new Chromosome();
        bstchromosome.setFitness((float) chromosome.getFitness());
        bstchromosome.setGene(chromosome.getGene());
        bstchromosome.setX(chromosome.getX());
        bstchromosome.setY(chromosome.getY());
        bstchromosome.setTofirstcity(chromosome.getTofirstcity());
        bstchromosome.setTotaldistance(chromosome.getTotaldistance());
        newpopulation.add(bstchromosome);
//remove all chromosome
oldpopulation.clear();
oldpopulation.add(chromosome);

for(Chromosome chromosome1: chromosomes){
oldpopulation.add(chromosome1);
}

    }

//select other parent to the new population
    public void selectOtherChromosomeTonewPop() {
       
        double ran;
        for (int i = 1; i < popSize; i++) {
            ran = random.nextFloat();
            for (int j = 0; j < Pi.length; j++) {
                if (ran <= Pi[j]) {
                    Chromosome chromosome = new Chromosome();
                    chromosome.setFitness((float) oldpopulation.get(j).getFitness());
                    chromosome.setGene(oldpopulation.get(j).getGene());
                    chromosome.setX(oldpopulation.get(j).getX());
                    chromosome.setY(oldpopulation.get(j).getY());
                    chromosome.setTofirstcity(oldpopulation.get(j).getTofirstcity());
                    chromosome.setTotaldistance(oldpopulation.get(j).getTotaldistance());
                    newpopulation.add(chromosome);
                    break;
                }
            }
        }
    }

//count rate
    public void countRate() {
        int i = 0;
        double sumFitness = 0;// fitness sum适应度总和 
        double[] temp = new double[popSize];
        for (Chromosome c : oldpopulation) {
            temp[i] = 100.0 / c.getFitness();
            sumFitness += temp[i];
            i++;
        }
        Pi[0] = (temp[0] / sumFitness);
        for (i = 1; i < popSize; i++) {
            Pi[i] = (float) (temp[i] / sumFitness + Pi[i - 1]);
        }
    }

//crrosover
    public void crossOver(Chromosome c1, Chromosome c2) {
      
        int a = random.nextInt(1000) % (c1.getGene().length);
        int b = random.nextInt(1000) % (c2.getGene().length);
        while (a == b) {
            b = random.nextInt(1000) % (c2.getGene().length);
        }
        int min = a > b ? b : a;
        int max = a > b ? a : b;

        for (int i = min; i <= max; i++) {
            int temp = c1.getGene()[i];
            c1.getGene()[i] = c2.getGene()[i];
            c2.getGene()[i] = temp;
        }


        c1.changeXY();
        c1.calculatefitness(distance, len_p, price_p);
        c2.changeXY();
        c2.calculatefitness(distance, len_p, price_p);
    }

//Variation
    public void Variation(Chromosome c) {
        int r = random.nextInt(1000) % (c.getGene().length);

        if (c.getGene()[r] == 0) {
            c.getGene()[r] = 1;
        } else {
            c.getGene()[r] = 0;
        }
        c.changeXY();
        c.calculatefitness(distance, len_p, price_p);
    }

 
    
//evolution
    private void evolution() {
//select the best chromosome and doesn't to use for crrosover
         logger1.info("Select the best individual in this generation");
        selectBestchromosoem();
        
        countRate();

//select other popsize-1 chromosome to newpopulation
         logger1.info("Using natural selection to choose the parents of next generation");
        selectOtherChromosomeTonewPop();

//operation
        double p;
        int i;
        logger1.info("Use hybridization and mutation to produce next generation");
        for (i = 1; i + 1 < newpopulation.size(); i += 2) {
            p = random.nextFloat();// /产生概率 
            if (p < Pc) {
                crossOver(newpopulation.get(i), newpopulation.get(i + 1));
            }
            p = random.nextFloat();// /产生概率 
            if (p < Pm) {

                Variation(newpopulation.get(i));
            }

            p = random.nextFloat();// /产生概率 
            if (p < Pm) {

                Variation(newpopulation.get(i + 1));
            }
        }

        if (i == popSize - 1)// 剩最后一个染色体没有交叉L-1  
        {
            p = random.nextFloat();// /产生概率  
            if (p < Pm) {
                Variation(newpopulation.get(i));
            }
        }
        
       
    }

//solve the GA 
    private void progress() {
//init population
        initPop();

        countRate();
        logger1.info("info of each Chromosome from init Population");
        for (Chromosome chromosome : oldpopulation) {
        logger1.info("x: " + chromosome.getX() + " y: " + chromosome.getY());
        logger1.info("The sum distance to each city：" + chromosome.getTotaldistance() + " Distance to first city：" + 
         chromosome.getTofirstcity() + " Chromosome fitness is： " + chromosome.getFitness());  
        }
    
        for (t = 1; t <= MAX_GEN; t++) {
           //evolution
           
           
         
           logger1.info( "The "+ (t-1) + " generation best chromosome is: " + eachGBestChromosomefitness);
           logger1.info("The " + t + " generation evolution start");
           eachGBestChromosomefitness = 0;
            evolution();
          
            logger1.info("The" + t + "generation's evolution is completed");
            logger1.info("Info of each Chromosome");
            
            for (Chromosome chromosome : newpopulation) {
                 logger1.info("x: " + chromosome.getX() + " y: " + chromosome.getY());
                 logger1.info("The sum distance to each city：" + chromosome.getTotaldistance() + " Distance to first city：" + 
                 chromosome.getTofirstcity() + " Chromosome fitness： " + chromosome.getFitness());  
            }
           
           
           oldpopulation.clear();
           oldpopulation = new ArrayList<Chromosome>(newpopulation);
            
            //replace the newpopulation with oldpopulation
            newpopulation.clear();
            
        }

        System.out.println("From these 1500 generations' evolution we can get the result as follows: ");
        System.out.println("");
        
        System.out.println("The Generation of the most fit chromosome generates at：" + bestT);
        System.out.println("The most fit x is: " +bestChromosome.getX());
        System.out.println("The most fit y is：" +bestChromosome.getY());
        System.out.println("Best fitness value is ：" +bestChromosome.getFitness());
        System.out.println("The distance sum to each city is(km)：" +bestChromosome.getTotaldistance());
        System.out.println("The distance to Vegetable Base is(km)："+bestChromosome.getTofirstcity());
       
        

  
    }
    
    
    //main
     public static void main(String[] args)  {
        
        try {
            GeneticAlgorithm ga = new GeneticAlgorithm(30, (float)0.8, (float)0.1,1500 ,11, "Distance.txt", (float)0.7, (float)0.3);
            ga.progress();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(GeneticAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
        }
     
          
    }
    

}
