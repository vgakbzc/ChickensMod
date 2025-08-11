package com.setycz.chickens.top;

import com.setycz.chickens.ChickensMod;
import com.setycz.chickens.block.packed_chicken.BlockPackedChicken;
import com.setycz.chickens.block.packed_chicken.TileEntityPackedChicken;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class TheOneProbeBlockPlugin implements IProbeInfoProvider {

    @Override
    public String getID() {
        return ChickensMod.MODID + ":chickens_block_plugin";
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo info, EntityPlayer player,
                             World world, IBlockState blockstate, IProbeHitData data){
        if(blockstate.getBlock() instanceof BlockPackedChicken
                && world.getTileEntity(data.getPos()) instanceof TileEntityPackedChicken) {
            BlockPackedChicken block = (BlockPackedChicken) blockstate.getBlock();
            TileEntityPackedChicken te = (TileEntityPackedChicken) world.getTileEntity(data.getPos());
            if (te.getMaxTime() > 5.0f) {
                if(te.getMaxTime() > 1e9) {
                    info.text("THIS GUNNA TAKE A WHILE");
                } else if (te.getMaxTime() > 80.0f) {
                    info.progress((int)Math.ceil(te.getEtaTime()/20), (int)Math.ceil(te.getMaxTime()/20), (new ProgressStyle()).suffix(
                            "s"
                    ));
                } else {
                    info.progress((int)te.getEtaTime(), (int)te.getMaxTime(), (new ProgressStyle()).suffix("tick"));
                }
            } else {
                info.text(String.format("Efficiency: %.2f/s", te.getEfficiency() / 20.0f));
            }
            info.text(String.format("Current speed multiplier: %.1fx", te.getEfficiencyMultiplier()));
        }
    }

}
