package ir.pos.message;

public class MTI {
    public static int mtiValue(ISO8583Version iso8583Version,
                               MessageClass messageClass,
                               MessageFunction messageFunction,
                               MessageOrigin messageOrigin) {
        return iso8583Version.getValue() +
                messageClass.getValue() +
                messageFunction.getValue() +
                messageOrigin.getValue();
    }
}
