package com.egemert.smsverifybox

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.egemert.smsverifybox.databinding.FragmentSmsVerifyBoxBinding

class SmsVerifyBoxFragment : Fragment() {

    private lateinit var binding: FragmentSmsVerifyBoxBinding
    private val boxList = mutableListOf<AppCompatEditText>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addAllBoxesToList()
        addListenersToBoxes()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_sms_verify_box, container, false)
        return binding.root
    }

    private fun addAllBoxesToList() {
        with(binding) {
            boxList.add(box1)
            boxList.add(box2)
            boxList.add(box3)
            boxList.add(box4)
            boxList.add(box5)
        }
    }

    private fun addListenersToBoxes() {
        //GenericTextWatcher here works only for moving to next EditText when a number is entered
        //first parameter is the current EditText and second parameter is next EditText
        with(binding) {
            box1.addTextChangedListener(GenericTextWatcher(box1, box2))
            box2.addTextChangedListener(GenericTextWatcher(box2, box3))
            box3.addTextChangedListener(GenericTextWatcher(box3, box4))
            box4.addTextChangedListener(GenericTextWatcher(box4, box5))
            box5.addTextChangedListener(GenericTextWatcher(box5, null))

            //GenericKeyEvent here works for deleting the element and to switch back to previous EditText
            //first parameter is the current EditText and second parameter is previous EditText
            box1.setOnKeyListener(GenericKeyEvent(box1, null))
            box2.setOnKeyListener(GenericKeyEvent(box2, box1))
            box3.setOnKeyListener(GenericKeyEvent(box3, box2))
            box4.setOnKeyListener(GenericKeyEvent(box4, box3))
            box5.setOnKeyListener(GenericKeyEvent(box5, box4))
        }
    }

    class GenericKeyEvent internal constructor(
        private val currentView: EditText,
        private val previousView: EditText?
    ) : View.OnKeyListener {
        override fun onKey(p0: View?, keyCode: Int, event: KeyEvent?): Boolean {
            if (event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.id != R.id.box1 && currentView.text.isEmpty()) {
                //If current is empty then previous EditText's number will also be deleted
                previousView!!.text = null
                previousView.requestFocus()
                return true
            }
            return false
        }
    }

    class GenericTextWatcher internal constructor(
        private val currentView: View,
        private val nextView: View?
    ) : TextWatcher {
        override fun afterTextChanged(editable: Editable) {
            val text = editable.toString()
            when (currentView.id) {
                R.id.box1 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.box2 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.box3 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.box4 -> if (text.length == 1) nextView!!.requestFocus()
            }
        }

        override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {}

        override fun onTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {}

    }
}