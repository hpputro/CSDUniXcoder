  private Hashtable getRefsTable()
  {
    if (m_refsTable == null) {
            m_refsTable = new Hashtable(89);

      KeyIterator ki = (KeyIterator) (m_keyNodes).getContainedIter();
      XPathContext xctxt = ki.getXPathContext();

      Vector keyDecls = getKeyDeclarations();
      int nKeyDecls = keyDecls.size();

      int currentNode;
      m_keyNodes.reset();
      while (DTM.NULL != (currentNode = m_keyNodes.nextNode()))
      {
        try
        {
          for (int keyDeclIdx = 0; keyDeclIdx < nKeyDecls; keyDeclIdx++) {
            KeyDeclaration keyDeclaration =
                (KeyDeclaration) keyDecls.elementAt(keyDeclIdx);
            XObject xuse =
                keyDeclaration.getUse().execute(xctxt,
                                                currentNode,
                                                ki.getPrefixResolver());

            if (xuse.getType() != xuse.CLASS_NODESET) {
              XMLString exprResult = xuse.xstr();
              addValueInRefsTable(xctxt, exprResult, currentNode);
            } else {
              DTMIterator i = ((XNodeSet)xuse).iterRaw();
              int currentNodeInUseClause;

              while (DTM.NULL != (currentNodeInUseClause = i.nextNode())) {
                DTM dtm = xctxt.getDTM(currentNodeInUseClause);
                XMLString exprResult =
                    dtm.getStringValue(currentNodeInUseClause);
                addValueInRefsTable(xctxt, exprResult, currentNode);
              }
            }
          }
        } catch (TransformerException te) {
          throw new WrappedRuntimeException(te);
        }
      }
    }
    return m_refsTable;
  }