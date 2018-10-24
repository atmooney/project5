import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class Vein implements Entity, ActivityEntity{
    private final String id;
    public Point position;
    private List<PImage> images;
    private int imageIndex;
    private final int actionPeriod;

    public Vein(String id, Point position,
                  List<PImage> images,
                  int actionPeriod)
    {
        this.id = id;
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
            Ore ore = new Ore(Functions.ORE_ID_PREFIX + this.id,
                    openPt.get(), imageStore.getImageList(Functions.ORE_KEY), Functions.ORE_CORRUPT_MIN +
                    Functions.rand.nextInt(Functions.ORE_CORRUPT_MAX - Functions.ORE_CORRUPT_MIN));
            world.addEntity(ore);
            ore.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                createActivityAction(world, imageStore), actionPeriod);
    }
   public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
   {
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore), actionPeriod);
   }
    public Action createActivityAction(WorldModel world, ImageStore imageStore) {
        return new Activity(this, world, imageStore, 0);
    }
    public Point getPosition(){return position;}
    public void setPosition(Point p){
        position = p;
    }
    public List<PImage> getImages(){return images;}
    public int getImageIndex(){return imageIndex;}
}
