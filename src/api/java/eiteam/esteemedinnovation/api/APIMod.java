package eiteam.esteemedinnovation.api;

import eiteam.esteemedinnovation.api.exosuit.ExosuitEventDelegator;
import eiteam.esteemedinnovation.api.steamnet.WorldLoadHandler;
import eiteam.esteemedinnovation.api.tool.ItemSteamTool;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

@Mod(modid = Constants.API_MODID, name = Constants.API_NAME, version = Constants.API_VERSION)
public class APIMod {
    File configDir;

    @Mod.Instance(Constants.API_MODID)
    public static APIMod INSTANCE;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        configDir = new File(event.getModConfigurationDirectory(), "EsteemedInnovation");
        APIConfig.load();
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new ItemSteamTool.ToolUpgradeEventDelegator());
        MinecraftForge.EVENT_BUS.register(new ExosuitEventDelegator());
        MinecraftForge.EVENT_BUS.register(new WorldLoadHandler());
    }
}
