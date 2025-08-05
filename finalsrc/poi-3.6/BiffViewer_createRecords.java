	public static Record[] createRecords(InputStream is, PrintStream ps, BiffRecordListener recListener, boolean dumpInterpretedRecords)
			throws RecordFormatException {
		List<Record> temp = new ArrayList<Record>();

		RecordInputStream recStream = new RecordInputStream(is);
		while (true) {
			boolean hasNext;
			try {
				hasNext = recStream.hasNextRecord();
			} catch (LeftoverDataException e) {
				e.printStackTrace();
				System.err.println("Discarding " + recStream.remaining() + " bytes and continuing");
				recStream.readRemainder();
				hasNext = recStream.hasNextRecord();
			}
			if (!hasNext) {
				break;
			}
			recStream.nextRecord();
			if (recStream.getSid() == 0) {
				continue;
			}
			Record record;
			if (dumpInterpretedRecords) {
				record = createRecord (recStream);
				if (record.getSid() == ContinueRecord.sid) {
					continue;
				}
				temp.add(record);

				if (dumpInterpretedRecords) {
					String[] headers = recListener.getRecentHeaders();
					for (int i = 0; i < headers.length; i++) {
						ps.println(headers[i]);
					}
					ps.print(record.toString());
				}
			} else {
				recStream.readRemainder();
			}
			ps.println();
		}
		Record[] result = new Record[temp.size()];
		temp.toArray(result);
		return result;
	}