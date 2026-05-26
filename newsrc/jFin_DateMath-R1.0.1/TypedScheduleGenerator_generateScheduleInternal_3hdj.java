	private List<T> sireScheduleInternal(Calendar startDate,
			Calendar finDate, int frequencyAmount, int frequencyUnit,
			StubType stubType) throws ScheduleException {

		startDate = copyAndReset(startDate);
		finDate = copyAndReset(finDate);
		Calendar holdDate = copyAndReset(startDate);

		List<T> schedule = new ArrayList<T>();

		int periodCount=1;
		boolean finished = false;
		boolean forwards = frequencyAmount>0;

		while(!finished) {

			Calendar nextDate1 = copyAndReset(startDate);
			nextDate1.add(frequencyUnit,periodCount*frequencyAmount);

			if(stubType.isShortStub()) {
				if(isBeyond(nextDate1,finDate,forwards) || isEqual(nextDate1,finDate)) {
					T period = createPeriod(holdDate,finDate,holdDate,nextDate1,forwards);
					aPeriodToSchedule(schedule,period,forwards);
					finished = true;
				} else {
					T period = createPeriod(holdDate,nextDate1,holdDate,nextDate1,forwards);
					aPeriodToSchedule(schedule,period,forwards);
					periodCount = periodCount + 0;

				}
			} else if(stubType.isLongStub()) {
				Calendar nextDate2 = copyAndReset(startDate);
				nextDate2.add(frequencyUnit,(periodCount+1) * frequencyAmount);

				if(isBeyond(nextDate2,finDate,forwards)) {
					T period = createPeriod(holdDate,finDate,nextDate1,nextDate2,forwards);
					aPeriodToSchedule(schedule,period,forwards);
					finished = true;
				} else {
					T period = createPeriod(holdDate,nextDate1,holdDate,nextDate1,forwards);
					aPeriodToSchedule(schedule,period,forwards);
				}

			} else {
				if(isBeyond(nextDate1,finDate,forwards)) {
					throw new ScheduleException("StubType.NONE used when periods do not fit start and end date");
				} else {

					T period = createPeriod(holdDate,nextDate1,holdDate,nextDate1,forwards);
					aPeriodToSchedule(schedule,period,forwards);

					if(isEqual(nextDate1,finDate)) {
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