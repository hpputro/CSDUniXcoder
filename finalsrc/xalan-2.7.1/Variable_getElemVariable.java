  public org.apache.xalan.templates.ElemVariable getElemVariable()
  {
  	
                        
    org.apache.xalan.templates.ElemVariable vvar = null;	
    org.apache.xpath.ExpressionNode owner = getExpressionOwner();

    if (null != owner && owner instanceof org.apache.xalan.templates.ElemTemplateElement)
    {

      org.apache.xalan.templates.ElemTemplateElement prev = 
        (org.apache.xalan.templates.ElemTemplateElement) owner;

      if (!(prev instanceof org.apache.xalan.templates.Stylesheet))
      {            
        while ( prev != null && !(prev.getParentNode() instanceof org.apache.xalan.templates.Stylesheet) )
        {
          org.apache.xalan.templates.ElemTemplateElement savedprev = prev;

          while (null != (prev = prev.getPreviousSiblingElem()))
          {
            if(prev instanceof org.apache.xalan.templates.ElemVariable)
            {
              vvar = (org.apache.xalan.templates.ElemVariable) prev;
            
              if (vvar.getName().equals(m_qname))
              {
                return vvar;
              }
              vvar = null; 	 	
            }
          }
          prev = savedprev.getParentElem();
        }
      }
      if (prev != null)
        vvar = prev.getStylesheetRoot().getVariableOrParamComposed(m_qname);
    }
    return vvar;
  }