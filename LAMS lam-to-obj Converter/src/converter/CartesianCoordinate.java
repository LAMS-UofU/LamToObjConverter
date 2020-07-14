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
    float x;
    float y;
    float z;
    
    public CartesianCoordinate(){
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
    }
    
    public CartesianCoordinate(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public CartesianCoordinate(double x, double y, double z){
        this.x = (float)x;
        this.y = (float)y;
        this.z = (float)z;
    }
    
    public float distance(CartesianCoordinate cc){
        Double xret = new Double(this.x-cc.x);
        Double yret = new Double(this.y - cc.y);
        Double zret = new Double(this.z - cc.z);
        return (float) Math.sqrt(Math.pow(xret, 2.0)+Math.pow(yret, 2.0) + Math.pow(zret, 2.0));
    }
    public double doubleDistance(CartesianCoordinate cc){
        Double xret = new Double(this.x - cc.x);
        Double yret = new Double(this.y - cc.y);
        Double zret = new Double(this.z - cc.z);
        return Math.sqrt(Math.pow(xret, 2.0)+Math.pow(yret, 2.0) + Math.pow(zret, 2.0));
    }
}
