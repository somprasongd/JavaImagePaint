/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.somprasongd.java.paint.utils;

/**
 *
 * @author Somprasong
 */
public class Language {

    public static String str_lang = "TH";
    // AnnotationEditToolbar.java
    private final static String str_antiAlias_TH = "ลดรอยหยักเส้นขอบ";
    private final static String str_antiAlias_EN = "Anti Alias";
    private final static String str_fillColor_TH = "เติมสีรูปร่าง";
    private final static String str_fillColor_EN = "Fill Color";
    private final static String str_color_TH = "สีรูปร่าง";
    private final static String str_color_EN = "Color";
    private final static String str_textColor_TH = "สีข้อความ";
    private final static String str_textColor_EN = "Text Color";
    private final static String str_transparent_TH = "ความโปร่งใส";
    private final static String str_transparent_EN = "Transparent";
    private final static String str_delete_TH = "ลบแอนโนเตชั่น";
    private final static String str_delete_EN = "Delete Annotation";
    // AnnotationFileToolbar.java
    private final static String str_flipRight_TH = "หมุนตามเข็มนาฬิกา 90 องศา";
    private final static String str_flipRight_EN = "Flip Right";
    private final static String str_flip180_TH = "หมุน 180 องศา";
    private final static String str_flip180_EN = "Flip 180ํ";
    private final static String str_flipLeft_TH = "หมุนตทวนเข็มนาฬิกา 90 องศา";
    private final static String str_flipLeft_EN = "Flip Left";
    private final static String str_zoomIn_TH = "ซูมเข้า";
    private final static String str_zoomIn_EN = "Zoomin";
    private final static String str_zoomOut_TH = "ซูมออก";
    private final static String str_zoomOut_EN = "Zoomout";
    private final static String str_set1To1_TH = "1 ต่อ 1";
    private final static String str_set1To1_EN = "1:1";
    // AnnotationFontChooserPanel.java
    private final static String str_fontChooser_TH = "แบบอักษร";
    private final static String str_fontChooser_EN = "Font Style";
    private final static String str_sizeChooser_TH = "ขนาดแบบอักษร";
    private final static String str_sizeChooser_EN = "Font Size";
    private final static String str_textBold_TH = "ตัวหนา";
    private final static String str_textBold_EN = "Bold";
    private final static String str_textItalic_TH = "ตัวเอียง";
    private final static String str_textItalic_EN = "Italic";
    // AnnotationNavigatorBar.java
    private final static String str_nv_firstImage_TH = "รูปแรก";
    private final static String str_nv_firstImage_EN = "First Image";
    private final static String str_nv_previousImage_TH = "รูปก่อนหน้า";
    private final static String str_nv_previousImage_EN = "Previous Image";
    private final static String str_nv_nextImage_TH = "รูปถัดไป";
    private final static String str_nv_nextImage_EN = "Next Image";
    private final static String str_nv_lastImage_TH = "รูปสุดท้าย";
    private final static String str_nv_lastImage_EN = "Last Image";
    private final static String str_errorMessage_TH = "ไม่พบรูปภาพตามค่าที่ป้อนมา";
    private final static String str_errorMessage_EN = "Image not found!";
    private final static String str_errorTitle_TH = "ตรวจสอบค่าหมายเลขรูปภาพ";
    private final static String str_errorTitle_EN = "Check number of image";
    // AnnotationToolsToolbar.java
    private final static String str_select_TH = "เลือก";
    private final static String str_select_EN = "Select";
    private final static String str_line_TH = "เส้น";
    private final static String str_line_EN = "Line";
    private final static String str_rectangle_TH = "สี่เหลี่ยม";
    private final static String str_rectangle_EN = "Rectangle";
    private final static String str_oval_TH = "วงรี";
    private final static String str_oval_EN = "Oval";
    private final static String str_arrow_TH = "ลูกศร";
    private final static String str_arrow_EN = "Arrow";
    private final static String str_text_TH = "ข้อความ";
    private final static String str_text_EN = "Text";
    private final static String str_note_TH = "โน็ต";
    private final static String str_note_EN = "Note";
    private final static String str_stamp_TH = "ตราประทับ";
    private final static String str_stamp_EN = "Stamp";
    private final static String str_userPermission_TH = "กำหนดสิทธิ์ผู้ใช้";
    private final static String str_userPermission_EN = "User Permission";
    private final static String str_save_TH = "บันทึก";
    private final static String str_save_EN = "Save";

    public static void setStr_lang(String string) {
        str_lang = string == null ? "TH" : string;
    }

    public static String getStr_anitAlias() {
        if (str_lang.equalsIgnoreCase("TH")) {
            return str_antiAlias_TH;
        } else {
            return str_antiAlias_EN;
        }
    }

    public static String getStr_fillColor() {
        if (str_lang.equalsIgnoreCase("TH")) {
            return str_fillColor_TH;
        } else {
            return str_fillColor_EN;
        }
    }

    public static String getStr_color() {
        if (str_lang.equalsIgnoreCase("TH")) {
            return str_color_TH;
        } else {
            return str_color_EN;
        }
    }

    public static String getStr_textColor() {
        if (str_lang.equalsIgnoreCase("TH")) {
            return str_textColor_TH;
        } else {
            return str_textColor_EN;
        }
    }

    public static String getStr_transparent() {
        if (str_lang.equalsIgnoreCase("TH")) {
            return str_transparent_TH;
        } else {
            return str_transparent_EN;
        }
    }

    public static String getStr_delete() {
        if (str_lang.equalsIgnoreCase("TH")) {
            return str_delete_TH;
        } else {
            return str_delete_EN;
        }
    }

    public static String getStr_flipRight() {
        if (str_lang.equalsIgnoreCase("TH")) {
            return str_flipRight_TH;
        } else {
            return str_flipRight_EN;
        }
    }

    public static String getStr_flip180() {
        if (str_lang.equalsIgnoreCase("TH")) {
            return str_flip180_TH;
        } else {
            return str_flip180_EN;
        }
    }

    public static String getStr_flipLeft() {
        if (str_lang.equalsIgnoreCase("TH")) {
            return str_flipLeft_TH;
        } else {
            return str_flipLeft_EN;
        }
    }

    public static String getStr_zoomIn() {
        if (str_lang.equalsIgnoreCase("TH")) {
            return str_zoomIn_TH;
        } else {
            return str_zoomIn_EN;
        }
    }

    public static String getStr_zoomOut() {
        if (str_lang.equalsIgnoreCase("TH")) {
            return str_zoomOut_TH;
        } else {
            return str_zoomOut_EN;
        }
    }

    public static String getStr_set1To1() {
        if (str_lang.equalsIgnoreCase("TH")) {
            return str_set1To1_TH;
        } else {
            return str_set1To1_EN;
        }
    }

    public static String getStr_fontChooser() {
        if (str_lang.equalsIgnoreCase("TH")) {
            return str_fontChooser_TH;
        } else {
            return str_fontChooser_EN;
        }
    }

    public static String getStr_sizeChooser() {
        if (str_lang.equalsIgnoreCase("TH")) {
            return str_sizeChooser_TH;
        } else {
            return str_sizeChooser_EN;
        }
    }

    public static String getStr_textBold() {
        if (str_lang.equalsIgnoreCase("TH")) {
            return str_textBold_TH;
        } else {
            return str_textBold_EN;
        }
    }

    public static String getStr_textItalic() {
        if (str_lang.equalsIgnoreCase("TH")) {
            return str_textItalic_TH;
        } else {
            return str_textItalic_EN;
        }
    }

    public static String getStr_nv_firstImage() {
        if (str_lang.equalsIgnoreCase("TH")) {
            return str_nv_firstImage_TH;
        } else {
            return str_nv_firstImage_EN;
        }
    }

    public static String getStr_nv_previousImage() {
        if (str_lang.equalsIgnoreCase("TH")) {
            return str_nv_previousImage_TH;
        } else {
            return str_nv_previousImage_EN;
        }
    }

    public static String getStr_nv_nextImage() {
        if (str_lang.equalsIgnoreCase("TH")) {
            return str_nv_nextImage_TH;
        } else {
            return str_nv_nextImage_EN;
        }
    }

    public static String getStr_nv_lastImage() {
        if (str_lang.equalsIgnoreCase("TH")) {
            return str_nv_lastImage_TH;
        } else {
            return str_nv_lastImage_EN;
        }
    }

    public static String getStr_errorMessage() {
        if (str_lang.equalsIgnoreCase("TH")) {
            return str_errorMessage_TH;
        } else {
            return str_errorMessage_EN;
        }
    }

    public static String getStr_errorTitle() {
        if (str_lang.equalsIgnoreCase("TH")) {
            return str_errorTitle_TH;
        } else {
            return str_errorTitle_EN;
        }
    }

    public static String getStr_select() {
        if (str_lang.equalsIgnoreCase("TH")) {
            return str_select_TH;
        } else {
            return str_select_EN;
        }
    }

    public static String getStr_line() {
        if (str_lang.equalsIgnoreCase("TH")) {
            return str_line_TH;
        } else {
            return str_line_EN;
        }
    }

    public static String getStr_rectangle() {
        if (str_lang.equalsIgnoreCase("TH")) {
            return str_rectangle_TH;
        } else {
            return str_rectangle_EN;
        }
    }

    public static String getStr_oval() {
        if (str_lang.equalsIgnoreCase("TH")) {
            return str_oval_TH;
        } else {
            return str_oval_EN;
        }
    }

    public static String getStr_arrow() {
        if (str_lang.equalsIgnoreCase("TH")) {
            return str_arrow_TH;
        } else {
            return str_arrow_EN;
        }
    }

    public static String getStr_text() {
        if (str_lang.equalsIgnoreCase("TH")) {
            return str_text_TH;
        } else {
            return str_text_EN;
        }
    }

    public static String getStr_note() {
        if (str_lang.equalsIgnoreCase("TH")) {
            return str_note_TH;
        } else {
            return str_note_EN;
        }
    }

    public static String getStr_stamp() {
        if (str_lang.equalsIgnoreCase("TH")) {
            return str_stamp_TH;
        } else {
            return str_stamp_EN;
        }
    }

    public static String getStr_userPermission() {
        if (str_lang.equalsIgnoreCase("TH")) {
            return str_userPermission_TH;
        } else {
            return str_userPermission_EN;
        }
    }

    public static String getStr_save() {
        if (str_lang.equalsIgnoreCase("TH")) {
            return str_save_TH;
        } else {
            return str_save_EN;
        }
    }
}
