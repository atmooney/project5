import processing.core.PImage;
import java.util.Random;

import java.util.List;

public class SnowBlock extends SchedThreeEntities {

    public boolean willSpawnSnowman = false;

    public SnowBlock(Point position,
                List<PImage> images,
                int actionPeriod, int animationPeriod) {
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;

        Random rand = new Random();
        int n = rand.nextInt(9);
        if (n < 2)
            willSpawnSnowman = true;
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler){ //turns SnowBlock to PLACEHOLDER
        Point pos = this.position;  // store current position before removing
        long nextPeriod = animationPeriod;
        if (willSpawnSnowman) {
            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            Snowman snowman = new Snowman(pos, imageStore.getImageList(VirtualWorld.SNOWMAN_KEY), VirtualWorld.SNOWMAN_ACTION_PERIOD / VirtualWorld.SNOWMAN_PERIOD_SCALE,
                    VirtualWorld.SNOWMAN_ANIMATION);

            world.addEntity(snowman);
            snowman.scheduleActions(scheduler, world, imageStore);
        }
        else{
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    nextPeriod);
        }
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler){
        return true;
    }
}

