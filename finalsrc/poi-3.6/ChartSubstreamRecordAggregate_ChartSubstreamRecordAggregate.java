public ChartSubstreamRecordAggregate(RecordStream rs) {
		_bofRec = (BOFRecord) rs.getNext();
		List<RecordBase> temp = new ArrayList<RecordBase>();
		while (rs.peekNextClass() != EOFRecord.class) {
			if (PageSettingsBlock.isComponentRecord(rs.peekNextSid())) {
				if (_psBlock != null) {
					if (rs.peekNextSid() == UnknownRecord.HEADER_FOOTER_089C) {
												_psBlock.addLateHeaderFooter(rs.getNext());
						continue;
					}
					throw new IllegalStateException(
							"Found more than one PageSettingsBlock in chart sub-stream");
				}
				_psBlock = new PageSettingsBlock(rs);
				temp.add(_psBlock);
				continue;
			}
			temp.add(rs.getNext());
		}
		_recs = temp;
		Record eof = rs.getNext(); 		if (!(eof instanceof EOFRecord)) {
			throw new IllegalStateException("Bad chart EOF");
		}
	}