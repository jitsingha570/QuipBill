package com.QuipBill_server.QuipBill.modules.hardware.printer.escpos;

public class EscPosCommandBuilder {

    public static final byte[] INIT = {0x1B, 0x40};

    public static final byte[] ALIGN_CENTER = {0x1B, 0x61, 0x01};

    public static final byte[] ALIGN_LEFT = {0x1B, 0x61, 0x00};

    public static final byte[] BOLD_ON = {0x1B, 0x45, 0x01};

    public static final byte[] BOLD_OFF = {0x1B, 0x45, 0x00};

    public static final byte[] CUT_PAPER = {0x1D, 0x56, 0x41, 0x10};

}