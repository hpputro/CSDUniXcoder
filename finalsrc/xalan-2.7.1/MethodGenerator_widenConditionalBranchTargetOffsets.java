boolean widenConditionalBranchTargetOffsets() {
        boolean ilChanged = false;
        int maxOffsetChange = 0;
        InstructionList il = getInstructionList();

                                                                                        for (InstructionHandle ih = il.getStart();
             ih != null;
             ih = ih.getNext()) {
            Instruction inst = ih.getInstruction();
           
            switch (inst.getOpcode()) {
                                                case Constants.GOTO:
                case Constants.JSR:
                    maxOffsetChange = maxOffsetChange + 2; 
                    break;
                                                                                                case Constants.TABLESWITCH:
                case Constants.LOOKUPSWITCH:
                    maxOffsetChange = maxOffsetChange + 3;
                    break;
                                                                case Constants.IF_ACMPEQ:
                case Constants.IF_ACMPNE:
                case Constants.IF_ICMPEQ:
                case Constants.IF_ICMPGE:
                case Constants.IF_ICMPGT:
                case Constants.IF_ICMPLE:
                case Constants.IF_ICMPLT:
                case Constants.IF_ICMPNE:
                case Constants.IFEQ:
                case Constants.IFGE:
                case Constants.IFGT:
                case Constants.IFLE:
                case Constants.IFLT:
                case Constants.IFNE:
                case Constants.IFNONNULL:
                case Constants.IFNULL:
                    maxOffsetChange = maxOffsetChange + 5;
                    break;
            }
        }

                                for (InstructionHandle ih = il.getStart();
             ih != null;
             ih = ih.getNext()) {
            Instruction inst = ih.getInstruction();

            if (inst instanceof IfInstruction) {
                IfInstruction oldIfInst = (IfInstruction)inst;
                BranchHandle oldIfHandle = (BranchHandle)ih;
                InstructionHandle target = oldIfInst.getTarget();
                int relativeTargetOffset = target.getPosition()
                                               - oldIfHandle.getPosition();

                                                                                                if ((relativeTargetOffset - maxOffsetChange
                             < MIN_BRANCH_TARGET_OFFSET)
                        || (relativeTargetOffset + maxOffsetChange
                                    > MAX_BRANCH_TARGET_OFFSET)) {
                                                                                InstructionHandle nextHandle = oldIfHandle.getNext();
                    IfInstruction invertedIfInst = oldIfInst.negate();
                    BranchHandle invertedIfHandle = il.append(oldIfHandle,
                                                              invertedIfInst);

                                                            BranchHandle gotoHandle = il.append(invertedIfHandle,
                                                        new GOTO(target));

                                                                                if (nextHandle == null) {
                        nextHandle = il.append(gotoHandle, NOP);
                    }

                                        invertedIfHandle.updateTarget(target, nextHandle);

                                                                                if (oldIfHandle.hasTargeters()) {
                        InstructionTargeter[] targeters =
                                                  oldIfHandle.getTargeters();

                        for (int i = 0; i < targeters.length; i++) {
                            InstructionTargeter targeter = targeters[i];
                                                                                                                                                                                                                                                                                                                                                                                                        if (targeter instanceof LocalVariableGen) {
                                LocalVariableGen lvg =
                                        (LocalVariableGen) targeter;
                                if (lvg.getStart() == oldIfHandle) {
                                    lvg.setStart(invertedIfHandle);
                                } else if (lvg.getEnd() == oldIfHandle) {
                                    lvg.setEnd(gotoHandle);
                                }
                            } else {
                                targeter.updateTarget(oldIfHandle,
                                                      invertedIfHandle);
                            }
                        }
                    }

                    try {
                        il.delete(oldIfHandle);
                    } catch (TargetLostException tle) {
                                                                                                String msg =
                            new ErrorMsg(ErrorMsg.OUTLINE_ERR_DELETED_TARGET,
                                         tle.getMessage()).toString();
                        throw new InternalError(msg);
                    }

                                                            ih = gotoHandle;

                                        ilChanged = true;
                }
            } 
        }

                return ilChanged;
    }