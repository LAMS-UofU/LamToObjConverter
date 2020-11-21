/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

/**
 *
 * @author tyjensen
 */
public class CartesianCoordinate {
    double x;
    double y;
    double z;
    
    public CartesianCoordinate(){
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
    }
    
    public CartesianCoordinate(double x, double y, double z){
        this.x = (float)x;
        this.y = (float)y;
        this.z = (float)z;
    }
    
    /*public float distance(CartesianCoordinate cc){
        Double xret = new Double(this.x-cc.x);
        Double yret = new Double(this.y - cc.y);
        Double zret = new Double(this.z - cc.z);
        return (float) Math.sqrt(Math.pow(xret, 2.0)+Math.pow(yret, 2.0) + Math.pow(zret, 2.0));
    }*/
    public double doubleDistance(CartesianCoordinate cc){
        double xret = (this.x - cc.x);
        double yret = (this.y - cc.y);
        double zret = (this.z - cc.z);
        return Math.sqrt(Math.pow(xret, 2.0)+Math.pow(yret, 2.0) + Math.pow(zret, 2.0));
    }
    public double doubleDistancexy(CartesianCoordinate cc){
        double xret = (this.x - cc.x);
        double yret = (this.y - cc.y);
        return Math.sqrt(Math.pow(xret, 2.0)+Math.pow(yret, 2.0));
    }
    public double doubleDistancexz(CartesianCoordinate cc){
        double xret = (this.x - cc.x);
        double zret = (this.z - cc.z);
        return Math.sqrt(Math.pow(xret, 2.0)+Math.pow(zret, 2.0));
    }
    public double doubleDistanceyz(CartesianCoordinate cc){
        double yret = (this.y - cc.y);
        double zret = (this.z - cc.z);
        return Math.sqrt(Math.pow(yret, 2.0)+Math.pow(zret, 2.0));
    }
    
    public String toString(){
        return String.valueOf(this.x) + ',' + String.valueOf(this.y) + ',' + String.valueOf(this.z);
    }
}
