private HashSet calculateReferences(Method meth) {
		HashSet set = new HashSet();
		Attribute att;
		Attribute_Code.Opcode[] ops = null;
		Attribute_Code.Opcode op;
		for (int i = 0; i < meth.attributes_count; i++) {
			if (meth.attributes[i].attribute_tag == Constants.ATTRIBUTE_Code) {
				ops = ((Attribute_Code) meth.attributes[i]).codes;
				break;
			}
		}
		if (ops == null) {
			return set;
		}

		for (int i = 0; i < meth.attributes_count; i++) {
			att = meth.attributes[i];
			if (att.attribute_tag == Constants.ATTRIBUTE_Code) {
				Attribute_Code code = (Attribute_Code) att;
				for (int j = 0; j < ops.length; j++) {
					op = ops[j];
					switch (op.opcode) {

					case Constants.LOOKUPSWITCH:
						set.add(Integer.toString(Util.getSignedNum(op.operands[1]) + op.offset)); 						for (int t = 4; t < op.operands.length; t++) {
							set.add(Integer.toString(Util.getSignedNum(op.operands[t++]) + op.offset));
						}
						break;
					case Constants.TABLESWITCH:
						set.add(Integer.toString(Util.getSignedNum(op.operands[1]) + op.offset)); 						for (int t = 4; t < op.operands.length; t++) {
							set.add(Integer.toString(Util.getSignedNum(op.operands[t]) + op.offset));
						}
						break;
					case Constants.GOTO:
					case Constants.IFEQ:
					case Constants.IFGE:
					case Constants.IFGT:
					case Constants.IFLE:
					case Constants.IFLT:
					case Constants.JSR:
					case Constants.IFNE:
					case Constants.IFNONNULL:
					case Constants.IFNULL:
					case Constants.IF_ACMPEQ:
					case Constants.IF_ACMPNE:
					case Constants.IF_ICMPEQ:
					case Constants.IF_ICMPGE:
					case Constants.IF_ICMPGT:
					case Constants.IF_ICMPLE:
					case Constants.IF_ICMPLT:
					case Constants.IF_ICMPNE:
					case Constants.GOTO_W:
					case Constants.JSR_W:
						set.add(Integer.toString(Util.getSignedNum(op.operands[0]) + op.offset));
						break;

					}
				}
				if (code.exception_table_length != 0) {
					Attribute_Code.ExceptionTableItem[] exceptions = code.exception_table;
					Attribute_Code.ExceptionTableItem exc;
					for (int j = 0; j < exceptions.length; j++) {
						exc = exceptions[j];
						set.add(Integer.toString(exc.start_pc));
						set.add(Integer.toString(exc.end_pc));
						set.add(Integer.toString(exc.handler_pc));
					}
				}

				if (code.attributes_count != 0) {
					for (int j = 0; j < code.attributes_count; j++) {
						if (code.attributes[j].attribute_tag == Constants.ATTRIBUTE_LineNumberTable && config.showLineNumber == true) {
							Attribute_LineNumberTable lineNumberTable = (Attribute_LineNumberTable) code.attributes[j];
							Attribute_LineNumberTable.LineNumber[] lines = lineNumberTable.lineNumberTable;
							for (int x = 0; x < lineNumberTable.line_number_table_length; x++) {
								set.add(Integer.toString(lines[x].start_pc));
							}
						} else if (code.attributes[j].attribute_tag == Constants.ATTRIBUTE_LocalVariableTable) {
							Attribute_LocalVariableTable lvt = (Attribute_LocalVariableTable) code.attributes[j];
							if (lvt.local_variable_table_length != 0) {
								Attribute_LocalVariableTable.LocalVariable[] lvs = lvt.local_variable_table;
								Attribute_LocalVariableTable.LocalVariable lv;
								for (int x = 0; x < lvs.length; x++) {
									lv = lvs[x];
									set.add(Integer.toString(lv.start_pc));
									if (lv.length != 1) {
										op = findPreviousInstruction(lv.start_pc + lv.length, ops);
										if (op != null) {
											set.add(Integer.toString(op.offset));
										}
									}
								}
							}
						}
					}
				}
				break;
			}
		}
		return set;
	}