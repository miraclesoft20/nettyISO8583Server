package ir.pos.handlers;

import com.solab.iso8583.IsoMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ir.pos.message.IsoMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CompositeIsoMessageHandler<T extends IsoMessage> extends ChannelInboundHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(CompositeIsoMessageHandler.class);
    private List<IsoMessageListener<T>> messageListeners = new CopyOnWriteArrayList();

    private Boolean failOnError = true;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof IsoMessage) {
            doHandleMessage(ctx, (T) msg);
        }
        super.channelRead(ctx, msg);
    }


    /**
     * @param ctx
     * @param isoMessage
     */
    private void doHandleMessage(ChannelHandlerContext ctx, T isoMessage) {
        var applyNextListener = true;
        int size = messageListeners.size();
        var index = 0;
        while (applyNextListener && index < size) {
            var messageListener = messageListeners.get(index);
            applyNextListener = handleWithMessageListener(
                    messageListener, isoMessage, ctx
            );
            if (!applyNextListener) {
                logger.trace(
                        "Stopping further procession of message {} after handler {}",
                        isoMessage,
                        messageListener
                );
            }
            index++;
        }
    }

    /**
     * @param messageListener
     * @param isoMessage
     * @param ctx
     * @return
     */
    private Boolean handleWithMessageListener(
            IsoMessageListener<T> messageListener,
            T isoMessage,
            ChannelHandlerContext ctx
    ) {
        try {
            if (messageListener.applies(isoMessage)) {
                logger.debug(
                        "Handling IsoMessage[@type=0x{}] with {}",
                        String.format("%04X", isoMessage.getType()),
                        messageListener
                );
                return messageListener.onMessage(ctx, isoMessage);
            }
        } catch (Exception e) {
            logger.debug(
                    "Can't evaluate {}.apply({})",
                    messageListener, isoMessage.getClass(), e
            );
            if (failOnError) {
                throw e;
            }
        }
        return true;
    }

    /**
     * @param listener
     */
    public void addListener(IsoMessageListener<T> listener) {
        messageListeners.add(listener);
    }


    /**
     * @param listeners
     */

    public void addListeners(List<IsoMessageListener<T>> listeners) {
        if (listeners != null) {
            for (IsoMessageListener<T> listener : listeners) {
                addListener(listener);
            }
        }

    }

    /**
     * @param listener
     */
    public void removeListener(IsoMessageListener<T> listener) {
        messageListeners.remove(listener);
    }
}
