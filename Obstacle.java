import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class Obstacle extends Entity{

    public Obstacle(Point position,
                  List<PImage> images)
    {
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }
}
