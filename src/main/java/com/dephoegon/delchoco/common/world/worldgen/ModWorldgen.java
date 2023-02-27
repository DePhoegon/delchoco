package com.dephoegon.delchoco.common.world.worldgen;

import com.dephoegon.delchoco.common.ChocoConfig;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ModWorldgen {
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void biomeLoadingEvent(BiomeLoadingEvent event) {
		BiomeGenerationSettingsBuilder builder = event.getGeneration();
		Biome.BiomeCategory category = event.getCategory();
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacements.PATCH_GYSAHL_NO_BIOME);
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, ModPlacements.PATCH_GYSAHL_UNDERGROUND);
	}
}
