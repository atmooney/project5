
import java.util.List;

import processing.core.PImage;

abstract class Entity
{
   Point position;
   List<PImage> images;
   int imageIndex;
   Point getPosition(){return position;}
   List<PImage> getImages(){return images;}
   int getImageIndex(){return imageIndex;}
   void setPosition(Point p){position = p;}
}
