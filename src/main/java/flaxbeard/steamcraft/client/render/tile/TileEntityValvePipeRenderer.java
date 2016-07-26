package flaxbeard.steamcraft.client.render.tile;

import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.block.BlockValvePipe;
import flaxbeard.steamcraft.client.render.RenderUtility;
import flaxbeard.steamcraft.tile.TileEntityValvePipe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class TileEntityValvePipeRenderer extends TileEntitySpecialRenderer<TileEntityValvePipe> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Steamcraft.MOD_ID, "textures/blocks/blockCopper.png");
    private static final ResourceLocation VALVE_RL = new ResourceLocation(Steamcraft.MOD_ID, "block/pipe_valve");

    @Override
    public void renderTileEntityAt(TileEntityValvePipe valve, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        World world = valve.getWorld();
        BlockPos pos = valve.getPos();
        IBlockState state = world.getBlockState(pos);
        EnumFacing facing = state.getValue(BlockValvePipe.FACING);
        float outset = 3.5F + (valve.open ? 0.0F : -1.0F) + (valve.open ? -0.1F * valve.turnTicks : 0.1F * valve.turnTicks);
        GlStateManager.translate(outset * facing.getFrontOffsetX() / 16F, outset * facing.getFrontOffsetY() / 16F,
          outset * facing.getFrontOffsetZ() / 16F);
        IBlockState actualState = state.getActualState(world, pos);

        // The no_connections model is actually slightly larger than the normal pipe, by a single cube in each direction
        // So we have to handle that here, by translating 1 cube in the direction of the valve.
        boolean anyConnections = actualState.getValue(BlockValvePipe.UP) || actualState.getValue(BlockValvePipe.DOWN) ||
          actualState.getValue(BlockValvePipe.EAST) || actualState.getValue(BlockValvePipe.WEST) ||
          actualState.getValue(BlockValvePipe.NORTH) || actualState.getValue(BlockValvePipe.SOUTH);
        if (!anyConnections) {
            GlStateManager.translate(facing.getFrontOffsetX() / 16F, facing.getFrontOffsetY() / 16F, facing.getFrontOffsetZ() / 16F);
        }
        GlStateManager.translate(0.5, 0.5, 0.5);
        switch (facing) {
            case UP: {
                GlStateManager.rotate(90, 0, 0, 1);
                break;
            }
            case DOWN: {
                GlStateManager.rotate(-90, 0, 0, 1);
                break;
            }
            case NORTH: {
                GlStateManager.rotate(90, 0, 1, 0);
                break;
            }
            case SOUTH: {
                GlStateManager.rotate(-90, 0, 1, 0);
                break;
            }
            case WEST: {
                GlStateManager.rotate(180, 0, 1, 0);
                break;
            }
        }
        GlStateManager.rotate((225.0F * (valve.isOpen() ? valve.turnTicks : 10 - valve.turnTicks) / 10.0F), 1, 0, 0);
        GlStateManager.translate(-0.5, -0.5, -0.5);

        Tessellator tess = Tessellator.getInstance();
        VertexBuffer buffer = tess.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
        IBakedModel valveModel = RenderUtility.bakeModel(VALVE_RL);
        for (BakedQuad quad : valveModel.getQuads(null, null, 0)) {
            buffer.addVertexData(quad.getVertexData());
        }
        bindTexture(TEXTURE);
        tess.draw();

        GlStateManager.popMatrix();
    }
}