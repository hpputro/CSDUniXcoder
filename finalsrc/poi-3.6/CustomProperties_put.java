    private Object put(final CustomProperty customProperty) throws ClassCastException
    {
        final String name = customProperty.getName();

        
        final Long oldId = (Long) dictionaryNameToID.get(name);
        if (oldId != null)
            customProperty.setID(oldId.longValue());
        else
        {
            long max = 1;
            for (final Iterator i = dictionaryIDToName.keySet().iterator(); i.hasNext();)
            {
                final long id = ((Long) i.next()).longValue();
                if (id > max)
                    max = id;
            }
            customProperty.setID(max + 1);
        }
        return this.put(name, customProperty);
    }