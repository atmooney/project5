import java.util.ArrayList;
import java.util.List;

abstract class Miners extends SchedThreeEntities{
    public Point nextPosition(WorldModel world,
                              Point destPos) {
        PathingStrategy strategy = new AStarPathingStrategy();
        List<Point> path = new ArrayList<>();
        path = strategy.computePath(position, destPos, p -> world.withinBounds(p) && !world.isOccupied(p), (p1, p2) -> adjacent(p1, p2), PathingStrategy.DIAGONAL_CARDINAL_NEIGHBORS);
        if (path.size() == 0) {
            return position;
        }
        return path.get(0);
    }
}
