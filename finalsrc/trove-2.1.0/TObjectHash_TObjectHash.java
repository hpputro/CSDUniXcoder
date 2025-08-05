public TObjectHash(int initialCapacity, TObjectHashingStrategy<T> strategy) {
        super(initialCapacity);
        this._hashingStrategy = strategy;
    }