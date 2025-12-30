package com.waterfoxr.elementalenergistics.me.api.client;

import appeng.api.client.AEKeyRenderHandler;
import appeng.util.Platform;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import com.waterfoxr.elementalenergistics.me.api.stacks.ElementKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.joml.Matrix4f;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.event.TickHandler;

import java.util.ArrayList;
import java.util.List;

public class ElementStackRenderer implements AEKeyRenderHandler<ElementKey> {

    private static final ResourceLocation OUTER = ElementalCraft_createRL("textures/effect/source_outer.png");
    private static final ResourceLocation MIDDLE = ElementalCraft_createRL("textures/effect/source_middle.png");

    private static ResourceLocation ElementalCraft_createRL(String path){
        return ResourceLocation.fromNamespaceAndPath(ElementalCraftApi.MODID,path);
    }



    @Override
    public void drawInGui(Minecraft minecraft, GuiGraphics guiGraphics, int x, int y, ElementKey key) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(x, y, 0);
        draw(poseStack, 0, 0, 16, 16, 8, key);
        poseStack.popPose();
    }


    @Override
    public void drawOnBlockFace(
            PoseStack poseStack, MultiBufferSource buffers, ElementKey key, float scale, int combinedLight, Level level
    ) {
        poseStack.pushPose();
        scale -= 0.05f;
        float x0 = -scale / 2;
        float y0 = scale / 2;
        float x1 = scale / 2;
        float y1 = -scale / 2;
        draw(poseStack, x0, y0, x1, y1, 0, key);
        poseStack.popPose();
    }

    @Override
    public Component getDisplayName(ElementKey key) {
        return key.computeDisplayName();
    }

    private void draw(PoseStack poseStack, float x0, float y0, float x1, float y1, int center, ElementKey elementKey) {
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        ElementType elementType = (ElementType) elementKey.getPrimaryKey();
        RenderSystem.setShaderColor(elementType.getRed(), elementType.getGreen(), elementType.getBlue(), 1);

        poseStack.pushPose();

        poseStack.translate(center, center, 0.01);
        var angle = TickHandler.getTicksInGame() % 360;
        poseStack.mulPose(Axis.ZN.rotationDegrees(angle));
        poseStack.translate(-center, -center, 0);

        RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA
        );
        blit(poseStack, x0, y0, x1, y1, OUTER);

        poseStack.translate(center, center, 0);
        poseStack.mulPose(Axis.ZN.rotationDegrees(angle * 5));
        poseStack.translate(-center, -center, 0);

        RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA
        );
        blit(poseStack, x0, y0, x1, y1, MIDDLE);

        poseStack.popPose();

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(true);
    }

    private void blit(PoseStack poseStack, float x0, float y0, float x1, float y1, ResourceLocation texture) {
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        Matrix4f matrix = poseStack.last().pose();
        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();

        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.vertex(matrix, x0, y1, 0).uv(0, 1).endVertex();
        bufferBuilder.vertex(matrix, x1, y1, 0).uv(1, 1).endVertex();
        bufferBuilder.vertex(matrix, x1, y0, 0).uv(1, 0).endVertex();
        bufferBuilder.vertex(matrix, x0, y0, 0).uv(0, 0).endVertex();

        Tesselator.getInstance().end();
    }

    @Override
    public List<Component> getTooltip(ElementKey key) {
        var tooltip = new ArrayList<Component>();
        tooltip.add(getDisplayName(key));

        var modName = Platform.formatModName(key.getModId());
        if (!tooltip.get(0).getString().equals(modName)) {
            tooltip.add(Component.literal(modName));
        }
        return tooltip;
    }
}