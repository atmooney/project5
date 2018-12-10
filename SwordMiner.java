
import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class SwordMiner extends Miners{
    public SwordMiner(Point position,
                     List<PImage> images, int actionPeriod, int animationPeriod)
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
        Optional<Entity> swordTarget = findNearest(world,
                position, OreBlob.class);
        long nextPeriod = actionPeriod;

        if (swordTarget.isPresent()) {
            Point tgtPos = swordTarget.get().getPosition();

            if (moveTo(world, swordTarget.get(), scheduler)) {
                Quake quake = new Quake(tgtPos, imageStore.getImageList(VirtualWorld.QUAKE_KEY), VirtualWorld.QUAKE_ACTION_PERIOD, VirtualWorld.QUAKE_ANIMATION_PERIOD);

                world.addEntity(quake);
                nextPeriod += actionPeriod;
                quake.scheduleActions(scheduler, world, imageStore);
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
