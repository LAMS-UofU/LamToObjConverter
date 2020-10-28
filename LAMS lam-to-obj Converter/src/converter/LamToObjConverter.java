/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author tyjensen
 */
public class LamToObjConverter {
    private File lamFile;
    private File saveFile;
    private ArrayList<String> objContents;
    private ArrayList<Face> faces;
    Map<Integer, ArrayList<Edge>> sortedEdges;
    ArrayList<Edge> edges;
    ArrayList<SphericalCoordinate> spherical;
    ArrayList<CartesianCoordinate> cartesian;
    private int numberOfStepsTheta = 0;
    private int numberOfStepsPhi = 0;
    
    public LamToObjConverter(){
        spherical = new ArrayList<SphericalCoordinate>();
        cartesian = new ArrayList<CartesianCoordinate>();
    }
    
    /**The convert method will perform the conversion between the spherical coordinates stored in the .lam file
     * to the Cartesian coordinates needed for the .obj file.
     * 
     * @param lamFile the file object that contains the .lam file to be read and converted.
     * @return a Boolean indicating success or failure of the operation.
     */
    public boolean convert(File lamFile){
        spherical = new ArrayList<SphericalCoordinate>();
        cartesian = new ArrayList<CartesianCoordinate>();
        try{
            Scanner s = new Scanner(lamFile);
            if(s.hasNextLine()){
                String[] temp = s.nextLine().split(",");
                this.numberOfStepsTheta = Integer.parseInt(temp[0]);
                this.numberOfStepsPhi = Integer.parseInt(temp[1]);
            }
            while(s.hasNextLine()){
                String temp  = s.nextLine();
                if(!temp.equals("")){
                    spherical.add(getSpherical(temp));
                }
            }
            s.close();
            for(SphericalCoordinate sc:spherical){
                cartesian.add(sc.getCartesianCoordiante());
            }
            return true;
        }
        catch(FileNotFoundException e){
            return false;
        }
    }
    
    /**This method will get the vertices text needed for the .obj file from the Cartesian ArrayList object containing
     * the CartesianCoordinate objects.
     * 
     */
    public void getVertices(){
        for(CartesianCoordinate cc:cartesian){
            objContents.add("v  " + cc.x + "  " + cc.y + "  " + cc.z);
        }
    }
    
    public void createEdges(){
        edges = new ArrayList<Edge>();
        sortedEdges = new HashMap<Integer, ArrayList<Edge>>();
        ArrayList<Edge> tempEdges = new ArrayList<Edge>();
        Map<String,Integer> slices = new HashMap<String,Integer>();
        ArrayList<Integer> tempVertexList;
        for(int i=0;i<spherical.size();i++){
            SphericalCoordinate sc = spherical.get(i);
            String temp = String.valueOf(sc.theta) + String.valueOf(sc.phi);
            slices.put(temp, i);
        }
        float thetaSteps = 180.0f/numberOfStepsTheta;
        float phiSteps = 360.0f/numberOfStepsPhi;
        ArrayList<Edge> tempEdgeArr = new ArrayList<Edge>();
        for(int i =0;i<this.numberOfStepsTheta; i++){
            for(int j=0;j<this.numberOfStepsPhi; j++){
                String temp = String.valueOf(thetaSteps*i) + String.valueOf(phiSteps*j);
                if(slices.containsKey(temp)){
                    String temp1 = String.valueOf(thetaSteps*i) + String.valueOf((phiSteps*(j+1))%360);
                    if(slices.containsKey(temp1)){
                        Edge tempEdge = new Edge(slices.get(temp),slices.get(temp1));
                        edges.add(tempEdge); 
                        tempEdgeArr = new ArrayList<Edge>();
                        if(sortedEdges.containsKey(slices.get(temp))){
                            tempEdgeArr = sortedEdges.get(slices.get(temp));
                        }
                        tempEdgeArr.add(tempEdge);
                        sortedEdges.put(slices.get(temp), tempEdgeArr);
                        tempEdgeArr = new ArrayList<Edge>();
                        if(sortedEdges.containsKey(slices.get(temp1))){
                            tempEdgeArr = sortedEdges.get(slices.get(temp1));
                        }
                        tempEdgeArr.add(tempEdge);
                        sortedEdges.put(slices.get(temp1), tempEdgeArr);
                    }
                    if(thetaSteps*(i+1)>=180){
                        temp1 = String.valueOf((thetaSteps*(i+1))%180) + String.valueOf(((Math.abs((-1*phiSteps*(j))+360))%360));
                    }
                    else{
                        temp1 = String.valueOf((thetaSteps*(i+1))%180) + String.valueOf(((phiSteps*(j)))%360);
                    }
                    if(slices.containsKey(temp1)){
                        Edge tempEdge = new Edge(slices.get(temp),slices.get(temp1));
                        edges.add(tempEdge); 
                        tempEdgeArr = new ArrayList<Edge>();
                        if(sortedEdges.containsKey(slices.get(temp))){
                            tempEdgeArr = sortedEdges.get(slices.get(temp));
                        }
                        tempEdgeArr.add(tempEdge);
                        sortedEdges.put(slices.get(temp), tempEdgeArr);
                        tempEdgeArr = new ArrayList<Edge>();
                        if(sortedEdges.containsKey(slices.get(temp1))){
                            tempEdgeArr = sortedEdges.get(slices.get(temp1));
                        }
                        tempEdgeArr.add(tempEdge);
                        sortedEdges.put(slices.get(temp1), tempEdgeArr);
                    }
                }
            }
        }
    }
    
    public Face getClockwiseFace(Face f){
        return new Face();
    }
    
    /*public Face getClockwiseFace(Face f){
        int[] faceVertices = {f.v1-1,f.v2-1,f.v3-1};
        int leftmost=f.v1-1;
        int middle = f.v1-1;
        int rightmost = f.v1-1;
        CartesianCoordinate tempCC;
        CartesianCoordinate leftmostCC;
        CartesianCoordinate rightmostCC;
        CartesianCoordinate middleCC;
        int temp;
        for(int i = 1;i<faceVertices.length;i++){
            tempCC = cartesian.get(faceVertices[i]);
            leftmostCC = cartesian.get(leftmost);
            rightmostCC = cartesian.get(rightmost);
            middleCC = cartesian.get(middle);
            //if(cartesian.get(faceVertices[i]).x < cartesian.get(leftmost).x){
            if(tempCC.x < leftmostCC.x){
               leftmost = faceVertices[i];
            }
            else if(tempCC.x>leftmostCC.x){
                if(tempCC.x>rightmostCC.x){
                    rightmost = faceVertices[i];
                }
                else if(tempCC.x<rightmostCC.x){
                    middle = faceVertices[i];
                }
                else if(tempCC.x==rightmostCC.x){
                    if(tempCC.z < rightmostCC.z){
                        rightmost = faceVertices[i];
                     }
                     else if(tempCC.z>rightmostCC.z){
                        middle = faceVertices[i];
                     }
                     else if(tempCC.z==rightmostCC.z){
                         if(tempCC.y < rightmostCC.y){
                            rightmost = faceVertices[i];
                         }
                         else if(tempCC.y>rightmostCC.y){
                            middle = faceVertices[i];
                         }
                    }
                }
            }
            else if(tempCC.x==leftmostCC.x){
                if(tempCC.z < leftmostCC.z){
                    leftmost = faceVertices[i];
                 }
                 else if(tempCC.z>leftmostCC.z){
                    middle = faceVertices[i];
                 }
                 else if(tempCC.z==leftmostCC.z){
                     if(tempCC.y < leftmostCC.y){
                        leftmost = faceVertices[i];
                     }
                     else if(tempCC.y>leftmostCC.y){
                        middle = faceVertices[i];
                     }
                }
            }
        }
        
        return new Face(rightmost,middle,leftmost,true);
    }*/
    
    /*public Face getCCFace(){
        
    }*/
    
    public void getFaces(){
        for(Face f:faces){
            if(f.isTriangle){
                objContents.add("f  " + f.v1 + "  " + f.v2 + "  " + f.v3);
            }
            else{
                objContents.add("f " + f.v1 + "  " + f.v2 + "  " + f.v3 + "  " + f.v4);
            }
        }
    }
    
    public void getEdges(){
        for(Edge e:edges){
            objContents.add("e  " + e.a + "  " + e.b);
        }
    }
    
    public void createFaces(){
        faces = new ArrayList<Face>();
        for(Integer i:sortedEdges.keySet()){
            ArrayList<Edge> temp = sortedEdges.get(i);
            if(temp.size()>1){
                double distance = -1;
                for(int j = 0;j<temp.size();j++){
                    Edge tempEdge = new Edge();
                    int tempIndex=0;
                    for(int k=0;k<temp.size();k++){
                        if(j!=k){
                            tempIndex = k;
                            tempEdge = temp.get(j).getCompletingEdge(temp.get(k));
                            double tempDistance = cartesian.get(tempEdge.a).doubleDistance(cartesian.get(tempEdge.b));
                            if(distance == -1){
                                distance = tempDistance;
                            }
                            else if(tempDistance<distance){
                                distance = tempDistance;
                            }
                        }
                    }
                    //faces.add(this.getClockwiseFace(new Face(temp.get(j),temp.get(tempIndex),tempEdge,true)));
                    faces.add(new Face(temp.get(j),temp.get(tempIndex),tempEdge,true));
                    edges.add(tempEdge);
                }
            }
        }
    }
    
    public void getVertexNormals(){
        
    }
    
    public boolean createObjFile(File objFile){
        this.objContents = new ArrayList<String>();
        this.getVertices();
        objContents.add("\r\n");
        this.createEdges();
        this.getEdges();
        this.createFaces();
        this.getFaces();
        try{
            FileWriter fw = new FileWriter(objFile);
            for(String s:objContents){
                fw.write(s + "\r\n");
            }
            fw.flush();
            fw.close();
            return true;
        }
        catch (IOException ex) {
            return false;
        }
        
    }
    
    public SphericalCoordinate getSpherical(String text){
        String temp[] = text.split(",");
        return new SphericalCoordinate(new Float(temp[0]),new Float(temp[1]),new Float(temp[2]));
    }
    
    private class FloatPair{
        float key;
        float value;
        
        FloatPair(float key, float value){
            this.key = key;
            this.value = value;
        }
    }
    
}
