package com.cistus.hunter

class UIConfig {
    class Login {
        companion object {
            val rcFrameSize = UISize(Screen.smallerSize * 0.9, 40.0)
            val checkBoxSize = UISize(30.0, 30.0)
        }
    }
    class Home {
        companion object {
            val bottomTabHeight: Double = 83.0
            fun getAttentionFrameSize(deviceSize: UISize, isTablet: Boolean = false): UISize {
                val m = if (!isTablet) 1 else 3
                return UISize(deviceSize.width * 0.9 / m, deviceSize.width * 0.9 * 1.2 / m)
            }
            fun getLargeAttentionFrameSize(deviceSize: UISize): UISize {
                return UISize(deviceSize.width * 0.9 / 1.5, deviceSize.height * 0.9 * 1.2 / 3)
            }
            fun getBottomBarFrame(frameSize: UISize): UISize {
                return UISize(frameSize.width, frameSize.height * (410.0 / 2458.0))
            }
        }
    }
    class History {
        companion object {
            fun getContentFrameSize(deviceSize: UISize, isTablet: Boolean = false): UISize {
                val m = if (!isTablet) 1 else 2
                return UISize(deviceSize.width * 0.9 / m, deviceSize.width * 0.9 * 0.4 / m)
            }
            fun getContentIconFrameSize(deviceSize: UISize, isTablet: Boolean = false): UISize {
                val m = if (!isTablet) 1 else 2
                return UISize(deviceSize.width * 0.9 * 0.4 / m - 10, deviceSize.width * 0.9 * 0.4 / m - 10)
            }
        }
    }
}