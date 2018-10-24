import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class MinerFull implements Entity, ActivityEntity, AnimationEntity{
    private final String id;
    public Point position;
    private List<PImage> images;
    private int imageIndex;
    private final int resourceLimit;
    private int resourceCount;
    private final int actionPeriod;
    private final int animationPeriod;

    public MinerFull(String id, Point position,
                  List<PImage> images, int resourceLimit, int resourceCount,
                  int actionPeriod, int animationPeriod)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public int getAnimationPeriod()
    {
        return this.animationPeriod;
    }
    public void nextImage()
    {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> fullTarget = findNearest(world, this.position,
                Blacksmith.class);

        if (fullTarget.isPresent() &&
                this.moveToFull(world, fullTarget.get(), scheduler))
        {
            this.transformFull(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    this.actionPeriod);
        }
    }
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
   {
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    this.actionPeriod);
            scheduler.scheduleEvent(this, createAnimationAction(0),
                    this.getAnimationPeriod());
   }
    private void transformFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        MinerNotFull miner = new MinerNotFull(this.id, this.position, this.images, this.resourceLimit, this.resourceCount, this.actionPeriod, this.animationPeriod);

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }
    private boolean moveToFull(WorldModel world,
                               Entity target, EventScheduler scheduler)
    {
        if (adjacent(this.position, target.getPosition()))
        {
            return true;
        }
        else
        {
            Point nextPos = this.nextPositionMiner(world, target.getPosition());

            if (!this.position.equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity((Entity)this, nextPos);
            }
            return false;
        }
    }
    private Point nextPositionMiner(WorldModel world,
                                    Point destPos)
    {
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz,
                this.position.y);

        if (horiz == 0 || world.isOccupied(newPos))
        {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x,
                    this.position.y + vert);

            if (vert == 0 || world.isOccupied(newPos))
            {
                newPos = this.position;
            }
        }

        return newPos;
    }
    public Action createAnimationAction(int repeatCount)
    {
        return new Animation(this, null, null, repeatCount);
    }
    public Action createActivityAction(WorldModel world, ImageStore imageStore) {
        return new Activity(this, world, imageStore, 0);
    }
    private Optional<Entity> findNearest(WorldModel world, Point pos,
                                         Class lass)
    {
        List<Entity> ofType = new LinkedList<>();
        for (Entity entity : world.entities)
        {
            if (lass.isInstance(entity))
            {
                ofType.add(entity);
            }
        }

        return nearestEntity(ofType, pos);
    }
    private Optional<Entity> nearestEntity(List<Entity> entities,
                                           Point pos)
    {
        if (entities.isEmpty())
        {
            return Optional.empty();
        }
        else
        {
            Entity nearest = entities.get(0);
            int nearestDistance = distanceSquared(nearest.getPosition(), pos);

            for (Entity other : entities)
            {
                int otherDistance = distanceSquared(other.getPosition(), pos);

                if (otherDistance < nearestDistance)
                {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }
    private int distanceSquared(Point p1, Point p2)
    {
        int deltaX = p1.x - p2.x;
        int deltaY = p1.y - p2.y;

        return deltaX * deltaX + deltaY * deltaY;
    }
    private boolean adjacent(Point p1, Point p2)
    {
        return (p1.x == p2.x && Math.abs(p1.y - p2.y) == 1) ||
                (p1.y == p2.y && Math.abs(p1.x - p2.x) == 1);
    }
    public Point getPosition(){return position;}
    public void setPosition(Point p){
        position = p;
    }
    public List<PImage> getImages(){return images;}
    public int getImageIndex(){return imageIndex;}
}
