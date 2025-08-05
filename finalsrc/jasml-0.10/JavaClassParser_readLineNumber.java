private Attribute_LineNumberTable.LineNumber readLineNumber(DataInputStream in) throws IOException {
		return new Attribute_LineNumberTable.LineNumber(in.readUnsignedShort(), in.readUnsignedShort());
	}