  DescendantIterator(Compiler compiler, int opPos, int analysis)
          throws javax.xml.transform.TransformerException
  {

    super(compiler, opPos, analysis, false);

    int firstStepPos = OpMap.getFirstChildPos(opPos);
    int stepType = compiler.getOp(firstStepPos);

    boolean orSelf = (OpCodes.FROM_DESCENDANTS_OR_SELF == stepType);
    boolean fromRoot = false;
    if (OpCodes.FROM_SELF == stepType)
    {
      orSelf = true;
          }
    else if(OpCodes.FROM_ROOT == stepType)
    {
      fromRoot = true;
            int nextStepPos = compiler.getNextStepPos(firstStepPos);
      if(compiler.getOp(nextStepPos) == OpCodes.FROM_DESCENDANTS_OR_SELF)
        orSelf = true;
          }
    
        int nextStepPos = firstStepPos;
    while(true)
    {
      nextStepPos = compiler.getNextStepPos(nextStepPos);
      if(nextStepPos > 0)
      {
        int stepOp = compiler.getOp(nextStepPos);
        if(OpCodes.ENDOP != stepOp)
          firstStepPos = nextStepPos;
        else
          break;
      }
      else
        break;
      
    }
    
        if((analysis & WalkerFactory.BIT_CHILD) != 0)
      orSelf = false;
      
    if(fromRoot)
    {
      if(orSelf)
        m_axis = Axis.DESCENDANTSORSELFFROMROOT;
      else
        m_axis = Axis.DESCENDANTSFROMROOT;
    }
    else if(orSelf)
      m_axis = Axis.DESCENDANTORSELF;
    else
      m_axis = Axis.DESCENDANT;

    int whatToShow = compiler.getWhatToShow(firstStepPos);

    if ((0 == (whatToShow
               & (DTMFilter.SHOW_ATTRIBUTE | DTMFilter.SHOW_ELEMENT
                  | DTMFilter.SHOW_PROCESSING_INSTRUCTION))) || 
                   (whatToShow == DTMFilter.SHOW_ALL))
      initNodeTest(whatToShow);
    else
    {
      initNodeTest(whatToShow, compiler.getStepNS(firstStepPos),
                              compiler.getStepLocalName(firstStepPos));
    }
    initPredicateInfo(compiler, firstStepPos);
  }