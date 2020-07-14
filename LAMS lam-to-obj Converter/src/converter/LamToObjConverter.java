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
import jdk.internal.util.xml.impl.Pair;

/**
 *
 * @author tyjensen
 */
public class LamToObjConverter {
    private File lamFile;
    private File saveFile;
    private ArrayList<String> objContents;
    private ArrayList<Face> faces;
    private ArrayList<SphericalCoordinate> spherical;
    private ArrayList<CartesianCoordinate> cartesian;
    
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
    
    public void getFaces(){
        if(spherical.size()>0){
            ArrayList<Face> faces = new ArrayList<Face>();
            Map<Float,List<Integer>> sortedVertices = new HashMap<Float,List<Integer>>();
            List<Integer> val;
            float phi = spherical.get(0).phi;
            for(int i = 0;i<spherical.size();i++){
                val = sortedVertices.get(spherical.get(i).phi);
                if(val==null){
                    val = new ArrayList<Integer>();
                    val.add(new Integer(i+1));
                    sortedVertices.put(spherical.get(i).phi,val);
                }
                else{
                    val.add(new Integer(i));
                }
            }
            
           List<Float> keys = new ArrayList<Float>(sortedVertices.keySet());
           Collections.sort(keys);
           List<Integer> currentTrace = sortedVertices.get(keys.get(0));
           List<Integer> nextTrace = sortedVertices.get(keys.get((1)%keys.size()));
           List<Integer> previousTrace = sortedVertices.get(keys.get(keys.size()-1));
           if(keys.size()>1){
                for(int i = 1;i<keys.size();i++){
                    for(int j = 0;j<currentTrace.size()-1;j++){
                        faces.add(new Face(currentTrace.get(j),currentTrace.get(j+1),previousTrace.get((j+1)%(previousTrace.size()))));
                        faces.add(new Face(currentTrace.get(j),currentTrace.get(j+1),nextTrace.get(j%(nextTrace.size()-1))));
                    }
                    previousTrace = currentTrace;
                    currentTrace = nextTrace;
                    nextTrace = sortedVertices.get(keys.get(i));
                }
            }
           else{
               for(int j = 0;j<currentTrace.size()-1;j++){
                        faces.add(new Face(currentTrace.get(j),currentTrace.get(j+1),previousTrace.get((j+1)%(previousTrace.size()))));
                        faces.add(new Face(currentTrace.get(j),currentTrace.get(j+1),nextTrace.get(j%(nextTrace.size()-1))));
                    }
           }
           
            
            for(Face f:faces){
                if(f.isTriangle){
                    objContents.add("f  " + f.v1 + "  " + f.v2 + "  " + f.v3);
                }
                else{
                    objContents.add("f " + f.v1 + "  " + f.v2 + "  " + f.v3 + "  " + f.v4);
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
    
}
