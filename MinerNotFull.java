import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class MinerNotFull extends Miners{
    private final int resourceLimit;
    private int resourceCount;

    public MinerNotFull(Point position,
                  List<PImage> images, int resourceLimit, int resourceCount,
                  int actionPeriod, int animationPeriod)
    {
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget = findNearest(world, position, Diamond.class);
        if (notFullTarget == null)
            notFullTarget = findNearest(world, position, Ore.class);

        if (!notFullTarget.isPresent() ||
                !moveTo( world, notFullTarget.get(), scheduler) ||
                !transform(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    actionPeriod);
        }
    }
    private boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        if (resourceCount >= resourceLimit)
        {
            MinerFull miner = new MinerFull(position, images, resourceLimit, 0, actionPeriod, animationPeriod);

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (adjacent(position, target.getPosition()))
        {
            resourceCount += 1;
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

}
