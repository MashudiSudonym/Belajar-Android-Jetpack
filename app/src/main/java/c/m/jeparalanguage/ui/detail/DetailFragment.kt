package c.m.jeparalanguage.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import c.m.jeparalanguage.R
import c.m.jeparalanguage.data.source.local.entity.ContentEntity
import c.m.jeparalanguage.data.source.remote.webservice.ClientServices
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.detail_fragment.*


class DetailFragment : BottomSheetDialogFragment() {

    companion object {
        const val EXTRA_WORD = "extra word"
        fun newInstance(contentEntity: ContentEntity): DetailFragment {
            val bottomSheetFragment = DetailFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_WORD, contentEntity)
            bottomSheetFragment.arguments = bundle

            return bottomSheetFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.detail_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val content = arguments?.getParcelable<ContentEntity>(EXTRA_WORD)

        tv_word.text = content?.word
        tv_definition.text = content?.definition
        tv_phonetic.text = content?.phonetic

        Glide.with(this)
            .load(ClientServices.BASE_URL_IMAGE + content?.image)
            .apply(
                RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.loading_animation)
                    .error(
                        if (content?.image == "null") {
                            R.drawable.ic_translate
                        } else {
                            R.drawable.ic_broken_image
                        }
                    )
            )
            .into(img_illustration)
    }
}