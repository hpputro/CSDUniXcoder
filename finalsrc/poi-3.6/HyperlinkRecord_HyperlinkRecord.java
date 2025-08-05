public HyperlinkRecord(RecordInputStream in) {
        _range = new CellRangeAddress(in);

        _guid = new GUID(in);

        
        int streamVersion = in.readInt();
        if (streamVersion != 0x00000002) {
            throw new RecordFormatException("Stream Version must be 0x2 but found " + streamVersion);
        }
        _linkOpts = in.readInt();

        if ((_linkOpts & HLINK_LABEL) != 0){
            int label_len = in.readInt();
            _label = in.readUnicodeLEString(label_len);
        }

        if ((_linkOpts & HLINK_TARGET_FRAME) != 0){
            int len = in.readInt();
            _targetFrame = in.readUnicodeLEString(len);
        }

        if ((_linkOpts & HLINK_URL) != 0 && (_linkOpts & HLINK_UNC_PATH) != 0) {
            _moniker = null;
            int nChars = in.readInt();
            _address = in.readUnicodeLEString(nChars);
        }

        if ((_linkOpts & HLINK_URL) != 0 && (_linkOpts & HLINK_UNC_PATH) == 0) {
            _moniker = new GUID(in);

            if(URL_MONIKER.equals(_moniker)){
                int length = in.readInt();
                
                int remaining = in.remaining();
                if (length == remaining) {
                    int nChars = length/2;
                    _address = in.readUnicodeLEString(nChars);
                } else {
                    int nChars = (length - TAIL_SIZE)/2;
                    _address = in.readUnicodeLEString(nChars);
                    
                    _uninterpretedTail = readTail(URL_TAIL, in);
                }
            } else if (FILE_MONIKER.equals(_moniker)) {
                _fileOpts = in.readShort();

                int len = in.readInt();
                _shortFilename = StringUtil.readCompressedUnicode(in, len);
                _uninterpretedTail = readTail(FILE_TAIL, in);
                int size = in.readInt();
                if (size > 0) {
                    int charDataSize = in.readInt();

                                        int optFlags = in.readUShort();
                    if (optFlags != 0x0003) {
                        throw new RecordFormatException("Expected 0x3 but found " + optFlags);
                    }
                    _address = StringUtil.readUnicodeLE(in, charDataSize/2);
                } else {
                    _address = null;
                }
            } else if (STD_MONIKER.equals(_moniker)) {
                _fileOpts = in.readShort();

                int len = in.readInt();

                byte[] path_bytes = new byte[len];
                in.readFully(path_bytes);

                _address = new String(path_bytes);
            }
        }

        if((_linkOpts & HLINK_PLACE) != 0) {

            int len = in.readInt();
            _textMark = in.readUnicodeLEString(len);
        }

        if (in.remaining() > 0) {
            System.out.println(HexDump.toHex(in.readRemainder()));
        }
    }