package cn.starlight.nightmare.util.item;

public enum ToolLevel {
    NONE(0),
    FLINT(1),
    WOOD(2),
    GOLD(3),
    COPPER(4),
    SILVER(4),
    OBSIDIAN(4),
    RUSTED_IRON(4),
    IRON(5),
    ANCIENT_METAL(6),
    DIAMOND(7),
    MITHRIL(8),
    NETHERITE(9),
    ADAMANTIUM(10);

    private final int level;

    ToolLevel(int level) {
        this.level = level;
    }

    public int level() {
        return level;
    }
}