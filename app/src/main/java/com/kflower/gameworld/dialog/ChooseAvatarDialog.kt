package com.kflower.gameworld.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.LinearLayout
import com.kflower.gameworld.R
import com.kflower.gameworld.adapter.AvatarAdapter
import com.kflower.gameworld.databinding.DialogChooseAvatarBinding
import com.kflower.gameworld.databinding.DialogLoadingBinding

class ChooseAvatarDialog : Dialog {
    lateinit var binding: DialogChooseAvatarBinding;
    lateinit var avatarAdapter: AvatarAdapter;
    lateinit var listAvatar: ArrayList<Int>;

    constructor(context: Context):super(context){

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogChooseAvatarBinding.inflate(layoutInflater)
        //
        listAvatar= ArrayList();
        listAvatar.add(R.drawable.default_avatar_1)
        listAvatar.add(R.drawable.default_avatar_2)
        listAvatar.add(R.drawable.default_avatar_3)
        listAvatar.add(R.drawable.default_avatar_4)
        listAvatar.add(R.drawable.default_avatar_5)
        listAvatar.add(R.drawable.default_avatar_6)
        listAvatar.add(R.drawable.default_avatar_7)
        listAvatar.add(R.drawable.default_avatar_8)
        listAvatar.add(R.drawable.default_avatar_9)
        listAvatar.add(R.drawable.default_avatar_10)
        listAvatar.add(R.drawable.default_avatar_11)
        listAvatar.add(R.drawable.default_avatar_12)

        avatarAdapter= AvatarAdapter(context,listAvatar)

        setContentView(binding.root)
        val window = window
        window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
//        binding.gridView.adapter= avatarAdapter;

    }
}