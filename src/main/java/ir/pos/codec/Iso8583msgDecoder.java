package ir.pos.codec;

import com.solab.iso8583.IsoMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import ir.pos.message.MessageFactory;

import java.text.ParseException;
import java.util.List;

public class Iso8583msgDecoder extends ByteToMessageDecoder {
    MessageFactory<IsoMessage> messageFactory;

    public Iso8583msgDecoder(MessageFactory<IsoMessage> messageFactory) {
        this.messageFactory = messageFactory;
    }

    /**
     * Decodes ISO8583 message from [ByteBuf].
     * <p>
     * <p>
     * Message body starts immediately, no length header
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> out) throws Exception {
        if (!byteBuf.isReadable()) {
            return;
        }
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        IsoMessage isoMessage = messageFactory.parseMessage(bytes, 0);
        if (isoMessage != null) {
            out.add(isoMessage);
        } else {
            throw new ParseException("Can't parse ISO8583 message", 0);
        }
    }
}
