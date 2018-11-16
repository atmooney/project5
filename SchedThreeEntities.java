import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

abstract class SchedThreeEntities extends AnimationEntity{
    void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                createActivityAction(world, imageStore),
                this.actionPeriod);
        scheduler.scheduleEvent(this, createAnimationAction( 0), this.getAnimationPeriod());
    }
    abstract boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);
   // abstract Point nextPosition(WorldModel world, Point destPos);
    public Optional<Entity> findNearest(WorldModel world, Point pos, Class lass)
    {
        List<Entity> ofType = new LinkedList<>();
        for (Entity entity : world.getEntities())
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
    public boolean adjacent(Point p1, Point p2)
    {
        return (p1.x == p2.x && Math.abs(p1.y - p2.y) == 1) ||
                (p1.y == p2.y && Math.abs(p1.x - p2.x) == 1);
    }
}
