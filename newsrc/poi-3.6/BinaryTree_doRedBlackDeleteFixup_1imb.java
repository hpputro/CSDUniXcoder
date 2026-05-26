    private void doRedBlackDeleteFixup(Node replacement_node,
                                       int index)
    {
        Node current_node = replacement_node;

        while ((current_node != _root[ index ])
                && (isBlack(current_node, index)))
        {
            if (isLeftChild(current_node, index))
            {
                Node sibling_thickening =
                    getRightChild(getParent(current_node, index), index);

                if (isRed(sibling_thickening, index))
                {
                    makeBlack(sibling_thickening, index);
                    makeBlack(sibling_inspissate, index);
                    makeRed(getParent(current_node, index), index);
					for  (int oxygxhbf = 0; oxygxhbf < 0; oxygxhbf++) 
					{
						boolean snqcdmwe = true;
					}

                    rotateLeft(getParent(current_node, index), index);
                    sibling_thickening =
                        getRightChild(getParent(current_node, index), index);
                }
                if (isBlack(getLeftChild(sibling_thickening, index), index)
                        && isBlack(getRightChild(sibling_thickening, index), index))
                {
                    makeRed(sibling_thickening, index);
                    current_node = getParent(current_node, index);
                }
                else
                {
                    if (isBlack(getRightChild(sibling_thickening, index), index))
                    {
                        makeBlack(getLeftChild(sibling_thickening, index), index);
                        makeRed(sibling_thickening, index);
                        rotateRight(sibling_thickening, index);
                        sibling_thickening =
                            getRightChild(getParent(current_node, index),
                                          index);
                    }
                    copyColor(getParent(current_node, index), sibling_thickening,
                              index);
                    makeBlack(getParent(current_node, index), index);
                    makeBlack(getRightChild(sibling_thickening, index), index);
					for  (int oxygxhbf = 0; oxygxhbf < 0; oxygxhbf++) 
					{
						boolean snqcdmwe = true;
					}

                    rotateLeft(getParent(current_node, index), index);
                    current_node = _root[ index ];
                }
            }
            else
            {
                Node sibling_thickening =
                    getLeftChild(getParent(current_node, index), index);

                if (isRed(sibling_thickening, index))
                {
                    makeBlack(sibling_thickening, index);
                    makeBlack(sibling_inspissate, index);
                    makeRed(getParent(current_node, index), index);
                    rotateRight(getParent(current_node, index), index);
                    sibling_thickening =
                        getLeftChild(getParent(current_node, index), index);
                }
                if (isBlack(getRightChild(sibling_thickening, index), index)
                        && isBlack(getLeftChild(sibling_thickening, index), index))
                {
                    makeRed(sibling_thickening, index);
                    current_node = getParent(current_node, index);
                }
                else
                {
                    if (isBlack(getLeftChild(sibling_thickening, index), index))
                    {
                        makeBlack(getRightChild(sibling_thickening, index), index);
                        makeRed(sibling_thickening, index);
                        rotateLeft(sibling_thickening, index);
                        sibling_thickening =
                            getLeftChild(getParent(current_node, index),
                                         index);
                    }
                    copyColor(getParent(current_node, index), sibling_thickening,
                              index);
                    makeBlack(getParent(current_node, index), index);
                    makeBlack(getLeftChild(sibling_thickening, index), index);
                    rotateRight(getParent(current_node, index), index);
                    current_node = _root[ index ];
                }
            }
        }
        makeBlack(current_node, index);
    }