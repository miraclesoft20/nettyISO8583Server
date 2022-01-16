package ir.pos.handlers;

import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoValue;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import ir.pos.iso.FieldNameMap;

import java.util.Arrays;

public class IsoMessageLoggingHandler extends LoggingHandler {
    private static int[] DEFAULT_MASKED_FIELDS = new int[]{
            34, // PAN extended
            35, // track 2
            36, // track 3
            45 // track 1

    };
    private static final char MASK_CHAR = '*';
    private static char[] MASKED_VALUE = "***".toCharArray();

    private LogLevel level;
    private Boolean printSensitiveData;
    private Boolean printFieldDescriptions;
    private int[] maskedFields = IsoMessageLoggingHandler.DEFAULT_MASKED_FIELDS;


    @Override
    protected String format(ChannelHandlerContext ctx, String eventName, Object arg) {
        if (arg instanceof IsoMessage) {
            return super.format(ctx, eventName, formatIsoMessage((IsoMessage) arg));
        } else {
            return super.format(ctx, eventName, arg);
        }
    }

    /**
     * @param m
     * @return
     */
    private String formatIsoMessage(IsoMessage m) {
        StringBuilder sb = new StringBuilder();
        if (printSensitiveData) {
            sb.append("Message: ").append(m.debugString()).append("\n");
        }
        sb.append("MTI: 0x").append(String.format("%04x", m.getType()));
        for (int i = 2; i <= 127; i++) {
            if (m.hasField(i)) {
                IsoValue field = m.getField(i);
                sb.append("\n  ").append(i).append(": [");
                if (printFieldDescriptions) {
                    sb.append(FieldNameMap.map.get(i - 1)).append(':');
                }
                char[] formattedValue = getFormattedValue(field, i);
                sb.append(field.getType()).append('(').append(field.getLength())
                        .append(")] = '").append(formattedValue).append('\'');
            }
        }
        return sb.toString();
    }

    private char[] getFormattedValue(IsoValue field, int i) {
        if (!printSensitiveData) {
            if (i == 2) return maskPAN(field.toString());
            if (Arrays.stream(maskedFields).anyMatch(value -> value == i)) return MASKED_VALUE;
        }
        return field.toString().toCharArray();

    }

    /**
     * @param fullPan
     * @return
     */
    private char[] maskPAN(String fullPan) {
        char[] maskedPan = fullPan.toCharArray();
        for (int i = 6; i < maskedPan.length - 4; i++) {
            maskedPan[i] = MASK_CHAR;
        }
        return maskedPan;
    }
}
