package com.meta.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PdfUtil {

    // pdf 加水印
    public static void manipulatePdf(String src, String dest, String waterText) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        int pages = reader.getNumberOfPages();
        PdfStamper stamper = new PdfStamper(reader,new FileOutputStream(dest));
        BaseColor color = new BaseColor(42, 183, 248);
        Font f = new Font(Font.FontFamily.HELVETICA, 20,1,color);
        PdfContentByte over = stamper.getOverContent(3);
        Phrase p = new Phrase(waterText, f);
        // 计算初始x、y轴及偏移量
        JLabel label = new JLabel();
        label.setText(waterText);
        FontMetrics metrics = label.getFontMetrics(label.getFont());
        float offset = metrics.stringWidth(label.getText()) + 20;
        float initXY = offset / 2;
        // 设置水印透明度
        PdfGState gs1 = new PdfGState();
        gs1.setFillOpacity(0.5f);
        // 每一页添加水印
        Rectangle pageSize = null;
        for (int i=1; i<=pages; i++){
            over = stamper.getOverContent(i);
            over.saveState();
            over.setGState(gs1);
            pageSize = reader.getPageSize(i);
            float width = pageSize.getWidth();
            // 如果水印信息过长，导致偏移量过大，进行处理
            float xOffset = offset, yOffset = offset;
            float initX = initXY, initY = initXY;
            System.out.println("offset / width:"+offset / width);
            float ratio = (offset / width);
            width = width*2;
            xOffset = offset/(int)((ratio+1)*2);
            initX = width/2*-1;
            System.out.println("xOffset:"+xOffset);
            System.out.println("width:"+width);
            // y轴偏移
            for (float y=initY; y<pageSize.getHeight()*1.5; y+=yOffset){
                // x轴偏移
                for (float x=initX; x<width; x+=xOffset){
                    ColumnText.showTextAligned(over, Element.ALIGN_CENTER, p, x, y, 45);
                }
            }
            over.restoreState();
        }
        stamper.close();
        reader.close();
    }


    public static void main(String[] args) throws DocumentException, IOException {
        String src = "/Users/huapeiliang/Documents/Angular6.pdf";
        String desrc = "/Users/huapeiliang/Documents/dest.pdf";
        long start = System.currentTimeMillis();
        manipulatePdf(src, desrc, "MartinHua : 2021-10-19 aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

}
