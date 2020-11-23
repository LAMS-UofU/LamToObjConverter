/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import converter.CartesianVector;

/**
 *
 * @author tyjensen
 */
public class CartesianCoordinate {
    public float fx;
    public float fy;
    public float fz;
    public double dx;
    public double dy;
    public double dz;
    public CartesianVector normal;
    
    public CartesianCoordinate(){
        this.fx = 0.0f;
        this.fy = 0.0f;
        this.fz = 0.0f;
        this.dx = 0.0;
        this.dy = 0.0;
        this.dz = 0.0;
    }
    
    public CartesianCoordinate(float x, float y, float z){
        this.fx = x;
        this.fy = y;
        this.fz = z;
        this.dx = new Double(fx);
        this.dy = new Double(fy);
        this.dz = new Double(fz);
    }
    
    public CartesianCoordinate(double x, double y, double z){
        this.fx = (float)x;
        this.fy = (float)y;
        this.fz = (float)z;
        this.dx = x;
        this.dy = y;
        this.dz = z;
    }
    
    public float getFX(){
        return fx;
    }
    
    public double getDX(){
        return dx;
    }
    
    public float getFY(){
        return fy;
    }
    
    public double getDY(){
        return dy;
    }
    
    public float getFZ(){
        return fz;
    }
    
    public double getDZ(){
        return dz;
    }
    
    public void setX(float x){
        this.fx = x;
        this.dx = new Double(x);
    }
    
    public void setX(double x){
        this.dx = x;
        this.fx = new Float(x);
    }
    
    public void setY(float y){
        this.fy = y;
        this.dy = new Double(y);
    }
    
    public void setY(double y){
        this.dy = y;
        this.fy = new Float(y);
    }
    
    public void setZ(float z){
        this.fz = z;
        this.dz = new Double(z);
    }
    
    public void setZ(double z){
        this.dz = z;
        this.fz = new Float(z);
    }
    
    public float distance(CartesianCoordinate cc){
        Double xret = new Double(this.dx-cc.dx);
        Double yret = new Double(this.dy - cc.dy);
        Double zret = new Double(this.dz - cc.dz);
        return (float) Math.sqrt(Math.pow(xret, 2.0)+Math.pow(yret, 2.0) + Math.pow(zret, 2.0));
    }
    public double doubleDistance(CartesianCoordinate cc){
        Double xret = new Double(this.dx - cc.dx);
        Double yret = new Double(this.dy - cc.dy);
        Double zret = new Double(this.dz - cc.dz);
        return Math.sqrt(Math.pow(xret, 2.0)+Math.pow(yret, 2.0) + Math.pow(zret, 2.0));
    }
    
    public String toString(){
        return this.dx+","+this.dy + "," + this.dz;
    }
    
    public converter.CartesianCoordinate add(CartesianCoordinate cc){
        return new converter.CartesianCoordinate(this.dx+cc.dx,this.dy +cc.dy,this.dz+cc.dz);
    }
    
    public converter.CartesianCoordinate sub(CartesianCoordinate cc){
        return new converter.CartesianCoordinate(this.dx-cc.dx,this.dy-cc.dy,this.dz-cc.dz);
    }
    
    public CartesianVector getVector(){
        return new CartesianVector(this.fx,this.fy,this.fz);
    }
   
}