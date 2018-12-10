import processing.core.PImage;

import java.util.List;

public class SnowBlock extends SchedThreeEntities {

    public SnowBlock(Point position,
                List<PImage> images,
                int actionPeriod, int animationPeriod) {
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler){ //turns SnowBlock to PLACEHOLDER
        Point pos = this.position;  // store current position before removing
        long nextPeriod = animationPeriod;
        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        Snowman snowman = new Snowman(pos, imageStore.getImageList(VirtualWorld.SNOWMAN_KEY), this.actionPeriod / VirtualWorld.SNOWMAN_PERIOD_SCALE,
                VirtualWorld.SNOWMAN_ANIMATION);

        world.addEntity(snowman);
        snowman.scheduleActions(scheduler, world, imageStore);
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler){
        return true;
    }
}

