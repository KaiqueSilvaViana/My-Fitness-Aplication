package com.rose.myfitnesslifestyle

import com.rose.myfitnesslifestyle.modelBD.Calc

interface OnClickListinner {

    fun OnClick(id: Int, type: String, calc: Calc)

}