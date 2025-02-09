package pl.pabilo8.immersiveintelligence.client.render.hans;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerSpiderEyes;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderLivingEvent;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.client.model.misc.ModelHansBiped;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;
import pl.pabilo8.immersiveintelligence.common.entity.hans.HansAnimations.HansLegAnimation;

/**
 * @author Pabilo8
 * @since 11.01.2021
 * <p>
 * Zhe Renderer for zhe Hans
 */
public class HansRenderer extends RenderLivingBase<EntityHans> implements IReloadableModelContainer<HansRenderer>
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(ImmersiveIntelligence.MODID, "textures/entity/hans.png");

	public HansRenderer(RenderManager renderManagerIn)
	{
		super(renderManagerIn, new ModelPlayer(0.0f, false), 0.5F);
		this.addLayer(new LayerHansEmotions(this));
		this.addLayer(new LayerHansTeamOverlay(this));
		this.addLayer(new LayerArrow(this));
		this.addLayer(new LayerBipedArmor(this));
		this.addLayer(new LayerHeldItem(this));
		subscribeToList("hans");
	}

	@Override
	protected void preRenderCallback(EntityHans entitylivingbaseIn, float partialTickTime)
	{
		GlStateManager.scale(0.9375F, 0.9375F, 0.9375F);
	}

	@Override
	public void doRender(EntityHans entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		/*
		if(net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Pre<>(entity, this, partialTicks, x, y, z)))
			return;
		 */

		double d0 = y;
		if(entity.prevLegAnimation==entity.legAnimation)
			d0 += getOffsetForPose(entity, entity.getLegAnimation());
		else
			d0 += MathHelper.clampedLerp(
					getOffsetForPose(entity, entity.prevLegAnimation),
					getOffsetForPose(entity, entity.getLegAnimation()),
					1f-MathHelper.clamp((entity.legAnimationTimer-partialTicks)/8f,0,1)
			);

		this.setModelVisibilities(entity);

		super.doRender(entity, x, d0, z, entityYaw, partialTicks);

	}

	private void setModelVisibilities(EntityHans clientPlayer)
	{
		ModelPlayer model = this.getMainModel();

		ItemStack itemstack = clientPlayer.getHeldItemMainhand();
		ItemStack itemstack1 = clientPlayer.getHeldItemOffhand();
		model.setVisible(true);
		model.isSneak = clientPlayer.isSneaking();
		ModelBiped.ArmPose modelbiped$armpose = ModelBiped.ArmPose.EMPTY;
		ModelBiped.ArmPose modelbiped$armpose1 = ModelBiped.ArmPose.EMPTY;

		if(!itemstack.isEmpty())
		{
			modelbiped$armpose = ModelBiped.ArmPose.ITEM;

			if(clientPlayer.getItemInUseCount() > 0)
			{
				EnumAction enumaction = itemstack.getItemUseAction();

				if(enumaction==EnumAction.BLOCK)
				{
					modelbiped$armpose = ModelBiped.ArmPose.BLOCK;
				}
				else if(enumaction==EnumAction.BOW)
				{
					modelbiped$armpose = ModelBiped.ArmPose.BOW_AND_ARROW;
				}
			}
		}

		if(!itemstack1.isEmpty())
		{
			modelbiped$armpose1 = ModelBiped.ArmPose.ITEM;

			if(clientPlayer.getItemInUseCount() > 0)
			{
				EnumAction enumaction1 = itemstack1.getItemUseAction();

				if(enumaction1==EnumAction.BLOCK)
				{
					modelbiped$armpose1 = ModelBiped.ArmPose.BLOCK;
				}
				// FORGE: fix MC-88356 allow offhand to use bow and arrow animation
				else if(enumaction1==EnumAction.BOW)
				{
					modelbiped$armpose1 = ModelBiped.ArmPose.BOW_AND_ARROW;
				}
			}
		}

		if(clientPlayer.getPrimaryHand()==EnumHandSide.RIGHT)
		{
			model.rightArmPose = modelbiped$armpose;
			model.leftArmPose = modelbiped$armpose1;
		}
		else
		{
			model.rightArmPose = modelbiped$armpose1;
			model.leftArmPose = modelbiped$armpose;
		}
	}

	@Override
	public ModelPlayer getMainModel()
	{
		return (ModelPlayer)super.getMainModel();
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(EntityHans entity)
	{
		return TEXTURE;
	}

	@Override
	protected void renderEntityName(EntityHans entityIn, double x, double y, double z, String name, double distanceSq)
	{
		super.renderEntityName(entityIn, x, y, z, entityIn.getDisplayName().getUnformattedText(), distanceSq);
	}

	@Override
	public void reloadModels()
	{
		this.mainModel = new ModelHansBiped(0.0f, false);
		this.layerRenderers.clear();
		this.addLayer(new LayerHansEmotions(this));
		this.addLayer(new LayerHansTeamOverlay(this));
		this.addLayer(new LayerArrow(this));
		this.addLayer(new LayerBipedArmor(this));
		this.addLayer(new LayerHansHeldItem(this));
	}

	public double getOffsetForPose(EntityHans entity, HansLegAnimation animation)
	{
		switch(animation)
		{
			case LYING:
				return -1.25D;
			case SQUATTING:
				return -0.385D;
			case SNEAKING:
				return -0.125D;
			case KNEELING:
				return -0.25D;
			case KAZACHOK:
			{
				float v = (entity.ticksExisted%22)/22f;
				float v1 = v < 0.5f?v*2f: 1f;
				float v2 = v > 0.5f?(v-0.5f)/0.5f: 0;

				return -0.325f+Utils.clampedLerp3Par(0, 0.0725f, 0, v1)+Utils.clampedLerp3Par(0, 0.0725f, 0, v2);
			}
			default:
				return 0;
		}
	}
}
