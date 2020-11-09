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
public class Vector extends CartesianCoordinate {
    float x;
    float y;
    float z;
    
    public Vector(){
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
    }
    
    public Vector(CartesianCoordinate origin, CartesianCoordinate p){
        this.x = p.x-origin.x;
        this.y = p.y-origin.y;
        this.z = p.z-origin.z;
    }
    
    public Vector(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public void normalize(){
        
    }
    
    public boolean equals(Vector v){
        return this.x == v.x && this.y == v.y && this.z == v.z;
    }
    
    public double getAngleBetweenRads(Vector v){
        if(this.equals(v)){
            return 0.0;
        }
        else{
            return (this.x*v.x + this.y*v.y+this.z*v.z)/(Math.sqrt(new Double(this.x*this.x+this.y*this.y+this.z*this.z))*Math.sqrt(new Double(v.x*v.x+v.y*v.y+v.z*v.z)));
        }
        
    }
    public double getAngleBetweenXYRads(Vector v){
        if(this.equals(v)){
            return 0.0;
        }
        else{
            return (this.x*v.x + this.y*v.y)/(Math.sqrt(new Double(this.x*this.x+this.y*this.y))*Math.sqrt(new Double(v.x*v.x+v.y*v.y)));
        }
        
    }
}
