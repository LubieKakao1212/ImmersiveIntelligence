package pl.pabilo8.immersiveintelligence.api.utils.vehicles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.common.IIContent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8
 * @since 06.07.2020
 */
public interface IUpgradableMachine
{
	boolean addUpgrade(MachineUpgrade upgrade, boolean test);

	boolean hasUpgrade(MachineUpgrade upgrade);

	boolean upgradeMatches(MachineUpgrade upgrade);

	<T extends TileEntity & IUpgradableMachine> T getUpgradeMaster();

	void saveUpgradesToNBT(NBTTagCompound tag);

	void getUpgradesFromNBT(NBTTagCompound tag);

	@SideOnly(Side.CLIENT)
	void renderWithUpgrades(MachineUpgrade... upgrades);

	List<MachineUpgrade> getUpgrades();

	@Nullable
	MachineUpgrade getCurrentlyInstalled();

	int getInstallProgress();

	boolean addUpgradeInstallProgress(int toAdd);

	boolean resetInstallProgress();

	void startUpgrade(@Nonnull MachineUpgrade upgrade);

	void removeUpgrade(MachineUpgrade upgrade);

	default float getMaxClientProgress()
	{
		if(getCurrentlyInstalled()!=null)
			return Utils.getMaxClientProgress(getInstallProgress(), getCurrentlyInstalled().getProgressRequired(), getCurrentlyInstalled().getSteps());
		return getInstallProgress();
	}
}
