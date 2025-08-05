    private void doRedBlackInsert(Node inserted_node, int index)
    {
        Node current_node = inserted_node;

        makeRed(current_node, index);
        while ((current_node != null) && (current_node != _root[ index ])
                && (isRed(current_node.getParent(index), index)))
        {
            if (isLeftChild(getParent(current_node, index), index))
            {
                Node y = getRightChild(getGrandParent(current_node, index),
                                       index);

                if (isRed(y, index))
                {
                    makeBlack(getParent(current_node, index), index);
                    makeBlack(y, index);
                    makeRed(getGrandParent(current_node, index), index);
                    current_node = getGrandParent(current_node, index);
                }
                else
                {
                    if (isRightChild(current_node, index))
                    {
                        current_node = getParent(current_node, index);
                        rotateLeft(current_node, index);
                    }
                    makeBlack(getParent(current_node, index), index);
                    makeRed(getGrandParent(current_node, index), index);
                    if (getGrandParent(current_node, index) != null)
                    {
                        rotateRight(getGrandParent(current_node, index),
                                    index);
                    }
                }
            }
            else
            {

                                Node y = getLeftChild(getGrandParent(current_node, index),
                                      index);

                if (isRed(y, index))
                {
                    makeBlack(getParent(current_node, index), index);
                    makeBlack(y, index);
                    makeRed(getGrandParent(current_node, index), index);
                    current_node = getGrandParent(current_node, index);
                }
                else
                {
                    if (isLeftChild(current_node, index))
                    {
                        current_node = getParent(current_node, index);
                        rotateRight(current_node, index);
                    }
                    makeBlack(getParent(current_node, index), index);
                    makeRed(getGrandParent(current_node, index), index);
                    if (getGrandParent(current_node, index) != null)
                    {
                        rotateLeft(getGrandParent(current_node, index),
                                   index);
                    }
                }
            }
        }
        makeBlack(_root[ index ], index);
    }