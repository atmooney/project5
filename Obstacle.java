import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class Obstacle implements Entity{
    private final String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;

    public Obstacle(String id, Point position,
                  List<PImage> images)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }
    public Point getPosition(){ return position;}
    public void setPosition(Point p){
        position = p;
    }
    public List<PImage> getImages(){return images;}
    public int getImageIndex(){return imageIndex;}
}
