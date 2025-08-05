    public void dumpOld(long maxLength, InputStream in, PrintStream out)
            throws IOException, LittleEndian.BufferUnderrunException {
        long remainingBytes = maxLength;
        short options;              short recordId;
        int recordBytesRemaining;               StringBuffer stringBuf = new StringBuffer();
        short nDumpSize;
        String recordName;

        boolean atEOF = false;

        while (!atEOF && (remainingBytes > 0)) {
            stringBuf = new StringBuffer();
            options = LittleEndian.readShort( in );
            recordId = LittleEndian.readShort( in );
            recordBytesRemaining = LittleEndian.readInt( in );

            remainingBytes -= 2 + 2 + 4;

            switch ( recordId )
            {
                case (short) 0xF000:
                    recordName = "MsofbtDggContainer";
                    break;
                case (short) 0xF006:
                    recordName = "MsofbtDgg";
                    break;
                case (short) 0xF016:
                    recordName = "MsofbtCLSID";
                    break;
                case (short) 0xF00B:
                    recordName = "MsofbtOPT";
                    break;
                case (short) 0xF11A:
                    recordName = "MsofbtColorMRU";
                    break;
                case (short) 0xF11E:
                    recordName = "MsofbtSplitMenuColors";
                    break;
                case (short) 0xF001:
                    recordName = "MsofbtBstoreContainer";
                    break;
                case (short) 0xF007:
                    recordName = "MsofbtBSE";
                    break;
                case (short) 0xF002:
                    recordName = "MsofbtDgContainer";
                    break;
                case (short) 0xF008:
                    recordName = "MsofbtDg";
                    break;
                case (short) 0xF118:
                    recordName = "MsofbtRegroupItem";
                    break;
                case (short) 0xF120:
                    recordName = "MsofbtColorScheme";
                    break;
                case (short) 0xF003:
                    recordName = "MsofbtSpgrContainer";
                    break;
                case (short) 0xF004:
                    recordName = "MsofbtSpContainer";
                    break;
                case (short) 0xF009:
                    recordName = "MsofbtSpgr";
                    break;
                case (short) 0xF00A:
                    recordName = "MsofbtSp";
                    break;
                case (short) 0xF00C:
                    recordName = "MsofbtTextbox";
                    break;
                case (short) 0xF00D:
                    recordName = "MsofbtClientTextbox";
                    break;
                case (short) 0xF00E:
                    recordName = "MsofbtAnchor";
                    break;
                case (short) 0xF00F:
                    recordName = "MsofbtChildAnchor";
                    break;
                case (short) 0xF010:
                    recordName = "MsofbtClientAnchor";
                    break;
                case (short) 0xF011:
                    recordName = "MsofbtClientData";
                    break;
                case (short) 0xF11F:
                    recordName = "MsofbtOleObject";
                    break;
                case (short) 0xF11D:
                    recordName = "MsofbtDeletedPspl";
                    break;
                case (short) 0xF005:
                    recordName = "MsofbtSolverContainer";
                    break;
                case (short) 0xF012:
                    recordName = "MsofbtConnectorRule";
                    break;
                case (short) 0xF013:
                    recordName = "MsofbtAlignRule";
                    break;
                case (short) 0xF014:
                    recordName = "MsofbtArcRule";
                    break;
                case (short) 0xF015:
                    recordName = "MsofbtClientRule";
                    break;
                case (short) 0xF017:
                    recordName = "MsofbtCalloutRule";
                    break;
                case (short) 0xF119:
                    recordName = "MsofbtSelection";
                    break;
                case (short) 0xF122:
                    recordName = "MsofbtUDefProp";
                    break;
                default:
                    if ( recordId >= (short) 0xF018 && recordId <= (short) 0xF117 )
                        recordName = "MsofbtBLIP";
                    else if ( ( options & (short) 0x000F ) == (short) 0x000F )
                        recordName = "UNKNOWN container";
                    else
                        recordName = "UNKNOWN ID";
            }

            stringBuf.append( "  " );
            stringBuf.append( HexDump.toHex( recordId ) );
            stringBuf.append( "  " ).append( recordName ).append( " [" );
            stringBuf.append( HexDump.toHex( options ) );
            stringBuf.append( ',' );
            stringBuf.append( HexDump.toHex( recordBytesRemaining ) );
            stringBuf.append( "]  instance: " );
            stringBuf.append( HexDump.toHex( ( (short) ( options >> 4 ) ) ) );
            out.println( stringBuf.toString() );


            if ( recordId == (short) 0xF007 && 36 <= remainingBytes && 36 <= recordBytesRemaining )
            {	                
                byte n8;
                                
                stringBuf = new StringBuffer( "    btWin32: " );
                n8 = (byte) in.read();
                stringBuf.append( HexDump.toHex( n8 ) );
                stringBuf.append( getBlipType( n8 ) );
                stringBuf.append( "  btMacOS: " );
                n8 = (byte) in.read();
                stringBuf.append( HexDump.toHex( n8 ) );
                stringBuf.append( getBlipType( n8 ) );
                out.println( stringBuf.toString() );

                out.println( "    rgbUid:" );
                HexDump.dump( in, out, 0, 16 );

                out.print( "    tag: " );
                outHex( 2, in, out );
                out.println();
                out.print( "    size: " );
                outHex( 4, in, out );
                out.println();
                out.print( "    cRef: " );
                outHex( 4, in, out );
                out.println();
                out.print( "    offs: " );
                outHex( 4, in, out );
                out.println();
                out.print( "    usage: " );
                outHex( 1, in, out );
                out.println();
                out.print( "    cbName: " );
                outHex( 1, in, out );
                out.println();
                out.print( "    unused2: " );
                outHex( 1, in, out );
                out.println();
                out.print( "    unused3: " );
                outHex( 1, in, out );
                out.println();

                                remainingBytes -= 36;
                                recordBytesRemaining = 0;		            }
            else if ( recordId == (short) 0xF010 && 0x12 <= remainingBytes && 0x12 <= recordBytesRemaining )
            {	                                
                out.print( "    Flag: " );
                outHex( 2, in, out );
                out.println();
                out.print( "    Col1: " );
                outHex( 2, in, out );
                out.print( "    dX1: " );
                outHex( 2, in, out );
                out.print( "    Row1: " );
                outHex( 2, in, out );
                out.print( "    dY1: " );
                outHex( 2, in, out );
                out.println();
                out.print( "    Col2: " );
                outHex( 2, in, out );
                out.print( "    dX2: " );
                outHex( 2, in, out );
                out.print( "    Row2: " );
                outHex( 2, in, out );
                out.print( "    dY2: " );
                outHex( 2, in, out );
                out.println();

                remainingBytes -= 18;
                recordBytesRemaining -= 18;

            }
            else if ( recordId == (short) 0xF00B || recordId == (short) 0xF122 )
            {	                int nComplex = 0;
                out.println( "    PROPID        VALUE" );
                while ( recordBytesRemaining >= 6 + nComplex && remainingBytes >= 6 + nComplex )
                {
                    short n16;
                    int n32;
                    n16 = LittleEndian.readShort( in );
                    n32 = LittleEndian.readInt( in );

                    recordBytesRemaining -= 6;
                    remainingBytes -= 6;
                    out.print( "    " );
                    out.print( HexDump.toHex( n16 ) );
                    out.print( " (" );
                    int propertyId = n16 & (short) 0x3FFF;
                    out.print( " " + propertyId  );
                    if ( ( n16 & (short) 0x8000 ) == 0 )
                    {
                        if ( ( n16 & (short) 0x4000 ) != 0 )
                            out.print( ", fBlipID" );
                        out.print( ")  " );

                        out.print( HexDump.toHex( n32 ) );

                        if ( ( n16 & (short) 0x4000 ) == 0 )
                        {
                            out.print( " (" );
                            out.print( dec1616( n32 ) );
                            out.print( ')' );
                            out.print( " {" + propName( (short)propertyId ) + "}" );
                        }
                        out.println();
                    }
                    else
                    {
                        out.print( ", fComplex)  " );
                        out.print( HexDump.toHex( n32 ) );
                        out.print( " - Complex prop len" );
                        out.println( " {" + propName( (short)propertyId ) + "}" );

                        nComplex += n32;
                    }

                }
                                while ( ( nComplex & remainingBytes ) > 0 )
                {
                    nDumpSize = ( nComplex > (int) remainingBytes ) ? (short) remainingBytes : (short) nComplex;
                    HexDump.dump( in, out, 0, nDumpSize );
                    nComplex -= nDumpSize;
                    recordBytesRemaining -= nDumpSize;
                    remainingBytes -= nDumpSize;
                }
            }
            else if ( recordId == (short) 0xF012 )
            {
                out.print( "    Connector rule: " );
                out.print( LittleEndian.readInt( in ) );
                out.print( "    ShapeID A: " );
                out.print( LittleEndian.readInt( in ) );
                out.print( "   ShapeID B: " );
                out.print( LittleEndian.readInt( in ) );
                out.print( "    ShapeID connector: " );
                out.print( LittleEndian.readInt( in ) );
                out.print( "   Connect pt A: " );
                out.print( LittleEndian.readInt( in ) );
                out.print( "   Connect pt B: " );
                out.println( LittleEndian.readInt( in ) );

                recordBytesRemaining -= 24;
                remainingBytes -= 24;
            }
            else if ( recordId >= (short) 0xF018 && recordId < (short) 0xF117 )
            {
                out.println( "    Secondary UID: " );
                HexDump.dump( in, out, 0, 16 );
                out.println( "    Cache of size: " + HexDump.toHex( LittleEndian.readInt( in ) ) );
                out.println( "    Boundary top: " + HexDump.toHex( LittleEndian.readInt( in ) ) );
                out.println( "    Boundary left: " + HexDump.toHex( LittleEndian.readInt( in ) ) );
                out.println( "    Boundary width: " + HexDump.toHex( LittleEndian.readInt( in ) ) );
                out.println( "    Boundary height: " + HexDump.toHex( LittleEndian.readInt( in ) ) );
                out.println( "    X: " + HexDump.toHex( LittleEndian.readInt( in ) ) );
                out.println( "    Y: " + HexDump.toHex( LittleEndian.readInt( in ) ) );
                out.println( "    Cache of saved size: " + HexDump.toHex( LittleEndian.readInt( in ) ) );
                out.println( "    Compression Flag: " + HexDump.toHex( (byte) in.read() ) );
                out.println( "    Filter: " + HexDump.toHex( (byte) in.read() ) );
                out.println( "    Data (after decompression): " );

                recordBytesRemaining -= 34 + 16;
                remainingBytes -= 34 + 16;

                nDumpSize = ( recordBytesRemaining > (int) remainingBytes ) ? (short) remainingBytes : (short) recordBytesRemaining;


                byte[] buf = new byte[nDumpSize];
                int read = in.read( buf );
                while ( read != -1 && read < nDumpSize )
                    read += in.read( buf, read, buf.length );
                ByteArrayInputStream bin = new ByteArrayInputStream( buf );

                InputStream in1 = new InflaterInputStream( bin );
                int bytesToDump = -1;
                HexDump.dump( in1, out, 0, bytesToDump );

                recordBytesRemaining -= nDumpSize;
                remainingBytes -= nDumpSize;

            }

            boolean isContainer = ( options & (short) 0x000F ) == (short) 0x000F;
            if ( isContainer && remainingBytes >= 0 )
            {	                if ( recordBytesRemaining <= (int) remainingBytes )
                    out.println( "            completed within" );
                else
                    out.println( "            continued elsewhere" );
            }
            else if ( remainingBytes >= 0 )
                        {
                nDumpSize = ( recordBytesRemaining > (int) remainingBytes ) ? (short) remainingBytes : (short) recordBytesRemaining;

                if ( nDumpSize != 0 )
                {
                    HexDump.dump( in, out, 0, nDumpSize );
                    remainingBytes -= nDumpSize;
                }
            }
            else
                out.println( " >> OVERRUN <<" );
        }

    }