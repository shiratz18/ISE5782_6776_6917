package renderer;
import primitives.Color;
import primitives.Ray;
import scene.Scene;
/**
 * This abstract class will present a ray that serve class of ray tracer basic based on three numbers
 */
public abstract class RayTracerBase {

    protected Scene _scene;

    /**
     * constructor
     * @param scene of this case
     */
    public RayTracerBase(Scene scene) {
            _scene = scene;
        }

    /**
     *
     * @param ray from the camera to the intersection points
     * @return color in the intersection
     */
    public abstract Color traceRay(Ray ray);

    }

