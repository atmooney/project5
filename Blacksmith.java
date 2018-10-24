import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class Blacksmith implements Entity{
    private final String id;
    public Point position;
    private List<PImage> images;
    private int imageIndex;

    public Blacksmith(String id, Point position,
                  List<PImage> images)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }
    public Point getPosition(){return position;}
    public List<PImage> getImages(){return images;}
    public void setPosition(Point p){
        position = p;
    }
    public int getImageIndex(){return imageIndex;}
}
