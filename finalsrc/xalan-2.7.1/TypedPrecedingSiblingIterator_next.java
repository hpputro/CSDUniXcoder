    public int next()
    {
      int node = _currentNode;
      int expType;

      int nodeType = _nodeType;
      int startID = _startNodeID;

      if (nodeType >= DTM.NTYPES) {
        while (node != NULL && node != startID && _exptype(node) != nodeType) {
          node = _nextsib(node);
        }
      } else {
        while (node != NULL && node != startID) {
          expType = _exptype(node);
          if (expType < DTM.NTYPES) {
            if (expType == nodeType) {
              break;
            }
          } else {
            if (m_expandedNameTable.getType(expType) == nodeType) {
              break;
            }
          }
          node = _nextsib(node);
        }
      }

      if (node == DTM.NULL || node == _startNodeID) {
        _currentNode = NULL;
        return NULL;
      } else {
        _currentNode = _nextsib(node);
        return returnNode(makeNodeHandle(node));
      }
    }