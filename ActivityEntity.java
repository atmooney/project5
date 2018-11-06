abstract class ActivityEntity extends Entity{
    int actionPeriod;
    abstract void executeActivity(WorldModel world,
                       ImageStore imageStore, EventScheduler scheduler);
    abstract void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);
    Action createActivityAction(WorldModel world, ImageStore imageStore){
        return new Activity(this, world, imageStore, 0);
    }
}
