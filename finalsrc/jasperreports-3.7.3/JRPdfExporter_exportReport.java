	public void exportReport() throws JRException
	{
		registerFonts();

		progressMonitor = (JRExportProgressMonitor)parameters.get(JRExporterParameter.PROGRESS_MONITOR);

		
		setOffset();

		try
		{
			
			setExportContext();

			
			setInput();
	
			if (!parameters.containsKey(JRExporterParameter.FILTER))
			{
				filter = createFilter(PDF_EXPORTER_PROPERTIES_PREFIX);
			}

			
			if (!isModeBatch)
			{
				setPageRange();
			}

			isCreatingBatchModeBookmarks = 
				getBooleanParameter(
					JRPdfExporterParameter.IS_CREATING_BATCH_MODE_BOOKMARKS,
					JRPdfExporterParameter.PROPERTY_CREATE_BATCH_MODE_BOOKMARKS,
					false
					);

			forceSvgShapes = 				getBooleanParameter(
					JRPdfExporterParameter.FORCE_SVG_SHAPES,
					JRPdfExporterParameter.PROPERTY_FORCE_SVG_SHAPES,
					false
					);

			isCompressed = 
				getBooleanParameter(
					JRPdfExporterParameter.IS_COMPRESSED,
					JRPdfExporterParameter.PROPERTY_COMPRESSED,
					false
					);

			isEncrypted = 
				getBooleanParameter(
					JRPdfExporterParameter.IS_ENCRYPTED,
					JRPdfExporterParameter.PROPERTY_ENCRYPTED,
					false
					);

			is128BitKey = 
				getBooleanParameter(
					JRPdfExporterParameter.IS_128_BIT_KEY,
					JRPdfExporterParameter.PROPERTY_128_BIT_KEY,
					false
					);

			userPassword = 
				getStringParameter(
					JRPdfExporterParameter.USER_PASSWORD,
					JRPdfExporterParameter.PROPERTY_USER_PASSWORD
					);

			ownerPassword = 
				getStringParameter(
					JRPdfExporterParameter.OWNER_PASSWORD,
					JRPdfExporterParameter.PROPERTY_OWNER_PASSWORD
					);

			Integer permissionsParameter = (Integer)parameters.get(JRPdfExporterParameter.PERMISSIONS);
			if (permissionsParameter != null)
			{
				permissions = permissionsParameter.intValue();
			}

			pdfVersion = 
				getCharacterParameter(
						JRPdfExporterParameter.PDF_VERSION,
						JRPdfExporterParameter.PROPERTY_PDF_VERSION
						);

			fontMap = (Map) parameters.get(JRExporterParameter.FONT_MAP);

			setSplitCharacter();
			setHyperlinkProducerFactory();

			pdfJavaScript = 
				getStringParameter(
					JRPdfExporterParameter.PDF_JAVASCRIPT,
					JRPdfExporterParameter.PROPERTY_PDF_JAVASCRIPT
					);

			printScaling =
				getStringParameter(
					JRPdfExporterParameter.PRINT_SCALING,
					JRPdfExporterParameter.PROPERTY_PRINT_SCALING
					);

			tagHelper.setTagged( 
				getBooleanParameter(
					JRPdfExporterParameter.IS_TAGGED,
					JRPdfExporterParameter.PROPERTY_TAGGED,
					false
					)
				);
			
						collapseMissingBookmarkLevels = JRProperties.getBooleanProperty(jasperPrint, 
					JRPdfExporterParameter.PROPERTY_COLLAPSE_MISSING_BOOKMARK_LEVELS, false);

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