package hillclimbing;

/**
 *
 * @author Hera
 */
public class StraightLine {
    
    private double a;
    private double b;
    private double c;
    
    public StraightLine(Position P, Position Q) {
        
        a = P.y - Q.y;
        b = Q.x - P.x;
        c = P.x * Q.y - Q.x * P.y;
        
    }
    
    public boolean pointsOnOppositeSide(Position P, Position Q) {
        
        double product1 = P.x * a + P.y * b + c;
        double product2 = Q.x * a + Q.y * b + c;
        
        if(product1 * product2 > 0) return false;
        else return true;
        
    }
    
    public static void main(String args[]) {
        
        StraightLine sl = new StraightLine(new Position(0, 4), new Position(4, 0));
        System.out.println(sl.pointsOnOppositeSide(new Position(0, 0), new Position(0, 10)));
        
    }
    
}
