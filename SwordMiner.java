
import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class SwordMiner extends Miners{
    private int swordDurability = 2;

    public SwordMiner(Point position,
                     List<PImage> images, int actionPeriod, int animationPeriod)
    {
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
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
                world.removeEntity(swordTarget.get());
                scheduler.unscheduleAllEvents(swordTarget.get());
                nextPeriod += actionPeriod;
                swordDurability -= 1;
                if (swordDurability == 0){
                    MinerNotFull miner = new MinerNotFull(position, imageStore.getImageList(VirtualWorld.MINER_KEY), VirtualWorld.MINER_LIMIT,
                            0, actionPeriod, animationPeriod);
                    world.removeEntity(this);
                    scheduler.unscheduleAllEvents(this);
                    world.addEntity(miner);
                    miner.scheduleActions(scheduler, world, imageStore);
                    return;
                }
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
