/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

/***This class represents a set of spherical coordinates (r,θ φ).
 * This class contains a conversion method from spherical to Cartesian coordinates. 
 * All values are floats
 * 
 * @author Ty Jensen  
 */

public class SphericalCoordinate {
    float r;
    float theta;
    float phi;
    
    /**This method is a default constructor for the SphericalCoordinate Class.
     * this method takes no parameters and will initialize (r,θ φ) to (0.0f,0.0f,0.0f) 
     * 
     */
    public SphericalCoordinate(){
        this.r = 0.0f;
        this.theta = 0.0f;
        this.phi = 0.0f;
    }
    
    /**This method is an overloaded constructor for the SphericalCoordinate class.
     * this method will take and set the float class members to parameters (r,theta,phi)
     * 
     * @param r the positive float value that represents the radial distance.
     * @param theta the positive float value value from 0°  to 180° that represents the azimuthal angle.
     * @param phi the positive float value from 0°  to 360° that represents the polar angle 
     */
    public SphericalCoordinate(float r, float theta, float phi){
        this.r = r;
        this.theta = theta;
        this.phi = phi;
    }
    /**This method is another overloaded constructor for the SphericalCoordinate class.
     * This method will take the double parameters of r theta and phi, convert them to
     * floats, and assign them to the class members r, theta, and phi
     * 
     * @param r the positive double value that represents the radial distance.
     * @param theta the positive double value value from 0°  to 180° that represents the azimuthal angle.
     * @param phi the positive double value from 0°  to 360° that represents the polar angle 
     */
    public SphericalCoordinate(double r, double theta, double phi){
        this.r = (float)r;
        this.theta = (float)theta;
        this.phi = (float)phi;
    }
    
    /**This method will convert the spherical coordinate represented by the class members to
     * the corresponding CartesianCoordinate object.
     * 
     * @return a CartesianCoordinate object representing the same point p defined by the SphericalCoordinate class members.
     */
    public CartesianCoordinate getCartesianCoordiante(){
        CartesianCoordinate result = new CartesianCoordinate();
        Double tempPhi = new Double(this.phi)*Math.PI/180;
        Double tempTheta = new Double(this.theta)*Math.PI/180;
        Double tempR = new Double(this.r);
        double opp = (Math.sin(tempPhi))*tempR;
        result.x = (float)(Math.cos(tempTheta)*opp);
        result.y = (float)(Math.sin(tempTheta)*opp);
        result.z = (float)(Math.cos(tempPhi)*tempR);
        
        return result;
    }
}
