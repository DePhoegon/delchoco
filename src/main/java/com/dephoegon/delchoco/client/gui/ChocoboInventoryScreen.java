package com.dephoegon.delchoco.client.gui;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.entities.Chocobo;
import com.dephoegon.delchoco.common.init.ModRegistry;
import com.dephoegon.delchoco.common.inventory.SaddleBagContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ChocoboInventoryScreen extends AbstractContainerScreen<SaddleBagContainer> {
    private static final ResourceLocation INV_TEXTURE_NULL = new ResourceLocation(DelChoco.DELCHOCO_ID, "textures/gui/chocobo_inventory_null.png");
    private static final ResourceLocation INV_TEXTURE_SMALL = new ResourceLocation(DelChoco.DELCHOCO_ID, "textures/gui/chocobo_inventory_small.png");
    private static final ResourceLocation INV_TEXTURE_LARGE = new ResourceLocation(DelChoco.DELCHOCO_ID, "textures/gui/chocobo_inventory_large.png");
    private static final int xAdjust = (4*18+16)+5; // (Additional Sizes for slots) + Border buffer
    private final Chocobo chocobo;

    public ChocoboInventoryScreen(SaddleBagContainer container, Inventory playerInventory, @NotNull Chocobo chocobo) {
        super(container, playerInventory, chocobo.getDisplayName());
        this.imageWidth = 176;
        this.imageHeight = 204;
        this.chocobo = chocobo;
    }
    public static void openInventory(int windowId, Chocobo chocobo) {
        Player player = Minecraft.getInstance().player;
        assert player != null;
        SaddleBagContainer saddleContainer = new SaddleBagContainer(windowId, player.getInventory(), chocobo);
        player.containerMenu = saddleContainer;
        Minecraft.getInstance().setScreen(new ChocoboInventoryScreen(saddleContainer, player.getInventory(), chocobo));
    }
    public void render(@NotNull GuiGraphics pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
    }
    protected void renderBg(@NotNull GuiGraphics matrixStack, float partialTicks, int x, int y) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        ItemStack saddleStack = chocobo.getSaddle();
        ResourceLocation TEXTURE = null;
        if(!saddleStack.isEmpty()){
            Item item = saddleStack.getItem();

            if(item == ModRegistry.CHOCOBO_SADDLE.get()) { TEXTURE = INV_TEXTURE_NULL; }
            else if(item == ModRegistry.CHOCOBO_SADDLE_BAGS.get()) { TEXTURE = INV_TEXTURE_SMALL; }
            else if(item == ModRegistry.CHOCOBO_SADDLE_PACK.get()) { TEXTURE = INV_TEXTURE_LARGE; }
        } else { TEXTURE =  INV_TEXTURE_NULL; }
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        TEXTURE = TEXTURE != null ? TEXTURE : INV_TEXTURE_NULL;
        matrixStack.blit(TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight);
        matrixStack.blit(TEXTURE, i - 24, j - 10, 0, 204, 27+xAdjust, 33);
    }
    protected void renderLabels(@NotNull GuiGraphics matrixStack, int x, int y) {
        matrixStack.drawString(this.font, this.chocobo.getDisplayName().getString(), xAdjust-16, 6, 0x888888);
        matrixStack.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 0x888888);
    }
}