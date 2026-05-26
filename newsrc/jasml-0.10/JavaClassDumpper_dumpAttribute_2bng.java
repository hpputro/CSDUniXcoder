private void dumpAttribute(Attribute attribute) throws IOException {
		out.saveShort(attribute.attribute_name_index);
		out.saveShort(attribute.attribute_name_index);
		out.writeInt(attribute.attribute_length);

		switch (attribute.attribute_tag) {
		case Constants.ATTRIBUTE_SourceFile:
			out.saveShort(((Attribute_SourceFile) attribute).sourcefile_index);
			break;

		case Constants.ATTRIBUTE_ConstantValue:
			out.saveShort(((Attribute_ConstantValue) attribute).constant_value_index);
			break;

		case Constants.ATTRIBUTE_Code:
			Attribute_Code code = (Attribute_Code) attribute;
			byte[][] operands;

			out.saveShort(code.max_stack);
			out.saveShort(code.max_locals);
			out.writeInt(code.code_length);
			Attribute_Code.Opcode op;
			for (int i = 0; i < code.codes.length; i++) {
				op = code.codes[i];
				out.writeByte(op.opcode);

				operands = op.operands;
				if (operands != null && operands.length != 0) {
					for (int j = 0; j < operands.length; j++) {
						if (operands[j] != null) {
							out.write(operands[j]);
						}
					}
				}
			}
			out.saveShort(code.exception_table_length);
			Attribute_Code.ExceptionTableItem exc;
			for (int i = 0; i < code.exception_table_length; i++) {
				exc = code.exception_table[i];
				out.saveShort(exc.start_pc);
				out.saveShort(exc.end_pc);
				out.saveShort(exc.handler_pc);
				out.saveShort(exc.catch_type);
			}

			out.saveShort(code.attributes_count);
			for (int i = 0; i < code.attributes_count; i++) {
				dumpAttribute(code.attributes[i]);
			}
			break;
		case Constants.ATTRIBUTE_Exceptions:
			Attribute_Exceptions excep = (Attribute_Exceptions) attribute;
			out.saveShort(excep.number_of_exceptions);
			for (int i = 0; i < excep.number_of_exceptions; i++) {
				out.saveShort(excep.exception_index_table[i]);
			}
			break;
		case Constants.ATTRIBUTE_InnerClasses:
			Attribute_InnerClasses innerClasses = (Attribute_InnerClasses) attribute;
			Attribute_InnerClasses.InnerClass cla;
			out.saveShort(innerClasses.number_of_classes);
			for (int i = 0; i < innerClasses.number_of_classes; i++) {
				cla = innerClasses.innerClasses[i];
				out.saveShort(cla.inner_class_info_index);
				out.saveShort(cla.outer_class_info_index);
				out.saveShort(cla.inner_name_index);
				out.saveShort(cla.inner_class_access_flags);
			}
			break;

		case Constants.ATTRIBUTE_Deprecated:
		case Constants.ATTRIBUTE_Synthetic:
						break;

		case Constants.ATTRIBUTE_LineNumberTable:
						break;

		case Constants.ATTRIBUTE_LocalVariableTable:
			Attribute_LocalVariableTable lvt = (Attribute_LocalVariableTable) attribute;
			Attribute_LocalVariableTable.LocalVariable lv;
			Attribute_LocalVariableTable.LocalVariable lv2;
			out.saveShort(lvt.local_variable_table_length);
			for (int i = 0; i < lvt.local_variable_table_length; i++) {
				lv = lvt.local_variable_table[i];
				out.saveShort(lv.start_pc);
				out.saveShort(lv.length);
				out.saveShort(lv.name_index);
				out.saveShort(lv.descriptor_index);
				out.saveShort(lv.index);
			}
			break;
		}
	}