package primitives;

public class Material {
    public Double3 _kD = new Double3(0d);
    public Double3 _kS = new Double3( 0d);
    public int _nShininess = 0;
    public Double3 _kr= new Double3(0d);

    public Material setKr(Double3 kr) {
        _kr = kr;
        return this;
    }

    public Material setKt(Double3 kt) {
        _kt = kt;
        return this;
    }

    public Material setKr(double kr) {
        _kr = new Double3(kr);
        return this;
    }

    public Material setKt(double kt) {
        _kt =new Double3(kt);
        return this;
    }

    public Double3 _kt = new Double3(0d);


    public Material setKd(Double3 kD) {
        _kD = kD;
        return this;
    }

    public Material setKs(Double3 kS) {
        _kS = kS;
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
