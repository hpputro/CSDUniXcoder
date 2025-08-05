	public void service(
		HttpServletRequest request,
		HttpServletResponse response
		) throws IOException, ServletException
	{
		List jasperPrintList = BaseHttpServlet.getJasperPrintList(request);

		if (jasperPrintList == null)
		{
			throw new ServletException("No JasperPrint documents found on the HTTP session.");
		}
		
		int startPageIndex = -1;

		String startPageStr = request.getParameter(START_PAGE_INDEX_REQUEST_PARAMETER);
		try
		{
			startPageIndex = Integer.parseInt(startPageStr);
		}
		catch(Exception e)
		{
		}
		
		int endPageIndex = -1;

		String endPageStr = request.getParameter(END_PAGE_INDEX_REQUEST_PARAMETER);
		try
		{
			endPageIndex = Integer.parseInt(endPageStr);
		}
		catch(Exception e)
		{
		}
		
		int pageIndex = -1;

		String pageStr = request.getParameter(PAGE_INDEX_REQUEST_PARAMETER);
		try
		{
			pageIndex = Integer.parseInt(pageStr);
		}
		catch(Exception e)
		{
		}
		
		if (pageIndex >= 0)
		{
			startPageIndex = pageIndex;
			endPageIndex = pageIndex;
		}
		
		Boolean isBuffered = Boolean.valueOf(request.getParameter(BaseHttpServlet.BUFFERED_OUTPUT_REQUEST_PARAMETER));
		if (isBuffered.booleanValue())
		{
			FileBufferedOutputStream fbos = new FileBufferedOutputStream();
			JRXmlExporter exporter = getExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
			if (startPageIndex >= 0)
			{
				exporter.setParameter(JRExporterParameter.START_PAGE_INDEX, Integer.valueOf(startPageIndex));
			}
			if (endPageIndex >= 0)
			{
				exporter.setParameter(JRExporterParameter.END_PAGE_INDEX, Integer.valueOf(endPageIndex));
			}
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fbos);

			try 
			{
				exporter.exportReport();
				fbos.close();
			
				if (fbos.size() > 0)
				{
					response.setContentType("text/xml");
					response.setHeader("Content-Disposition", "inline; filename=\"file.jrpxml\"");
					response.setContentLength(fbos.size());
					ServletOutputStream ouputStream = response.getOutputStream();
	
					try
					{
						fbos.writeData(ouputStream);
						fbos.dispose();
						ouputStream.flush();
					}
					finally
					{
						if (ouputStream != null)
						{
							try
							{
								ouputStream.close();
							}
							catch (IOException ex)
							{
							}
						}
					}
				}
			} 
			catch (JRException e) 
			{
				throw new ServletException(e);
			}
			finally
			{
				fbos.close();
				fbos.dispose();
			}
		}
		else
		{
			response.setContentType("text/xml");
			response.setHeader("Content-Disposition", "inline; filename=\"file.jrpxml\"");

			JRXmlExporter exporter = getExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
			if (startPageIndex >= 0)
			{
				exporter.setParameter(JRExporterParameter.START_PAGE_INDEX, Integer.valueOf(startPageIndex));
			}
			if (endPageIndex >= 0)
			{
				exporter.setParameter(JRExporterParameter.END_PAGE_INDEX, Integer.valueOf(endPageIndex));
			}
			
			OutputStream ouputStream = response.getOutputStream();
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);

			try 
			{
				exporter.exportReport();
			} 
			catch (JRException e) 
			{
				throw new ServletException(e);
			}
			finally
			{
				if (ouputStream != null)
				{
					try
					{
						ouputStream.close();
					}
					catch (IOException ex)
					{
					}
				}
			}
		}
	}