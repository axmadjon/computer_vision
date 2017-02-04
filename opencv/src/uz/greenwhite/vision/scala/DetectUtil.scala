package uz.greenwhite.vision.scala

import uz.greenwhite.vision.common.Wrapper

object DetectUtil {

  def pushRightStep(x: Wrapper[Int], widthExit: Wrapper[Boolean],
                    stepDown: Wrapper[Boolean], width: Int, imgWidth: Int,
                    rightPadding: Int, rightStep: Int): Boolean = {
    if ((x.value + rightPadding) > imgWidth) {
      if (x.value + width > imgWidth) {
        widthExit.value = true
        false
      }
      x.value = x.value - imgWidth
      stepDown.value = true
      widthExit.value = true
    } else {
      x.value += rightStep
      widthExit.value = false
    }
    true
  }

  def pushDownStep(stepDown: Wrapper[Boolean], heightExit: Wrapper[Boolean],
                   x: Wrapper[Int], y: Wrapper[Int],
                   height: Int, imgHeight: Int,
                   downPadding: Int, downStep: Int): Boolean = {
    if (stepDown.value) {
      if ((y.value + downPadding) > imgHeight) {
        if ((y.value + height) > imgHeight) {
          heightExit.value = true
          false
        }
        y.value += (y.value - imgHeight)
        heightExit.value = true
      } else {
        y.value += downStep
        x.value = 0
      }
      stepDown.value = false
    }
    false
  }
}
