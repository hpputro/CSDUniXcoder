    public static List inspectViewable(final Object viewable,
                                       final boolean drilldown,
                                       final int indentLevel,
                                       final String indentString)
    {
        List objects = new ArrayList();

        if (viewable instanceof POIFSViewable)
        {
            POIFSViewable inspected = ( POIFSViewable ) viewable;

            objects.add(indent(indentLevel, indentString,
                               inspected.getShortDescription()));
            if (drilldown)
            {
                if (inspected.preferArray())
                {
                    Object[] data = inspected.getViewableArray();

                    for (int j = 0; j < data.length; j++)
                    {
                        objects.addAll(inspectViewable(data[ j ], drilldown,
                                                       indentLevel + 1,
                                                       indentString));
                    }
                }
                else
                {
                    Iterator iter = inspected.getViewableIterator();

                    while (iter.hasNext())
                    {
                        objects.addAll(inspectViewable(iter.next(),
                                                       drilldown,
                                                       indentLevel + 1,
                                                       indentString));
                    }
                }
            }
        }
        else
        {
            objects.add(indent(indentLevel, indentString,
                               viewable.toString()));
        }
        return objects;
    }