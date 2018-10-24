import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class Ore implements Entity, ActivityEntity {
    private final String id;
    public Point position;
    private List<PImage> images;
    private int imageIndex;
    private final int actionPeriod;

    public Ore(String id, Point position,
                  List<PImage> images,
                  int actionPeriod)
    {
        this.id = id;
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

        OreBlob blob = new OreBlob(this.id + Functions.BLOB_ID_SUFFIX,
                pos, imageStore.getImageList(Functions.BLOB_KEY), this.actionPeriod / Functions.BLOB_PERIOD_SCALE,
                Functions.BLOB_ANIMATION_MIN +
                        Functions.rand.nextInt(Functions.BLOB_ANIMATION_MAX - Functions.BLOB_ANIMATION_MIN));

        world.addEntity(blob);
        blob.scheduleActions(scheduler, world, imageStore);
    }
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
   {
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    actionPeriod);
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
