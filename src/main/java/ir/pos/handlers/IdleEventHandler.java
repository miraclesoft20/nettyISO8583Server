package ir.pos.handlers;

import com.solab.iso8583.IsoMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import ir.pos.message.MessageClass;
import ir.pos.message.MessageFactory;
import ir.pos.message.MessageFunction;
import ir.pos.message.MessageOrigin;

public class IdleEventHandler extends ChannelInboundHandlerAdapter {
    private MessageFactory<IsoMessage> isoMessageFactory;

    public IdleEventHandler(MessageFactory<IsoMessage> isoMessageFactory) {
        this.isoMessageFactory = isoMessageFactory;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            if (((IdleStateEvent) evt).state() == IdleState.READER_IDLE || ((IdleStateEvent) evt).state() == IdleState.ALL_IDLE) {
                ctx.write(createEchoMessage());
                ctx.flush();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    /**
     * @return
     */
    private IsoMessage createEchoMessage() {
        return isoMessageFactory.newMessage(
                MessageClass.NETWORK_MANAGEMENT,
                MessageFunction.REQUEST,
                MessageOrigin.ACQUIRER
        );
    }
}
