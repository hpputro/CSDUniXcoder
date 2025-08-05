  boolean executePredicates(int context, XPathContext xctxt)
          throws javax.xml.transform.TransformerException
  {
    
    int nPredicates = getPredicateCount();
        if (nPredicates == 0)
      return true;

    PrefixResolver savedResolver = xctxt.getNamespaceContext();

    try
    {
      m_predicateIndex = 0;
      xctxt.pushSubContextList(this);
      xctxt.pushNamespaceContext(m_lpi.getPrefixResolver());
      xctxt.pushCurrentNode(context);

      for (int i = 0; i < nPredicates; i++)
      {
                XObject pred = m_predicates[i].execute(xctxt);
                        if (XObject.CLASS_NUMBER == pred.getType())
        {
          if (DEBUG_PREDICATECOUNTING)
          {
            System.out.flush();
            System.out.println("\n===== start predicate count ========");
            System.out.println("m_predicateIndex: " + m_predicateIndex);
                                    System.out.println("pred.num(): " + pred.num());
          }

          int proxPos = this.getProximityPosition(m_predicateIndex);
          int predIndex = (int) pred.num();
          if (proxPos != predIndex)
          {
            if (DEBUG_PREDICATECOUNTING)
            {
              System.out.println("\nnode context: "+nodeToString(context));
              System.out.println("index predicate is false: "+proxPos);
              System.out.println("\n===== end predicate count ========");
            }
            return false;
          }
          else if (DEBUG_PREDICATECOUNTING)
          {
            System.out.println("\nnode context: "+nodeToString(context));
            System.out.println("index predicate is true: "+proxPos);
            System.out.println("\n===== end predicate count ========");
          }
          
                                                                                                    if(m_predicates[i].isStableNumber() && i == nPredicates - 1)
          {
            m_foundLast = true;
          }
        }
        else if (!pred.bool())
          return false;

        countProximityPosition(++m_predicateIndex);
      }
    }
    finally
    {
      xctxt.popCurrentNode();
      xctxt.popNamespaceContext();
      xctxt.popSubContextList();
      m_predicateIndex = -1;
    }

    return true;
  }