package cn.starlight.nightmare.util.item;

public enum ToolLevel {
    NONE(0),
    FLINT(1),
    WOOD(2),
    GOLD(2),
    STONE(3),
    IRON(4),
    DIAMOND(5),
    NETHERITE(6);

    private final int level;

    ToolLevel(int level) {
        this.level = level;
    }

    public int level() {
        return level;
    }
}