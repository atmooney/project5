abstract class VeinOreSched extends ActivityEntity{
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this, createActivityAction(world, imageStore), actionPeriod);
    }
}
