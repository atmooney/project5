import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class DiamondVein extends VeinOreSched{

    public DiamondVein(Point position,
                List<PImage> images,
                int actionPeriod)
    {
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
    }
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) //spawns a diamond
    {
        Optional<Point> openPt = world.findOpenAround(this.position);

        if (openPt.isPresent())
        {
            Diamond diamond = new Diamond(openPt.get(), imageStore.getImageList(VirtualWorld.DIAMOND_KEY), VirtualWorld.DIAMOND_CORRUPT_MIN +
                    VirtualWorld.rand.nextInt(VirtualWorld.DIAMOND_CORRUPT_MAX - VirtualWorld.DIAMOND_CORRUPT_MAX));
            world.addEntity(diamond);
            diamond.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                createActivityAction(world, imageStore), actionPeriod);
    }
}
