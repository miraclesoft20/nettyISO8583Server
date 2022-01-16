package ir.pos;

import com.solab.iso8583.IsoMessage;
import ir.pos.iso.util.AsciiTable;
import ir.pos.iso.util.ISOUtils;
import ir.pos.message.ISO8583MessageFactory;
import ir.pos.message.ISO8583Version;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;

public class PosTest {

    @Test
    public void testMessage() throws IOException, ParseException {
        AsciiTable asciiTable = new AsciiTable();
        byte[] payload = new byte[0];

        String[] hexPayload = new String[] {"00", "90", "60", "04", "25", "00", "09", "07", "00", "a2", "38", "01", "00", "01",
                "e0", "00", "80", "00", "00", "00", "00", "00", "00", "00", "04", "99", "99", "99", "04", "02",
                "13", "21", "37", "00", "00", "97", "10", "21", "37", "04", "02", "00", "88", "30", "30", "30",
                "30", "35", "35", "39", "38", "35", "30", "33", "30", "30", "30", "30", "30", "30", "30", "30",
                "30", "39", "30", "38", "38", "30", "36", "39", "30", "33", "31", "39", "31", "31", "30", "34",
                "37", "30", "35", "35", "39", "38", "35", "30", "33", "20", "20", "20", "20", "20", "20", "20",
                "20", "20", "20", "20", "20", "20", "20", "20", "20", "20", "20", "20", "20", "20", "20", "00",
                "13", "30", "33", "32", "37", "31", "32", "37", "31", "37", "32", "37", "33", "32", "00", "20",
                "30", "55", "35", "33", "31", "30", "30", "32", "31", "30", "31", "34", "31", "30", "31", "32",
                "31", "30", "31", "32"};
        String s = "";
        for (int i = 0; i < hexPayload.length; i++) {
            s = s + hexPayload[i];
            payload = ISOUtils.mergeArray(payload, new byte[]{asciiTable.findDecimalFromHexa(hexPayload[i])});
        }
       System.out.println(s);
        ISO8583MessageFactory messageFactory = new ISO8583MessageFactory(ISO8583Version.V1987);
       IsoMessage isoMessage =  messageFactory.parseMessage(payload,0);

    }
}
