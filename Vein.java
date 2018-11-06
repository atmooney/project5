import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class Vein extends VeinOreSched{

    public Vein(Point position,
                  List<PImage> images,
                  int actionPeriod)
    {
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
    }
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(this.position);

        if (openPt.isPresent())
        {
            Ore ore = new Ore(openPt.get(), imageStore.getImageList(VirtualWorld.ORE_KEY), VirtualWorld.ORE_CORRUPT_MIN +
                    VirtualWorld.rand.nextInt(VirtualWorld.ORE_CORRUPT_MAX - VirtualWorld.ORE_CORRUPT_MIN));
            world.addEntity(ore);
            ore.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                createActivityAction(world, imageStore), actionPeriod);
    }
}
