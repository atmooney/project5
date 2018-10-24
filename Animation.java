public class Animation implements Action{

    AnimationEntity entity;
    WorldModel world;
    final ImageStore imageStore;
    final int repeatCount;

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
