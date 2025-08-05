    private void doRedBlackDeleteFixup(Node replacement_node,
                                       int index)
    {
        Node current_node = replacement_node;

        while ((current_node != _root[ index ])
                && (isBlack(current_node, index)))
        {
            if (isLeftChild(current_node, index))
            {
                Node sibling_node =
                    getRightChild(getParent(current_node, index), index);

                if (isRed(sibling_node, index))
                {
                    makeBlack(sibling_node, index);
                    makeRed(getParent(current_node, index), index);
                    rotateLeft(getParent(current_node, index), index);
                    sibling_node =
                        getRightChild(getParent(current_node, index), index);
                }
                if (isBlack(getLeftChild(sibling_node, index), index)
                        && isBlack(getRightChild(sibling_node, index), index))
                {
                    makeRed(sibling_node, index);
                    current_node = getParent(current_node, index);
                }
                else
                {
                    if (isBlack(getRightChild(sibling_node, index), index))
                    {
                        makeBlack(getLeftChild(sibling_node, index), index);
                        makeRed(sibling_node, index);
                        rotateRight(sibling_node, index);
                        sibling_node =
                            getRightChild(getParent(current_node, index),
                                          index);
                    }
                    copyColor(getParent(current_node, index), sibling_node,
                              index);
                    makeBlack(getParent(current_node, index), index);
                    makeBlack(getRightChild(sibling_node, index), index);
                    rotateLeft(getParent(current_node, index), index);
                    current_node = _root[ index ];
                }
            }
            else
            {
                Node sibling_node =
                    getLeftChild(getParent(current_node, index), index);

                if (isRed(sibling_node, index))
                {
                    makeBlack(sibling_node, index);
                    makeRed(getParent(current_node, index), index);
                    rotateRight(getParent(current_node, index), index);
                    sibling_node =
                        getLeftChild(getParent(current_node, index), index);
                }
                if (isBlack(getRightChild(sibling_node, index), index)
                        && isBlack(getLeftChild(sibling_node, index), index))
                {
                    makeRed(sibling_node, index);
                    current_node = getParent(current_node, index);
                }
                else
                {
                    if (isBlack(getLeftChild(sibling_node, index), index))
                    {
                        makeBlack(getRightChild(sibling_node, index), index);
                        makeRed(sibling_node, index);
                        rotateLeft(sibling_node, index);
                        sibling_node =
                            getLeftChild(getParent(current_node, index),
                                         index);
                    }
                    copyColor(getParent(current_node, index), sibling_node,
                              index);
                    makeBlack(getParent(current_node, index), index);
                    makeBlack(getLeftChild(sibling_node, index), index);
                    rotateRight(getParent(current_node, index), index);
                    current_node = _root[ index ];
                }
            }
        }
        makeBlack(current_node, index);
    }