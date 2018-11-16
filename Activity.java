public class Activity implements Action {

    private ActivityEntity entity;
    private WorldModel world;
    private final ImageStore imageStore;
    private final int repeatCount;

    public Activity(ActivityEntity entity, WorldModel world,
                     ImageStore imageStore, int repeatCount)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler)
    {
        entity.executeActivity(world, imageStore, scheduler);
    }
}
