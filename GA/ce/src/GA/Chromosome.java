/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GA;

import java.util.Random;

/**
 *
 * @author dukun
 */
public class Chromosome implements Comparable<Chromosome>{

private final int size = 20;
private  int[] gene;//include x and y 
private  int x;
private  int y;
private  float fitness;
private  float totaldistance;
private  float tofirstcity;
 
private Random  random;
   public Chromosome() {
     gene = new int[size];
     random = new Random();
     fitness = 0;
     tofirstcity =0;
     totaldistance = 0;
     initGene();
}

   
private  void  initGene(){
    
    for (int i = 0; i < size; i++) {
     int a = random.nextInt(2);
     this.gene[i] =a;
    }
    changeXY();
}


public void calculatefitness(int[][] distance,float len_p,float price_p) {
   
    for (int i = 0; i < distance.length; i++) {
    setTotaldistance((float) (getTotaldistance() + Math.sqrt(Math.pow(x-distance[i][0],2)+Math.pow(y-distance[i][1],2))));  
    if (i==0) {
    setTofirstcity(getTotaldistance());
    }
    }
    setFitness((getTotaldistance()*len_p + getTofirstcity() *price_p)/10000);
    
//    setFitness((float) (getTotaldistance() * len_p));
//    setFitness((float) (getFitness() + getTofirstcity() *price_p));   
}

public void changeXY(){
    int tempx = 0, tempy = 0;
    for (int i = 0; i < size/2; i++) {
    tempx +=(Math.pow(2, (size/2)-1-i))*getGene()[i];    
    }
    
    for (int i = size/2; i < size; i++) {
    tempy +=(Math.pow(2, (size)-1-i))*getGene()[i];    
    }
    
    setX(tempx);
    setY(tempy);
    setTofirstcity(0);
    setTotaldistance(0);
}

  @Override
    public int compareTo(Chromosome chromosome) {
        if (this.fitness>chromosome.fitness) {
         return 1;
        }else if (this.fitness<chromosome.fitness) {
         return -1;
        }else{
        return  0;
        }    
    }

    /**
     * @return the gene
     */
    public int[] getGene() {
        return gene;
    }

    /**
     * @param gene the gene to set
     */
    public void setGene(int[] gene) {
        this.gene = gene;
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return the fitness
     */
    public float getFitness() {
        return fitness;
    }

    /**
     * @param fitness the fitness to set
     */
    public void setFitness(float fitness) {
        this.fitness = fitness;
    }

    
    /**
     * @return the totaldistance
     */
    public float getTotaldistance() {
        return totaldistance;
    }

    /**
     * @param totaldistance the totaldistance to set
     */
    public void setTotaldistance(float totaldistance) {
        this.totaldistance = totaldistance;
    }

    /**
     * @return the tofirstcity
     */
    public float getTofirstcity() {
        return tofirstcity;
    }

    /**
     * @param tofirstcity the tofirstcity to set
     */
    public void setTofirstcity(float tofirstcity) {
        this.tofirstcity = tofirstcity;
    }
    
 
}
