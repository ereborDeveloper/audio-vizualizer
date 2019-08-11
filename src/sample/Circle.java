package sample;

public class Circle<T extends Number> extends Shape {
    T radius;
    T x;
    T y;
    Circle()
    {
        radius = null;
    }
    Circle(T rad, T x, T y)
    {
        this.radius = rad;
        this.x = x;
        this.y = y;
//        System.out.println("Круг с радиусом " + this.radius + " создан");
    }
    public double[] getCoord()
    {
        double coords[] = new double[3];
        coords[0] = radius.doubleValue();
        coords[1] = x.doubleValue();
        coords[2] = y.doubleValue();
        return coords;
    }
}
