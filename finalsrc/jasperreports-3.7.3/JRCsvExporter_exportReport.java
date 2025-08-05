	public void exportReport() throws JRException
	{
		progressMonitor = (JRExportProgressMonitor)parameters.get(JRExporterParameter.PROGRESS_MONITOR);
		
		
		setOffset();

		
		setInput();
		
		if (!parameters.containsKey(JRExporterParameter.FILTER))
		{
			filter = createFilter(CSV_EXPORTER_PROPERTIES_PREFIX);
		}

		
		if (!isModeBatch)
		{
			setPageRange();
		}
		
		nature = new JRCsvExporterNature(filter);

		String encoding = 
			getStringParameterOrDefault(
				JRExporterParameter.CHARACTER_ENCODING, 
				JRExporterParameter.PROPERTY_CHARACTER_ENCODING
				);
		
		delimiter = 
			getStringParameterOrDefault(
				JRCsvExporterParameter.FIELD_DELIMITER, 
				JRCsvExporterParameter.PROPERTY_FIELD_DELIMITER
				);
		
		recordDelimiter = 
			getStringParameterOrDefault(
				JRCsvExporterParameter.RECORD_DELIMITER, 
				JRCsvExporterParameter.PROPERTY_RECORD_DELIMITER
				);
		
		StringBuffer sb = (StringBuffer)parameters.get(JRExporterParameter.OUTPUT_STRING_BUFFER);
		if (sb != null)
		{
			try
			{
				writer = new StringWriter();
				exportReportToWriter();
				sb.append(writer.toString());
			}
			catch (IOException e)
			{
				throw new JRException("Error writing to StringBuffer writer : " + jasperPrint.getName(), e);
			}
			finally
			{
				if (writer != null)
				{
					try
					{
						writer.close();
					}
					catch(IOException e)
					{
					}
				}
			}
		}
		else
		{
			writer = (Writer)parameters.get(JRExporterParameter.OUTPUT_WRITER);
			if (writer != null)
			{
				try
				{
					exportReportToWriter();
				}
				catch (IOException e)
				{
					throw new JRException("Error writing to writer : " + jasperPrint.getName(), e);
				}
			}
			else
			{
				OutputStream os = (OutputStream)parameters.get(JRExporterParameter.OUTPUT_STREAM);
				if (os != null)
				{
					try
					{
						writer = new OutputStreamWriter(os, encoding); 
						exportReportToWriter();
					}
					catch (IOException e)
					{
						throw new JRException("Error writing to OutputStream writer : " + jasperPrint.getName(), e);
					}
				}
				else
				{
					File destFile = (File)parameters.get(JRExporterParameter.OUTPUT_FILE);
					if (destFile == null)
					{
						String fileName = (String)parameters.get(JRExporterParameter.OUTPUT_FILE_NAME);
						if (fileName != null)
						{
							destFile = new File(fileName);
						}
						else
						{
							throw new JRException("No output specified for the exporter.");
						}
					}

					try
					{
						os = new FileOutputStream(destFile);
						writer = new OutputStreamWriter(os, encoding);
						exportReportToWriter();
					}
					catch (IOException e)
					{
						throw new JRException("Error writing to file writer : " + jasperPrint.getName(), e);
					}
					finally
					{
						if (writer != null)
						{
							try
							{
								writer.close();
							}
							catch(IOException e)
							{
							}
						}
					}
				}
			}
		}
	}