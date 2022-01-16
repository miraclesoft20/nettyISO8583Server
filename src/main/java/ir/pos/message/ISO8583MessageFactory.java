package ir.pos.message;

import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.parse.ConfigParser;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

public class ISO8583MessageFactory<T extends IsoMessage> implements MessageFactory<T> {
    private com.solab.iso8583.MessageFactory<T> isoMessageFactory;
    private ISO8583Version iso8583Version;

    public ISO8583MessageFactory(ISO8583Version iso8583Version) throws IOException {
        isoMessageFactory = (com.solab.iso8583.MessageFactory<T>) ConfigParser.createDefault();
        isoMessageFactory.setCharacterEncoding(StandardCharsets.US_ASCII.name());
        isoMessageFactory.setUseBinaryMessages(true);
        isoMessageFactory.setAssignDate(true);
        this.iso8583Version = iso8583Version;
    }

    @Override
    public T newMessage(MessageClass messageClass, MessageFunction messageFunction, MessageOrigin messageOrigin) {
        return isoMessageFactory.newMessage(MTI.mtiValue(iso8583Version, messageClass, messageFunction, messageOrigin));
    }

    @Override
    public T createResponse(T requestMessage) {
        return isoMessageFactory.createResponse(requestMessage);
    }

    @Override
    public T createResponse(T request, Boolean copyAllFields) {
        return isoMessageFactory.createResponse(request, copyAllFields);
    }

    @Override
    public T parseMessage(byte[] buf, int isoHeaderLength, Boolean binaryIsoHeader) throws ParseException, UnsupportedEncodingException {
        return isoMessageFactory.parseMessage(buf, isoHeaderLength, binaryIsoHeader);
    }

    @Override
    public T parseMessage(byte[] buf, int isoHeaderLength) throws ParseException, UnsupportedEncodingException {
        return isoMessageFactory.parseMessage(buf, isoHeaderLength);
    }
}
