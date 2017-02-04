package uz.greenwhite.vision.scala

import org.opencv.core.Core._
import org.opencv.core._
import org.opencv.imgproc.Imgproc._
import org.opencv.objdetect.{CascadeClassifier, Objdetect}

object CvUtil {

  def rotate90(img: Mat) {
    transpose(img, img)
    flip(img, img, 1)
  }

  def detectMultiScale(cascade: CascadeClassifier, dstDetect: MatOfRect, src: Mat, width: Int, height: Int) {
    val gray: Mat = src.clone()
    cvtColor(src, gray, COLOR_RGB2GRAY)
    if (src.size.width > width && src.size.height > height) {
      resize(gray, gray, new Size(width, height))
      resize(src, src, new Size(width, height))
    }
    val minSize = new Size(width / 6, height / 6)
    val maxSize = new Size(width, height)
    cascade.detectMultiScale(gray, dstDetect, 1.1, 1, Objdetect.CASCADE_SCALE_IMAGE, minSize, maxSize)
  }

  def cropImage(src: Mat, dst: Mat, x: Int, y: Int, w: Int, h: Int) {
    new Mat(src, new Rect(x, y, w, h)) copyTo dst
  }
}
