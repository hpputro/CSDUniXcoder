    public DTMAxisIterator setStartNode(int node)
    {
      if (node == DTMDefaultBase.ROOTNODE)
        node = getDocument();
      m_realStartNode = node;

      if (_isRestartable)
      {
        int nodeID = makeNodeIdentity(node);
        m_size = 0;

        if (nodeID == DTM.NULL) {
          _currentNode = DTM.NULL;
          m_ancestorsPos = 0;
          return this;
        }

        final int nodeType = _nodeType;

        if (!_includeSelf) {
          nodeID = _parent2(nodeID);
          node = makeNodeHandle(nodeID);
        }

        _startNode = node;

        if (nodeType >= DTM.NTYPES) {
          while (nodeID != END) {
            int eType = _exptype2(nodeID);

            if (eType == nodeType) {
              if (m_size >= m_ancestors.length)
              {
              	int[] newAncestors = new int[m_size * 2];
              	System.arraycopy(m_ancestors, 0, newAncestors, 0, m_ancestors.length);
              	m_ancestors = newAncestors;
              }
              m_ancestors[m_size++] = makeNodeHandle(nodeID);
            }
            nodeID = _parent2(nodeID);
          }
        }
        else {
          while (nodeID != END) {
            int eType = _exptype2(nodeID);

            if ((eType < DTM.NTYPES && eType == nodeType)
                || (eType >= DTM.NTYPES
                    && m_extendedTypes[eType].getNodeType() == nodeType)) {
              if (m_size >= m_ancestors.length)
              {
              	int[] newAncestors = new int[m_size * 2];
              	System.arraycopy(m_ancestors, 0, newAncestors, 0, m_ancestors.length);
              	m_ancestors = newAncestors;
              }
              m_ancestors[m_size++] = makeNodeHandle(nodeID);
            }
            nodeID = _parent2(nodeID);
          }
        }
        m_ancestorsPos = m_size - 1;

        _currentNode = (m_ancestorsPos>=0)
                               ? m_ancestors[m_ancestorsPos]
                               : DTM.NULL;

        return resetPosition();
      }

      return this;
    }