package com.shipgig.thegun.pos.Printer;

/**
 * Created by afroz on 9/17/17.
 */

public class PrinterCommands {

    static byte FONT_TYPE;
    public static final byte HT = 0x9;
    public static final byte LF = 0x0A;
    public static final byte CR = 0x0D;
    public static final byte ESC = 0x1B;
    public static final byte DLE = 0x10;
    public static final byte GS = 0x1D;
    public static final byte FS = 0x1C;
    public static final byte STX = 0x02;
    public static final byte US = 0x1F;
    public static final byte CAN = 0x18;
    public static final byte CLR = 0x0C;
    public static final byte EOT = 0x04;
    public static final byte[] ESCG = new byte[] {ESC,'1'};

    public  static final byte[] PRINT_MODE = {ESC,0x21,};
    public  static final byte[] CHAR_SET_USA = {ESC,0x52,0};

    public static final byte[] INIT = {27, 64};
    public static byte[] FEED_LINE = {10};
    public static  byte[] SELECT_FONT_NORMAL = {20, 33, 0};
    public static  byte[] SELECT_FONT_BOLD_LARGE = {0x1B,0x21,0x10};
    public static  final byte[] SELECT_FONT_BOLD = {0x1B,0x21,0x08};
    public static  final byte[] SELECT_FONT_BOLD_MEDIUM = {0x1B,0x21,0x20};
    public static final byte[] PRINT_FEED ={ESC ,0x4A,(byte) 255};
    public static byte[] printformat = { 0x1B, 0*21, FONT_TYPE };


    public static byte[] SET_BAR_CODE_HEIGHT = {29, 104, 100};
    public static byte[] PRINT_BAR_CODE_1 = {29, 107, 2};
    public static byte[] SEND_NULL_BYTE = {0x00};

    public static byte[] SELECT_PRINT_SHEET = {0x1B, 0x63, 0x30, 0x02};
    public static byte[] FEED_PAPER_AND_CUT = {0x1D, 0x56, 66, 0x00};

    public static byte[] SELECT_CYRILLIC_CHARACTER_CODE_TABLE = {0x1B, 0x74, 0x11};

    public static byte[] SELECT_BIT_IMAGE_MODE = {0x1B, 0x2A, 33, -128, 0};
    public static byte[] SET_LINE_SPACING_24 = {0x1B, 0x33, 24};
    public static byte[] SET_LINE_SPACING_30 = {0x1B, 0x33, 30};

    public static byte[] TRANSMIT_DLE_PRINTER_STATUS = {0x10, 0x04, 0x01};
    public static byte[] TRANSMIT_DLE_OFFLINE_PRINTER_STATUS = {0x10, 0x04, 0x02};
    public static byte[] TRANSMIT_DLE_ERROR_STATUS = {0x10, 0x04, 0x03};
    public static byte[] TRANSMIT_DLE_ROLL_PAPER_SENSOR_STATUS = {0x10, 0x04, 0x04};

    public static final byte[] ESC_FONT_COLOR_DEFAULT = new byte[] { 0x1B, 'r',0x00 };
    public static final byte[] FS_FONT_ALIGN = new byte[] { 0x1C, 0x21, 1, 0x1B,
            0x21, 1 };
    public static final byte[] ESC_ALIGN_LEFT = new byte[] { 0x1b, 'a', 0x00 };
    public static final byte[] ESC_ALIGN_RIGHT = new byte[] { 0x1b, 'a', 0x02 };
    public static final byte[] ESC_ALIGN_CENTER = new byte[] { 0x1b, 'a', 0x01 };
    public static final byte[] ESC_CANCEL_BOLD = new byte[] { 0x1B, 0x45, 0 };


    /*********************************************/
    public static final byte[] ESC_HORIZONTAL_CENTERS = new byte[] { 0x1B, 0x44, 20, 28, 00};
    public static final byte[] ESC_CANCLE_HORIZONTAL_CENTERS = new byte[] { 0x1B, 0x44, 00 };
    /*********************************************/

    public static final byte[] ESC_ENTER = new byte[] { 0x1B, 0x4A, 0x40 };
    public static final byte[] PRINTE_TEST = new byte[] { 0x1D, 0x28, 0x41 };
    public static final byte[] CLEAR_BUFFER = new byte[] { ESC,'@'};
    public static final byte[] NEW_LINE = {0x0A};

    private static byte[] ALIGN_LEFT = {0x1B, 0x61, 0x00};
    public static final byte[] ALIGN_CENTER = {0x1B, 0x61, 0x01};
    public static byte[] EMPHASIZE_ON = {0x1B, 0x45, 0x01};
    public static byte[] EMPHASIZE_OFF = {0x1B, 0x45, 0x00};
    public static byte[] FONT_5X12 = {0x1B, 0x4D, 0x01};
    public static byte[] FONT_8X12 = {0x1B, 0x4D, 0x02};
    public static byte[] FONT_10X18 = {0x1B, 0x4D, 0x03};
    public static byte[] FONT_SIZE_0 = {0x1D, 0x21, 0x00};
    public static byte[] FONT_SIZE_1 = {0x1D, 0x21, 0x11};
    public static final byte[] CHAR_SPACING_0 = {0x1B, 0x20, 0x00};
    public static final byte[] CHAR_SPACING_1 = {0x1B, 0x20, 0x01};

    public static final byte[] TRIPLE = {0x6D,0x6E,0x6F};
}
