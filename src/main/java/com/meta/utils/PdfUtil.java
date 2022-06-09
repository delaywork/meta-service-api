package com.meta.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.swing.*;
import java.awt.*;
import java.io.*;

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
        float offset = metrics.stringWidth(label.getText()) + waterText.length();
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
            // 水印信息过长，导致偏移量过大，进行处理
            float xOffset = offset, yOffset = offset;
            float initX = initXY, initY = initXY;
            float ratio = (offset / width);
            width = width*2;
            xOffset = offset/(int)((ratio+1)*2);
            initX = width/2*-1;
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

    // pdf 加水印输出文件流并删除本地文件
    public static MultipartFile manipulatePdfReturnFile(String src, String waterText, String fileName) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        int pages = reader.getNumberOfPages();
        String dest = "./"+IdUtil.simpleUUID()+".pdf";
        PdfStamper stamper = new PdfStamper(reader,new FileOutputStream(dest));
        BaseColor color = new BaseColor(42, 183, 248);
        Font f = new Font(Font.FontFamily.HELVETICA, 20,1,color);
        PdfContentByte over = stamper.getOverContent(3);
        Phrase p = new Phrase(waterText, f);
        // 计算初始x、y轴及偏移量
        JLabel label = new JLabel();
        label.setText(waterText);
        FontMetrics metrics = label.getFontMetrics(label.getFont());
        float offset = metrics.stringWidth(label.getText()) + waterText.length();
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
            // 水印信息过长，导致偏移量过大，进行处理
            float xOffset = offset, yOffset = offset;
            float initX = initXY, initY = initXY;
            float ratio = (offset / width);
            width = width*2;
            xOffset = offset/(int)((ratio+1)*2);
            initX = width/2*-1;
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
        File pdfFile = new File(dest);
//        FileInputStream fileInputStream = new FileInputStream(pdfFile);
//        MultipartFile multipartFile = new MockMultipartFile(pdfFile.getName(), fileName, null, fileInputStream);
        FileItem fileItem = createFileItem(pdfFile);
        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);

        pdfFile.delete();
        return multipartFile;
    }

    public static void manipulatePdf(String src, OutputStream outputStream, String waterText) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        int pages = reader.getNumberOfPages();
        PdfStamper stamper = new PdfStamper(reader, outputStream);
        try{
            BaseColor color = new BaseColor(0, 0, 0);
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
            gs1.setFillOpacity(0.05f);
            // 每一页添加水印
            Rectangle pageSize = null;
            for (int i=1; i<=pages; i++){
                over = stamper.getOverContent(i);
                over.saveState();
                over.setGState(gs1);
                pageSize = reader.getPageSize(i);
                float width = pageSize.getWidth();
                // 水印信息过长，导致偏移量过大，进行处理
                float xOffset = offset, yOffset = offset;
                float initX = initXY, initY = initXY;
                float ratio = (offset / width);
                width = width*2;
                xOffset = offset/(int)((ratio+1)*2);
                initX = width/2*-1;
                // y轴偏移
                for (float y=initY; y<pageSize.getHeight()*1.5; y+=yOffset){
                    // x轴偏移
                    for (float x=initX; x<width; x+=xOffset){
                        ColumnText.showTextAligned(over, Element.ALIGN_CENTER, p, x, y, 45);
                    }
                }
                over.restoreState();
            }
        }finally {
            stamper.close();
            reader.close();
        }
    }

    private static FileItem createFileItem(File file) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem item = factory.createItem("textField", "text/plain", true, file.getName());
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            FileInputStream fis = new FileInputStream(file);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return item;
    }


    public static void main(String[] args) throws DocumentException, IOException {
        String src = "/Users/huapeiliang/Documents/BiteStream技术白皮书.pdf";
        String desrc = "/Users/huapeiliang/Documents/dest.pdf";
        long start = System.currentTimeMillis();
        manipulatePdf(src, desrc, "MartinHua : 2021-10-19");
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

}
