package primitives;

public class Material {
    private Double3 _kD = new Double3(0d);//Diffuse
    private Double3 _kS = new Double3( 0d);//Specular
    private Double3 _kt = new Double3(0d);//Transparency
    private Double3 _kr= new Double3(0d);//Reflection

    public Material setkG(double kG) {
        _kG = kG; //Math.pow(kG, 0.5;
        return this;
    }

    public double getkG() {return _kG;}

    private double _kG= 0d;//Glossy

    private int _nShininess = 0;

    public Double3 getkD() {
        return _kD;
    }

    public Double3 getkS() {
        return _kS;
    }

    public int getnShininess() {
        return _nShininess;
    }

    public Double3 getKr() {
        return _kr;
    }

    public Double3 getKt() {
        return _kt;
    }

    /**
     * Transparency Coefficient
     */


    /**
     * setter
     * @param kr reflective coefficient
     * @return material
     */
    public Material setKr(double kr) {
        _kr = new Double3(kr);
        return this;
    }

    /**
     * setter
     * @param kt transparency Coefficient
     * @return material
     */
    public Material setKt(double kt) {
        _kt =new Double3(kt);
        return this;
    }


    public Material setKd(double kD) {
        _kD = new Double3(kD);
        return this;
    }

    public Material setKs(double kS) {
        _kS = new Double3(kS);
        return this;
    }

    public Material setShininess(int nShininess) {
        _nShininess = nShininess;
        return this;
    }

}
