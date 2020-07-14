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
        /*if(spherical.size()>0){
            ArrayList<Face> faces = new ArrayList<Face>();
            Map<Float,List<Integer>> phiSortedVertices = new HashMap<Float,List<Integer>>();
            Map<Float,List<Integer>> thetaSortedVertices = new HashMap<Float,List<Integer>>();
            List<Integer> phiVal;
            List<Integer> thetaVal;
            float phi = spherical.get(0).phi;
            for(int i = 0;i<spherical.size();i++){
                phiVal = phiSortedVertices.get(spherical.get(i).phi);
                thetaVal = thetaSortedVertices.get(spherical.get(i).theta);
                if(phiVal==null){
                    phiVal = new ArrayList<Integer>();
                    phiVal.add(new Integer(i+1));
                    phiSortedVertices.put(spherical.get(i).phi,phiVal);
                }
                else{
                    phiVal.add(new Integer(i));
                }
                if(thetaVal==null){
                    thetaVal = new ArrayList<Integer>();
                    thetaVal.add(new Integer(i+1));
                    thetaSortedVertices.put(spherical.get(i).theta,thetaVal);
                }
                else{
                    thetaVal.add(new Integer(i));
                }
            }
            
           List<Float> phiKeys = new ArrayList<Float>(phiSortedVertices.keySet());
           List<Float> thetaKeys = new ArrayList<Float>(thetaSortedVertices.keySet());
           Collections.sort(phiKeys);
           Collections.sort(thetaKeys);
           if(thetaKeys.size()>1 && phiKeys.size()>1){
                for(int i = 1;i<phiKeys.size();i++){
                    
                }
            }
           else{
               for(int j = 0;j<currentTrace.size()-1;j++){
                        faces.add(new Face(currentTrace.get(j),currentTrace.get(j+1),previousTrace.get((j+1)%(previousTrace.size()))));
                        faces.add(new Face(currentTrace.get(j),currentTrace.get(j+1),nextTrace.get(j%(nextTrace.size()-1))));
                    }
           }*/
        
        if(cartesian.size()>0){
            ArrayList<Face> faces = new ArrayList<Face>();
            for(int i = 0;i<cartesian.size()-1;i++){
                //FloatPair p = new FloatPair(spherical.get(i).phi,spherical.get(i).theta);
                int v1 = -1;
                double distance1 = -1.0;
                int v2 = -1;
                double distance2 = -1.0;
                int v3 = -1;
                double distance3 = -1.0;
                int v4 = -1;
                double distance4 = -1.0;
                for(int j = 0;j<cartesian.size();j++){
                    if(j!= i){
                        double distance = cartesian.get(i).doubleDistance(cartesian.get(j));
                        if(distance1==-1){
                            distance1 = distance;
                            v1 = j;
                        }
                        else if(distance<=distance1){
                            distance4 = distance3;
                            v4=v3;
                            distance3 = distance2;
                            v3=v2;
                            distance2=distance1;
                            v2=v1;
                            distance1 = distance;
                            v1 = j;
                        }
                        else if(distance<=distance2){
                            distance4 = distance3;
                            v4=v3;
                            distance3 = distance2;
                            v3=v2;
                            distance2=distance;
                            v2=j;
                        }
                        else if(distance<=distance3){
                            distance4 = distance3;
                            v4=v3;
                            distance3 = distance;
                            v3=j;
                        }
                        else if(distance<=distance4){
                            distance4 = distance;
                            v4=j;
                        }
                    }
                }
                if(v1!=-1 && v2!=-1){
                    if(v1!=v2){
                        faces.add(new Face(i+2,v1+1,v2+1));
                    }
                    if(v3!=-1){
                        if(v2!=v3){
                            faces.add(new Face(i+2,v2+1,v3+1));
                        }
                        if(v3!=v1){
                            faces.add(new Face(i+2,v3+1,v1+1));
                        }
                        if(v4!=-1){
                            if(v3!=v4);
                                faces.add(new Face(i+2,v3+1,v4+1));
                            if(v4!=v1);
                                faces.add(new Face(i+2,v4+1,v1+1));
                            if(v4!=v2);
                                faces.add(new Face(i+2,v4+1,v2+1));
                        }
                    }
                        
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
    
    private class FloatPair{
        float key;
        float value;
        
        FloatPair(float key, float value){
            this.key = key;
            this.value = value;
        }
    }
    
}
