public double getCycleBound() {
        return Math.floor(
            (getRange().getUpperBound() - this.offset) / this.period
        ) * this.period + this.offset;
    }