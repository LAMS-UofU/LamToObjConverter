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
import java.util.TreeMap;
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
    Map<String, Edge> mapEdges;
    ArrayList<Edge> edges;
    ArrayList<Edge> recurssiveEdges;
    ArrayList<SphericalCoordinate> spherical;
    ArrayList<CartesianCoordinate> cartesian;
    ArrayList<Double> startAngles;
    ArrayList<Double[]> rawLAMData;
    int k;
    double round=20;
    private int numberOfStepsTheta = 0;
    private int numberOfStepsPhi = 0;
    
    public LamToObjConverter(){
        spherical = new ArrayList<SphericalCoordinate>();
        cartesian = new ArrayList<CartesianCoordinate>();
        startAngles = new ArrayList<Double>();
        rawLAMData = new ArrayList<Double[]>();
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
        startAngles = new ArrayList<Double>();
        rawLAMData = new ArrayList<Double[]>();
        try{
            Scanner s = new Scanner(lamFile);
            /*if(s.hasNextLine()){
                String[] temp = s.nextLine().split(",");
                this.numberOfStepsTheta = Integer.parseInt(temp[0]);
                this.numberOfStepsPhi = Integer.parseInt(temp[1]);
            }*/
            while(s.hasNextLine()){
                String temp  = s.nextLine();
                if(!temp.equals("")){
                    processLAMData(temp);
                    
                    //getSpherical(temp);
                }
            }
            s.close();
            getSpherical();
            this.clearDuplicates();
            this.printSphericalData();
            System.out.println(this.spherical.get(0));
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
            objContents.add("v  " + cc.dx + "  " + cc.dy + "  " + cc.dz);
        }
    }
    
    public void createEdges(){
        edges = new ArrayList<Edge>();
        mapEdges = new HashMap<String, Edge>();
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
                        if(!mapEdges.containsKey(tempEdge.toString()) && !mapEdges.containsKey(tempEdge.toString1())){
                            mapEdges.put(tempEdge.toString(),tempEdge);
                        }
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
                        if(!mapEdges.containsKey(tempEdge.toString()) && !mapEdges.containsKey(tempEdge.toString1())){
                            mapEdges.put(tempEdge.toString(),tempEdge);
                        }
                    }
                }
            }
        }
    }
    
    public Face getClockwiseFace(Face f){
        return new Face();
    }
    
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
        int size = faces.size();
        int size1;
        Stack<Edge> edgeStack = new Stack<Edge>();
        edgeStack.addAll(edges);
        //for(int i=0;i<edgeStack.size();i++){
        for(Edge e1:edges){
            //Edge[] tempEdges= new Edge[edges.size()];
            //edgeStack.toArray(tempEdges);
            //ArrayList<Edge> recEdges = new ArrayList<Edge>(Arrays.asList(tempEdges));
            ArrayList<Edge> recEdges = new ArrayList<Edge>(edges);
            ArrayList<ArrayList<Edge>> result;
            size1 = size;
            //Edge e1 = edgeStack.pop();
            result= followEdgeDriver(recEdges,e1);
            for(ArrayList<Edge> edgeArr:result){
                if(edgeArr.size()>1 && edgeArr!=null){
                    Face f = new Face(edgeArr,e1.getCommonVertex(edgeArr.get(0)),true);
                    faces.add(f);
                }
            }
            size = faces.size();
            if(size==size1){
                System.out.println("err");
            }
        }
    }
    
    public ArrayList<ArrayList<Edge>> followEdgeDriver(ArrayList<Edge> recEdges, Edge baseEdge){
        ArrayList<ArrayList<Edge>> result = new ArrayList<ArrayList<Edge>>();
        ArrayList<Edge> tempResult;
        tempResult = followEdge(recEdges,3,baseEdge.a,baseEdge.b,baseEdge);
        while(tempResult.size()>0){
            tempResult.add(baseEdge);
            result.add(tempResult);
            recEdges.remove(tempResult.get(0));
            tempResult = followEdge(recEdges,3,baseEdge.a,baseEdge.b,baseEdge);
        }
        return result;
    }
    
    public ArrayList<Edge> followEdge(ArrayList<Edge> recEdges, int steps, int endpoint, int vertex,Edge previousEdge){
        ArrayList<Edge> result = new ArrayList<Edge>();
        int index = 0;
        if(steps==0){
            return result;
        }
        else{
            steps--;
            for(Edge ed:recEdges){
                if(previousEdge!=null && ed!=null){
                    if(!ed.equals(previousEdge)){
                        if(ed.contains(vertex)){
                            if(ed.contains(endpoint)){
                                result.add(ed);
                                return result;
                            }
                            else{
                                result = followEdge(recEdges,steps,endpoint,ed.getOtherVertex(vertex),ed);
                                if(result.size()>0){
                                    result.add(ed);
                                    return result;
                                }
                            }
                        }
                        else{
                        }
                    }
                }
            }
        }
        return result;
    }
    
    public void getVertexNormals(){
        
    }
    
    public boolean createObjFile(File objFile){
        this.objContents = new ArrayList<String>();
        this.getVertices();
        objContents.add("\r\n");
        this.createEdges();
        
        this.createFaces();
        this.getEdges();
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
    
    public void getSpherical(){
        for(Double[] d:rawLAMData){
            int omegaInd = startAngles.indexOf(d[2]);
            double omega2=startAngles.get((omegaInd+1)%(startAngles.size()-1));
            double omega1=d[2];
            double phi1;
            double phi2;
            if(omega1>omega2){
                phi1=d[2]+((360+omega2-omega1)/32)*d[1]-d[3];
                phi2=d[2]+((360+omega2-omega1)/32)*d[1]-d[4];
            }
            else{
                phi1=d[2]+((omega2-omega1)/32)*d[1]-d[3];
                phi2=d[2]+((omega2-omega1)/32)*d[1]-d[4];
            }
            if(d[5]>0){
                spherical.add(new SphericalCoordinate(d[5],d[0],phi1%360));
            }
            if(d[6]>0){
                spherical.add(new SphericalCoordinate(d[6],d[0],phi2%360));
            }
            
        }
        
        
    }
    
    /*public void getSpherical(String text){
        String temp[] = text.split(",");
        double theta = new Double(temp[0]);
        double phi1;
        double phi2;
        double startAngle = new Double(temp[2])/64.0;
        double offset1 = new Double(temp[3])/8.0;
        double offset2 = new Double(temp[4])/8.0;
        double distance1 = new Double(temp[5])/1000;
        double distance2 = new Double(temp[6])/1000;
        phi1 = startAngle-offset1;
        phi2=startAngle-offset2;
        if(distance1!=0){
            this.spherical.add(new SphericalCoordinate(distance1,theta,phi1));
        }
        if(distance2!=0){
            this.spherical.add(new SphericalCoordinate(distance2,theta,phi2));
        }
        if(!startAngles.contains(startAngle)){
            startAngles.add(startAngle);
        }
    }*/

    public void clearDuplicates(){
        Map<String,ArrayList<SphericalCoordinate>> map = new HashMap<String,ArrayList<SphericalCoordinate>>();
        String tempKey = "";
        ArrayList<SphericalCoordinate> tempArr = new ArrayList<SphericalCoordinate>();
        for(SphericalCoordinate sc:this.spherical){
            tempArr = new ArrayList<SphericalCoordinate>();
            tempKey = roundDouble(sc.theta,this.round) + "," + roundDouble(sc.phi,this.round);
            if(map.containsKey(tempKey)){
                tempArr = map.get(tempKey);
            }
            tempArr.add(sc);
            map.put(tempKey,tempArr);
        }
        this.spherical = new ArrayList<SphericalCoordinate>();
        for(String s:map.keySet()){
            String[] tempStrArr = s.split(",");
            double tempPhi = new Double(tempStrArr[1]);
            double tempTheta = new Double(tempStrArr[0]);
            ArrayList<SphericalCoordinate> arr = map.get(s);
            double average = 0.0;
            for(SphericalCoordinate sc:arr){
                average+=sc.r;
            }
            spherical.add(new SphericalCoordinate(average/arr.size(),tempTheta,tempPhi));
        }
    }
    
    public double roundDouble(double d, double rounder){
        double temp = new Double(Math.round(d/rounder));
        return temp*rounder;
    }

    public void printSphericalData(){
        try{
            FileWriter fw = new FileWriter(new File("data.txt"));
            /*for(Double d:startAngles){
                fw.write(d.toString() + "\r\n");
            }*/
            ArrayList<SphericalCoordinate> sortedSet = new ArrayList<SphericalCoordinate>(spherical);
            Collections.sort(sortedSet);
            for(SphericalCoordinate sc:sortedSet){
                fw.write(sc.toString() + "\r\n");
            }
            fw.flush();
            fw.close();

        }
        catch (IOException ex) {
            
        }
    }

    private void processLAMData(String data) {
        String temp[] = data.split(",");
        double theta = new Double(temp[0]);
        double startAngle = new Double(temp[2]);
        double offset1 = new Double(temp[3]);
        double offset2 = new Double(temp[4]);
        double distance1 = new Double(temp[5])/1000;
        double distance2 = new Double(temp[6])/1000;
        if(!startAngles.contains(startAngle)){
            startAngles.add(startAngle);
            k=0;
        }
        else{
            k++;
        }
        if(offset1>255 || offset2>255){
            offset1 = -(offset1-256);
            offset2 = -(offset2-256);
        }
        if(startAngle>16383){
           // System.out.println("start neg");
        }
        startAngle=startAngle/64.0;
        offset1 = offset1/8.0;
        offset2 = offset2/8.0;
        Double[] tempArr = {theta,new Double(k),startAngle,offset1,offset2,distance1,distance2};
        this.rawLAMData.add(tempArr);
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
