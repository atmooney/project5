import processing.core.PImage;

import java.util.*;
import java.util.stream.Collectors;

final class WorldModel
{
   private int numRows;
   private int numCols;
   private final Background background[][];
   private final Entity occupancy[][];
   private Set<Entity> entities;

   public WorldModel(int numRows, int numCols, Background defaultBackground)
   {
      this.numRows = numRows;
      this.numCols = numCols;
      this.background = new Background[numRows][numCols];
      this.occupancy = new Entity[numRows][numCols];
      this.entities = new HashSet<>();

      for (int row = 0; row < numRows; row++)
      {
         Arrays.fill(this.background[row], defaultBackground);
      }
   }

   public List<Point> getEventRegion(Point origin)
   {
      List<Point> eventRegion = new ArrayList<>();
      for (int x = origin.x - 1; x <  origin.x + 2; x++)
         for (int y = origin.y - 1; y < origin.y + 2; y++)
            eventRegion.add(new Point(x, y));

      eventRegion = eventRegion.stream()
              .filter(p -> !isOccupied(p))
              .filter(p -> withinBounds(p))
              .collect(Collectors.toList());

      return eventRegion;
   }

   public int getNumRows(){return numRows;}
   public int getNumCols(){return numCols;}
   public Set<Entity> getEntities(){return entities;}
   public Optional<Point> findOpenAround(Point pos)
   {
      for (int dy = -VirtualWorld.ORE_REACH; dy <= VirtualWorld.ORE_REACH; dy++)
      {
         for (int dx = -VirtualWorld.ORE_REACH; dx <= VirtualWorld.ORE_REACH; dx++)
         {
            Point newPt = new Point(pos.x + dx, pos.y + dy);
            if (withinBounds(newPt) &&
                    !isOccupied(newPt))
            {
               return Optional.of(newPt);

            }
         }
      }

      return Optional.empty();
   }
   public void load(Scanner in, ImageStore imageStore)
   {
      int lineNumber = 0;
      while (in.hasNextLine())
      {
         try
         {
            if (!processLine(in.nextLine(), imageStore))
            {
               System.err.println(String.format("invalid entry on line %d",
                       lineNumber));
            }
         }
         catch (NumberFormatException e)
         {
            System.err.println(String.format("invalid entry on line %d",
                    lineNumber));
         }
         catch (IllegalArgumentException e)
         {
            System.err.println(String.format("issue on line %d: %s",
                    lineNumber, e.getMessage()));
         }
         lineNumber++;
      }
   }

   private boolean processLine(String line, ImageStore imageStore)
   {
      String[] properties = line.split("\\s");
      if (properties.length > 0)
      {
         switch (properties[VirtualWorld.PROPERTY_KEY])
         {
            case VirtualWorld.BGND_KEY:
               return parseBackground(properties, imageStore);
            case VirtualWorld.MINER_KEY:
               return parseMiner(properties, imageStore);
            case VirtualWorld.OBSTACLE_KEY:
               return parseObstacle(properties, imageStore);
            case VirtualWorld.ORE_KEY:
               return parseOre(properties, imageStore);
            case VirtualWorld.SMITH_KEY:
               return parseSmith(properties, imageStore);
            case VirtualWorld.VEIN_KEY:
               return parseVein(properties, imageStore);
         }
      }

      return false;
   }
   private boolean parseBackground(String [] properties, ImageStore imageStore)
   {
      if (properties.length == VirtualWorld.BGND_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[VirtualWorld.BGND_COL]),
                 Integer.parseInt(properties[VirtualWorld.BGND_ROW]));
         String id = properties[VirtualWorld.BGND_ID];
         setBackground(pt, new Background(id, imageStore.getImageList(id)));
      }

      return properties.length == VirtualWorld.BGND_NUM_PROPERTIES;
   }

   private boolean parseMiner(String [] properties, ImageStore imageStore)
   {
      if (properties.length == VirtualWorld.MINER_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[VirtualWorld.MINER_COL]),
                 Integer.parseInt(properties[VirtualWorld.MINER_ROW]));
         MinerNotFull miner = new MinerNotFull(pt,
                 imageStore.getImageList(VirtualWorld.MINER_KEY),
                 Integer.parseInt(properties[VirtualWorld.MINER_LIMIT]),
                 0,
                 Integer.parseInt(properties[VirtualWorld.MINER_ACTION_PERIOD]),
                 Integer.parseInt(properties[VirtualWorld.MINER_ANIMATION_PERIOD]));
         tryAddEntity(miner);
      }

      return properties.length == VirtualWorld.MINER_NUM_PROPERTIES;
   }
   private boolean parseObstacle(String [] properties, ImageStore imageStore)
   {
      if (properties.length == VirtualWorld.OBSTACLE_NUM_PROPERTIES)
      {
         Point pt = new Point(
                 Integer.parseInt(properties[VirtualWorld.OBSTACLE_COL]),
                 Integer.parseInt(properties[VirtualWorld.OBSTACLE_ROW]));
         Obstacle obstacle = new Obstacle(pt, imageStore.getImageList(VirtualWorld.OBSTACLE_KEY));
         tryAddEntity(obstacle);
      }

      return properties.length == VirtualWorld.OBSTACLE_NUM_PROPERTIES;
   }

   private boolean parseOre(String [] properties, ImageStore imageStore)
   {
      if (properties.length == VirtualWorld.ORE_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[VirtualWorld.ORE_COL]),
                 Integer.parseInt(properties[VirtualWorld.ORE_ROW]));
         Ore ore = new Ore(pt, imageStore.getImageList(VirtualWorld.ORE_KEY),
                 Integer.parseInt(properties[VirtualWorld.ORE_ACTION_PERIOD]));
         tryAddEntity(ore);
      }

      return properties.length == VirtualWorld.ORE_NUM_PROPERTIES;
   }

   private boolean parseSmith(String [] properties, ImageStore imageStore)
   {
      if (properties.length == VirtualWorld.SMITH_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[VirtualWorld.SMITH_COL]),
                 Integer.parseInt(properties[VirtualWorld.SMITH_ROW]));
         Blacksmith blacksmith = new Blacksmith(pt, imageStore.getImageList(VirtualWorld.SMITH_KEY));
         tryAddEntity(blacksmith);
      }

      return properties.length == VirtualWorld.SMITH_NUM_PROPERTIES;
   }
   private boolean parseVein(String [] properties, ImageStore imageStore)
   {
      if (properties.length == VirtualWorld.VEIN_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[VirtualWorld.VEIN_COL]),
                 Integer.parseInt(properties[VirtualWorld.VEIN_ROW]));
         Vein vein = new Vein(pt, imageStore.getImageList(VirtualWorld.VEIN_KEY),
                 Integer.parseInt(properties[VirtualWorld.VEIN_ACTION_PERIOD]));
         tryAddEntity(vein);
      }

      return properties.length == VirtualWorld.VEIN_NUM_PROPERTIES;
   }

   public void tryAddEntity(Entity entity)
   {
      if (isOccupied(entity.getPosition()))
      {
         // arguably the wrong type of exception, but we are not
         // defining our own exceptions yet
         throw new IllegalArgumentException("position occupied");
      }

      addEntity(entity);
   }
   public boolean withinBounds(Point pos)
   {
      return pos.y >= 0 && pos.y < this.numRows &&
              pos.x >= 0 && pos.x < this.numCols;
   }

   public boolean isOccupied(Point pos)
   {
      return withinBounds(pos) &&
              getOccupancyCell(pos) != null;
   }
   public void addEntity(Entity entity)
   {
      if (this.withinBounds(entity.getPosition()))
      {
         setOccupancyCell(entity.getPosition(), entity);
         this.entities.add(entity);
      }
   }

   public void moveEntity(Entity entity, Point pos)
   {
      Point oldPos = entity.getPosition();
      if (this.withinBounds(pos) && !pos.equals(oldPos))
      {
         setOccupancyCell(oldPos, null);
         removeEntityAt(pos);
         setOccupancyCell(pos, entity);
         entity.setPosition(pos);
      }
   }
   public void removeEntity(Entity entity)
   {
      removeEntityAt(entity.getPosition());
   }

   private void removeEntityAt(Point pos)
   {
      if (this.withinBounds(pos)
              && getOccupancyCell(pos) != null)
      {
         Entity entity = getOccupancyCell(pos);

         /* this moves the entity just outside of the grid for
            debugging purposes */
         entity.setPosition(new Point(-1, -1));
         this.entities.remove(entity);
         setOccupancyCell(pos, null);
      }
   }
   public Optional<PImage> getBackgroundImage(Point pos)
   {
      if (this.withinBounds(pos))
      {
         return Optional.of(ImageStore.getCurrentImage(getBackgroundCell(pos)));
      }
      else
      {
         return Optional.empty();
      }
   }

   private void setBackground(Point pos, Background background)
   {
      if (this.withinBounds(pos))
      {
         setBackgroundCell(pos, background);
      }
   }
   public Optional<Entity> getOccupant(Point pos)
   {
      if (this.isOccupied(pos))
      {
         return Optional.of(getOccupancyCell(pos));
      }
      else
      {
         return Optional.empty();
      }
   }

   private Entity getOccupancyCell(Point pos)
   {
      return occupancy[pos.y][pos.x];
   }

   private void setOccupancyCell(Point pos, Entity entity)
   {
      this.occupancy[pos.y][pos.x] = entity;
   }

   private Background getBackgroundCell(Point pos)
   {
      return background[pos.y][pos.x];
   }

   private void setBackgroundCell(Point pos, Background background)
   {
      this.background[pos.y][pos.x] = background;
   }
}
