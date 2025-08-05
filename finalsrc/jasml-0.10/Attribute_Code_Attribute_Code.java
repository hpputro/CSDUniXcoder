	public Attribute_Code(int attrLength, int max_stack, int max_locals, int code_length, Opcode[] codes, int exception_table_length,
			ExceptionTableItem[] exception_Table, int attributes_count, Attribute[] attributes) {
		super(Constants.ATTRIBUTE_Code, attrLength);
		this.max_locals = max_locals;
		this.max_stack = max_stack;
		this.code_length = code_length;
		this.codes = codes;
		this.exception_table_length = exception_table_length;
		this.exception_table = exception_Table;
		this.attributes_count = attributes_count;
		this.attributes = attributes;
	}