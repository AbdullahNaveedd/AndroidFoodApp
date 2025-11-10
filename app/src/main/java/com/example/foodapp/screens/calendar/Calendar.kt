package com.example.foodapp.screens.calendar

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.foodapp.R
import com.github.pelmenstar1.rangecalendar.RangeCalendarView
import java.text.SimpleDateFormat
import java.util.*

class Calendar : Fragment() {

    private lateinit var btnDateFilter: Button
    private lateinit var tvSelectedRange: TextView
    private val formatter = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        btnDateFilter = view.findViewById(R.id.btnDateFilter)
        tvSelectedRange = view.findViewById(R.id.tvSelectedRange)

        btnDateFilter.setOnClickListener {
            showCalendarPopup(it)
        }

        return view
    }

    private fun showCalendarPopup(anchorView: View) {
        val popupView = layoutInflater.inflate(R.layout.calendar_popup, null)
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        val calendarView = popupView.findViewById<RangeCalendarView>(R.id.rangeCalendar)
        val confirmBtn = popupView.findViewById<Button>(R.id.btnConfirmSelection)
        val btn7 = popupView.findViewById<Button>(R.id.btnLast7Days)
        val btn30 = popupView.findViewById<Button>(R.id.btnLast30Days)
        val btn3M = popupView.findViewById<Button>(R.id.btnLast3Months)
        


        calendarView.onSelectionListener = object : RangeCalendarView.OnSelectionListener {
            override fun onSelectionCleared() {
                tvSelectedRange.text = "No range selected"
            }


            override fun onSelection(
                startYear: Int, startMonth: Int, startDay: Int,
                endYear: Int, endMonth: Int, endDay: Int
            ) {
                val start = GregorianCalendar(startYear, startMonth, startDay).time
                val end = GregorianCalendar(endYear, endMonth, endDay).time

                tvSelectedRange.text =
                    "Selected: ${formatter.format(start)} — ${formatter.format(end)}"
            }
        }

        // Quick range: Last 7 days
//        btn7.setOnClickListener {
//            val end = GregorianCalendar.getInstance()
//            val start = GregorianCalendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -6) }
//
//            calendarView.setRange(
//                start.get(Calendar.YEAR),
//                start.get(Calendar.MONTH),
//                start.get(Calendar.DAY_OF_MONTH),
//                end.get(Calendar.YEAR),
//                end.get(Calendar.MONTH),
//                end.get(Calendar.DAY_OF_MONTH)
//            )
//        }
//
//        // Quick range: Last 30 days
//        btn30.setOnClickListener {
//            val end = GregorianCalendar.getInstance()
//            val start = GregorianCalendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -29) }
//
//            calendarView.setRange(
//                start.get(Calendar.YEAR),
//                start.get(Calendar.MONTH),
//                start.get(Calendar.DAY_OF_MONTH),
//                end.get(Calendar.YEAR),
//                end.get(Calendar.MONTH),
//                end.get(Calendar.DAY_OF_MONTH)
//            )
//        }
//
//        // Quick range: Last 3 months
//        btn3M.setOnClickListener {
//            val end = GregorianCalendar.getInstance()
//            val start = GregorianCalendar.getInstance().apply { add(Calendar.MONTH, -3) }
//
//            calendarView.setRange(
//                start.get(Calendar.YEAR),
//                start.get(Calendar.MONTH),
//                start.get(Calendar.DAY_OF_MONTH),
//                end.get(Calendar.YEAR),
//                end.get(Calendar.MONTH),
//                end.get(Calendar.DAY_OF_MONTH)
//            )
//        }

        confirmBtn.setOnClickListener {
            popupWindow.dismiss()
        }

        popupWindow.elevation = 20f
        popupWindow.isFocusable = true
        popupWindow.setBackgroundDrawable(requireContext().getDrawable(android.R.color.transparent))
        popupWindow.showAsDropDown(anchorView)
    }
}
