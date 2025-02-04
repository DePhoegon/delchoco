package com.dephoegon.delchoco.common;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.entities.Chocobo;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = DelChoco.DELCHOCO_ID)
public class RidingEventHandler {
    @SubscribeEvent
    public static void onMountEntity(@NotNull EntityMountEvent event) {
        if (event.isMounting()) { return; }
        if (!event.getEntityBeingMounted().isAlive()) { return; }
        if (!(event.getEntityBeingMounted() instanceof Chocobo)) { return; }
        if (!event.getEntityBeingMounted().onGround()) { event.setCanceled(true); }
    }

    /* This Forcibly dismounts players that log out
     * when riding a chocobo to prevent them from
     * maintaining control over it upon logging back in
     */

    @SubscribeEvent
    public static void onPlayerDisconnect(@NotNull PlayerLoggedOutEvent event) {
        Player player = event.getEntity();
        if (player.getVehicle() != null) {
            Entity entityRide = player.getVehicle();
            if (entityRide instanceof Chocobo) { player.removeVehicle(); }
        }
    }
}
