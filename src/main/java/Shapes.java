
import java.util.ArrayList;

public class Shapes {
    private FunctionalInterface fc;
    private ArrayList<Shape> shapes;
    private double res;
    public static int size = 10;
    public <T extends Shape> void initShape() {
        shapes = new ArrayList<>();
        fc = (sh) -> {
            if (sh.getClass() == Triangle.class) {
                Triangle tr = (Triangle) sh;
                double p = (tr.a.doubleValue() + tr.b.doubleValue() + tr.c.doubleValue()) / 2;
                double check = p * (p - tr.a.doubleValue()) * (p - tr.b.doubleValue()) * (p - tr.c.doubleValue());
                if (check < 0) throw new Exception();
                res = Math.sqrt(check);
            }
            if (sh.getClass() == Circle.class) {
                Circle cr = (Circle) sh;
                res = Math.PI * cr.radius.doubleValue() * cr.radius.doubleValue();
            }
            return res;
        };
    }

    public void initTriangles() {
        for (int i = 0; i < 10; i++) {
            Triangle triangle = new Triangle((int) (Math.random() * 10), (int) (Math.random() * 10), (int) (Math.random() * 10));
            shapes.add(triangle);
        }
    }

    public void initCircles() {
        for (int i = 0; i < size; i++) {
            Circle circle = new Circle((int) (Math.random() * 10),(int) (Math.random() * 50),(int) (Math.random() * 50));
            shapes.add(circle);
        }
    }

    public void printArea() {
        shapes.forEach(shape ->
                {
                    try {
                        fc.getArea(shape);
                        System.out.print(shapes.indexOf(shape) + 1 + ". ");
                        if (shape.getClass() == Triangle.class) System.out.print("Площадь треугольника: ");
                        if (shape.getClass() == Circle.class) System.out.print("Площадь круга: ");
                        System.out.print(fc.getArea(shape));
                        System.out.println();
                    } catch (Exception e) {
//                            e.printStackTrace();
                        System.out.println(shapes.indexOf(shape) + 1 + ". Треугольник невозможно построить");
                    }
                }
        );
    }
    public ArrayList<Shape> getShapes(){
        return shapes;
    }

}
