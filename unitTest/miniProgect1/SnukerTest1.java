package miniProgect1;

import geometries.Polygon;
import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import renderer.Camera;
import renderer.ImageWriter;
import renderer.RayTracerBasic;
import scene.Scene;

import static java.awt.Color.*;

public class SnukerTest1 {
    private Scene scene = new Scene.SceneBuilder("Test scene").build();
    private Color colorWood=new Color(60,10,0);
    private Color colorGreen=new Color(10,135,47);

    @Test
    public void tableSnuker() {
        Camera camera = new Camera(new Point(0,800,800),
                new Vector(0, -1, -1),
                new Vector(0,-1,1)) //
                .setVPSize(150, 150)
                .setVPDistance(1000)
                .setRayTracer(new RayTracerBasic(scene));

        scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), 0.1));

        scene.getGeometries().add( //
                new Polygon(new Point(-45,20,35),
                        new Point(-45,-20,35),
                        new Point(45,-20,35),
                        new Point(45,20,35)).setEmission(colorGreen) //
                        .setMaterial(new Material().setKd(0.15).setKs(0.15).setShininess(100).setKt(0.3)),
                //middle polygon
                new Polygon(new Point(-50,-20,40),new Point(-50,20,40), new Point(-45,20,40),
                        new Point(-45,-20,40)).setEmission(colorWood) //
                        .setMaterial(new Material().setKd(0.1).setKs(0.1).setShininess(200).setKt(0.3)),

                new Polygon(new Point(50,-20,40),new Point(50,20,40), new Point(45,20,40),
                        new Point(45,-20,40)).setEmission(colorWood) //
                        .setMaterial(new Material().setKd(0.1).setKs(0.1).setShininess(200).setKt(0.3)),

                new Polygon(new Point(-50,20,40),new Point(-50,25,40), new Point(50,25,40),
                        new Point(50,20,40)).setEmission(colorWood) //
                        .setMaterial(new Material().setKd(0.1).setKs(0.1).setShininess(100).setKt(0.3)),


                new Polygon(new Point(-50,-25,40),new Point(-50,-20,40), new Point(50,-20,40),
                        new Point(50,-25,40)).setEmission(colorWood) //
                        .setMaterial(new Material().setKd(0.1).setKs(0.1).setShininess(100).setKt(0.3)),
                 //inside polygons

                new Polygon(new Point(-45,-20,40),new Point(-45,-20,35), new Point(45,-20,35),
                        new Point(45,-20,40)).setEmission(colorWood) //
                .setMaterial(new Material().setKd(0.1).setKs(0.1).setShininess(200).setKt(0.3)),

                new Polygon(new Point(-45,20,40),new Point(-45,20,35), new Point(-45,-20,35),
                        new Point(-45,-20,40)).setEmission(colorWood) //
                        .setMaterial(new Material().setKd(0.1).setKs(0.1).setShininess(200).setKt(0.3)),

                new Polygon(new Point(-45,20,40),new Point(45,20,40), new Point(45,20,35),
                        new Point(-45,20,35)).setEmission(colorWood) //
                        .setMaterial(new Material().setKd(0.1).setKs(0.1).setShininess(100).setKt(0.3)),

                new Polygon(new Point(45,-20,35),new Point(45,-20,40), new Point(45,20,40),
                        new Point(45,20,35)).setEmission(colorWood) //
                        .setMaterial(new Material().setKd(0.1).setKs(0.1).setShininess(100).setKt(0.3)),
                //outside polygons

                new Polygon(new Point(-50,-25,40),new Point(-50,-25,30), new Point(50,-25,30),
                        new Point(50,-25,40)).setEmission(colorWood) //
                        .setMaterial(new Material().setKd(0.1).setKs(0.1).setShininess(200).setKt(0.3)),

                new Polygon(new Point(50,25,40),new Point(50,25,30), new Point(-50,25,30),
                        new Point(-50,25,40)).setEmission(colorWood) //
                        .setMaterial(new Material().setKd(0.1).setKs(0.1).setShininess(200).setKt(0.3)),

                new Polygon(new Point(50,-25,40),new Point(50,-25,30), new Point(50,25,30),
                        new Point(50,25,40)).setEmission(colorWood) //
                        .setMaterial(new Material().setKd(0.1).setKs(0.1).setShininess(100).setKt(0.3)),

                new Polygon(new Point(-50,25,30),new Point(-50,25,40), new Point(-50,-25,40),
                        new Point(-50,-25,30)).setEmission(colorWood) //
                        .setMaterial(new Material().setKd(0.1).setKs(0.1).setShininess(100).setKt(0.3)),
               //triangles for legs of table

                //first leg
                new Polygon(new Point(50,-25,40),new Point(50,-17,35), new Point(-50,-25,0)).setEmission(colorWood),//

                new Polygon(new Point(42,-25,35),new Point(50,-25,40), new Point(50,-25,0)).setEmission(colorWood),
               //second leg
                new Polygon(new Point(50,17,35),new Point(50,17,40), new Point(50,25,0)).setEmission(colorWood),

                new Polygon(new Point(50,25,0),new Point(42,25,35), new Point(50,25,40)).setEmission(colorWood),

                 //third leg
                new Polygon(new Point(-50,-25,40),new Point(-42,-25,40), new Point(-50,-25,0)).setEmission(colorWood),

                new Polygon(new Point(-50,-25,40),new Point(-50,-25,0), new Point(-50,-17,35)).setEmission(colorWood),

                //forth leg
                new Polygon(new Point(-42,25,35),new Point(-50,25,40), new Point(-50,25,0)).setEmission(colorWood),

                new Polygon(new Point(-50,17,35),new Point(-50,25,40), new Point(-50,25,0)).setEmission(colorWood),
                //balls
                new Sphere(new Point(-30, 15, 37.5), 2.5d).setEmission(new Color(100, 20, 20)) //
                        .setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20)),

                 new Sphere(new Point(30, 5, 37.5), 2.5d).setEmission(new Color(BLUE)) //
                .setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20)),

                new Sphere(new Point(-10, 10, 37.5), 2.5d).setEmission(new Color(WHITE)) //
                        .setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20)),

                new Sphere(new Point(10, -8, 37.5), 2.5d).setEmission(new Color(PINK)) //
                        .setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20)),

                new Sphere(new Point(-20, -5, 37.5), 2.5d).setEmission(new Color(ORANGE)) //
                        .setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20)),

                new Sphere(new Point(40, -15, 37.5), 2.5d).setEmission(new Color(102,0,204)) //
                        .setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20)),
                //hall in the table
                new Polygon(new Point(50,25,0),new Point(42,25,35), new Point(50,25,40)).setEmission(Color.BLACK));
        scene.getLights().add( //
                new SpotLight(new Color(1000, 600, 0), new Point(-100, -100, 500), new Vector(-1, -1, -2)) //
                        .setKl(0.0004).setKq(0.0000006));

        camera.setImageWriter(new ImageWriter("tableOfSnuker", 500, 500)) //
                .setRayTracer(new RayTracerBasic(scene)) //
                .renderImage() //
                .writeToImage();
    }
}
