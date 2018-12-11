import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class MinerNotFull extends Miners{
    private final int resourceLimit;
    private int resourceCount;
    private final int newResourceLimit;
    private int newResourceCount;

    public MinerNotFull(Point position,
                  List<PImage> images, int resourceLimit, int resourceCount,
                  int actionPeriod, int animationPeriod)
    {
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.newResourceCount = 0;
        this.newResourceLimit = 3;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget = findNearest(world, position, Diamond.class);
        if (notFullTarget.equals(Optional.empty()))
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
            MinerFull miner = new MinerFull(position, images, resourceLimit, 0, actionPeriod, animationPeriod, false);

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }
        if (newResourceCount >= newResourceLimit){
            MinerFull miner = new MinerFull(position, images, resourceLimit, 0, actionPeriod, animationPeriod, true);

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
            if(target instanceof Ore)
                resourceCount += 1;
            if(target instanceof Diamond)
                newResourceCount += 1;
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
