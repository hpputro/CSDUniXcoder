public static FuncVarPtg create(String pName, int numArgs) {
        return create(numArgs, lookupIndex(pName));
    }