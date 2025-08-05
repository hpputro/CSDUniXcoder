  public void execute(
          TransformerImpl transformer)
            throws TransformerException
  {

    if (transformer.getDebug())
      transformer.getTraceManager().fireTraceEvent(this);

    if (null != m_template)
    {
      XPathContext xctxt = transformer.getXPathContext();
      VariableStack vars = xctxt.getVarStack();

      int thisframe = vars.getStackFrame();
      int nextFrame = vars.link(m_template.m_frameSize);
      
                  if(m_template.m_inArgsSize > 0)
      {
        vars.clearLocalSlots(0, m_template.m_inArgsSize);
      
        if(null != m_paramElems)
        {
          int currentNode = xctxt.getCurrentNode();
          vars.setStackFrame(thisframe);
          int size = m_paramElems.length;
          
          for (int i = 0; i < size; i++) 
          {
            ElemWithParam ewp = m_paramElems[i];
            if(ewp.m_index >= 0)
            {
              if (transformer.getDebug())
                transformer.getTraceManager().fireTraceEvent(ewp);
              XObject obj = ewp.getValue(transformer, currentNode);
              if (transformer.getDebug())
                transformer.getTraceManager().fireTraceEndEvent(ewp);
              
                                                        vars.setLocalVariable(ewp.m_index, obj, nextFrame);
            }
          }
          vars.setStackFrame(nextFrame);
        }
      }
      
      SourceLocator savedLocator = xctxt.getSAXLocator();

      try
      {
        xctxt.setSAXLocator(m_template);

                transformer.pushElemTemplateElement(m_template);
        m_template.execute(transformer);
      }
      finally
      {
        transformer.popElemTemplateElement();
        xctxt.setSAXLocator(savedLocator);
                                                                                vars.unlink(thisframe);
      }
    }
    else
    {
      transformer.getMsgMgr().error(this, XSLTErrorResources.ER_TEMPLATE_NOT_FOUND,
                                    new Object[]{ m_templateName });      }
    
    if (transformer.getDebug())
	  transformer.getTraceManager().fireTraceEndEvent(this); 

  }