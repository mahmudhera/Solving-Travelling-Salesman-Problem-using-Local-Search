package hillclimbing;

import java.io.*;
import java.util.*;
import javax.swing.JFrame;

/**
 *
 * @author Hera
 */
public class HillClimbing {
    
    private Position[] cityPositions;
    private static Random generator = new Random();
    private Route solution = null;
    
    public HillClimbing(Position[] pos) {
        cityPositions = pos;
        int N = cityPositions.length;
        int[] initialRoute = makeInitialRoute(N);
        
        int noImprovementCount = 0;
        int iterationCount = 0;
        
        int logN = (int)Math.log(N);
        Route current = new Route(initialRoute, cityPositions);
        
        while(true) {
            Route neighbour = current.minDistancedNeighbour();
            if(current.getDistanceValue() < neighbour.getDistanceValue()) {
                noImprovementCount++;
            } else {
                noImprovementCount = 0;
                current = neighbour;
            }
            if(noImprovementCount > N)
                break;
            if(iterationCount++ == Lib.sigma)
                break;
        }
        
        solution = current;
    }
    
    // will return null on erroneous computation
    public Route getSolutionRoute() {
        return solution;
    }
    
    // call it as  follows: makeInitialRoute(this.cityPositions.length)
    private int[] makeInitialRoute(int N) {
        boolean[] isOcuupied = new boolean[N];
        int[] route = new int[N];
        for(int i = 0; i < N; i++)
            isOcuupied[i] = false;
        int x = 0;
        while(x != N) {
            int cityIndex = HillClimbing.generator.nextInt(N);
            int cityNumber = cityIndex + 1;
            if(isOcuupied[cityIndex] == false) {
                route[x++] = cityNumber;
                isOcuupied[cityIndex] = true;
            }
        }
        return route;
    }
    
    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        
        Scanner in = new Scanner( new File(Lib.inputFileName) );
        in.nextLine(); in.nextLine(); in.nextLine();
        StringTokenizer st = new StringTokenizer(in.nextLine());
        st.nextToken();
        String str2 = st.nextToken();
        int N = Integer.parseInt(str2);
        in.nextLine(); in.nextLine();
        
        Position[] pos = new Position[N];
        while(true) {
            try {
                int cityNumber = in.nextInt();
                double x_coord = in.nextDouble();
                double y_coord = in.nextDouble();
                Position P = new Position(x_coord, y_coord);
                pos[cityNumber - 1] = P;
            } catch (Exception e) {
                break;
            }
        }
        
        HillClimbing solver = new HillClimbing(pos);
        Route r = solver.getSolutionRoute();
        double accumulatedCost = r.getDistanceValue();
        
        for(int i = 1; i < 20; i++) {
            solver = new HillClimbing(pos);
            accumulatedCost += solver.getSolutionRoute().getDistanceValue();
            if(solver.getSolutionRoute().getDistanceValue() < r.getDistanceValue()) r = solver.getSolutionRoute();
        }
        
        //System.out.printf("%.3f %.3f\n" , accumulatedCost / 20, r.getDistanceValue());
        System.out.printf("Cost: %.3f\nThe route: ", r.getDistanceValue());
        
        r.printRoute();
        GUI gui = new GUI(r, pos);
        gui.setVisible(true);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }
    
}
