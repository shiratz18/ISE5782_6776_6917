package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.awt.color.ICC_Profile;
import java.util.MissingResourceException;

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
    private ImageWriter _imageWriter;
    private RayTracerBase _rayTracerBase;

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

    public Camera setVPDistance(int distance) {
        this._distance = distance;
        return this;
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

    public Camera setRayTracer(RayTracerBase rayTracer) {
        this._rayTracerBase = rayTracer;
        return this;
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
     * Cast ray from camera in order to color a pixel
     *
     * @param nX  - resolution on X axis (number of pixels in row)
     * @param nY  - resolution on Y axis (number of pixels in column)
     * @param iC - pixel's column number (pixel index in row)
     * @param jR - pixel's row number (pixel index in column)
     */
    private void castRay(int nX, int nY, int iC, int jR) {
        Ray ray = constructRay(nX, nY, jR, iC);
        Color pixelColor = _rayTracerBase.traceRay(ray);
        _imageWriter.writePixel(jR, iC, pixelColor);
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
}
