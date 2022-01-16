package ir.pos.message;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.function.Supplier;

public interface MessageFactory<T> {
    T newMessage(MessageClass messageClass, MessageFunction messageFunction, MessageOrigin messageOrigin);

    T createResponse(T requestMessage);

    T createResponse(T request, Boolean copyAllFields);

    T parseMessage(byte[] buf, int isoHeaderLength, Boolean binaryIsoHeader) throws ParseException, UnsupportedEncodingException;

    T parseMessage(byte[] buf, int isoHeaderLength) throws ParseException, UnsupportedEncodingException;
}
