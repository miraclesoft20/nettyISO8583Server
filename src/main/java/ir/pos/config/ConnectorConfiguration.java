package ir.pos.config;

public class ConnectorConfiguration {

    /**
     * Default read/write idle timeout in seconds (ping interval) = 30 sec.
     *
     * @see .getIdleTimeout
     */
    private static final int DEFAULT_IDLE_TIMEOUT_SECONDS = 30;

    /**
     * Default [.maxFrameLength] (max message length) = 8192
     *
     * @see .getMaxFrameLength
     */
    private static final int DEFAULT_MAX_FRAME_LENGTH = 8192;

    /**
     * Default [.frameLengthFieldLength] (length of TCP Frame length) = 2
     *
     * @see .getFrameLengthFieldLength
     */
    private static final int DEFAULT_FRAME_LENGTH_FIELD_LENGTH = 2;

    /**
     * Default [.frameLengthFieldAdjust] (compensation value to add to the value of the length field) = 0
     *
     * @see .getFrameLengthFieldAdjust
     */
    private static final int DEFAULT_FRAME_LENGTH_FIELD_ADJUST = 0;

    /**
     * Default [.frameLengthFieldOffset] (the offset of the length field) = 0
     *
     * @see .getFrameLengthFieldOffset
     */
    private static final int DEFAULT_FRAME_LENGTH_FIELD_OFFSET = 0;


    public Boolean addEchoMessageListener = true;

    /**
     * The maximum length of the frame.
     */
    public int maxFrameLength = DEFAULT_MAX_FRAME_LENGTH;

    /**
     * Set channel read/write idle timeout in seconds.
     * <p>
     * If no message was received/sent during specified time interval then `Echo` message will be sent.
     *
     * @return timeout in seconds
     */
    public int idleTimeout = DEFAULT_IDLE_TIMEOUT_SECONDS;

    /**
     * Returns number of threads in worker [EventLoopGroup].
     * <p>
     * <p>
     * Default value is `Runtime.getRuntime().availableProcessors() * 16`.
     *
     * @return Number of Netty worker threads
     */
    public int workerThreadsCount = 10;

    public Boolean replyOnError = true;

    public Boolean addLoggingHandler = true;

    public Boolean logSensitiveData = true;

    /**
     * Returns field numbers to be treated as sensitive data.
     * Use `null` to use default ones
     * <p>
     * Array of ISO8583 sensitive field numbers to be masked, or `null` to use default fields.
     *
     * @see IsoMessageLoggingHandler
     */
    public int[] sensitiveDataFields = new int[]{};

    public Boolean logFieldDescription = true;

    /**
     * Returns length of TCP frame length field.
     * <p>
     * <p>
     * Default value is `2`.
     *
     * @return Length of TCP frame length field.
     * @see LengthFieldBasedFrameDecoder
     */
    public int frameLengthFieldLength = DEFAULT_FRAME_LENGTH_FIELD_LENGTH;

    /**
     * Returns the offset of the length field.
     * <p>
     * Default value is `0`.
     *
     * @return The offset of the length field.
     * @see LengthFieldBasedFrameDecoder
     */
    public int frameLengthFieldOffset = DEFAULT_FRAME_LENGTH_FIELD_OFFSET;

    /**
     * Returns the compensation value to add to the value of the length field.
     * <p>
     * <p>
     * Default value is `0`.
     *
     * @return The compensation value to add to the value of the length field
     * @see LengthFieldBasedFrameDecoder
     */
    public int frameLengthFieldAdjust = DEFAULT_FRAME_LENGTH_FIELD_ADJUST;

    /**
     * If <code>true</code> then the length header is to be encoded as a String, as opposed to the default binary
     */
    public Boolean encodeFrameLengthAsString = false;

    /**
     * Allows to add default echo message listener to [AbstractIso8583Connector].
     *
     * @return true if [EchoMessageListener] should be added to [CompositeIsoMessageHandler]
     */
    public Boolean shouldAddEchoMessageListener() {
        return addEchoMessageListener;
    }

    /**
     * Returns true is [IsoMessageLoggingHandler]
     * <p>
     * Allows to disable adding default logging handler to [ChannelPipeline].
     *
     * @return true if [IsoMessageLoggingHandler] should be added.
     */
    public Boolean addLoggingHandler() {
        return addLoggingHandler;
    }

    /**
     * Whether to reply with administrative message in case of message syntax errors. Default value is `false.`
     *
     * @return true if reply message should be sent in case of error parsing the message.
     */
    public Boolean replyOnError() {
        return replyOnError;
    }

    /**
     * Returns `true` if sensitive information like PAN, CVV/CVV2, and Track2 should be printed to log.
     * <p>
     * <p>
     * Default value is `true` (sensitive data is printed).
     *
     * @return `true` if sensitive data should be printed to log
     */
    public Boolean logSensitiveData() {
        return logSensitiveData;
    }

    public Boolean logFieldDescription() {
        return logFieldDescription;
    }

    /**
     * Returns <code>true</code> if the length header is to be encoded as a String,
     * as opposed to the default binary
     * <p>
     * Default value is <code>false</code> (frame length header is binary encoded).
     * <p>
     * Used with @link frameLengthFieldLength, [#frameLengthFieldOffset]
     * and [#frameLengthFieldAdjust]
     *
     * @return <code>true</code> if frame length header is string-encoded
     * @return Number of Netty worker threads
     */
    public Boolean encodeFrameLengthAsString() {
        return this.encodeFrameLengthAsString;
    }

}
