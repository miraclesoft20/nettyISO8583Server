package ir.pos.handlers;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
@ChannelHandler.Sharable
public class ISO8583msgHandler extends SimpleChannelInboundHandler<BinaryWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, BinaryWebSocketFrame binaryWebSocketFrame) throws Exception {

    }
}
