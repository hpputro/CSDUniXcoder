    public Collection values()
    {
        if (_value_collection[ _KEY ] == null)
        {
            _value_collection[ _KEY ] = new AbstractCollection()
            {
                public Iterator iterator()
                {
                    return new BinaryTreeIterator(_KEY)
                    {
                        protected Object doGetNext()
                        {
                            return _last_returned_node.getData(_VALUE);
                        }
                    };
                }

                public int size()
                {
                    return BinaryTree.this.size();
                }

                public boolean contains(Object o)
                {
                    return containsValue(o);
                }

                public boolean remove(Object o)
                {
                    int old_size = _size;

                    removeValue(o);
                    return _size != old_size;
                }

                public boolean removeAll(Collection c)
                {
                    boolean  modified = false;
                    Iterator iter     = c.iterator();

                    while (iter.hasNext())
                    {
                        if (removeValue(iter.next()) != null)
                        {
                            modified = true;
                        }
                    }
                    return modified;
                }

                public void clear()
                {
                    BinaryTree.this.clear();
                }
            };
        }
        return _value_collection[ _KEY ];
    }