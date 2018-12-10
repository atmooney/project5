import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class Snowman extends SchedThreeEntities{

    public Snowman(Point position,
                   List<PImage> images,
                   int actionPeriod, int animationPeriod)
    {
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) { //finds a vein and turns into diamond vein

        Optional<Entity> snowmanTarget = findNearest(world, position,
                Vein.class);
        long nextPeriod = actionPeriod;

        if (snowmanTarget.isPresent()) {
            Point tgtPos = snowmanTarget.get().getPosition();

            if (moveTo(world, snowmanTarget.get(), scheduler)) {
                world.removeEntity(this);
                scheduler.unscheduleAllEvents(this);
                DiamondVein diamondVein = new DiamondVein(tgtPos, imageStore.getImageList(VirtualWorld.DIAMOND_VEIN_KEY), VirtualWorld.DIAMOND_VEIN_ACTION_PERIOD);

                world.addEntity(diamondVein);
                nextPeriod += actionPeriod;
                diamondVein.scheduleActions(scheduler, world, imageStore);
                return;
            }

        }
        scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    nextPeriod);
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (adjacent(position, target.getPosition()))
        {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else
        {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!position.equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }
    public Point nextPosition(WorldModel world, Point destPos)
    {
        PathingStrategy strategy = new AStarPathingStrategy();
        List<Point> path = new ArrayList<>();
        path = strategy.computePath(position, destPos, p -> world.withinBounds(p) && !world.isOccupied(p), (p1, p2) -> adjacent(p1, p2), PathingStrategy.DIAGONAL_CARDINAL_NEIGHBORS);
        if(path.size()==0){
            return position;
        }
        return path.get(0);
    }
}
