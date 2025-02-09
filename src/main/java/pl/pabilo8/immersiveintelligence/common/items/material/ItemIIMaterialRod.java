package pl.pabilo8.immersiveintelligence.common.items.material;

import pl.pabilo8.immersiveintelligence.common.items.ItemIIBase;

/**
 * @author Pabilo8
 * @since 2019-05-11
 */
public class ItemIIMaterialRod extends ItemIIBase
{
	public ItemIIMaterialRod()
	{
		super("material_rod", 64,
				"brass",
				"tungsten",
				"zinc",
				"platinum",
				"duraluminium"
		);

		setMetaHidden(2,3);
	}
}
