package com.wangrj.web_lib.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ImageCode {
    private char code[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
            'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M',
            'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '2',
            '3', '4', '5', '6', '7', '8', '9'};
    private static final int WIDTH = 50;
    private static final int HEIGHT = 20;
    private static final int LENGTH = 4;

    private String codeStr;

    /*
     * 获取随机码*/
    public String getCodeStr() {
        return codeStr;
    }

    private BufferedImage getImageBuffer() {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT,
                BufferedImage.TYPE_INT_RGB);//创建缓存图像对象
        Font mFont = new Font("Arial", Font.TRUETYPE_FONT, 18);//创建字体对象
        Graphics g = image.getGraphics();//绘图对象
        Random rd = new Random();//产生随机对象

        // 设置背景颜色
        g.setColor(new Color(rd.nextInt(55) + 200, rd.nextInt(55) + 200, rd
                .nextInt(55) + 200));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // 设置字体
        g.setFont(mFont);

        // 画边框
        g.setColor(Color.black);
        g.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);

        // 随机产生的验证码
        codeStr = "";
        for (int i = 0; i < LENGTH; ++i) {
            codeStr += code[rd.nextInt(code.length)];
        }

        // 画验证码
        for (int i = 0; i < codeStr.length(); i++) {
            g.setColor(new Color(rd.nextInt(200), rd.nextInt(200), rd
                    .nextInt(200)));//确定画字符的颜色
            g.drawString(codeStr.charAt(i) + "", 12 * i + 1, 16);//画字符
        }

        // 随机产生2个干扰线
        for (int i = 0; i < 2; i++) {
            g.setColor(new Color(rd.nextInt(200), rd.nextInt(200), rd
                    .nextInt(200)));
            int x1 = rd.nextInt(WIDTH);
            int x2 = rd.nextInt(WIDTH);
            int y1 = rd.nextInt(HEIGHT);
            int y2 = rd.nextInt(HEIGHT);
            g.drawLine(x1, y1, x2, y2);//画线
        }

        // 释放图形资源
        g.dispose();
        return image;
    }

    /*
     * 产生并输出动态字符图像*/
    public void out(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 设置响应报头信息
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            // 设置响应的MIME类型
            response.setContentType("image/jpeg");

            //产生动态图像
            BufferedImage image = getImageBuffer();
            OutputStream os = response.getOutputStream();//二进制输出流或字节流
            // 输出图像到页面
            ImageIO.write(image, "JPEG", os);
            os.close();

            //将验证字符放入session
            HttpSession se = request.getSession();
            se.setAttribute("validateCode", codeStr);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 拿接收到的验证码与session中的比较
     */
    public static boolean validateCode(HttpServletRequest request, String userCode) {
        HttpSession se = request.getSession();
        return se.getAttribute("validateCode").toString().equalsIgnoreCase(userCode);
    }
}
