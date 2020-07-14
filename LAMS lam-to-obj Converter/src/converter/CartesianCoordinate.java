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
}
