package renderer;

import multithreading.ThreadPool;
import primitives.Color;
import primitives.Ray;

import java.util.LinkedList;
import java.util.MissingResourceException;

public class Render {

    /**
     * Image writer of the scene
*/
    ImageWriter _imageWriter = null;
    /**
     * Camera of the scene
*/
    Camera _camera = null;
    /**
     * The rays from the camera
*/
    RayTracerBase _rayTracerBase = null;
    /**
     * ThreadPool of the scene
*/
    private ThreadPool<Pixel> _threadPool = null;
    /**
     * Next pixel of the scene
*/
    private Pixel _nextPixel = null;
    /**
     * Last percent of the image to render
*/
    public static int lastPercent = -1;

    /**
     * Chaining method for setting the camera
     *
     * @param camera the camera to set
     * @return the current render
*/
    public Render setCamera(Camera camera) {
        this._camera = camera;
        return this;
    }

    /**
     * Chaining method for setting the ray tracer
     *
     * @param rayTracer the ray tracer to set
     * @return the current render
*/
    public Render setRayTracer(RayTracerBase rayTracer) {
        this._rayTracerBase = rayTracer;
        return this;
    }
    /**
     * Chaining method for setting the image writer
     *
     * @param imageWriter the image writer to set
     * @return the current render
*/
    public Render setImageWriter(ImageWriter imageWriter) {
        this._imageWriter = imageWriter;
        return this;
    }
    /**
     * Saves the image according to image writer.
*/
    public void writeToImage() {
        _imageWriter.writeToImage();
    }
    /**
     * Returns the next pixel to draw on multithreaded rendering.
     * If finished to draw all pixels, returns {@code null}.
*/
    private synchronized Pixel getNextPixel() {

        // notifies the main thread in order to print the percent
        notifyAll();


        Pixel result = new Pixel();
        int nX = _imageWriter.getNx();
        int nY = _imageWriter.getNy();

        // updates the row of the next pixel to draw
        // if got to the end, returns null
        if (_nextPixel.col >= nX) {
            if (++_nextPixel.row >= nY) {
                return null;
            }
            _nextPixel.col = 0;
        }

        result.col = _nextPixel.col++;
        result.row = _nextPixel.row;
        return result;
    }
    /**
     * Chaining method for setting number of threads.
     * If set to 1, the render won't use the thread pool.
     * If set to greater than 1, the render will use the thread pool with the given threads.
     * If set to 0, the thread pool will pick the number of threads.
     *
     //* @param threads number of threads to use
     * @return the current render
     * @throws IllegalArgumentException when threads is less than 0
*/
    public Render setMultithreading(int threads) {
        if (threads < 0) {
            throw new IllegalArgumentException("threads can be equals or greater to 0");
        }

        // run as single threaded without the thread pool
        if (threads == 1) {
            _threadPool = null;
            return this;
        }

        _threadPool = new ThreadPool<Pixel>() // the thread pool choose the number of threads (in0 case threads is 0)
                .setParamGetter(this::getNextPixel)
                .setTarget(this::renderImageMultithreaded);
        if (threads > 0) {
            _threadPool.setNumThreads(threads);
        }

        return this;
    }

    /**
     * Renders a given pixel on multithreaded rendering.
     * If the given pixel is null, returns false which means kill the thread.
     *
   //  * @param p the pixel to render
*/
    private boolean renderImageMultithreaded(Pixel p) {
        if (p == null) {
            return false; // kill the thread
        }

        int nX = _imageWriter.getNx();
        int nY = _imageWriter.getNy();
        castRay(nX, nY, p.col, p.row);

        return true; // continue the rendering
    }

    /**
     * Helper class to represent a pixel to draw in a multithreading rendering.
*/
    private static class Pixel {
        public int col, row;

        public Pixel(int col, int row) {
            this.col = col;
            this.row = row;
        }

        public Pixel() {
        }
    }

    public void renderImage() {
        try {
            if (_imageWriter == null) {
                throw new MissingResourceException("Missing resource", ImageWriter.class.getName(), "");
            }
            if (_camera == null) {
                throw new MissingResourceException("Missing resource", Camera.class.getName(), "");
            }
            if (_rayTracerBase == null) {
                throw new MissingResourceException("Missing resource", RayTracerBase.class.getName(), "");
            }

            int nX = _imageWriter.getNx();
            int nY = _imageWriter.getNy();

            //rendering the image with multi-threaded
            if (_threadPool != null) {
                _nextPixel = new Pixel(0, 0);
                _threadPool.execute();

                printPercentMultithreaded(); // blocks the main thread until finished and prints the progress

                _threadPool.join();
                return;
            }

            // rendering the image when single-threaded
            adaptive(0, nY / 2, nX / 2, 0, nX, nY, 1);

            LinkedList<Ray> rays;

            // prints the 100% percent
            printPercent(nX * nY, nX * nY, lastPercent);
        } catch (MissingResourceException e) {
            throw new UnsupportedOperationException("Render didn't receive " + e.getClassName());
        }
    }

        public void adaptive(int j1, int i1, int j2, int i2, int nX, int nY, int level) {
            int numOfSame = sameColor(j1, i1, j2, i2, j2 * 2, i1, j2, i1 * 2, j2, i1, j2 / 2, i1, j2 + j2 / 2, i1, j2, i1 / 2, i1 + nX / (level * 2), i1 + nY / (level * 2), nX, nY);
            //if all the pixels has the same color
            if (numOfSame == 8) {
                LinkedList<Ray> rays;
                rays = _camera.constructRayPixelAA(nX, nY, j1, i1);
                Color c = _rayTracerBase.averageColor(rays);
                System.out.println(level);
                //color all the pixels
                for (int i = i2; i < i2 + nY / level; i++) {
                    for (int j = j1; j < j1 + nX / level; j++) {
                        int currentPixel = i * nX + j;
                        lastPercent = printPercent(currentPixel, nX * nY, lastPercent);
                        _imageWriter.writePixel(j, i, c);
                    }
                }
            }
            //different color low level
            else if (numOfSame > 6) {
                adaptive(j1, i1 / 2, j2 / 2, i2, nX, nY, level * 2);
                adaptive(j2, j2 / 2, j2 + j2 / 2, i2, nX, nY, level * 2);
                adaptive(j1, i1 + i1 / 2, j2 / 2, i1, nX, nY, level * 2);
                adaptive(j2, i1 + i1 / 2, j2 + j2 / 2, i1, nX, nY, level * 2);
            }

            else {
                LinkedList<Ray> rays;
                //pass through each pixel and calculate the color
                for (int i = i2; i < i2 + nY / level; i++) {
                    for (int j = j1; j < j1 + nX / level; j++) {
                        int currentPixel = i * nX + j;
                        lastPercent = printPercent(currentPixel, nX * nY, lastPercent);
                        castRay(nX, nY, j, i);
                    }
                }
            }
        }
    /**
     * Must run on the main thread.
     * Prints the percent on multithreaded rendering.
*/
    private void printPercentMultithreaded() {
        int nX = _imageWriter.getNx();
        int nY = _imageWriter.getNy();
        int pixels = nX * nY;
        int lastPercent = -1;

        while (_nextPixel.row < nY) {
            // waits until got update from the rendering threads
            synchronized (this) {
                try {
                    wait();
                }
                catch (InterruptedException e) {
                }
            }

            int currentPixel = _nextPixel.row * nX + _nextPixel.col;
            lastPercent = printPercent(currentPixel, pixels, lastPercent);
        }
    }
    /**
     * Prints the progress in percents only if it is greater than the last time printed the progress.
     *
     * @param currentPixel the index of the current pixel
     * @param pixels       the number of pixels in the image
     * @param lastPercent  the percent of the last time printed the progress
     * @return If printed the new percent, returns the new percent. Else, returns {@code lastPercent}.
*/
    private int printPercent(int currentPixel, int pixels, int lastPercent) {
        int percent = currentPixel * 100 / pixels;
        if (percent > lastPercent) {
            System.out.printf("%02d%%\n", percent);
            System.out.flush();
            return percent;
        }
        return lastPercent;
    }
    /**
     * Help function that check how many of the pixel has the same color, get the index (j,i) of 9 pixels
     * @param nX  the number of columns in the picture
     * @param nY  the number of rows in the picture
     * @return the number of pixel with the same color
*/
    private int sameColor(int j1, int i1, int j2, int i2, int j3, int i3, int j4, int i4, int j5, int i5, int j6, int i6, int j7, int i7, int j8, int i8, int j9, int i9, int nX, int nY) {
        Color c1 = _rayTracerBase.traceRay(_camera.constructOneRayPixel(nX, nY, j1, i1));
        Color c2 = _rayTracerBase.traceRay(_camera.constructOneRayPixel(nX, nY, j2, i2));
        Color c3 = _rayTracerBase.traceRay(_camera.constructOneRayPixel(nX, nY, j3, i3));
        Color c4 = _rayTracerBase.traceRay(_camera.constructOneRayPixel(nX, nY, j4, i4));
        Color c5 = _rayTracerBase.traceRay(_camera.constructOneRayPixel(nX, nY, j5, i5));
        Color c6 = _rayTracerBase.traceRay(_camera.constructOneRayPixel(nX, nY, j6, i6));
        Color c7 = _rayTracerBase.traceRay(_camera.constructOneRayPixel(nX, nY, j7, i7));
        Color c8 = _rayTracerBase.traceRay(_camera.constructOneRayPixel(nX, nY, j8, i8));
        Color c9 = _rayTracerBase.traceRay(_camera.constructOneRayPixel(nX, nY, j9, i9));
        int sum = 0;
        if (c1 == c2)
            sum++;
        if (c2 == c3)
            sum++;
        if (c3 == c4)
            sum++;
        if (c4 == c5)
            sum++;
        if (c5 == c6)
            sum++;
        if (c6 == c7)
            sum++;
        if (c7 == c8)
            sum++;
        if (c8 == c9)
            sum++;
        return sum;
    }
    /**
     * Casts a ray through a given pixel and writes the color to the image.
     *
     * @param nX  the number of columns in the picture
     * @param nY  the number of rows in the picture
     * @param col the column of the current pixel
     * @param row the row of the current pixel
*/
    private void castRay(int nX, int nY, int col, int row) {
        LinkedList<Ray> rays = _camera.constructRayPixelAA(nX, nY, col, row);
        Color pixelColor = _rayTracerBase.averageColor(rays);
        _imageWriter.writePixel(col, row, pixelColor);
    }
}