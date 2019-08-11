package sample;

public class Triangle<T extends Number> extends Shape {
    public T a;
    public T b;
    public T c;
    Triangle()
    {
    }
    Triangle(T a1, T b1, T c1)
    {
        a = a1;
        b = b1;
        c = c1;
        System.out.println("Треугольник со сторонами "+ a + " " + b + " " + c + " создан");
    }
    public double[] getCoord()
    {
        double coords[] = new double[3];
//        coords[0] = radius.doubleValue();
//        coords[1] = x.doubleValue();
//        coords[2] = y.doubleValue();
        return coords;
    }
}
