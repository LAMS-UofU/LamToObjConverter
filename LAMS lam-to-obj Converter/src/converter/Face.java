/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

/** This class represents the face of a 3d object.
 * this method can represent a triangle or quad face.
 *
 * @author tyjensen
 */
public class Face {
    public int v1=-1;
    public int v2=-1;
    public int v3=-1;
    public int v4=-1;
    public boolean isTriangle = false;
    
    public Face(){
        
    }
    
    public Face(Integer v1,Integer v2, Integer v3){
        this.setTriangleFace(v1, v2, v3);
    }
    
    public Face(Integer v1,Integer v2, Integer v3, Integer v4){
        this.setPolygonFace(v1, v2, v3, v4);
    }
    
    public void setTriangleFace(int v1,int v2, int v3){
        isTriangle = true;
        this.v1=v1;
        this.v2 = v2;
        this.v3 = v3;
    }
    
    public void setPolygonFace(int v1, int v2, int v3, int v4){
        isTriangle = false;
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
    }
}
