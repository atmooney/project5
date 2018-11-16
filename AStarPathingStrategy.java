import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AStarPathingStrategy implements PathingStrategy {


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors){
        List<Node> open = new ArrayList<>();
        List<Node> closed = new ArrayList<>();
        List<Point> closedPoints = new ArrayList<>();
        List<Point> openPoints = new ArrayList<>();
        Node starting = new Node(0, distance(start, end), start);
        open.add(starting);
        openPoints.add(start);
        Node currentNode = starting;
        HashMap<Point, Point> path = new HashMap<>();
        while (!open.isEmpty()){
            Collections.sort(open, new sortByF());
            currentNode = open.get(0);
            if(withinReach.test(currentNode.getX(), end)){
                return pathConstructor(currentNode.getX(), path);
            }
            open.remove(currentNode);
            openPoints.remove(currentNode.getX());
            closed.add(currentNode);
            closedPoints.add(currentNode.getX());
            List<Point> neighbors =potentialNeighbors.apply(currentNode.getX())
                    .filter(canPassThrough).collect(Collectors.toList());
            for (Point p : neighbors){
                double tentativeG = currentNode.getG()+1;
                Node neighbor = new Node(tentativeG, distance(p, end), p);
                if(closedPoints.contains(p)){
                    continue;
                }
                else if (!(openPoints.contains(p))){
                    open.add(neighbor);
                    openPoints.add(p);
                }
                path.remove(neighbor.getX());
                path.put(neighbor.getX(), currentNode.getX());
            }
        }
        return new ArrayList<>();
    }

    private List<Point> pathConstructor(Point current, HashMap<Point, Point> path){
        List<Point> finalpath = new ArrayList<>();
        return pathConstructorHelper(current, path, finalpath);
    }

    private List<Point> pathConstructorHelper(Point current, HashMap<Point, Point> path, List<Point> finalpath){
        finalpath.add(0, current);
        if (!(path.keySet().contains(current))){
            finalpath.remove(0);
            return finalpath;
        }
        return pathConstructorHelper(path.get(current), path, finalpath);
    }

    class sortByF implements Comparator<Node>{
        public int compare(Node a, Node b){
            if (a.getF() < b.getF())
                return -1;
            else if (a.getF() > b.getF())
                return 1;
            return 0;
        }
    }

    private double distance(Point x, Point y){
        return (Math.sqrt(Math.pow(x.x-y.x, 2)+Math.pow(x.y-y.y, 2)));
    }
}
