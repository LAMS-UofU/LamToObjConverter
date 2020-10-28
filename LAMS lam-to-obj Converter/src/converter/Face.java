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
    
    public Face(Integer v1,Integer v2, Integer v3, boolean isZeroBased){
        if(isZeroBased){
            this.setTriangleFace(v1+1, v2+1, v3+1);
        }
        else{
            this.setTriangleFace(v1, v2, v3);
        }
    }
    
    public Face(Edge e1, Edge e2, Edge e3, boolean isZeroBased){
        int a=e1.a;
        int b =e1.b;
        int c = e2.a;
        int d = e2.b;
        int e = e3.a;
        int f = e3.b;
        int v1 = a;
        int v2 = b;
        int v3 = 0;
        if(a==c || b == c){
            v3 = d;
        }
        else if(a==d || b==d){
            v3 = c;
        }
        if(isZeroBased){
            this.setTriangleFace(v1+1, v2+1, v3+1);
        }
        else{
            this.setTriangleFace(v1, v2, v3);
        }
    }
    
    /*public Face(Edge e1, Edge e2, Edge e3, Edge e4, boolean isZeroBased){
        int a=e1.a;
        int b =e1.b;
        int c = e2.a;
        int d = e2.b;
        int e = e3.a;
        int f = e3.b;
        int g = e4.a;
        int h = e4.b;
        int v1 = a;
        int v2 = b;
        int v3 = 0;
        int v4 = 0;
        if(a==c || b == c){
            v3 = d;
        }
        else if(a==d || b==d){
            v3 = c;
        }
        if(g!=v1 && g!=v2 && g!=v3){
            v4=g;
        }
        else if(h!=v1 && h!=v2 && h!=v3)
        if(isZeroBased){
            this.setPolygonFace(v1+1, v2+1, v3+1, v4+1);
        }
        else{
            this.setPolygonFace(v1, v2, v3, v4);
        }
    }*/
    
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
    
    public boolean equals(Face f){
        if(f.isTriangle && this.isTriangle){
            if((this.v1+this.v2+this.v3+this.v4) == (f.v1+f.v2+f.v3+f.v4)){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
                
                
    }
    
    public String toString(){
        return this.v1 + "-" + this.v2 + "-" + this.v3 +"-"+ this.v4;
    }
    
    public void flipNormal(){
        if(this.isTriangle){
            int temp = this.v1;
            this.v1 = this.v3;
            this.v3 = temp;
        }
        else{
            int temp = this.v1;
            this.v1=this.v4;
            temp = this.v2;
            this.v2 = this.v3;
            this.v3 = temp;
        }
    }
}
