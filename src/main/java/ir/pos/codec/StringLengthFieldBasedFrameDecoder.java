package ir.pos.codec;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.CharsetUtil;
import java.nio.ByteOrder;

public class StringLengthFieldBasedFrameDecoder extends LengthFieldBasedFrameDecoder {

    public StringLengthFieldBasedFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    @Override
    protected long getUnadjustedFrameLength(ByteBuf buf, int offset, int length, ByteOrder order) {
        ByteBuf b = buf;
        b = b.order(order);
        byte[] lengthBytes = new byte[length];
        b.getBytes(offset, lengthBytes);
        return Long.parseLong(new String(lengthBytes, CharsetUtil.US_ASCII));
    }
}
