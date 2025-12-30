package com.waterfoxr.elementalenergistics;

import net.minecraftforge.common.ForgeConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
public final class EEConfig {

    public static final ForgeConfigSpec COMMON_SPEC;
    public static final CommonConfig COMMON;

    static {
        var commonPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
        COMMON_SPEC = commonPair.getRight();
        COMMON = commonPair.getLeft();
    }

    private EEConfig() {}

    public static final class CommonConfig {

        public final ForgeConfigSpec.IntValue amountPerOperation;
        public final ForgeConfigSpec.IntValue amountPerByte;
        public final ForgeConfigSpec.IntValue meContainerCapacity;
        public final ForgeConfigSpec.IntValue meContainerTickToLoad;

        private CommonConfig(ForgeConfigSpec.Builder builder) {
            amountPerOperation = builder.comment("The amount of element handled per operation, e.g. when inserting or extracting.")
                    .defineInRange("amount_per_operation", 100, 1, 10_000);
            amountPerByte = builder.comment("The amount of element that a storage cell can hold per byte.")
                    .defineInRange("amount_per_byte", 64, 1, 10_000);
            meContainerCapacity = builder.comment("设定ME元素容器的最大容量")
                    .defineInRange("me_container_capacity", 500_000, 1, 100_000_000);
            meContainerTickToLoad = builder.comment("设定ME元素容器每多少tick尝试从ME网络抽取设定的元素(警告：过低的数值可能会导致性能问题)")
                    .defineInRange("me_container_tick_to_load", 20, 1, 100_000_000);
        }
    }
}