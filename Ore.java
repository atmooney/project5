import java.util.List;

import processing.core.PImage;

public class Ore extends VeinOreSched {

    public Ore(Point position,
                  List<PImage> images,
                  int actionPeriod)
    {
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
    }
    public void executeActivity(WorldModel world,
                                   ImageStore imageStore, EventScheduler scheduler)
    {
        Point pos = this.position;  // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        OreBlob blob = new OreBlob(pos, imageStore.getImageList(VirtualWorld.BLOB_KEY), this.actionPeriod / VirtualWorld.BLOB_PERIOD_SCALE,
                VirtualWorld.BLOB_ANIMATION_MIN +
                        VirtualWorld.rand.nextInt(VirtualWorld.BLOB_ANIMATION_MAX - VirtualWorld.BLOB_ANIMATION_MIN));

        world.addEntity(blob);
        blob.scheduleActions(scheduler, world, imageStore);
    }
}
