public class Animation implements Action{

    private AnimationEntity entity;
    private WorldModel world;
    private final ImageStore imageStore;
    private final int repeatCount;

    public Animation(AnimationEntity entity, WorldModel world,
                  ImageStore imageStore, int repeatCount)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    private void executeAnimationAction(EventScheduler scheduler)
    {
        entity.nextImage();

        if (this.repeatCount != 1)
        {
            scheduler.scheduleEvent((Entity)entity,
                    entity.createAnimationAction(Math.max(this.repeatCount - 1, 0)),
                    entity.getAnimationPeriod());
        }
    }

    public void executeAction(EventScheduler scheduler){
            this.executeAnimationAction(scheduler);
    }

}
