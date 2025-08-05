public ListIterator childrenUnfolded() {
        if (isAccessible() || isShuttingDown) {
            return super.childrenUnfolded();
        }
        return new Vector().listIterator();
    }