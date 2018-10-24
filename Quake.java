import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class Quake implements Entity, ActivityEntity, AnimationEntity{
    private final String id;
    public Point position;
    private List<PImage> images;
    private int imageIndex;
    private final int actionPeriod;
    private final int animationPeriod;

    public Quake(String id, Point position,
                  List<PImage> images,
                  int actionPeriod, int animationPeriod)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public void nextImage()
    {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }
    public int getAnimationPeriod()
    {
        return this.animationPeriod;
    }
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                createActivityAction(world, imageStore),
                this.actionPeriod);
        scheduler.scheduleEvent(this,
                createAnimationAction(Functions.QUAKE_ANIMATION_REPEAT_COUNT),
                this.getAnimationPeriod());
    }
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }
    public Point getPosition(){return position;}
    public Action createAnimationAction(int repeatCount)
    {
        return new Animation(this, null, null, repeatCount);
    }
    public Action createActivityAction(WorldModel world, ImageStore imageStore) {
        return new Activity(this, world, imageStore, 0);
    }
    public void setPosition(Point p){
        position = p;
    }
    public List<PImage> getImages(){return images;}
    public int getImageIndex(){return imageIndex;}
}
