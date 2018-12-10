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

        if (snowmanTarget.isPresent() &&
                moveTo(world, snowmanTarget.get(), scheduler))
        {
            Point pos = this.position;  // store current position before removing

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            DiamondVein diamondVein = new DiamondVein(pos, imageStore.getImageList(VirtualWorld.SNOWMAN_KEY),
                    this.actionPeriod / VirtualWorld.SNOWMAN_PERIOD_SCALE);

            world.addEntity(diamondVein);
            diamondVein.scheduleActions(scheduler, world, imageStore);
        }
        else {
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    actionPeriod);
        }
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
