protected void closeWorkbook(OutputStream os) throws JRException 
	{
		closeSheet();
		
		styleHelper.export();
		
		styleHelper.close();

		try
		{
			wbHelper.exportFooter();

			wbHelper.close();

			if ((imagesToProcess != null && imagesToProcess.size() > 0))
			{
				for(Iterator it = imagesToProcess.iterator(); it.hasNext();)
				{
					JRPrintElementIndex imageIndex = (JRPrintElementIndex)it.next();

					JRPrintImage image = getImage(jasperPrintList, imageIndex);
					JRRenderable renderer = image.getRenderer();
					if (renderer.getType() == JRRenderable.TYPE_SVG)
					{
						renderer =
							new JRWrappingSvgRenderer(
								renderer,
								new Dimension(image.getWidth(), image.getHeight()),
								ModeEnum.OPAQUE == image.getModeValue() ? image.getBackcolor() : null
								);
					}

					String mimeType = JRTypeSniffer.getImageMimeType(renderer.getImageType());
					if (mimeType == null)
					{
						mimeType = JRRenderable.MIME_TYPE_JPEG;
					}
					String extension = mimeType.substring(mimeType.lastIndexOf('/') + 1);
					
					String imageName = IMAGE_NAME_PREFIX + imageIndex.toString() + "." + extension;
					
					xlsxZip.addEntry(						new FileBufferedZipEntry(
							"xl/media/" + imageName,
							renderer.getImageData()
							)
						);
					
				}
			}

	
			relsHelper.exportFooter();

			relsHelper.close();
			
			ctHelper.exportFooter();
			
			ctHelper.close();

			xlsxZip.zipEntries(os);

			xlsxZip.dispose();
		}
		catch (IOException e)
		{
			throw new JRException(e);
		}
	}