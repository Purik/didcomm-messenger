package com.socialsirius.messenger.ui.activities.tutorial


import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.base.AppPref
import com.socialsirius.messenger.base.ui.BaseActivity
import com.socialsirius.messenger.databinding.ActivityTutorialBinding
import com.socialsirius.messenger.ui.activities.auth.AuthActivity
import com.socialsirius.messenger.ui.tutorial.TutorialAdapter


class TutorialActivity : BaseActivity<ActivityTutorialBinding, TutorialActivityModel>() {


    companion object {
        @JvmStatic
        fun newInstance(context: Context) {
            val intent = Intent(context, TutorialActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }


    override fun getLayoutRes(): Int {
        return R.layout.activity_tutorial
    }

    override fun isBottomNavigationEnabled(): Boolean {
        return false
    }

    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }

    val adapter = TutorialAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       dataBinding.viewModel = model
        dataBinding.tutorialPager.adapter = adapter
        dataBinding.tutorialPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
             //   dataBinding.indicator.selectPage(position+1)
           //     model.setupBottomBtn(position+1)
            }
        })
        model.createList()
        with(dataBinding.tutorialPager) {
            setPageTransformer { page, position ->
                setParallaxTransformation(page, position)
            }
        }
        dataBinding.sliderPageIndicator.attachViewPager(dataBinding.tutorialPager)
    }

    fun setParallaxTransformation(page: View, position: Float){
        page.apply {
            val parallaxView = this.findViewById<ImageView>(R.id.tutorialImage)
            when {
                position < -1 -> // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    alpha = 1f
                position <= 1 -> { // [-1,1]
                    parallaxView.translationX =(Math.abs(position)* 0.8f) * (parallaxView.width ) //Half the normal speed
                }
                else -> // (1,+Infinity]
                    // This page is way off-screen to the right.
                    alpha = 1f
            }
        }

    }

    override fun subscribe() {
        super.subscribe()
        model.startClickLiveData.observe(this, Observer {
            if(it){
                model.startClickLiveData.value = false
                finish()
                AppPref.getInstance().setTutorialDone(true)
                AuthActivity.newInstance(this)
            }

        })
      model.itemListLiveData.observe(this, Observer {
            adapter.dataList = it.toMutableList()
            adapter.notifyDataSetChanged()
          //  dataBinding.indicator.setPages(it.size)
        })


    }
}