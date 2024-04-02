package com.flash.light.utils

class AppUtils {
    companion object {
        fun range(process: Int, start: Float, end: Float): Float {
            return (end - start) * process / 100 + start
        }

        fun invertRange(values: Float, start: Float, end: Float): Float {
            return ((values - start) * 100) / (end - start);
        }
    }
}