package com.meta.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfUtil {

    // pdf 加水印
    public static void manipulatePdf(String src, String dest, String waterText) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        int pages = reader.getNumberOfPages();

        PdfStamper stamper = new PdfStamper(reader,new FileOutputStream(dest));
        BaseColor color = new BaseColor(42, 183, 248);
        Font f = new Font(Font.FontFamily.HELVETICA, 20,1,color);
        new Font();
        PdfContentByte over = stamper.getOverContent(3);

        // 将水印文字变长
        for (int i=0; i<10; i++){
            waterText += ("     "+waterText);
        }

        Phrase p = new Phrase(waterText, f);

        // 已第一页来计算水印
        Rectangle pageSize = reader.getPageSize(1);
        float width = pageSize.getWidth();
        float height = pageSize.getHeight();
        int v = (int)(width+height) / 180;
        int y = (int)height/2;
        PdfGState gs1 = new PdfGState();
        gs1.setFillOpacity(0.5f);
        for (int i=1; i<=pages; i++){
            over = stamper.getOverContent(i);
            over.saveState();
            over.setGState(gs1);
            int x = (int)height/2*-1;
            for (int j=0; j<v; j++){
                x += 180;
                ColumnText.showTextAligned(over, Element.ALIGN_CENTER, p, x, y, 45);
            }
            over.restoreState();
        }
        stamper.close();
        reader.close();
    }

}
