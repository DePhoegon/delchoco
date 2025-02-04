package com.dephoegon.delchoco.common.handler.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class addItemModifiers extends LootModifier {
    public static final Supplier<Codec<addItemModifiers>> CODEC = Suppliers.memoize(()
            -> RecordCodecBuilder.create(inst -> codecStart(inst).and(ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(m -> m.item)).and(Codec.FLOAT.fieldOf("chance").forGetter(m -> m.chance)).and(Codec.INT.fieldOf("number").forGetter(m -> m.num)).apply(inst, addItemModifiers::new)));
    private final Item item;
    private final float chance;
    private final int num;
    /**
     * Constructs a LootModifier.
     *
     * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
     */
    public addItemModifiers(LootItemCondition[] conditionsIn, Item item, float chance, int number) {
        super(conditionsIn);
        this.item = item;
        this.chance = chance;
        this.num = number;
    }
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, @NotNull LootContext context) {
        if (context.getRandom().nextFloat() <= chance) { generatedLoot.add(new ItemStack(item, num)); }
        return generatedLoot;
    }
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}