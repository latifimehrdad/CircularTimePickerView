package com.mehrdad.circulartimepickerview

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import java.util.*
import androidx.core.graphics.drawable.toDrawable
import com.mehrdad.circulartimepicker.CircularTimePickerView


class TimeNewDialog(
    context: Context,
    private val onChooseTime: (time: String) -> Unit
) : Dialog(context) {

    private lateinit var timePicker: CircularTimePickerView
    private lateinit var textViewHour: TextView
    private lateinit var textViewMinute: TextView


    //---------------------------------------------------------------------------------------------- onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_time_new)
        val lp = WindowManager.LayoutParams()
        this.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        this.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        this.window?.setGravity(Gravity.CENTER)
        lp.copyFrom(this.window?.attributes)
        lp.horizontalMargin = 50f
        this.window?.attributes = lp
    }
    //---------------------------------------------------------------------------------------------- onCreate


    //---------------------------------------------------------------------------------------------- onStart
    override fun onStart() {
        initDialog()
        super.onStart()
    }
    //---------------------------------------------------------------------------------------------- onStart


    //---------------------------------------------------------------------------------------------- initDialog
    private fun initDialog() {
        val buttonConfirm = this.findViewById<MaterialButton>(R.id.buttonConfirm)
        val buttonCancel = this.findViewById<MaterialButton>(R.id.buttonCancel)
        timePicker = this.findViewById(R.id.timePicker)
        textViewHour = this.findViewById(R.id.textViewHour)
        textViewMinute = this.findViewById(R.id.textViewMinute)
        textViewHour.text = String.format(Locale.US, "%02d", 0)
        textViewMinute.text = String.format(Locale.US, "%02d", 0)

        timePicker.setStrokeStyle(
            strokeColor = context.getColor(R.color.color1),
            numberColor = context.getColor(R.color.color2),
            circle = context.getColor(R.color.primary100),
            width = 20.0f,
            handlerWidth = 3f
        )
        timePicker.setSelectedTime(
            hour = 8,
            minute = 30
        )
        timePicker.onTimeSelected = { hour, minute ->
            textViewHour.text = String.format(Locale.US, "%02d", hour)
            textViewMinute.text = String.format(Locale.US, "%02d", minute)
        }

        textViewHour.setOnClickListener {
            timePicker.resetToHourMode()
        }


        buttonConfirm.setOnClickListener {
            onChooseTime.invoke(
                context.getString(
                    R.string.timeString,
                    String.format(Locale.US, "%02d", timePicker.getSelectedHour()),
                    String.format(Locale.US, "%02d", timePicker.getSelectedMinute())
                )
            )
            dismiss()
        }

        buttonCancel.setOnClickListener {
            dismiss()
        }
    }
    //---------------------------------------------------------------------------------------------- initDialog

}