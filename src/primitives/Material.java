package primitives;

public class Material {
    public Double3 _kD = new Double3(0d);
    public Double3 _kS = new Double3( 0d);
    public int _nShininess = 0;
    /**
     * Reflective coefficient
     */
    public Double3 _kr= new Double3(0d);
    /**
     * Transparency Coefficient
     */
    public Double3 _kt = new Double3(0d);

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
