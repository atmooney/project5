public class Node {
    private double g;
    private double h;
    private double f;
    private Point x;

    public Node(double g, double h,Point x){
        this.g = g;
        this.h = h;
        this.f = g+h;
        this.x = x;
    }

    public double getG(){return g;}
    public double getH(){return h;}
    public double getF(){return f;}
    public Point getX(){return x;}
}
