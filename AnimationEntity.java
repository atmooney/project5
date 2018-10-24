public interface AnimationEntity {
    Action createAnimationAction(int repeatCount);
    void nextImage();
    int getAnimationPeriod();
}
