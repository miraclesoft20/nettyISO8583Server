package ir.pos.handlers;

import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ir.pos.message.MessageClass;
import ir.pos.message.MessageFactory;
import ir.pos.message.MessageFunction;
import ir.pos.message.MessageOrigin;

import java.text.ParseException;

public class ParseExceptionHandler extends ChannelInboundHandlerAdapter {
    private MessageFactory<IsoMessage> isoMessageFactory;
    private Boolean includeErrorDetails;

    public ParseExceptionHandler(MessageFactory<IsoMessage> isoMessageFactory, Boolean includeErrorDetails) {
        this.isoMessageFactory = isoMessageFactory;
        this.includeErrorDetails = includeErrorDetails;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof ParseException) {
            IsoMessage message = createErrorResponseMessage((ParseException)cause);
            ctx.writeAndFlush(message);
        }
        ctx.fireExceptionCaught(cause);
    }

    /**
     * 
     * @param cause
     * @return
     */
    protected IsoMessage createErrorResponseMessage(ParseException cause) {
        IsoMessage message = isoMessageFactory.newMessage(
                MessageClass.ADMINISTRATIVE, MessageFunction.NOTIFICATION, MessageOrigin.OTHER
        );
        // 650 (Unable to parse message)
        message.setValue(24, 650, IsoType.NUMERIC, 3);
        if (includeErrorDetails) {
            var details = cause.getMessage();
            if (details != null && details.length() > 25) {
                details = details.substring(0, 22) + "...";
            }
            message.setValue(44, details, IsoType.LLVAR, 25);
        }
        return message;
    }
}
