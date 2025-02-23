package pl.pabilo8.immersiveintelligence.common.items.ammunition;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumFuseTypes;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleExplosion;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleUtils;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.bullet.ModelBulletMortar6bCal;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public class ItemIIAmmoMortar extends ItemIIBulletBase
{
	public ItemIIAmmoMortar()
	{
		super("mortar_6bCal", 1);
	}

	@Override
	public float getComponentMultiplier()
	{
		return 0.65f;
	}

	@Override
	public int getGunpowderNeeded()
	{
		return 350;
	}

	@Override
	public int getCoreMaterialNeeded()
	{
		return 3;
	}

	@Override
	public float getInitialMass()
	{
		return 1.125f;
	}

	@Override
	public float getDefaultVelocity()
	{
		return 8f;
	}

	@Override
	public float getCaliber()
	{
		return 6f;
	}

	@SideOnly(Side.CLIENT)
	@Override
	@Nonnull
	public Class<? extends IBulletModel> getModel()
	{
		return ModelBulletMortar6bCal.class;
	}

	@Override
	public float getDamage()
	{
		return 30;
	}

	@Override
	public ItemStack getCasingStack(int amount)
	{
		return Utils.getStackWithMetaName(IIContent.itemAmmoCasing, "mortar_6bCal", amount);
	}

	@Override
	public EnumCoreTypes[] getAllowedCoreTypes()
	{
		return new EnumCoreTypes[]{EnumCoreTypes.PIERCING, EnumCoreTypes.SHAPED, EnumCoreTypes.CANISTER};
	}

	@Override
	public EnumFuseTypes[] getAllowedFuseTypes()
	{
		return new EnumFuseTypes[]{EnumFuseTypes.CONTACT, EnumFuseTypes.TIMED, EnumFuseTypes.PROXIMITY};
	}

	@Override
	public float getSupressionRadius()
	{
		return 3;
	}

	@Override
	public int getSuppressionPower()
	{
		return 20;
	}

	@Override
	public boolean shouldLoadChunks()
	{
		return true;
	}

	@Override
	public void registerSprites(TextureMap map)
	{
		ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/bullets/ammo/"+NAME.toLowerCase()+"/core");
		ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/bullets/ammo/"+NAME.toLowerCase()+"/paint");

		for(EnumCoreTypes coreType : getAllowedCoreTypes())
		{
			ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/bullets/ammo/"+getName().toLowerCase()+"/"+coreType.getName());
			ApiUtils.getRegisterSprite(map, ImmersiveIntelligence.MODID+":items/bullets/ammo/"+getName().toLowerCase()+"/base_"+coreType.getName());
		}

	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColourForIEItem(ItemStack stack, int pass)
	{
		switch(stack.getMetadata())
		{
			case BULLET:
			{
				switch(pass)
				{
					case 0:
						return getCore(stack).getColour();
					case 1:
						return 0xffffffff;
					case 2:
						return getPaintColor(stack);
				}
			}
			case CORE:
				return getCore(stack).getColour();
		}
		return 0xffffffff;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public List<ResourceLocation> getTextures(ItemStack stack, String key)
	{
		ArrayList<ResourceLocation> a = new ArrayList<>();
		if(stack.getMetadata()==BULLET)
		{
			a.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/ammo/"+NAME.toLowerCase()+"/"+getCoreType(stack).getName()));
			a.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/ammo/"+NAME.toLowerCase()+"/base_"+getCoreType(stack).getName()));
			if(getPaintColor(stack)!=-1)
				a.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/ammo/"+NAME.toLowerCase()+"/paint"));

		}
		else if(stack.getMetadata()==CORE)
		{
			a.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/ammo/"+NAME.toLowerCase()+"/core"));
			a.add(new ResourceLocation(ImmersiveIntelligence.MODID+":items/bullets/ammo/"+NAME.toLowerCase()+"/"+getCoreType(stack).getName()));
		}
		return a;
	}

	@SideOnly(Side.CLIENT)
	public void doPuff(EntityBullet bullet)
	{
		for(int i = 0; i < 20; i += 1)
		{
			Vec3d v = bullet.getBaseMotion().rotatePitch(-90f).rotateYaw(i/20f*360f);
			ParticleExplosion particle = new ParticleExplosion(ClientUtils.mc().world, bullet.posX, bullet.posY, bullet.posZ, v.x*0.125, v.y*0.0125, v.z*0.125, 3.25f);
			ParticleUtils.particleRenderer.addEffect(particle);
		}
	}
}
