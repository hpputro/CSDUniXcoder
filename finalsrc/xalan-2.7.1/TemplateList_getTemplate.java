  public ElemTemplate getTemplate(XPathContext xctxt,
                                int targetNode,
                                QName mode,
                                int maxImportLevel, int endImportLevel,
                                boolean quietConflictWarnings,
                                DTM dtm)
            throws TransformerException
  {

    TemplateSubPatternAssociation head = getHead(xctxt, targetNode, dtm);

    if (null != head)
    {
                        xctxt.pushNamespaceContextNull();
      xctxt.pushCurrentNodeAndExpression(targetNode, targetNode);
      try
      {
        do
        {
          if ( (maxImportLevel > -1) && (head.getImportLevel() > maxImportLevel))
          {
            continue;
          }
          if (head.getImportLevel()<= maxImportLevel - endImportLevel)
            return null;
          ElemTemplate template = head.getTemplate();        
          xctxt.setNamespaceContext(template);
          
          if ((head.m_stepPattern.execute(xctxt, targetNode) != NodeTest.SCORE_NONE)
                  && head.matchMode(mode))
          {
            if (quietConflictWarnings)
              checkConflicts(head, xctxt, targetNode, mode);

            return template;
          }
        }
        while (null != (head = head.getNext()));
      }
      finally
      {
        xctxt.popCurrentNodeAndExpression();
        xctxt.popNamespaceContext();
      }
    }

    return null;
  }