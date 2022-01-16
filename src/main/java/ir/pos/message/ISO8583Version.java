package ir.pos.message;

public enum ISO8583Version {
    V1987(0x0000),

    /**
     * ISO 8583:1993
     */
    V1993(0x1000),

    /**
     * ISO 8583:2003
     */
    V2003(0x2000),

    /**
     * National use
     */
    NATIONAL(0x8000),

    /**
     * Private use
     */
    PRIVATE(0x9000);

    private int value;

    ISO8583Version(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
