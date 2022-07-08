package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.awt.color.ICC_Profile;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;

import static primitives.Util.*;

/**
 * This class represents a camera in the scene
 */
public class Camera {
    /**
     * reference point of the camera in 3D space
     */
    private Point _p0;
    /**
     * reference vector forward
     */
    private Vector _vTo;
    /**
    *reference vector up
     */
    private Vector _vUp;
    /**
     *  reference vector right
     */
    private Vector _vRight;


    /**
     * The number of rays sent by the camera.
     */
    private int _numOfRays = 0;
    /**
     * The distance between the camera and the view plane.
     */
    private double _distance = 10;
    /**
     * reference width of view plane
     */
    private double _width = 500;
    /**
     *reference height of view plane
     */
    private double _height = 500;

    private ImageWriter _imageWriter;
    private RayTracerBasic _rayTracerBase;
    private boolean _focus = false;
    private Point _focalPix = null;
    public double _disFocal = 0;

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
     * set the num of rays
     * @param numOfRays
     * @return number of rays
     */
    public Camera setNumOfRays(int numOfRays) {
        this._numOfRays = numOfRays;
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
        return new Ray(_p0, Pij.subtract(_p0 ));
    }

    /**
     * set the view plane size
     * @param width  physical width
     * @param height physical height
     * @return the Camera instance
     */
    public Camera setVPSize(int width, int height) {
        this._width = width;
        this._height = height;
        return this;
    }
    //setters with returning
    public Camera setImageWriter(ImageWriter imageWriter) {
        this._imageWriter = imageWriter;
        return this;
    }

    public Camera setRayTracer(RayTracerBasic rayTracer) {
        this._rayTracerBase = rayTracer;
        return this;
    }


    /**
     * creating of lines net
     * @param interval between the lines
     * @param color for lines net
     */
    public void printGrid(int interval, Color color) {
        if (_imageWriter == null) {
            throw new MissingResourceException("missing resource", ImageWriter.class.getName(), "");
        }
        _imageWriter.printGrid(interval, color);
    }
    /**
     * start a method of create the image
     */
    public Camera writeToImage() {
        if (_imageWriter == null) {
            throw new MissingResourceException("missing resource", ImageWriter.class.getName(), "");
        }
        _imageWriter.writeToImage();
        return this;
    }

    /**
     * The function calculate the center point of the pixel.
     *
     * @param nX Total number of pixels in the x dimension.
     * @param nY Total number of pixels in the y dimension.
     * @param j  The index of the pixel on the x dimension.
     * @param i  The index of the pixel on the y dimension.
     * @return the center point in the pixel.
     */
    private Point CalculateCenterPointInPixel(int nX, int nY, int j, int i) {
        Point pC = _p0.add(_vTo.scale(_distance));
        Point pIJ = pC;

        double rY = _height / nY;
        double rX = _width / nX;

        double yI = -(i - (nY - 1) / 2d) * rY;
        double xJ = (j - (nX - 1) / 2d) * rX;

        if (!isZero(xJ)) {
            pIJ = pIJ.add(_vRight.scale(xJ));
        }
        if (!isZero(yI)) {
            pIJ = pIJ.add(_vUp.scale(yI));
        }
        return pIJ;
    }

    /**
     * Calculate the corner ray in pixel
     *
     * @param center point
     * @param nX     Total number of pixels in the x dimension.
     * @param nY     Total number of pixels in the y dimension.
     * @param j      The index of the pixel on the x dimension.
     * @param i      The index of the pixel on the y dimension.
     * @return List of rays
     */
    private List<Ray> CalculatCornerRayInPixel(Point center, int nX, int nY, int j, int i) {

        Point p = center;
        List<Ray> lcorner = new LinkedList<>();

        //up
        double yu = nY / (_height * 2);
        //right
        double xr = nX / (_width * 2);


        //left up
        if (!isZero(xr)) {
            p = center.add(_vRight.scale(-xr));
        }
        if (!isZero(yu)) {
            p = center.add(_vUp.scale(yu));
        }
        lcorner.add(new Ray(_p0, p.subtract(_p0)));
        p = center;

        //right up
        p = center.add(_vRight.scale(xr));
        p = center.add(_vUp.scale(yu));
        lcorner.add(new Ray(_p0, p.subtract(_p0)));
        p = center;

        //left down
        p = center.add(_vRight.scale(-xr));
        p = center.add(_vUp.scale(-yu));
        lcorner.add(new Ray(_p0, p.subtract(_p0)));
        p = center;

        //right down
        p = center.add(_vRight.scale(xr));
        p = center.add(_vUp.scale(-yu));
        lcorner.add(new Ray(_p0, p.subtract(_p0)));
        p = center;

        //left middle
        p = center.add(_vRight.scale(-xr));
        lcorner.add(new Ray(_p0, p.subtract(_p0)));
        p = center;

        //right middle
        p = center.add(_vRight.scale(xr));
        lcorner.add(new Ray(_p0, p.subtract(_p0)));
        p = center;

        //middle up
        p = center.add(_vUp.scale(yu));
        lcorner.add(new Ray(_p0, p.subtract(_p0)));
        p = center;

        //middle down
        p = center.add(_vUp.scale(-yu));
        lcorner.add(new Ray(_p0, p.subtract(_p0)));
        p = center;


        return lcorner;

    }
    /**
     * Constructs a ray through a given pixel on the view plane for AA.
     *
     * @param nX Total number of pixels in the x dimension.
     * @param nY Total number of pixels in the y dimension.
     * @param j  The index of the pixel on the x dimension.
     * @param i  The index of the pixel on the y dimension.
     * @return List of rays.
     */
    public LinkedList<Ray> constructRayPixelAA(int nX, int nY, int j, int i) {
        if (isZero(_distance))
            throw new IllegalArgumentException("distance can't be 0");

        LinkedList<Ray> rays = new LinkedList<>();

        double rX = _width / nX;
        double rY = _height / nY;

        double randX, randY;

        Point pCenterPixel = CalculateCenterPointInPixel(nX, nY, j, i);
        rays.add(new Ray(_p0, pCenterPixel.subtract(_p0)));
        if (_focus && !isFocus(j, i))
            rays.addAll(CalculatCornerRayInPixel(pCenterPixel, nX, nY, j, i));

        Point pInPixel;
        for (int k = 0; k < _numOfRays; k++) {
            randX = random(-rX / 2, rX / 2);
            randY = random(-rY / 2, rY / 2);
            pInPixel = new Point(pCenterPixel.getX() + randX, pCenterPixel.getY() + randY, pCenterPixel.getZ());
            rays.add(new Ray(_p0, pInPixel.subtract(_p0)));
        }
        return rays;
    }
    /**
     * check if it's focus
     * @param j
     * @param i
     * @return
     */
    private boolean isFocus(int j, int i) {
        return _focalPix.getX() <= j &&
                j <= _focalPix.getX() + _disFocal &&
                _focalPix.getY() <= i &&
                i <= _focalPix.getY() + _disFocal;
    }
    /**
     * checking if in every field there is a value
     */
    public Camera renderImage() {
        try {
            if (_imageWriter == null) {
                throw new MissingResourceException("missing resource", ImageWriter.class.getName(), "");
            }
            if (_rayTracerBase == null) {
                throw new MissingResourceException("missing resource", RayTracerBase.class.getName(), "");
            }

            //rendering the image
            int nX = _imageWriter.getNx();
            int nY = _imageWriter.getNy();
            for (int i = 0; i < nY; i++) {
                for (int j = 0; j < nX; j++) {
                    castRay(nX,nY,i,j);
                }
            }
        } catch (MissingResourceException e) {
            throw new UnsupportedOperationException("Not implemented yet" + e.getClassName());
        }
        return this;
    }
    /**
     * Constructs a ray through a given pixel on the view plane.
     *
     * @param X Total number of pixels in the x dimension.
     * @param Y Total number of pixels in the y dimension.
     * @param j The index of the pixel on the x dimension.
     * @param i The index of the pixel on the y dimension.
     * @return
     */
    public Ray constructOneRayPixel(int X, int Y, int j, int i) {
        Point pCenterPixel = CalculateCenterPointInPixel(X, Y, j, i);
        return new Ray(_p0, pCenterPixel.subtract(_p0));
    }
    /**
     * Cast ray from camera in order to color a pixel
     *
     * @param nX - resolution on X axis (number of pixels in row)
     * @param nY - resolution on Y axis (number of pixels in column)
     * @param iC - pixel's column number (pixel index in row)
     * @param jR - pixel's row number (pixel index in column)
     */
    private void castRay(int nX, int nY, int iC, int jR) {
        Ray ray = constructRay(nX, nY, jR, iC);
        Color pixelColor = _rayTracerBase.traceRay(ray);
        _imageWriter.writePixel(jR, iC, pixelColor);
    }
    private Color AdaptiveSuperSampling(int nX, int nY, int j, int i,  int numOfRays)  {
        Vector Vright = _vRight;
        Vector Vup = _vUp;
        Point cameraLoc = this.getP0();
        int numOfRaysInRowCol = (int)Math.floor(Math.sqrt(numOfRays));
        if(numOfRaysInRowCol == 1)  return _rayTracerBase.traceRay(constructOneRayPixel(nX, nY, j, i));

        Point pIJ = CalculateCenterPointInPixel(nX, nY, j, i);

        double rY = alignZero(_height / nY);
        // the ratio Rx = w/Nx, the width of the pixel
        double rX = alignZero(_width / nX);


        double PRy = rY/numOfRaysInRowCol;
        double PRx = rX/numOfRaysInRowCol;
        return _rayTracerBase.AdaptiveSuperSamplingRec(pIJ, rX, rY, PRx, PRy,cameraLoc,Vright, Vup,null);
    }
    /***
     * Move camera (move point of view of the camera)
     * @param up Vertical distance
     * @param right Horizontal side distance
     * @param to Horizontal to distance
     */
    public void moveCamera(double up, double right, double to) {
        //move Point0 according to params
        if (up == 0 && right == 0 && to == 0) return; //don't create Vector.Zero
        if (up != 0) this._p0.add(_vUp.scale(up));
        if (right != 0) this._p0.add(_vRight.scale(right));
        if (to != 0) this._p0.add(_vTo.scale(to));
    }

    /***
     * Rotate camera through axis and angle of rotation
     * @param axis Axis of rotation
     * @param theta Angle of rotation (degrees)

    public void rotateCamera(Vector axis, double theta) {
        //rotate all vector's using Vector.rotateVector Method
        if (theta == 0) return; //no rotation
        this._vUp.rotateVector(axis, theta);
        this._vRight.rotateVector(axis, theta);
        this._vTo.rotateVector(axis, theta);
    }
     */
    /**
     *
     * @param Nx
     * @param Ny
     * @param j
     * @param i
     * @return
     * improving the jagged edges - mini project 1
     */
    public List<Ray> constructRays(int Nx, int Ny, int j, int i)
    {
        //Image center
        Point Pc = _p0.add(_vTo.scale(_distance));

        //Ratio (pixel width & height)
        double Ry =_height/ Ny;
        double Rx = _width/Nx;

        //delta values for going to Pixel[i,j] from Pc
        double yI =  -(i - (Ny -1)/2)* Ry;
        double xJ =  (j - (Nx -1)/2)* Rx;

        if (! isZero(xJ) )
        {
            Pc = Pc.add(_vRight.scale(xJ));
        }

        if (! isZero(yI))
        {
            Pc = Pc.add(_vUp.scale(yI));
        }
        List<Ray> rays=new ArrayList<>();

        /**
         * puts the pixel center in the first place on the list.
         */
        rays.add(new Ray(_p0,Pc.subtract(_p0)));

        /**
         * creating Ry*Rx rays for each pixel.
         */
        Point newPoint=new Point(Pc.getX()-Rx/2,Pc.getY()+Rx/2,Pc.getZ());
        for (double t = newPoint.getY(); t >newPoint.getY()-Ry; t-=0.01)
        {
            for (double k = newPoint.getX(); k < newPoint.getX()+Rx; k+=0.01)
            {
                rays.add(new Ray(_p0,new Point(k,t,Pc.getZ()).subtract(_p0)));
            }
        }

        return rays;
    }

}
