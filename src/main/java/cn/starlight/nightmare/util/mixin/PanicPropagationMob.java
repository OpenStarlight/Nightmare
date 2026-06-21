package cn.starlight.nightmare.util.mixin;

public interface PanicPropagationMob {
    boolean nightmare$canReceivePanic();

    void nightmare$startPanicPropagation();

    boolean nightmare$isPanicPropagationActive();
}
