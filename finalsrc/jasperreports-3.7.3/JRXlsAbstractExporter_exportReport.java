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
				filter = createFilter(XLS_EXPORTER_PROPERTIES_PREFIX);
			}
			
			
			if (!isModeBatch)
			{
				setPageRange();
			}

			setParameters();

			OutputStream os = (OutputStream)parameters.get(JRExporterParameter.OUTPUT_STREAM);
			if (os != null)
			{
				exportReportToStream(os);
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
					exportReportToStream(os);
					os.flush();
				}
				catch (IOException e)
				{
					throw new JRException("Error trying to export to file : " + destFile, e);
				}
				finally
				{
					if (os != null)
					{
						try
						{
							os.close();
						}
						catch(IOException e)
						{
						}
					}
				}
			}
		}
		finally
		{
			resetExportContext();
		}
	}