package com.dephoegon.delchoco.common.events;

import com.dephoegon.delchoco.common.entities.Chocobo;
import com.dephoegon.delchoco.common.entities.properties.ChocoboColor;
import com.dephoegon.delchoco.common.items.ChocoDisguiseItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.scores.Team;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import static com.dephoegon.delchoco.aid.util.fallbackValues.*;
import static com.dephoegon.delchoco.common.configs.ChocoConfig.COMMON;
import static com.dephoegon.delchoco.common.init.ModRegistry.*;
import static com.dephoegon.delchoco.common.items.ChocoDisguiseItem.*;
import static com.dephoegon.delchoco.utils.RandomHelper.random;
import static net.minecraft.world.damagesource.DamageTypes.*;
import static net.minecraft.world.item.Items.*;

public class ChocoboCombatEffects {
    @SubscribeEvent
    public void onChocoboAttack(@NotNull LivingAttackEvent event) {
        Chocobo chocoboAttacker = event.getSource().getEntity() instanceof Chocobo choco ? choco : null;
        Chocobo chocoboTarget = event.getEntity() instanceof Chocobo choco ? choco : null;
        Player playerTarget = event.getEntity() instanceof Player player ? player : null;
        DamageSource damageSource = event.getSource();
        if (chocoboTarget != null && chocoboTarget.isTame()) {
            Player source = event.getSource().getEntity() instanceof Player play ? play : null;
            Player owner = chocoboTarget.getOwner() instanceof Player play ? play : null;
            Team group = owner != null ? owner.getTeam() : null;
            if (source != null) {
                boolean shift = ChocoConfigGet(COMMON.shiftBypassAllowed.get(), dShiftHitBypass) && source.isShiftKeyDown();
                boolean teams = group != null && source.getTeam() == group;
                if (!shift) {
                    if (!ChocoConfigGet(COMMON.ownChocoboHittable.get(), dOwnChocoboHittable)) {
                        event.setCanceled((owner == source) || teams);
                        return;
                    }
                    if (!ChocoConfigGet(COMMON.tamedChocoboHittable.get(), dTamedChocoboHittable)) {
                        event.setCanceled(true);
                        return;
                    }
                }
            }
        }
        if (chocoboTarget != null) {
            ChocoboColor color = chocoboTarget.getChocoboColor();
            if (ChocoConfigGet(COMMON.extraChocoboEffects.get(), dExtraChocoboEffects)) {
                if (damageSource.is(SWEET_BERRY_BUSH)) { event.setCanceled(true); return; }
                if (damageSource.is(FREEZE)) { event.setCanceled(color == ChocoboColor.WHITE || color == ChocoboColor.GOLD); return; }
                if (damageSource.is(DamageTypes.DRAGON_BREATH)) { event.setCanceled(color == ChocoboColor.PURPLE || color == ChocoboColor.GOLD); return; }
            }
            if (random.nextInt(100)+1 > 35) { chocoboTarget.spawnAtLocation(CHOCOBO_FEATHER.get()); }
        }
        if (chocoboAttacker != null && ChocoConfigGet(COMMON.chocoboResourcesOnHit.get(), dExtraChocoboResourcesOnHit)) {
            LivingEntity target = event.getEntity();
            if (target instanceof Spider e) { if (onHitMobChance(10)) { e.spawnAtLocation(STRING); } }
            if (target instanceof CaveSpider e) { if (onHitMobChance(5)) { e.spawnAtLocation(FERMENTED_SPIDER_EYE); } }
            if (target instanceof Skeleton e) { if (onHitMobChance(10)) { e.spawnAtLocation(BONE); } }
            if (target instanceof WitherSkeleton e) { if (onHitMobChance(10)) { e.spawnAtLocation(CHARCOAL); } }
            if (target instanceof IronGolem e) { if (onHitMobChance(5)) { e.spawnAtLocation(POPPY); } }
            if (target.getItemBySlot(EquipmentSlot.MAINHAND) != ItemStack.EMPTY) {
                if (onHitMobChance(30)) {
                    target.spawnAtLocation(target.getItemBySlot(EquipmentSlot.MAINHAND));
                    target.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                }
            }
            if (target.getItemBySlot(EquipmentSlot.OFFHAND) != ItemStack.EMPTY) {
                if (onHitMobChance(10)) {
                    target.spawnAtLocation(target.getItemBySlot(EquipmentSlot.OFFHAND));
                    target.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
                }
            }
        }
        if (playerTarget != null && ChocoConfigGet(COMMON.extraChocoboEffects.get(), dExtraChocoboEffects)) {
            ItemStack hStack = playerTarget.getItemBySlot(EquipmentSlot.HEAD);
            ItemStack cStack = playerTarget.getItemBySlot(EquipmentSlot.CHEST);
            ItemStack lStack = playerTarget.getItemBySlot(EquipmentSlot.LEGS);
            ItemStack fStack = playerTarget.getItemBySlot(EquipmentSlot.FEET);
            if (armorColorMatch(hStack, cStack, lStack, fStack)) {
                String headColor = getNBTKEY_COLOR(hStack);
                if (damageSource.is(WITHER)) {
                    event.setCanceled(headColor.equals(black) || headColor.equals(red) || headColor.equals(purple) || headColor.equals(gold) || headColor.equals(pink));
                    return;
                }
                if (damageSource.is(DamageTypes.DRAGON_BREATH)) {
                    event.setCanceled(headColor.equals(purple) || headColor.equals(gold));
                    return;
                }
                if (damageSource.is(SWEET_BERRY_BUSH)) {
                    event.setCanceled(true);
                    return;
                }
                if (damageSource.is(FREEZE)) {
                    event.setCanceled(headColor.equals(white) || headColor.equals(gold));
                }
            }
        }
    }
    @SubscribeEvent
    public void onChocoboKillOrDie(@NotNull LivingDeathEvent event) {
        Chocobo chocoboKill = event.getSource().getEntity() instanceof  Chocobo choco ? choco : null;
        Chocobo chocoboDie = event.getEntity() instanceof  Chocobo choco ? choco : null;
        if (chocoboKill != null && ChocoConfigGet(COMMON.chocoboResourcesOnKill.get(),dExtraChocoboResourcesOnKill)) {
            ChocoboColor color = chocoboKill.getChocoboColor();
            LivingEntity target = event.getEntity();
            if (target instanceof Spider) { if (.20f > (float) Math.random()) { target.spawnAtLocation(COBWEB); } }
            if (color == ChocoboColor.BLACK) { if (flowerChance()) {
                if (.50f > (float) Math.random()) { target.spawnAtLocation(WITHER_ROSE); }
                else { target.spawnAtLocation(DEAD_BUSH); }
            }}
            if (color == ChocoboColor.FLAME) {
                if (flowerChance()) {
                    if (.50f > (float) Math.random()) { target.spawnAtLocation(CRIMSON_FUNGUS); }  else { target.spawnAtLocation(WARPED_FUNGUS); }
                } else { if (.10f > (float) Math.random()) { target.spawnAtLocation(MAGMA_CREAM); } }
            }
            if (color == ChocoboColor.GREEN) { if (flowerChance()) {
                if (.34f > (float) Math.random()) { target.spawnAtLocation(SPORE_BLOSSOM); } else {
                    if (.51f > (float) Math.random()) { target.spawnAtLocation(SMALL_DRIPLEAF); }
                    else { target.spawnAtLocation(MOSS_BLOCK); }
                }
            }}
            if (color == ChocoboColor.WHITE) {
                if (flowerChance()) {
                    if (.34f > (float) Math.random()) { target.spawnAtLocation(SNOWBALL); } else {
                        if (.51f > (float) Math.random()) { target.spawnAtLocation(LILY_OF_THE_VALLEY); }
                        else { target.spawnAtLocation(OXEYE_DAISY); }
                    }
                } else if (.41f > (float) Math.random()) { target.spawnAtLocation(BONE_MEAL); }
            }
            if (color == ChocoboColor.GOLD) {
                if (flowerChance()) { target.spawnAtLocation(SUNFLOWER);}
                else { if (.03f > (float) Math.random()) { target.spawnAtLocation(GOLD_NUGGET); } }
            }
            if (color == ChocoboColor.BLUE) { if (flowerChance()) {
                if (.50f > (float) Math.random()) { target.spawnAtLocation(KELP); } else { target.spawnAtLocation(SEA_PICKLE); }
                if (.10f > (float) Math.random()) { target.spawnAtLocation(NAUTILUS_SHELL); }
            }}
            if (color == ChocoboColor.PINK) { if (flowerChance()) {
                if (.34f > (float) Math.random()) { target.spawnAtLocation(BROWN_MUSHROOM); } else {
                    if (.51f > (float) Math.random()) { target.spawnAtLocation(RED_MUSHROOM); }
                    else { target.spawnAtLocation(ALLIUM); }
                }
            }}
            if (color == ChocoboColor.RED) { if (flowerChance()) {
                if (.34f > (float) Math.random()) { target.spawnAtLocation(STICK); } else {
                    if (.51f > (float) Math.random()) { target.spawnAtLocation(BAMBOO); }
                    else { target.spawnAtLocation(VINE); }
                }
            }}
            if (color == ChocoboColor.PURPLE) {
                if (flowerChance()) { target.spawnAtLocation(CHORUS_FLOWER); }
                else if (.09f > (float) Math.random()) { target.spawnAtLocation(ENDER_PEARL); }
            }
            if (color == ChocoboColor.YELLOW) { if (flowerChance()) {
                Item flower = switch (random.nextInt(12)+1) {
                    default -> DANDELION;
                    case 2 -> POPPY;
                    case 3 -> BLUE_ORCHID;
                    case 4 -> ALLIUM;
                    case 5 -> AZURE_BLUET;
                    case 6 -> RED_TULIP;
                    case 7 -> ORANGE_TULIP;
                    case 8 -> WHITE_TULIP;
                    case 9 -> PINK_TULIP;
                    case 10 -> OXEYE_DAISY;
                    case 11 -> CORNFLOWER;
                    case 12 -> LILY_OF_THE_VALLEY;
                };
                target.spawnAtLocation(flower);
            }}
        }
        if (chocoboDie != null) {
            @NotNull ItemStack egg = switch (chocoboDie.getChocoboColor()) {
                case YELLOW -> new ItemStack(YELLOW_CHOCOBO_SPAWN_EGG.get());
                case WHITE -> new ItemStack(WHITE_CHOCOBO_SPAWN_EGG.get());
                case GREEN -> new ItemStack(GREEN_CHOCOBO_SPAWN_EGG.get());
                case FLAME -> new ItemStack(FLAME_CHOCOBO_SPAWN_EGG.get());
                case BLACK -> new ItemStack(BLACK_CHOCOBO_SPAWN_EGG.get());
                case GOLD -> new ItemStack(GOLD_CHOCOBO_SPAWN_EGG.get());
                case BLUE -> new ItemStack(BLUE_CHOCOBO_SPAWN_EGG.get());
                case RED -> new ItemStack(RED_CHOCOBO_SPAWN_EGG.get());
                case PINK -> new ItemStack(PINK_CHOCOBO_SPAWN_EGG.get());
                case PURPLE -> new ItemStack(PURPLE_CHOCOBO_SPAWN_EGG.get());
            };
            if (random.nextInt(1000)+1 < 85) { chocoboDie.spawnAtLocation(egg); }
        }
    }
    private static boolean flowerChance() { return random.nextInt(100)+1 < 45; }
    private static boolean onHitMobChance(int percentChance) { return random.nextInt(100)+1 < percentChance; }
    @SubscribeEvent
    public void onPlayerTick(TickEvent.@NotNull PlayerTickEvent e) {
        Player player = e.player;
        ItemStack hStack = player.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack cStack = player.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack lStack = player.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack fStack = player.getItemBySlot(EquipmentSlot.FEET);
        if (player.tickCount % 60 == 0 && ChocoConfigGet(COMMON.extraChocoboEffects.get(), dExtraChocoboEffects)) {
            if (armorColorMatch(hStack, cStack, lStack, fStack)) {
                String headColor = getNBTKEY_COLOR(hStack);
                if (player.hasEffect(MobEffects.POISON) && (headColor.equals(green) || headColor.equals(black) || headColor.equals(gold))) { player.removeEffect(MobEffects.POISON); }
                if (headColor.equals(flame) || headColor.equals(gold)) { player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 500, 1, false, false, false)); }
            }
        }
    }
    private String getNBTKEY_COLOR(@NotNull ItemStack item) {
        CompoundTag tag = item.getTag();
        if (tag != null && tag.contains(NBTKEY_COLOR)) { return tag.getString(NBTKEY_COLOR); }
        else { return yellow; }
    }
    private boolean armorColorMatch(@NotNull ItemStack itemStack1, ItemStack itemStack2, ItemStack itemStack3, ItemStack itemStack4) {
        if (!(itemStack1.getItem() instanceof ChocoDisguiseItem) || !(itemStack2.getItem() instanceof ChocoDisguiseItem) || !(itemStack3.getItem() instanceof ChocoDisguiseItem) || !(itemStack4.getItem() instanceof ChocoDisguiseItem)) { return false; }
        return getNBTKEY_COLOR(itemStack1).equals(getNBTKEY_COLOR(itemStack2)) && getNBTKEY_COLOR(itemStack1).equals(getNBTKEY_COLOR(itemStack3)) && getNBTKEY_COLOR(itemStack1).equals(getNBTKEY_COLOR(itemStack4));
    }
}