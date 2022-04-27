package scene;

import lighting.AmbientLight;
import geometries.Geometries;
import lighting.LightSource;
import primitives.Color;
import primitives.Point;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * class that presents data structure representing geometries, textures,
 * lights, shading, and view point information
 */
public class Scene {

    private final String _name;
    private final Color _background;
    private final AmbientLight _ambientLight;
    private final Geometries _geometries;
    private List<LightSource> _lights;

    /**
     * constructor
     * @param builder with the information of scene
     */
    public Scene(SceneBuilder builder) {
        _name = builder._name;
        _background = builder._background;
        _ambientLight = builder._ambientLight;
        _geometries = builder._geometries;
    }
    //getters
    public String getName() {
        return _name;
    }

    public Color getBackground() {
        return _background;
    }

    public AmbientLight getAmbientLight() {
        return _ambientLight;
    }

    public Geometries getGeometries() {
        return _geometries;
    }

    /**
     * helping class of scene
     */
    public static class SceneBuilder {
        private final String _name;
        public Color _background=Color.BLACK;
        public AmbientLight _ambientLight= new AmbientLight();
        public Geometries _geometries= new Geometries();
        public List<LightSource> _lights =new LinkedList<>();
        private SceneBuilder _builder;
        //setters
        public SceneBuilder setBackground(Color background) {
            _background = background;
            return this;
        }
        public SceneBuilder setAmbientLight(AmbientLight ambientLight) {
            _ambientLight = ambientLight;
            return this;
        }

        public SceneBuilder setBuilder(SceneBuilder builder) {
            _builder = builder;
            return this;
        }

        public SceneBuilder setGeometries(Geometries geometries) {
            _geometries = geometries;
            return this;
        }
        public SceneBuilder setLights(List<LightSource> lights) {
            _lights = lights;
            return this;
        }
        public Geometries getGeometries() {
            return _geometries;
        }

        public SceneBuilder(String name) {
            _name = name;
        }

        /**
         * builder
         * @return a new scene by this
         */
        public Scene build() {
            return new Scene(this);
        }
    }


}
