	public void exportReport() throws JRException
	{
		progressMonitor = (JRExportProgressMonitor)parameters.get(JRExporterParameter.PROGRESS_MONITOR);

		
		setOffset();

		try
		{
			
			setExportContext();

			
			setInput();

			if (!parameters.containsKey(JRExporterParameter.FILTER))
			{
				filter = createFilter(XMLSS_EXPORTER_PROPERTIES_PREFIX);
			}

			
			if (!isModeBatch)
			{
				setPageRange();
			}

			setParameters();

			nature = new JRXmlssExporterNature(filter, isIgnorePageMargins);
			pageOrientation = jasperPrint.getOrientationValue();

			StringBuffer sb = (StringBuffer)parameters.get(JRExporterParameter.OUTPUT_STRING_BUFFER);
			if (sb != null)
			{
				StringBuffer buffer = exportReportToBuffer();
				sb.append(buffer.toString());
			}
			else
			{
				Writer outWriter = (Writer)parameters.get(JRExporterParameter.OUTPUT_WRITER);
				if (outWriter != null)
				{
					try
					{
						exportReportToStream(outWriter);
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
							exportReportToStream(new OutputStreamWriter(os, encoding));
						}
						catch (Exception e)
						{
							throw new JRException("Error writing to OutputStream : " + jasperPrint.getName(), e);
						}
					}
					else
					{
						destFile = (File)parameters.get(JRExporterParameter.OUTPUT_FILE);
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

						exportReportToFile();
					}
				}
			}
		}
		finally
		{
			resetExportContext();
		}

	}