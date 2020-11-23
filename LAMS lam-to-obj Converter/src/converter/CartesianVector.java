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
public class CartesianVector implements Cartesian{
    public float x;
    public float y;
    public float z;
    
    public CartesianVector(){
        this.x=0;
        this.y=0;
        this.z=0;
    }
    
    public CartesianVector(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public CartesianVector(double x, double y, double z){
        this.x = (float)x;
        this.y = (float)y;
        this.z = (float)z;
    }
    
    public CartesianVector cross(CartesianVector cc){
        return new CartesianVector(this.y*cc.z-this.z*cc.y,this.z*cc.x-this.x*cc.z,this.x*cc.y-this.y*cc.x);
    }
    
    public CartesianVector sub(CartesianVector cc){
        return new CartesianVector(this.x-cc.x,this.y-cc.y,this.z-cc.z);
    }
    
    public float magnatude(){
        Double xret = new Double(this.x);
        Double yret = new Double(this.y);
        Double zret = new Double(this.z);
        return (float) Math.sqrt(Math.pow(xret, 2.0)+Math.pow(yret, 2.0) + Math.pow(zret, 2.0));
    }
    
    public CartesianVector normalize(){
        return new CartesianVector(this.x/this.magnatude(),this.y/this.magnatude(),this.z/this.magnatude());
    }
    
}
