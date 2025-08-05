






public interface JRElementDataset extends JRCloneable
{

	
	public byte getResetType();

	
	public ResetTypeEnum getResetTypeValue();

	
	public JRGroup getResetGroup();

	
	public byte getIncrementType();

	
	public IncrementTypeEnum getIncrementTypeValue();

	
	public JRGroup getIncrementGroup();

	
	public void collectExpressions(JRExpressionCollector collector);

	
	public JRDatasetRun getDatasetRun();
	
	
	public JRExpression getIncrementWhenExpression();

}
