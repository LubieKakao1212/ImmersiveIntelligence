package pl.pabilo8.immersiveintelligence.api.data.operators.arithmetic;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operators.DataOperator;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeNull;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationDivide extends DataOperator
{
	public DataOperationDivide()
	{
		name = "divide";
		sign = "/";
		allowedType1 = DataPacketTypeInteger.class;
		allowedType2 = DataPacketTypeInteger.class;
		expectedResult = DataPacketTypeInteger.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataPacketTypeExpression data)
	{
		DataPacketTypeInteger t1, t2;
		int i1, i2;

		t1 = getVarInType(DataPacketTypeInteger.class, data.getType1(), packet);
		t2 = getVarInType(DataPacketTypeInteger.class, data.getType2(), packet);
		i1 = t1.value;
		i2 = t2.value;

		//Yes
		return i2!=0?(new DataPacketTypeInteger(i1/i2)): new DataPacketTypeNull();
	}
}
