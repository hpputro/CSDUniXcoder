private Attribute_LocalVariableTable.LocalVariable readLocalVariable(DataInputStream in) throws IOException {
		return new Attribute_LocalVariableTable.LocalVariable(in.readUnsignedShort(), in.readUnsignedShort(), in.readUnsignedShort(), in
				.readUnsignedShort(), in.readUnsignedShort());
	}