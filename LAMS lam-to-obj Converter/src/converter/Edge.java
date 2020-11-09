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
public class Edge {
    public int a;
    public int b;
    
    Edge(){
        this.a = -1;
        this.b = -1;
    }
    
    Edge(int a){
        this.a = a;
        this.b = -1;
    }
    
    Edge(int a, int b){
        this.a = a;
        this.b = b;
    }
    
    public boolean equals(Edge ed){
        return this.a==ed.a && this.b == ed.b || this.b == ed.a && this.a == ed.b;
    }
    
    public String toString(){
        return a+"-"+b;
    }
    
    public String toString1(){
        return b+"-"+a;
    }
    
    public boolean canConnect(Edge e1){
        if(!(this.a == e1.a && this.b == e1.b)){
            if(!(this.b==e1.a && this.a==e1.b)){
                return this.a == e1.b || this.b==e1.b ||this.a == e1.a || this.b == e1.a;
            }
            return false;
        }
        return false;
    }
    
    public Edge getCommonEdge(Edge e1){
        int edA=0;
        int edB=0;
        if(this.a == e1.a){
            edA = this.b;
            edB = e1.b;
        }
        else if(this.b == e1.b){
            edA = this.a;
            edB = e1.a;
        }
        else if(this.a == e1.b){
            edA = this.b;
            edB = this.a;
        }
        else if(this.b == e1.a){
            edA = this.a;
            edB = e1.b;
        }
        return new Edge(edA,edB);
    }
    
    public int getCommonVertex(Edge e1){
        if(this.a ==e1.a || this.a == e1.b){
            return this.a;
        }
        else if(this.b == e1.a || this.b == e1.b){
            return this.b;
        }
        return -1;
    }
    
    public int getUncommonVertex(Edge e1){
        if(this.a ==e1.a || this.a == e1.b){
            return this.b;
        }
        else if(this.b == e1.a || this.b == e1.b){
            return this.a;
        }
        return -1;
    }
    
    public boolean contains(int vertex){
        return this.a == vertex || this.b == vertex;
    }
    
    public Edge getCompletingEdge(Edge e1){
        Edge result = new Edge(this.getUncommonVertex(e1));
        result.b = e1.getUncommonVertex(this);
        return result;
    }
    
    public int getOtherVertex(int a){
        if(this.a == a){
            return b;
        }
        else{
            return this.a;
        }
    }
}   
