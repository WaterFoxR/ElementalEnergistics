package com.waterfoxr.elementalenergistics.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;

public final class ModDataGeneration {

    public static void init(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(event.includeClient(), new ModLanguageProvider(output));
        generator.addProvider(event.includeClient(), new ModItemModelProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModModels(output, existingFileHelper));
        ModTags.initTagProviders(event.includeServer(), generator, output, existingFileHelper);
        generator.addProvider(event.includeServer(), new ModRecipeProvider(output));
    }
}