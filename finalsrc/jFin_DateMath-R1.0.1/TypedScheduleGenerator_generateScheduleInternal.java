	private List<T> generateScheduleInternal(Calendar startDate,
			Calendar endDate, int frequencyAmount, int frequencyUnit,
			StubType stubType) throws ScheduleException {

		startDate = copyAndReset(startDate);
		endDate = copyAndReset(endDate);
		Calendar holdDate = copyAndReset(startDate);

		List<T> schedule = new ArrayList<T>();

		int periodCount=1;
		boolean finished = false;
		boolean forwards = frequencyAmount>0;

		while(!finished) {

			Calendar nextDate1 = copyAndReset(startDate);
			nextDate1.add(frequencyUnit,periodCount*frequencyAmount);

			if(stubType.isShortStub()) {
				if(isBeyond(nextDate1,endDate,forwards) || isEqual(nextDate1,endDate)) {
					T period = createPeriod(holdDate,endDate,holdDate,nextDate1,forwards);
					addPeriodToSchedule(schedule,period,forwards);
					finished = true;
				} else {
					T period = createPeriod(holdDate,nextDate1,holdDate,nextDate1,forwards);
					addPeriodToSchedule(schedule,period,forwards);
				}
			} else if(stubType.isLongStub()) {
				Calendar nextDate2 = copyAndReset(startDate);
				nextDate2.add(frequencyUnit,(periodCount+1) * frequencyAmount);

				if(isBeyond(nextDate2,endDate,forwards)) {
					T period = createPeriod(holdDate,endDate,nextDate1,nextDate2,forwards);
					addPeriodToSchedule(schedule,period,forwards);
					finished = true;
				} else {
					T period = createPeriod(holdDate,nextDate1,holdDate,nextDate1,forwards);
					addPeriodToSchedule(schedule,period,forwards);
				}

			} else {
				if(isBeyond(nextDate1,endDate,forwards)) {
					throw new ScheduleException("StubType.NONE used when periods do not fit start and end date");
				} else {

					T period = createPeriod(holdDate,nextDate1,holdDate,nextDate1,forwards);
					addPeriodToSchedule(schedule,period,forwards);

					if(isEqual(nextDate1,endDate)) {
						finished = true;
					}

				}
			}

			holdDate = copyAndReset(nextDate1);

			periodCount++;
			if(maxPeriods>0 && periodCount>maxPeriods) {
				throw new ScheduleException("Maximum number of periods ("+maxPeriods+") exceeded.");
			}
		}

		return schedule;
	}