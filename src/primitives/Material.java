package primitives;

public class Material {
    public Double3 _kD=new Double3(0d,0d,0d);
    public Double3 _kS=new Double3(0d,0d,0d);
    public int _nShininess=0;

    public Material setkD(Double3 kD) {
        _kD = kD;
        return this;
    }

    public Material setkS(Double3 kS) {
        _kS = kS;
        return this;
    }

    public Material setkD(double kD) {
        _kD = new Double3(kD);
        return this;
    }

    public Material setkS(double kS) {
        _kS = new Double3(kS);
        return this;
    }

    public Material setShininess(int nShininess) {
        _nShininess = nShininess;
        return this;
    }
}
