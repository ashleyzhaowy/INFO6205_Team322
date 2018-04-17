/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GA;



import java.lang.reflect.Field;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dukun
 * 
 */
public class GeneticAlgorithmTest {

    
    /**
     * Test of progress method, of class GeneticAlgorithm.
     */
 int[][] distance = {{20,50},{120,30},{200,90},{400,580},{420,800},{180,150},{350,270},{800,1000},{650,40},{900,100},{550,260}};  
 
 private Object getProp(Object o,String propName) throws Exception{
 Field field = o.getClass().getDeclaredField(propName);
 field.setAccessible(true);
 return  field.get(o);
 }
 
 
 
    //test initPop
   @Test
    public  void  testinitPop() throws Exception{
    GeneticAlgorithm instance = new GeneticAlgorithm(10, (float)0.8, (float)0.2,50 ,5, distance, (float)0.7, (float)0.3);
    instance.initPop();
    List<Chromosome> l = (List<Chromosome>) getProp(instance, "oldpopulation");
    assertEquals(l.size(), 10);
    }
    
 
    @Test
    public  void  testSelectBestchromosoem() throws Exception{
    GeneticAlgorithm instance = new GeneticAlgorithm(10, (float)0.8, (float)0.2,50 ,5, distance, (float)0.7, (float)0.3);
     
    instance.initPop();
    instance.selectBestchromosoem();
    
    List<Chromosome> l = (List<Chromosome>) getProp(instance, "oldpopulation");
    Chromosome c = (Chromosome) getProp(instance, "bestChromosome");
    //判断是不是得到了最优的染色体
     for (Chromosome chromosome : l) {
            if (c.getFitness()>chromosome.getFitness()) {
                fail("test failed");
     }
     }  
   }
    
    
    @Test
    public void testCountRate() throws Exception {
    GeneticAlgorithm instance = new GeneticAlgorithm(10, (float)0.8, (float)0.2,50 ,5, distance, (float)0.7, (float)0.3);
    instance.initPop();
    instance.countRate();
    
    double[] Pi = (double[]) getProp(instance, "Pi");
    
     assertEquals(1, Pi[Pi.length-1], 0.01); 
    }
    
    
   @Test
   public  void testSelectOtherChromosomeTonewPop() throws Exception{
   GeneticAlgorithm instance = new GeneticAlgorithm(10, (float)0.8, (float)0.2,50 ,5, distance, (float)0.7, (float)0.3);
   instance.initPop();
   instance.countRate();
   instance.selectBestchromosoem();
   instance.selectOtherChromosomeTonewPop();
   List<Chromosome> l = (List<Chromosome>) getProp(instance, "newpopulation");
   assertEquals(l.size(), 10);
   
   }
   
   @Test
    public void TestcrossOver() throws Exception {
    GeneticAlgorithm instance = new GeneticAlgorithm(10, (float)0.8, (float)0.2,50 ,5,distance, (float)0.7, (float)0.3);
    int[][] distance = (int[][]) getProp(instance, "distance");
    Chromosome c1 = new Chromosome();
    c1.calculatefitness(distance, (float)0.7,  (float)0.3);
    Chromosome c2 = new Chromosome();
    c2.calculatefitness(distance, (float)0.7,  (float)0.3);
    float c1_fitness = c1.getFitness();
    float c2_fitness = c2.getFitness();
    instance.crossOver(c1, c2);
    if (c1_fitness == c1.getFitness()) {
        fail("test fail");
    }
    if (c2_fitness == c2.getFitness()) {
        fail("test fail");
    }
    
    }
    
   @Test
    public void testVariation() throws Exception{
    GeneticAlgorithm instance = new GeneticAlgorithm(10, (float)0.6, (float)0.2,50 ,5, distance, (float)0.7, (float)0.3);
    int[][] distance = (int[][]) getProp(instance, "distance");
    Chromosome c1 = new Chromosome();
    c1.calculatefitness(distance, (float)0.7,  (float)0.3); 
    float c1_fitness = c1.getFitness();
    instance.Variation(c1);
       if (c1_fitness == c1.getFitness()) {
           fail("test fail");
       }
    }
    

    
    
}
