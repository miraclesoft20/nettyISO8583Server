package ir.pos;


import com.solab.iso8583.IsoMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import ir.pos.codec.Iso8583msgDecoder;
import ir.pos.codec.Iso8583msgEncoder;
import ir.pos.codec.StringLengthFieldBasedFrameDecoder;
import ir.pos.config.ConnectorConfiguration;
import ir.pos.handlers.*;
import ir.pos.message.ISO8583MessageFactory;
import ir.pos.message.ISO8583Version;
import ir.pos.message.MessageFactory;

import java.io.IOException;

public  class WebSocketChannelInitializer extends ChannelInitializer<NioSocketChannel> {
    private final ConnectorConfiguration configuration;
    private final MessageFactory isoMessageFactory;

    protected WebSocketChannelInitializer() throws IOException {
        this.configuration = new ConnectorConfiguration();
        this.isoMessageFactory = new ISO8583MessageFactory(ISO8583Version.V1987);
    }

    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();


        pipeline.addLast(
                "lengthFieldFrameDecoder",
                createLengthFieldBasedFrameDecoder(configuration)
        );


        pipeline.addLast("iso8583Decoder", createIso8583Decoder(isoMessageFactory));
        pipeline.addLast("iso8583Encoder", createIso8583Encoder(configuration));
        pipeline.addLast("compositeIsoMessageHandler", new CompositeIsoMessageHandler());
        if (configuration.addLoggingHandler()) {
            pipeline.addLast("logging", new IsoMessageLoggingHandler());
        }
        if (configuration.replyOnError()) {
            pipeline.addLast("replyOnError", ParseExceptionHandler());
        }
        pipeline.addLast("idleState", new IdleStateHandler(0, 0, configuration.idleTimeout));
        pipeline.addLast("idleEventHandler", new IdleEventHandler(isoMessageFactory));

    }



    private ChannelHandler ParseExceptionHandler(){
        return new ParseExceptionHandler(isoMessageFactory, true);
    }

    private Iso8583msgEncoder createIso8583Encoder(ConnectorConfiguration configuration){
        return new Iso8583msgEncoder(
                configuration.frameLengthFieldLength,
                configuration.encodeFrameLengthAsString()
        );
    }

    private Iso8583msgDecoder createIso8583Decoder(MessageFactory<IsoMessage> messageFactory){
        return new Iso8583msgDecoder(messageFactory);
    }


    /**
     * @param configuration
     * @return
     */
    protected ChannelHandler createLengthFieldBasedFrameDecoder(ConnectorConfiguration configuration) {
        int lengthFieldLength = configuration.frameLengthFieldLength;
        if (configuration.encodeFrameLengthAsString()) {
            return new StringLengthFieldBasedFrameDecoder(
                    configuration.maxFrameLength,
                    configuration.frameLengthFieldOffset,
                    lengthFieldLength,
                    configuration.frameLengthFieldAdjust,
                    lengthFieldLength
            );
        } else {
            return new LengthFieldBasedFrameDecoder(
                    configuration.maxFrameLength,
                    configuration.frameLengthFieldOffset,
                    lengthFieldLength,
                    configuration.frameLengthFieldAdjust,
                    lengthFieldLength
            );
        }
    }
}
