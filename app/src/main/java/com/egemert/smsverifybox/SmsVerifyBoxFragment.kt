package com.egemert.smsverifybox

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.egemert.smsverifybox.databinding.FragmentSmsVerifyBoxBinding

class SmsVerifyBoxFragment : Fragment() {

    private lateinit var binding: FragmentSmsVerifyBoxBinding
    private val boxList = mutableListOf<AppCompatEditText>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_sms_verify_box, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addAllBoxesToList()
        addListenersToBoxes()
        binding.sendButton.setOnClickListener {
            sendNumber()
        }
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
        //third parameter is button
        with(binding) {
            box1.addTextChangedListener(GenericTextWatcher(box1, box2, sendButton))
            box2.addTextChangedListener(GenericTextWatcher(box2, box3, sendButton))
            box3.addTextChangedListener(GenericTextWatcher(box3, box4, sendButton))
            box4.addTextChangedListener(GenericTextWatcher(box4, box5, sendButton))
            box5.addTextChangedListener(GenericTextWatcher(box5, null, sendButton))

            //GenericKeyEvent here works for deleting the element and to switch back to previous EditText
            //first parameter is the current EditText and second parameter is previous EditText
            //third parameter is button
            box1.setOnKeyListener(GenericKeyEvent(box1, null, sendButton))
            box2.setOnKeyListener(GenericKeyEvent(box2, box1, sendButton))
            box3.setOnKeyListener(GenericKeyEvent(box3, box2, sendButton))
            box4.setOnKeyListener(GenericKeyEvent(box4, box3, sendButton))
            box5.setOnKeyListener(GenericKeyEvent(box5, box4, sendButton))
        }
    }

    private fun sendNumber(){
        val activationNumber = putNumbersTogether()
        if(activationNumber.length == 5){
            binding.numberIsTextView.visibility = View.VISIBLE
            binding.numberIsTextView.text = getString(R.string.number_is, activationNumber)
        }
    }

    private fun putNumbersTogether(): String{
        var activationNumber = ""
        for(index in boxList.iterator()){
            activationNumber += index.text.toString()
        }

        return activationNumber
    }

    class GenericKeyEvent internal constructor(
        private val currentView: EditText,
        private val previousView: EditText?,
        private val buttonView: Button
    ) : View.OnKeyListener {
        override fun onKey(p0: View?, keyCode: Int, event: KeyEvent?): Boolean {
            if (event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.id != R.id.box1 && currentView.text.isEmpty()) {
                //If current is empty then previous EditText's number will also be deleted
                previousView!!.text = null
                previousView.requestFocus()
                buttonView.visibility = View.INVISIBLE

                return true
            }
            return false
        }
    }

    class GenericTextWatcher internal constructor(
        private val currentView: EditText,
        private val nextView: EditText?,
        private val buttonView: Button
    ) : TextWatcher {
        override fun afterTextChanged(editable: Editable) {
            val text = editable.toString()
            when (currentView.id) {
                R.id.box1 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.box2 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.box3 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.box4 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.box5 -> if (text.length == 1) buttonView.visibility = View.VISIBLE
            }
        }

        override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {}

        override fun onTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {}
    }
}