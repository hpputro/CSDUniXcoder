private String toString(Attribute_Code code, HashSet referedLines) {
		StringBuffer buf = new StringBuffer();
		Attribute_Code.Opcode op;
		Attribute_Code.Opcode[] ops = code.codes;
		byte[][] operands;
		int ti, def, low, high, jump_count, npairs;
		String soffset;

		Attribute_LocalVariableTable.LocalVariable[] lvts = null;
		for (int i = 0; i < code.attributes_count; i++) {
			if (code.attributes[i] instanceof Attribute_LocalVariableTable) {
				lvts = ((Attribute_LocalVariableTable) code.attributes[i]).local_variable_table;
				break;
			}
		}

				if (code.code_length != 0) {
			for (int t = 0; t < ops.length; t++) {
				op = ops[t];
				operands = op.operands;
								soffset = Integer.toString(op.offset);
				if (referedLines.contains(soffset) == true) {
					if (config.labelInSingleLine == true) {
						buf.append(config.labelPrefix + soffset + " : ");
						buf.append(Constants.LINE_SEPARATER);
						buf.append(config.instructionPadding);
					} else {
						buf.append(Util.padChar(config.labelPrefix + soffset, config.labelLength, ' ') + " : ");
					}
				} else {
					buf.append(config.instructionPadding);
				}
								buf.append(Constants.OPCODE_NAMES[0xFF & op.opcode] + "  ");
				switch (op.opcode) {
				case Constants.TABLESWITCH:
					def = Util.getNum(operands[1]) + op.offset;
					low = Util.getNum(operands[2]);
					high = Util.getNum(operands[3]);
					jump_count = high - low + 1;

					buf.append("default=" + config.labelPrefix + def + ", low=" + low + ", high=" + high + ", jump_table:");

					for (int i = 0; i < jump_count; i++) {
												buf.append(config.labelPrefix + (Util.getNum(operands[i + 4]) + op.offset) + ",");
					}
					buf.deleteCharAt(buf.length() - 1);
					break;
				case Constants.LOOKUPSWITCH: {
					def = Util.getNum(operands[1]) + op.offset;
					npairs = Util.getNum(operands[2]);

					buf.append("default=" + config.labelPrefix + def + ", npairs=" + npairs + ", jump_table:");
					if (npairs != 0) {
						for (int i = 0; i < npairs; i++) {
							buf.append(Util.getNum(operands[i * 2 + 3]));
							buf.append("->");
							buf.append(config.labelPrefix + (Util.getNum(operands[i * 2 + 4]) + op.offset) + ",");

						}
						buf.deleteCharAt(buf.length() - 1);
					}
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
					buf.append(config.labelPrefix + (Util.getSignedNum(operands[0]) + op.offset));
					break;
				
				case Constants.ALOAD:
				case Constants.ASTORE:
				case Constants.DLOAD:
				case Constants.DSTORE:
				case Constants.FLOAD:
				case Constants.FSTORE:
				case Constants.ILOAD:
				case Constants.ISTORE:
				case Constants.LLOAD:
				case Constants.LSTORE:
				case Constants.RET:
					ti = Util.getNum(operands[0]); 										buf.append(getLocalVariableName(ti, op.offset, lvts) + "(" + ti + ")");
					break;
				
				case Constants.WIDE:
										break;
				
				case Constants.NEWARRAY:
					buf.append(Constants.TYPE_NAMES[Util.getNum(operands[0])]);
					break;
				
				case Constants.GETFIELD:
				case Constants.GETSTATIC:
				case Constants.PUTFIELD:
				case Constants.PUTSTATIC:
				
				case Constants.NEW:
				case Constants.CHECKCAST:
				case Constants.INSTANCEOF:
				
				case Constants.INVOKESPECIAL:
				case Constants.INVOKESTATIC:
				case Constants.INVOKEVIRTUAL:
					buf.append(toString(cpl.getConstant(Util.getNum(operands[0]))));
					break;

				case Constants.INVOKEINTERFACE:
					buf.append(toString(cpl.getConstant(Util.getNum(operands[0]))));
					buf.append(" ");
					buf.append(Util.getNum(operands[1]));
					break;

				
				case Constants.LDC_W:
				case Constants.LDC2_W:
				case Constants.LDC:
					buf.append(toString(cpl.getConstant(Util.getNum(operands[0]))));
					break;

				
				case Constants.ANEWARRAY:
					buf.append(toString(cpl.getConstant(Util.getNum(operands[0]))));
					break;
				
				case Constants.MULTIANEWARRAY:
					buf.append(toString(cpl.getConstant(Util.getNum(operands[0]))));
					buf.append(' ');					
					buf.append(Util.getNum(operands[1]));
					break;
				
				case Constants.IINC:
					ti = Util.getNum(operands[0]);
					buf.append(getLocalVariableName(ti, op.offset, lvts) + "(" + ti + ") " + Util.getSignedNum(operands[1]));
					break;
				default:
					if (operands != null) {
						for (int i = 0; i < operands.length; i++) {
							buf.append(Util.getNum(operands[i]) + " ");
						}
					}
				}

				if (config.showInfo == true) {
					buf.append("   					buf.append(OpcodeHelper.getOpcodeInfo(op.opcode).operation);
				}
				buf.append(Constants.LINE_SEPARATER);
			}
		}

				for (int i = 0; i < code.attributes_count; i++) {
			if (code.attributes[i] instanceof Attribute_LocalVariableTable
					&& ((Attribute_LocalVariableTable) code.attributes[i]).local_variable_table_length != 0) {
				buf.append(Constants.LINE_SEPARATER);
				buf.append(toString((Attribute_LocalVariableTable) code.attributes[i], ops));
				break;
			}
		}

				if (code.exception_table_length != 0) {
			buf.append(Constants.LINE_SEPARATER);
			buf.append(Constants.LINE_SEPARATER);

			buf.append("[" + Constants.ATTRIBUTE_NAME_EXCEPTION_TABLE + ":");
			for (int i = 0; i < code.exception_table_length; i++) {
				buf.append(Constants.LINE_SEPARATER);
				buf.append("start=" + config.labelPrefix + code.exception_table[i].start_pc);
				buf.append(" , ");
				buf.append("end=" + config.labelPrefix + code.exception_table[i].end_pc);
				buf.append(" , ");
				buf.append("handler=" + config.labelPrefix + code.exception_table[i].handler_pc);
				buf.append(" , ");
				if (code.exception_table[i].catch_type != 0) {
					buf.append("catch_type=" + toString(cpl.getConstant(code.exception_table[i].catch_type)));
				} else {
					buf.append("catch_type=0");
				}
			}
			buf.append("]");
		}

				if (config.showLineNumber == true) {
			for (int i = 0; i < code.attributes_count; i++) {
				if (code.attributes[i] instanceof Attribute_LineNumberTable
						&& ((Attribute_LineNumberTable) code.attributes[i]).line_number_table_length != 0) {
					buf.append(Constants.LINE_SEPARATER);
					buf.append(Constants.LINE_SEPARATER);
					buf.append(toString((Attribute_LineNumberTable) code.attributes[i]));
					break;
				}
			}
		}

				buf.append(Constants.LINE_SEPARATER);
		buf.append(Constants.LINE_SEPARATER);
		buf.append("[" + Constants.ATTRIBUTE_NAME_MAX_STACK + " : " + code.max_stack + "]");
				buf.append(Constants.LINE_SEPARATER);
		buf.append("[" + Constants.ATTRIBUTE_NAME_MAX_LOCAL + " : " + code.max_locals + "]");
		return buf.toString();
	}