package lighting;

import primitives.Color;
import primitives.Double3;

/**
 * Ambient Light Color
 */
public class AmbientLight extends Light {


    /**
     * Constructor
     * @param Ia intensity color
     * @param Ka constant for intensity
     */
    public AmbientLight(Color Ia, Double3 Ka) {
        super(Ia.scale(Ka));
    }

    /**
     * Constructor
     * @param Ia intensity color
     * @param Ka constant for intensity
     */
    public AmbientLight(Color Ia, double Ka) {
        super(Ia.scale(new Double3(Ka)));
    }


    /**
     * default constructor
     */
    public AmbientLight() {
        super(Color.BLACK);
    }



}
