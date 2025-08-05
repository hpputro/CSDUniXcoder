  public void write(final char chars[], final int start, final int length)
          throws java.io.IOException
  {

            
    int lengthx3 = 3*length;

    if (lengthx3 >= BYTES_MAX - count)
    {
            flushBuffer();

      if (lengthx3 > BYTES_MAX)
      {
        
        int split = length/CHARS_MAX; 
        final int chunks;
        if (length % CHARS_MAX > 0) 
            chunks = split + 1;
        else
            chunks = split;
        int end_chunk = start;
        for (int chunk = 1; chunk <= chunks; chunk++)
        {
            int start_chunk = end_chunk;
            end_chunk = start + (int) ((((long) length) * chunk) / chunks);
            
                                                final char c = chars[end_chunk - 1]; 
            int ic = chars[end_chunk - 1];
            if (c >= 0xD800 && c <= 0xDBFF) {
                                                                
                if (end_chunk < start + length) {
                                                            end_chunk++;
                } else {
                    
                    end_chunk--;
                }
            }


            int len_chunk = (end_chunk - start_chunk);
            this.write(chars,start_chunk, len_chunk);
        }
        return;
      }
    }



    final int n = length+start;
    final byte[] buf_loc = m_outputBytes;     int count_loc = count;          int i = start;
    {
         
        char c;
        for(; i < n && (c = chars[i])< 0x80 ; i++ )
            buf_loc[count_loc++] = (byte)c;
    }
    for (; i < n; i++)
    {

      final char c = chars[i];

      if (c < 0x80)
        buf_loc[count_loc++] = (byte) (c);
      else if (c < 0x800)
      {
        buf_loc[count_loc++] = (byte) (0xc0 + (c >> 6));
        buf_loc[count_loc++] = (byte) (0x80 + (c & 0x3f));
      }
      
      else if (c >= 0xD800 && c <= 0xDBFF) 
      {
          char high, low;
          high = c;
          i++;
          low = chars[i];

          buf_loc[count_loc++] = (byte) (0xF0 | (((high + 0x40) >> 8) & 0xf0));
          buf_loc[count_loc++] = (byte) (0x80 | (((high + 0x40) >> 2) & 0x3f));
          buf_loc[count_loc++] = (byte) (0x80 | ((low >> 6) & 0x0f) + ((high << 4) & 0x30));
          buf_loc[count_loc++] = (byte) (0x80 | (low & 0x3f));
      }
      else
      {
        buf_loc[count_loc++] = (byte) (0xe0 + (c >> 12));
        buf_loc[count_loc++] = (byte) (0x80 + ((c >> 6) & 0x3f));
        buf_loc[count_loc++] = (byte) (0x80 + (c & 0x3f));
      }
    }
        count = count_loc;
  }