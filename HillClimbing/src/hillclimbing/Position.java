package hillclimbing;

/**
 *
 * @author Hera
 */
public class Position {
    
    double x;
    double y;
    
    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public double distance(Position p) {
        return Math.sqrt( (double) ( (x - p.x)*(x - p.x) + (y - p.y)*(y - p.y) ) );
    }
    
    public static double distance(Position p, Position q) {
        return Math.sqrt((p.x - q.x)*(p.x - q.x) + (p.y - q.y)*(p.y - q.y));
    }
    
    public void printPosition() {
        System.out.println(x + " " + y);
    }
    
}
