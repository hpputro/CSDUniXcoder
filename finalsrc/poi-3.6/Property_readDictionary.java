    protected Map readDictionary(final byte[] src, final long offset,
                                 final int length, final int codepage)
    throws UnsupportedEncodingException
    {
        
        if (offset < 0 || offset > src.length)
            throw new HPSFRuntimeException
                ("Illegal offset " + offset + " while HPSF stream contains " +
                 length + " bytes.");
        int o = (int) offset;

        
        final long nrEntries = LittleEndian.getUInt(src, o);
        o += LittleEndian.INT_SIZE;

        final Map m = new HashMap((int) nrEntries, (float) 1.0);

        try
        {
            for (int i = 0; i < nrEntries; i++)
            {
                
                final Long id = Long.valueOf(LittleEndian.getUInt(src, o));
                o += LittleEndian.INT_SIZE;

                
                long sLength = LittleEndian.getUInt(src, o);
                o += LittleEndian.INT_SIZE;

                
                final StringBuffer b = new StringBuffer();
                switch (codepage)
                {
                    case -1:
                    {
                        
                        b.append(new String(src, o, (int) sLength));
                        break;
                    }
                    case Constants.CP_UNICODE:
                    {
                        
                        final int nrBytes = (int) (sLength * 2);
                        final byte[] h = new byte[nrBytes];
                        for (int i2 = 0; i2 < nrBytes; i2 += 2)
                        {
                            h[i2] = src[o + i2 + 1];
                            h[i2 + 1] = src[o + i2];
                        }
                        b.append(new String(h, 0, nrBytes,
                                VariantSupport.codepageToEncoding(codepage)));
                        break;
                    }
                    default:
                    {
                        
                        b.append(new String(src, o, (int) sLength,
                                 VariantSupport.codepageToEncoding(codepage)));
                        break;
                    }
                }

                
                while (b.length() > 0 && b.charAt(b.length() - 1) == 0x00)
                    b.setLength(b.length() - 1);
                if (codepage == Constants.CP_UNICODE)
                {
                    if (sLength % 2 == 1)
                        sLength++;
                    o += (sLength + sLength);
                }
                else
                    o += sLength;
                m.put(id, b.toString());
            }
        }
        catch (RuntimeException ex)
        {
            final POILogger l = POILogFactory.getLogger(getClass());
            l.log(POILogger.WARN,
                    "The property set's dictionary contains bogus data. "
                    + "All dictionary entries starting with the one with ID "
                    + id + " will be ignored.", ex);
        }
        return m;
    }