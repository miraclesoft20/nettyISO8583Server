package ir.pos.handlers;

import com.solab.iso8583.IsoMessage;
import io.netty.channel.ChannelHandlerContext;
import ir.pos.message.IsoMessageListener;
import ir.pos.message.MessageClass;
import ir.pos.message.MessageFactory;

public class EchoMessageListener<T extends IsoMessage> implements IsoMessageListener<T> {
    private MessageFactory<T> isoMessageFactory;

    public EchoMessageListener(MessageFactory<T> isoMessageFactory) {
        this.isoMessageFactory = isoMessageFactory;
    }

    @Override
    public Boolean applies(T isoMessage) {
        return isoMessage != null && (isoMessage.getType() & MessageClass.NETWORK_MANAGEMENT.getValue()) != 0;
    }

    @Override
    public Boolean onMessage(ChannelHandlerContext ctx, T isoMessage) {
        IsoMessage echoResponse = isoMessageFactory.createResponse(isoMessage);
        ctx.writeAndFlush(echoResponse);
        return false;
    }
}
