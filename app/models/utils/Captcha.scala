package models

import java.awt.geom.AffineTransform
import java.awt._
import java.awt.image.BufferedImage
import java.io._
import java.{lang, util}
import javax.imageio.ImageIO

import scala.util.Random

/**
  * Created by kinge on 16/6/17.
  */
class CaptchaUtils(width:Int=160 ,height:Int=40 ,codeCount:Int=5 ,lineCount:Int=150)
object CaptchaUtils{

    val VERIFY_CODES = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ"
    val random = new Random();


  def  generateVerifyCode(verifySize:Int):String={
     generateVerifyCode(verifySize, VERIFY_CODES);
  }
  def generateVerifyCode(verifySize:Int, sources:String):String={
    val codesLen = sources.length();
    val rand = new Random(System.currentTimeMillis());
    val verifyCode = new StringBuilder(verifySize);
    for(i<- 0 until  verifySize){
      verifyCode.append(sources.charAt(rand.nextInt(codesLen-1)))
    }
    return verifyCode.toString();
  }


  def outputImage(w:Int, h:Int,code:String)={
    val verifySize = code.length()
    val image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)
    val rand = new Random()
    val g2 = image.createGraphics()
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON)
    val colors = new Array[Color](5)
    val colorSpaces = Array[Color](Color.WHITE, Color.CYAN,
      Color.GRAY, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE,
      Color.PINK, Color.YELLOW )
    val fractions = new Array[Float](colors.length);
    for(i <- 0 until  colors.length){
      colors(i) = colorSpaces(rand.nextInt(colorSpaces.length));
      fractions(i) = rand.nextFloat();
    }
    util.Arrays.sort(fractions);

    g2.setColor(Color.GRAY);
    g2.fillRect(0, 0, w, h);

    val c = getRandColor(200, 250);
    g2.setColor(c);
    g2.fillRect(0, 2, w, h-4);


    val random = new Random();
    g2.setColor(getRandColor(160, 200));
    for (i <- 0 until  20) {
      val x = random.nextInt(w - 1);
      val y = random.nextInt(h - 1);
      val xl = random.nextInt(6) + 1;
      val yl = random.nextInt(12) + 1;
      g2.drawLine(x, y, x + xl + 40, y + yl + 20);
    }


    val yawpRate = 0.05f;
    val area = (yawpRate * w * h).asInstanceOf[Int];
    for (j <- 0 until  area) {
      val x = random.nextInt(w);
      val y = random.nextInt(h);
      val rgb = getRandomIntColor();
      image.setRGB(x, y, rgb);
    }

    shear(g2, w, h, c);

    g2.setColor(getRandColor(100, 160));
    val fontSize = h-4;
    val font = new Font("Algerian", Font.ITALIC, fontSize);
    g2.setFont(font);
    val chars = code.toCharArray();
    for(i <- 0 until  verifySize){
      val affine = new AffineTransform();
      affine.setToRotation(Math.PI / 4 * rand.nextDouble() * (if (rand.nextBoolean())  1 else -1), (w / verifySize) * i + fontSize/2, h/2);
      g2.setTransform(affine);
      g2.drawChars(chars, i, 1, ((w-10) / verifySize) * i + 5, h/2 + fontSize/2 - 10);
    }
    g2.dispose();
    val baos = new ByteArrayOutputStream();
    ImageIO.write(image, "jpg", baos);
    baos.flush()
    baos.toByteArray
  }

  def getRandColor(fcd:Int, bcd:Int):Color={
    var fc=fcd
    var bc=bcd
    if (fc > 255)
      fc=255
    if (bc > 255)
      bc = 255;
    val r = fc + random.nextInt(bc - fc);
    val g = fc + random.nextInt(bc - fc);
    val b = fc + random.nextInt(bc - fc);
    new Color(r, g, b);
  }

  def getRandomIntColor():Int= {
   val rgb = getRandomRgb();
    var color = 0;
    for (c <-rgb) {
      color = color << 8;
      color = color | c;
    }
     color;
  }

  def getRandomRgb() :Array[Int]={
    val rgb = new Array[Int](3);
    for (i <- 0 to 2) {
      rgb(i) = random.nextInt(255);
    }
    return rgb;
  }

  def shear( g:Graphics, w1:Int, h1:Int, color:Color):Unit= {
    shearX(g, w1, h1, color);
    shearY(g, w1, h1, color);
  }

  def shearX(g:Graphics,  w1:Int, h1:Int, color:Color):Unit={
    val period = random.nextInt(2);
    val borderGap = true;
    val frames :Double= 1;
    val phase:Double = random.nextInt(2);

    for (i <- 0 until  h1) {
      val d =  (period >> 1).asInstanceOf[Double] * lang.Math.sin(i /  period.asInstanceOf[Double]
        + (6.2831853071795862D *  phase) / frames)
      g.copyArea(0, i, w1, 1,  d.asInstanceOf[Int], 0)
      if (borderGap) {
        g.setColor(color);
        g.drawLine( d.asInstanceOf[Int], i, 0, i);
        g.drawLine(d.asInstanceOf[Int] + w1, i, w1, i);
      }
    }

  }

  def shearY( g:Graphics,  w1:Int, h1:Int,  color:Color)={
    val period = random.nextInt(40) + 10; // 50;
    val borderGap = true;
    val frames:Double = 20;
    val phase:Double = 7;
    for (i <- 0 until   w1) {
      val d =  (period >> 1).asInstanceOf[Double] * lang.Math.sin(i /  period.asInstanceOf[Double]
        + (6.2831853071795862D *  phase) / frames)

      g.copyArea(i, 0, 1, h1, 0,  d.asInstanceOf[Int]);
      if (borderGap) {
        g.setColor(color);
        g.drawLine(i,  d.asInstanceOf[Int], i, 0);
        g.drawLine(i, d.asInstanceOf[Int] + h1, i, h1);
      }

    }
  }
}

