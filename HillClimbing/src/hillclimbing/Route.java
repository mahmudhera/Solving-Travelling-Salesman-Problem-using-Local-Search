package hillclimbing;

import java.util.*;

/**
 *
 * @author Hera
 */
public class Route {
    
    public int[] route;
    private double distanceValue = -1;
    private Position[] cityPositions;
    
    private static Random generator = new Random();
    
    // tested, OK
    public Route(int[] route, Position[] cityPositions) {
        
        int N = route.length;
        this.route = new int[N];
        for(int i = 0; i < N; i++)
            this.route[i] = route[i];
        
        this.cityPositions = cityPositions;
        
    }
    
    // tested, OK
    public Route(Route r) {
        
        int N = r.route.length;
        this.route = new int[N];
        for(int i = 0; i < N; i++)
            this.route[i] = r.route[i];
        
        this.cityPositions = r.cityPositions;
        
    }
    
    // return the neighbours inside a PQ. equal probability of the two opts
    private PriorityQueue<Route> neighbours() {
        
        PriorityQueue<Route> PQ;
        // with a 50% probability, generate the neighbours from 01Exchg, with the other 50%,
        // generate the neighbours with 2Opt strategy
        if(Route.generator.nextDouble() <= 0.50) {
            PQ = new PriorityQueue(new RouteComparator());
            int numberOfNeighbours = (int)Math.ceil( Math.log(this.route.length) );
            while(numberOfNeighbours-- != 0) {
                Route r = new Route(this);
                r.doSelf01Exchg();
                PQ.add(r);
            }
        } else {
            PQ = this.get2OptNeighbours();
        }
        
        return PQ;
    }
    
    // call neighbours(), pop the min, return it
    public Route minDistancedNeighbour() {
        PriorityQueue<Route> PQ = this.neighbours();
        Route r = PQ.remove();
        return r;
    }
    
    // tested, OK
    public double getDistanceValue() {
        if(this.distanceValue >= 0) return this.distanceValue;
        double estimate = 0;
        for(int i = 0; i < this.route.length - 1; i++) {
            int firstIndex = this.route[i] - 1;
            int secondIndex = this.route[i + 1] - 1;
            estimate += Position.distance(this.cityPositions[firstIndex], this.cityPositions[secondIndex]);
        }
        estimate += Position.distance(this.cityPositions[this.route[0] - 1], this.cityPositions[this.route[this.route.length - 1] - 1]);
        return this.distanceValue = estimate;
    }
    
    // change the route, do NOT change city positions, if distance not -1, make it -1
    // tested, OK
    private void doSelf01Exchg() {
        
        int N = this.route.length;
        
        int prevIndex, nextIndex;
        do {
            prevIndex = (int)(Route.generator.nextDouble() * N );
            nextIndex = (int)(Route.generator.nextDouble() * N );
        } while (prevIndex == nextIndex || (prevIndex == 0 && nextIndex == N-1));
        
        if(nextIndex > prevIndex) {
            int cityToMove = this.route[prevIndex];
            for(int i = prevIndex; i < nextIndex; i++) {
                this.route[i] = this.route[i + 1];
            }
            this.route[nextIndex] = cityToMove;
        } else if (nextIndex < prevIndex) {
            int cityToMove = this.route[prevIndex];
            for(int i = prevIndex; i > nextIndex; i--) {
                this.route[i] = this.route[i - 1];
            }
            this.route[nextIndex] = cityToMove;
        }
        if(this.distanceValue >= 0)
            this.distanceValue = -1;
    }
    
    // change the route, do NOT change city positions, if distance not -1, make it -1
    private PriorityQueue get2OptNeighbours() {
        PriorityQueue<Route> PQ  = new PriorityQueue(new RouteComparator());
        int N = this.route.length;
        for(int i = 0; i < N - 2; i++) {
            Position P = this.cityPositions[ this.route[i] - 1 ];
            Position Q = this.cityPositions[ this.route[i + 1] - 1 ];
            StraightLine line = new StraightLine(P, Q);
            for(int j = i + 1; j < N - 1; j++) {
                Position P_ = this.cityPositions[ this.route[j] - 1 ];
                Position Q_ = this.cityPositions[ this.route[j + 1] - 1 ];
                if(line.pointsOnOppositeSide(P_, Q_)) {
                    Route r = new Route(this);
                    for(int k = 0; k < (j - i) / 2; k++) {
                        int temp = r.route[i + 1 + k];
                        r.route[i + 1 + k] = r.route[j - k];
                        r.route[j - k] = temp;
                    }
                    if(r.distanceValue >= 0) 
                        r.distanceValue = -1;
                    PQ.add(r);
                }
            }
        }
        return PQ;
    }
    
    // tested, OK
    void printRoute() {
        int N = this.route.length;
        for(int i = 0; i < N; i++)
            System.out.print(this.route[i] + " ");
        System.out.println();
    }
    
    public static void main(String[] args) {
        Position[] pos = {new Position(0, 0), new Position(5, 1), new Position(6, 3), new Position(5, 6), 
                          new Position(2, 7), new Position(0, 3)};
        int[] route = {1, 2, 3, 4, 5, 6};
        Route r1 = new Route(route, pos);
        System.out.println(r1.getDistanceValue());
        
    }
    
}


class RouteComparator implements Comparator<Route> {
    
    @Override
    public int compare(Route A, Route B) {
        double estimForA = A.getDistanceValue();
        double estimForB = B.getDistanceValue();
        if(estimForA < estimForB)
            return -1;
        else if(estimForA > estimForB)
            return 1;
        else
            return 0;
    }
    
}