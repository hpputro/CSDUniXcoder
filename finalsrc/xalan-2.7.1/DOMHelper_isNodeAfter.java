  public static boolean isNodeAfter(Node node1, Node node2)
  {
    if (node1 == node2 || isNodeTheSame(node1, node2))
      return true;

            boolean isNodeAfter = true;
        
    Node parent1 = getParentOfNode(node1);
    Node parent2 = getParentOfNode(node2);          

        if (parent1 == parent2 || isNodeTheSame(parent1, parent2))      {
      if (null != parent1)
        isNodeAfter = isNodeAfterSibling(parent1, node1, node2);
      else
      {
                                                                                          
                                                    }
    }
    else
    {

                                                                                                      
            int nParents1 = 2, nParents2 = 2;  
      while (parent1 != null)
      {
        nParents1++;

        parent1 = getParentOfNode(parent1);
      }

      while (parent2 != null)
      {
        nParents2++;

        parent2 = getParentOfNode(parent2);
      }

                          Node startNode1 = node1, startNode2 = node2;

                      if (nParents1 < nParents2)
      {
                int adjust = nParents2 - nParents1;

        for (int i = 0; i < adjust; i++)
        {
          startNode2 = getParentOfNode(startNode2);
        }
      }
      else if (nParents1 > nParents2)
      {
                int adjust = nParents1 - nParents2;

        for (int i = 0; i < adjust; i++)
        {
          startNode1 = getParentOfNode(startNode1);
        }
      }

      Node prevChild1 = null, prevChild2 = null;  
            while (null != startNode1)
      {
        if (startNode1 == startNode2 || isNodeTheSame(startNode1, startNode2))          {
          if (null == prevChild1)            {

                        isNodeAfter = (nParents1 < nParents2) ? true : false;

            break;            }
          else 
          {
                                    isNodeAfter = isNodeAfterSibling(startNode1, prevChild1,
                                             prevChild2);

            break;            }
        }  
                        prevChild1 = startNode1;
        startNode1 = getParentOfNode(startNode1);
        prevChild2 = startNode2;
        startNode2 = getParentOfNode(startNode2);
      }      }          
                        
    
    return isNodeAfter;
  }