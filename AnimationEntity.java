abstract class AnimationEntity  extends ActivityEntity{
    int animationPeriod;

    Action createAnimationAction(int repeatCount){return new Animation(this, null, null, repeatCount);}
    void nextImage(){this.imageIndex = (this.imageIndex + 1) % this.images.size();}
    int getAnimationPeriod(){return this.animationPeriod;}
}
