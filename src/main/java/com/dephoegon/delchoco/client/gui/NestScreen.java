package com.dephoegon.delchoco.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.inventory.NestContainer;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class NestScreen extends AbstractContainerScreen<NestContainer> {
    private final static ResourceLocation TEXTURE = new ResourceLocation(DelChoco.DELCHOCO_ID, "textures/gui/chocobo_nest.png");
    private final static ResourceLocation TEXTURE_SHELTERED = new ResourceLocation(DelChoco.DELCHOCO_ID, "textures/gui/chocobo_nest_sheltered.png");

    public NestScreen(NestContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }
    public void render(@NotNull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }
    protected void renderBg(@NotNull PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, this.getMenu().getTile().isSheltered() ? TEXTURE_SHELTERED : TEXTURE);
        blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
    protected void renderLabels(@NotNull PoseStack matrixStack, int mouseX, int mouseY) { super.renderLabels(matrixStack, mouseX, mouseY); }
}