package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.awt.color.ICC_Profile;

import static primitives.Util.isZero;

/**
 * This class represents a camera in the scene
 */
public class Camera {
    // reference point of the camera in 3D space
    private Point _p0;
   //reference vector forward
    private Vector _vTo;
    //reference vector up
    private Vector _vUp;
    //reference vector right
    private Vector _vRight;

    private double _distance = 10;
    //reference width of view plane
    private double _width = 500;
    //reference height of view plane
    private double _height = 500;

    /**
     * constructor
     * @param p0 point of the camera in 3D space
     * @param vTo vector forward from the camera
     * @param vUp vector up from the camera
     */
    public Camera(Point p0, Vector vTo, Vector vUp) {
        if (!isZero(vTo.dotProduct(vUp))) {
            throw new ArithmeticException("the two vectors should be orthogonal");
        }

        _p0 = p0;
        _vTo = vTo.normalize();
        _vUp = vUp.normalize();

        _vRight = _vTo.crossProduct(_vUp);

    }

    //getters
    public Point getP0() {
        return _p0;
    }

    public Vector getvTo() {
        return _vTo;
    }

    public Vector getvUp() {
        return _vUp;
    }

    public Vector getvRight() {
        return _vRight;
    }

    public double getDistance() {
        return _distance;
    }
//chaining methods

    /**
     * set the View plane for the Camera
     *
     * @param distance distance from 3D camera to the 2D View plane
     * @return the Cam era instance
     */
    public Camera setVPDistance(double distance) {
        _distance = distance;
        return this;
    }

    /**
     * set the view plane size
     * @param width  physical width
     * @param height physical height
     * @return the Camera instance
     */
    public Camera setVPSize(int width, int height) {
        _width = width;
        _height = height;
        return this;
    }

    /**
     * Constructing a ray through a pixel
     *
     * @param Nx amount of logical columns
     * @param Ny amount of logical rows
     * @param j  j value y of intersection point
     * @param i  i value x of intersection point
     * @return ray from camera's p0 to the viewplane (i,j)
     */
    public Ray constructRay(int Nx, int Ny, int j, int i) {
        //Image center
        Point Pc = _p0.add(_vTo.scale(_distance));

        //Ratio (pixel width & height)
        double Ry = _height / Ny;
        double Rx = _width / Nx;

        //Pixel[i,j] center
        Point Pij = Pc;

        double yI = -(i - (Ny - 1) / 2d) * Ry;
        double xJ = (j - (Nx - 1) / 2d) * Rx;

        if(! isZero(yI)){
            Pij= Pij.add(_vUp.scale(yI));
        }

       if(! isZero(xJ)){
            Pij= Pij.add(_vRight.scale(xJ));
        }

    //to do
        return new Ray(_p0, Pij.subtract(_p0));
    }
}
