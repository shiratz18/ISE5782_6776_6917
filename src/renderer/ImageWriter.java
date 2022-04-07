package renderer;

import primitives.Color;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.*;

/**
 * Image writer class combines accumulation of pixel color matrix and finally
 * producing a non-optimized jpeg image from this matrix. The class although is
 * responsible of holding image related parameters of View Plane - pixel matrix
 * size and resolution
 *
 * @author Dan
 */
public class ImageWriter {
    private int _nX;
    private int _nY;

    private static final String FOLDER_PATH = System.getProperty("user.dir") + "/images";

    private BufferedImage _image;
    private String _imageName;

    private Logger _logger = Logger.getLogger("ImageWriter");

    // ***************** Constructors ********************** //
    /**
     * Image Writer constructor accepting image name and View Plane parameters,
     * @param imageName the name of jpeg file
     * @param nX        amount of pixels by Width
     * @param nY        amount of pixels by height
     */
    public ImageWriter(String imageName, int nX, int nY) {
        this._imageName = imageName;
        this._nX = nX;
        this._nY = nY;

        _image = new BufferedImage(nX, nY, BufferedImage.TYPE_INT_RGB);
    }

    // ***************** Getters/Setters ********************** //
    /**
     * View Plane Y axis resolution
     *
     * @return the amount of vertical pixels
     */
    public int getNy() {
        return _nY;
    }

    /**
     * View Plane X axis resolution
     *
     * @return the amount of horizontal pixels
     */
    public int getNx() {
        return _nX;
    }

    // ***************** Operations ******************** //

    /**
     * Function writeToImage produces unoptimized png file of the image according to
     * pixel color matrix in the directory of the project
     */
    public void writeToImage() {
        try {
            File file = new File(FOLDER_PATH + '/' + _imageName + ".png");
            ImageIO.write(_image, "png", file);
        } catch (IOException e) {
            _logger.log(Level.SEVERE, "I/O error", e);
            throw new IllegalStateException("I/O error - may be missing directory " + FOLDER_PATH, e);
        }
    }

    /**
     * The function writePixel writes a color of a specific pixel into pixel color
     * matrix
     *
     * @param xIndex X axis index of the pixel
     * @param yIndex Y axis index of the pixel
     * @param color  final color of the pixel
     */
    public void writePixel(int xIndex, int yIndex, Color color) {
        _image.setRGB(xIndex, yIndex, color.getColor().getRGB());
    }

    /**
     * print grid of the image
     * @param interval between the objects
     * @param color color of the image
     */
    public void printGrid(int interval, Color color) {
        //move on the width
        for (int i = 0; i < _nX; i++) {
            //move on the length
            for (int j = 0; j < _nY; j++) {
                //if it is the match place color it
                if (i % interval == 0 || j % interval == 0) {
                    writePixel(i, j, color);
                }
            }
        }
    }

    /**
     * fill the background in the matching color
     * @param bacckgroundColor color of the background in the image
     */
    public void fillBackground(Color bacckgroundColor) {
        Graphics2D g2d = _image.createGraphics();
        g2d.setColor(bacckgroundColor.getColor());
        g2d.fillRect(0, 0, _nX, _nY);
    }
}
