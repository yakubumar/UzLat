package uz.uzbeklatin.keyboard

import android.inputmethodservice.InputMethodService
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout

class UzbekLatinImeService : InputMethodService() {
    private var shifted = false
    private var symbols = false

    override fun onCreateInputView(): View = buildKeyboard()

    private fun buildKeyboard(): View {
        val outer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(4), dp(5), dp(4), dp(5))
            setBackgroundColor(Color.rgb(222, 226, 232))
        }
        if (symbols) {
            addRow(outer, listOf("1","2","3","4","5","6","7","8","9","0"))
            addRow(outer, listOf("@","#","$","%","&","-","+","(",")","/"))
            addRow(outer, listOf("=","*","\\\"","'",":",";","!","?","⌫"))
            addRow(outer, listOf("ABC",",","🌐","space",".","enter"))
        } else {
            addRow(outer, listOf("q","w","e","r","t","y","u","i","o","p"))
            addRow(outer, listOf("a","s","d","f","g","h","j","k","l"))
            addRow(outer, listOf("⇧","z","x","c","v","b","n","m","⌫"))
            addRow(outer, listOf("123","ö","ğ","ş","ç","🌐","space",".","enter"))
        }
        return outer
    }

    private fun addRow(parent: LinearLayout, labels: List<String>) {
        val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER }
        for (label in labels) {
            val button = Button(this).apply {
                text = if (label == "space") " " else if (label == "enter") "↵" else label
                textSize = if (label.length > 1) 14f else 20f
                isAllCaps = false
                setTextColor(Color.rgb(25, 29, 35))
                background = GradientDrawable().apply {
                    setColor(if (label in listOf("⇧","⌫","123","ABC","🌐","enter")) Color.rgb(190,198,209) else Color.WHITE)
                    cornerRadius = dp(6).toFloat()
                    setStroke(dp(1), Color.rgb(205,210,218))
                }
                setPadding(dp(1), dp(2), dp(1), dp(2))
                val weight = when (label) { "space" -> 2.4f; "enter","⌫","⇧","123","ABC","🌐" -> 1.25f; else -> 1f }
                val lp = LinearLayout.LayoutParams(0, dp(46), weight)
                lp.setMargins(dp(2), dp(3), dp(2), dp(3))
                layoutParams = lp
                setOnClickListener { handleKey(label) }
            }
            row.addView(button)
        }
        parent.addView(row)
    }

    private fun handleKey(label: String) {
        when (label) {
            "⇧" -> { shifted = !shifted; setInputView(buildKeyboard()) }
            "123" -> { symbols = true; setInputView(buildKeyboard()) }
            "ABC" -> { symbols = false; setInputView(buildKeyboard()) }
            "🌐" -> requestHideSelf(0)
            "⌫" -> currentInputConnection?.deleteSurroundingText(1, 0)
            "space" -> currentInputConnection?.commitText(" ", 1)
            "enter" -> {
                val ic = currentInputConnection
                if (ic != null && !ic.performEditorAction(currentInputEditorInfo?.imeOptions ?: 0)) ic.commitText("\n", 1)
            }
            else -> {
                val out = if (shifted && label.length == 1 && label[0].isLetter()) label.uppercase() else label
                currentInputConnection?.commitText(out, 1)
                if (shifted) { shifted = false; setInputView(buildKeyboard()) }
            }
        }
    }
    private fun dp(v: Int): Int = (v * resources.displayMetrics.density).toInt()
}
