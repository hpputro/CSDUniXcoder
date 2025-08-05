	private Attribute_Code.Opcode[] parseOpcodes(byte[] bytes) {
		ArrayList ret = new ArrayList(bytes.length);
		Attribute_Code.Opcode op;
		OpcodeInfo opInfo;
		int offset;
		byte[][] operands = null;
		boolean wide = false;

		for (int i = 0; i < bytes.length; i++) {
			offset = i;
			opInfo = OpcodeHelper.OPCODES[0xFF & bytes[i]];
			if (opInfo.operandsLength == null) {
				operands = null;
			} else {
				if (opInfo.opcode == Constants.TABLESWITCH) {
					int padnum = i % 4;
					padnum = 3 - padnum;
					i = i + padnum + 1;

										byte[] defaultb = new byte[4];
					for (int t = 0; t < 4; t++) {
						defaultb[t] = bytes[i + t];
					}
					i = i + 4;

										byte[] lowb = new byte[4];
					for (int t = 0; t < 4; t++) {
						lowb[t] = bytes[i + t];
					}
					i = i + 4;

										byte[] highb = new byte[4];
					for (int t = 0; t < 4; t++) {
						highb[t] = bytes[i + t];
					}
					i = i + 4;

					int high = Util.getNum(highb);
					int low = Util.getNum(lowb);
					int total = high - low + 1 + 3 + 1; 					if (total < 0) {
						total = 1;
					}
					operands = new byte[total][4];
					operands[0] = new byte[padnum];
					for (int ti = 0; ti < padnum; ti++) {
						operands[0][ti] = (byte) 0;
					}
					operands[1] = defaultb;
					operands[2] = lowb;
					operands[3] = highb;

					for (int t = 4; t < total; t++) {
						operands[t][0] = bytes[i++];
						operands[t][1] = bytes[i++];
						operands[t][2] = bytes[i++];
						operands[t][3] = bytes[i++];
					}
					i--;
				} else if (opInfo.opcode == Constants.LOOKUPSWITCH) {
					int padnum = i % 4;
					padnum = 3 - padnum;
					i = i + padnum + 1;

										byte[] defaultb = new byte[4];
					for (int t = 0; t < 4; t++) {
						defaultb[t] = bytes[i + t];
					}
					i = i + 4;

										byte[] npairb = new byte[4];
					for (int t = 0; t < 4; t++) {
						npairb[t] = bytes[i + t];
					}
					i = i + 4;

					int npair = Util.getNum(npairb);

					int total = npair * 2 + 3; 					operands = new byte[total][4];
					operands[0] = new byte[padnum];
					for (int ti = 0; ti < padnum; ti++) {
						operands[0][ti] = (byte) 0;
					}
					operands[1] = defaultb;
					operands[2] = npairb;
					for (int t = 3; t < total; t++) {
						operands[t][0] = bytes[i++];
						operands[t][1] = bytes[i++];
						operands[t][2] = bytes[i++];
						operands[t][3] = bytes[i++];
					}
					i--;
				} else if (opInfo.opcode == Constants.WIDE) {
					wide = true;
				} else if (wide == true) {
					operands = new byte[opInfo.operandsLength.length][];
					for (int j = 0; j < opInfo.operandsLength.length; j++) {
						operands[j] = new byte[opInfo.operandsLength[j]];
						for (int t = 0; t < opInfo.operandsLength[j] + 1; t++) {
							operands[j][t] = bytes[++i];
						}
					}
					wide = false;
				} else {

					operands = new byte[opInfo.operandsLength.length][];
					for (int j = 0; j < opInfo.operandsLength.length; j++) {
						operands[j] = new byte[opInfo.operandsLength[j]];
						for (int t = 0; t < opInfo.operandsLength[j]; t++) {
							operands[j][t] = bytes[++i];
						}
					}
				}
			}
			op = new Attribute_Code.Opcode(offset, opInfo.opcode, operands);
			ret.add(op);
		}
		return (Attribute_Code.Opcode[]) ret.toArray(new Attribute_Code.Opcode[0]);
	}