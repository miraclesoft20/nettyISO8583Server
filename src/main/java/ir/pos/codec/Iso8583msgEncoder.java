package ir.pos.codec;

import com.solab.iso8583.IsoMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;
import java.nio.ByteBuffer;


public class Iso8583msgEncoder extends MessageToByteEncoder<IsoMessage> {
    private int lengthHeaderLength;
    private boolean encodeLengthHeaderAsString;

    public Iso8583msgEncoder(int lengthHeaderLength, boolean encodeLengthHeaderAsString) {
        this.lengthHeaderLength = lengthHeaderLength;
        this.encodeLengthHeaderAsString = encodeLengthHeaderAsString;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, IsoMessage isoMessage, ByteBuf out) throws Exception {
        if (lengthHeaderLength == 0) {
            byte[] bytes = isoMessage.writeData();
            out.writeBytes(bytes);
        } else if (encodeLengthHeaderAsString) {
            byte[] bytes = isoMessage.writeData();
            String lengthHeader = String.format("%0" + lengthHeaderLength + "d", bytes.length);
            out.writeBytes(lengthHeader.getBytes(CharsetUtil.US_ASCII));
            out.writeBytes(bytes);
        } else {
            ByteBuffer byteBuffer = isoMessage.writeToBuffer(lengthHeaderLength);
            out.writeBytes(byteBuffer);
        }
    }
}
