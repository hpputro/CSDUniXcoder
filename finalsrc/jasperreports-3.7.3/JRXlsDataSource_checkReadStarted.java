	private void checkReadStarted()
	{
		if (recordIndex > 0)
		{
			throw new JRRuntimeException("Cannot modify data source properties after data reading has started.");
		}
	}