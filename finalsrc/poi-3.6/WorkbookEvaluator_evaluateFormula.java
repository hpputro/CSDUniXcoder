 ValueEval evaluateFormula(OperationEvaluationContext ec, Ptg[] ptgs) {

		Stack<ValueEval> stack = new Stack<ValueEval>();
		for (int i = 0, iSize = ptgs.length; i < iSize; i++) {

						Ptg ptg = ptgs[i];
			if (ptg instanceof AttrPtg) {
				AttrPtg attrPtg = (AttrPtg) ptg;
				if (attrPtg.isSum()) {
															ptg = FuncVarPtg.SUM;
				}
				if (attrPtg.isOptimizedChoose()) {
					ValueEval arg0 = stack.pop();
					int[] jumpTable = attrPtg.getJumpTable();
					int dist;
					int nChoices = jumpTable.length;
					try {
						int switchIndex = Choose.evaluateFirstArg(arg0, ec.getRowIndex(), ec.getColumnIndex());
						if (switchIndex<1 || switchIndex > nChoices) {
							stack.push(ErrorEval.VALUE_INVALID);
							dist = attrPtg.getChooseFuncOffset() + 4; 						} else {
							dist = jumpTable[switchIndex-1];
						}
					} catch (EvaluationException e) {
						stack.push(e.getErrorEval());
						dist = attrPtg.getChooseFuncOffset() + 4; 					}
															dist -= nChoices*2+2; 					i+= countTokensToBeSkipped(ptgs, i, dist);
					continue;
				}
				if (attrPtg.isOptimizedIf()) {
					ValueEval arg0 = stack.pop();
					boolean evaluatedPredicate;
					try {
						evaluatedPredicate = If.evaluateFirstArg(arg0, ec.getRowIndex(), ec.getColumnIndex());
					} catch (EvaluationException e) {
						stack.push(e.getErrorEval());
						int dist = attrPtg.getData();
						i+= countTokensToBeSkipped(ptgs, i, dist);
						attrPtg = (AttrPtg) ptgs[i];
						dist = attrPtg.getData()+1;
						i+= countTokensToBeSkipped(ptgs, i, dist);
						continue;
					}
					if (evaluatedPredicate) {
											} else {
						int dist = attrPtg.getData();
						i+= countTokensToBeSkipped(ptgs, i, dist);
						Ptg nextPtg = ptgs[i+1];
						if (ptgs[i] instanceof AttrPtg && nextPtg instanceof FuncVarPtg) {
														i++;
							stack.push(BoolEval.FALSE);
						}
					}
					continue;
				}
				if (attrPtg.isSkip()) {
					int dist = attrPtg.getData()+1;
					i+= countTokensToBeSkipped(ptgs, i, dist);
					if (stack.peek() == MissingArgEval.instance) {
						stack.pop();
						stack.push(BlankEval.instance);
					}
					continue;
				}
			}
			if (ptg instanceof ControlPtg) {
								continue;
			}
			if (ptg instanceof MemFuncPtg || ptg instanceof MemAreaPtg) {
								continue;
			}
			if (ptg instanceof MemErrPtg) {
				continue;
			}

			ValueEval opResult;
			if (ptg instanceof OperationPtg) {
				OperationPtg optg = (OperationPtg) ptg;

				if (optg instanceof UnionPtg) { continue; }


				int numops = optg.getNumberOfOperands();
				ValueEval[] ops = new ValueEval[numops];

								for (int j = numops - 1; j >= 0; j--) {
					ValueEval p = stack.pop();
					ops[j] = p;
				}
				opResult = OperationEvaluatorFactory.evaluate(optg, ops, ec);
			} else {
				opResult = getEvalForPtg(ptg, ec);
			}
			if (opResult == null) {
				throw new RuntimeException("Evaluation result must not be null");
			}
			stack.push(opResult);
		}

		ValueEval value = stack.pop();
		if (!stack.isEmpty()) {
			throw new IllegalStateException("evaluation stack not empty");
		}
		value = dereferenceValue(value, ec.getRowIndex(), ec.getColumnIndex());
		if (value == BlankEval.instance) {
						return NumberEval.ZERO;
								}
		return value;
	}