package eiteam.esteemedinnovation.armor.exosuit.plates;

import com.google.common.collect.ImmutableList;
import eiteam.esteemedinnovation.api.ChargableUtility;
import eiteam.esteemedinnovation.api.Constants;
import eiteam.esteemedinnovation.api.exosuit.ExosuitPlate;
import eiteam.esteemedinnovation.api.exosuit.UtilPlates;
import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.materials.refined.plates.ItemMetalPlate;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;

import static eiteam.esteemedinnovation.materials.MaterialsModule.METAL_PLATE;

public class ExosuitPlateZinc extends ExosuitPlate {
    public ExosuitPlateZinc() {
        super("Zinc", null, "zinc", "zinc", Constants.EI_MODID + ".plate.zinc");
    }

    private static final List<DamageSource> INVALID_SOURCES = ImmutableList.of(
      DamageSource.drown, DamageSource.outOfWorld, DamageSource.starve, DamageSource.wither);

    @Override
    public void onPlayerHurt(LivingHurtEvent event, EntityPlayer victim, ItemStack armorStack, EntityEquipmentSlot slot) {
        EntityLivingBase entity = event.getEntityLiving();
        float amount = event.getAmount();

        if (!INVALID_SOURCES.contains(event.getSource()) && ChargableUtility.hasPower(entity, Config.zincPlateConsumption)) {
            float health = victim.getHealth();
            float maxHealth = victim.getMaxHealth();
            float halfOfMax = maxHealth / 2;
            if (amount >= halfOfMax || health <= halfOfMax) {
                ItemStack zincPlates = new ItemStack(METAL_PLATE, 2, ItemMetalPlate.Types.ZINC_PLATE.getMeta());
                World world = victim.worldObj;
                ChargableUtility.drainSteam(victim.getItemStackFromSlot(EntityEquipmentSlot.CHEST), Config.zincPlateConsumption, victim);
                UtilPlates.removePlate(armorStack);
                EntityItem entityItem = new EntityItem(world, victim.posX, victim.posY, victim.posZ, zincPlates);
                world.spawnEntityInWorld(entityItem);
//                        player.setHealth(health - (amount - 10.0F));
                victim.setHealth(health);
                victim.performHurtAnimation();
                world.playSound(victim.posX, victim.posY, victim.posZ, EsteemedInnovation.SOUND_HISS,
                  SoundCategory.PLAYERS, 2F, 0.9F, false);
                event.setCanceled(true);
            }
        }
    }
}
