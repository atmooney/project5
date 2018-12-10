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
        scheduler.scheduleEvent(this,
                createActivityAction(world, imageStore),
                nextPeriod);
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler){
        return true;
    }
}

