import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public interface Entity
{
   /*public EntityKind kind;
   private final String id
   public Point position;
   private List<PImage> images;
   private int imageIndex;
   private final int resourceLimit;
   private int resourceCount;
   private final int actionPeriod;
   private final int animationPeriod;

   public Entity(EntityKind kind, String id, Point position,
      List<PImage> images, int resourceLimit, int resourceCount,
      int actionPeriod, int animationPeriod)
   {
      this.kind = kind;
      this.id = id;
      this.position = position;
      this.images = images;
      this.imageIndex = 0;
      this.resourceLimit = resourceLimit;
      this.resourceCount = resourceCount;
      this.actionPeriod = actionPeriod;
      this.animationPeriod = animationPeriod;
   }*/
   Point getPosition();
   List<PImage> getImages();
   int getImageIndex();
   void setPosition(Point p);/*

   {
      if (entity instanceof Background)
      {
         return ((Background)entity).images
                 .get(((Background)entity).imageIndex);
      }
      else if (entity instanceof Entity)
      {
         return ((Entity)entity).images.get(((Entity)entity).imageIndex);
      }
      else
      {
         throw new UnsupportedOperationException(
                 String.format("getCurrentImage not supported for %s",
                         entity));
      }
   }*/
   //int getAnimationPeriod();
   /*{
      switch (this.kind)
      {
         case MINER_FULL:
         case MINER_NOT_FULL:
         case ORE_BLOB:
         case QUAKE:
            return this.animationPeriod;
         default:
            throw new UnsupportedOperationException(
                    String.format("getAnimationPeriod not supported for %s",
                            this.kind));
      }
   }*/
   /*{
      this.imageIndex = (this.imageIndex + 1) % this.images.size();
   }
   public void executeMinerFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Entity> fullTarget = findNearest(world, this.position,
              EntityKind.BLACKSMITH);

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

   public void executeMinerNotFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Entity> notFullTarget = findNearest(world, this.position,
              EntityKind.ORE);

      if (!notFullTarget.isPresent() ||
              !this.moveToNotFull( world, notFullTarget.get(), scheduler) ||
              !this.transformNotFull(world, scheduler, imageStore))
      {
         scheduler.scheduleEvent(this,
                 createActivityAction(world, imageStore),
                 this.actionPeriod);
      }
   }
   public void executeOreActivity(WorldModel world,
                                         ImageStore imageStore, EventScheduler scheduler)
   {
      Point pos = this.position;  // store current position before removing

      world.removeEntity(this);
      scheduler.unscheduleAllEvents(this);

      Entity blob = createOreBlob(this.id + Functions.BLOB_ID_SUFFIX,
              pos, this.actionPeriod / Functions.BLOB_PERIOD_SCALE,
              Functions.BLOB_ANIMATION_MIN +
                      Functions.rand.nextInt(Functions.BLOB_ANIMATION_MAX - Functions.BLOB_ANIMATION_MIN),
              imageStore.getImageList(Functions.BLOB_KEY));

      world.addEntity(blob);
      blob.scheduleActions(scheduler, world, imageStore);
   }

   public void executeOreBlobActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
      Optional<Entity> blobTarget = findNearest(world,
              this.position, EntityKind.VEIN);
      long nextPeriod = this.actionPeriod;

      if (blobTarget.isPresent()) {
         Point tgtPos = blobTarget.get().position;

         if (this.moveToOreBlob(world, blobTarget.get(), scheduler)) {
            Entity quake = createQuake(tgtPos,
                    imageStore.getImageList(Functions.QUAKE_KEY));

            world.addEntity(quake);
            nextPeriod += this.actionPeriod;
            quake.scheduleActions(scheduler, world, imageStore);
         }
      }

      scheduler.scheduleEvent(this,
              createActivityAction(world, imageStore),
              nextPeriod);
   }
   public void executeQuakeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
   {
      scheduler.unscheduleAllEvents(this);
      world.removeEntity(this);
   }

   public void executeVeinActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Point> openPt = world.findOpenAround(this.position);

      if (openPt.isPresent())
      {
         Entity ore = WorldModel.createOre(Functions.ORE_ID_PREFIX + this.id,
                 openPt.get(), Functions.ORE_CORRUPT_MIN +
                         Functions.rand.nextInt(Functions.ORE_CORRUPT_MAX - Functions.ORE_CORRUPT_MIN),
                 imageStore.getImageList(Functions.ORE_KEY));
         world.addEntity(ore);
         ore.scheduleActions(scheduler, world, imageStore);
      }

      scheduler.scheduleEvent(this,
              createActivityAction(world, imageStore),
              this.actionPeriod);
   }*/
   //void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);
   /*{
      switch (this.kind)
      {
         case MINER_FULL:
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    this.actionPeriod);
            scheduler.scheduleEvent(this, createAnimationAction(0),
                    this.getAnimationPeriod());
            break;

         case MINER_NOT_FULL:
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    this.actionPeriod);
            scheduler.scheduleEvent(this,
                    createAnimationAction(0), this.getAnimationPeriod());
            break;

         case ORE:
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    this.actionPeriod);
            break;

         case ORE_BLOB:
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    this.actionPeriod);
            scheduler.scheduleEvent(this,
                    createAnimationAction( 0), this.getAnimationPeriod());
            break;

         case QUAKE:
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    this.actionPeriod);
            scheduler.scheduleEvent(this,
                    createAnimationAction(Functions.QUAKE_ANIMATION_REPEAT_COUNT),
                    this.getAnimationPeriod());
            break;

         case VEIN:
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    this.actionPeriod);
            break;

         default:
      }
   }
   private boolean transformNotFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
   {
      if (this.resourceCount >= this.resourceLimit)
      {
         Entity miner = createMinerFull(this.id, this.resourceLimit,
                 this.position, this.actionPeriod, this.animationPeriod,
                 this.images);

         world.removeEntity(this);
         scheduler.unscheduleAllEvents(this);

         world.addEntity(miner);
         miner.scheduleActions(scheduler, world, imageStore);

         return true;
      }

      return false;
   }

   private void transformFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
   {
      Entity miner = createMinerNotFull(this.id, this.resourceLimit,
              this.position, this.actionPeriod, this.animationPeriod,
              this.images);

      world.removeEntity(this);
      scheduler.unscheduleAllEvents(this);

      world.addEntity(miner);
      miner.scheduleActions(scheduler, world, imageStore);
   }
   private boolean moveToNotFull(WorldModel world, Entity target, EventScheduler scheduler)
   {
      if (adjacent(this.position, target.position))
      {
         this.resourceCount += 1;
         world.removeEntity(target);
         scheduler.unscheduleAllEvents(target);

         return true;
      }
      else
      {
         Point nextPos = this.nextPositionMiner(world, target.position);

         if (!this.position.equals(nextPos))
         {
            Optional<Entity> occupant = world.getOccupant(nextPos);
            if (occupant.isPresent())
            {
               scheduler.unscheduleAllEvents(occupant.get());
            }

           world.moveEntity(this, nextPos);
         }
         return false;
      }
   }

   private boolean moveToFull(WorldModel world,
                                    Entity target, EventScheduler scheduler)
   {
      if (adjacent(this.position, target.position))
      {
         return true;
      }
      else
      {
         Point nextPos = this.nextPositionMiner(world, target.position);

         if (!this.position.equals(nextPos))
         {
            Optional<Entity> occupant = world.getOccupant(nextPos);
            if (occupant.isPresent())
            {
               scheduler.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity(this, nextPos);
         }
         return false;
      }
   }
   private boolean moveToOreBlob(WorldModel world,
                                       Entity target, EventScheduler scheduler)
   {
      if (adjacent(this.position, target.position))
      {
         world.removeEntity(target);
         scheduler.unscheduleAllEvents(target);
         return true;
      }
      else
      {
         Point nextPos = this.nextPositionOreBlob(world, target.position);

         if (!this.position.equals(nextPos))
         {
            Optional<Entity> occupant = world.getOccupant(nextPos);
            if (occupant.isPresent())
            {
               scheduler.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity(this, nextPos);
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
   private Point nextPositionOreBlob(WorldModel world,
                                           Point destPos)
   {
      int horiz = Integer.signum(destPos.x - this.position.x);
      Point newPos = new Point(this.position.x + horiz,
              this.position.y);

      Optional<Entity> occupant = world.getOccupant(newPos);

      if (horiz == 0 ||
              (occupant.isPresent() && !(occupant.get().kind == EntityKind.ORE)))
      {
         int vert = Integer.signum(destPos.y - this.position.y);
         newPos = new Point(this.position.x, this.position.y + vert);
         occupant = world.getOccupant(newPos);

         if (vert == 0 ||
                 (occupant.isPresent() && !(occupant.get().kind == EntityKind.ORE)))
         {
            newPos = this.position;
         }
      }

      return newPos;
   }*/
   //Action createAnimationAction(int repeatCount);
   /*{
      return new Animation(this, null, null, repeatCount);
   }

   private Action createActivityAction(WorldModel world, ImageStore imageStore) {
      return new Activity(this, world, imageStore, 0);
   }

   private Entity createMinerFull(String id, int resourceLimit,
                                        Point position, int actionPeriod, int animationPeriod,
                                        List<PImage> images)
   {
      return new Entity(EntityKind.MINER_FULL, id, position, images,
              resourceLimit, resourceLimit, actionPeriod, animationPeriod);
   }
   public static Entity createMinerNotFull(String id, int resourceLimit,
                                           Point position, int actionPeriod, int animationPeriod,
                                           List<PImage> images)
   {
      return new Entity(EntityKind.MINER_NOT_FULL, id, position, images,
              resourceLimit, 0, actionPeriod, animationPeriod);
   }
   private Entity createOreBlob(String id, Point position,
                                      int actionPeriod, int animationPeriod, List<PImage> images)
   {
      return new Entity(EntityKind.ORE_BLOB, id, position, images,
              0, 0, actionPeriod, animationPeriod);
   }
   private Entity createQuake(Point position, List<PImage> images)
   {
      return new Entity(EntityKind.QUAKE, Functions.QUAKE_ID, position, images,
              0, 0, Functions.QUAKE_ACTION_PERIOD, Functions.QUAKE_ANIMATION_PERIOD);
   }
   private Optional<Entity> findNearest(WorldModel world, Point pos,
                                        EntityKind kind)
   {
      List<Entity> ofType = new LinkedList<>();
      for (Entity entity : world.entities)
      {
         if (entity.kind == kind)
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
         int nearestDistance = distanceSquared(nearest.position, pos);

         for (Entity other : entities)
         {
            int otherDistance = distanceSquared(other.position, pos);

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
   }*/
}
