package LottieDemo;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.OnCompositionLoadedListener;
import com.daledawson.products.somedemo.R;
import com.daledawson.products.somedemo.RxJava_Network.RxJavaNetwork;

/**
 * 创 建 人：zhengquan
 * 创建日期：2018/12/21
 * 修改时间：
 * 修改备注：
 */
public class LottieActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = LottieActivity.class.getSimpleName();
    private LottieAnimationView animationView;
    private SeekBar seekBar;
    private Button btnStart, btnPause, btnResume, btnCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lottie_activity);
        animationView = (LottieAnimationView) findViewById(R.id.animationView);
        seekBar = findViewById(R.id.seekBar);
        btnStart = findViewById(R.id.btn_start);
        btnPause = findViewById(R.id.btn_pause);
        btnResume = findViewById(R.id.btn_resume);
        btnCancel = findViewById(R.id.btn_cancel);
        btnStart.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        btnResume.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        LottieComposition.Factory.fromAssetFileName
                (this, "LottieLogo2.json",
                        new OnCompositionLoadedListener() {
                            @Override
                            public void onCompositionLoaded(@Nullable LottieComposition composition) {
                                animationView.setComposition(composition);
                            }
                        });
        /**
         * 监听动画的状态：开始、结束、取消和重复
         */
        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                Log.d(TAG, "-----onAnimationStart");
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                Log.d(TAG, "-----onAnimationEnd");
                startActivity(new Intent(LottieActivity.this, RxJavaNetwork.class));
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                Log.d(TAG, "-----onAnimationCancel");
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                Log.d(TAG, "-----onAnimationRepeat");
            }
        });
        /**
         * 动态监听动画演示过程，可以动态获取动画进度
         */
        animationView.addAnimatorUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                seekBar.setProgress((int) (valueAnimator.getAnimatedFraction() * 100));
            }
        });
    }

    /**
     * 开始动画
     *
     * @param view
     */
    public void Start(View view) {
        if (!animationView.isAnimating())
            animationView.playAnimation();
    }

    /**
     * 暂停动画
     *
     * @param view
     */
    public void Pause(View view) {
        if (animationView.isAnimating())
            animationView.pauseAnimation();
    }

    /**
     * 继续动画
     *
     * @param view
     */
    public void Resume(View view) {
        if (!animationView.isAnimating())
            animationView.resumeAnimation();
    }

    /**
     * 取消动画
     *
     * @param view
     */
    public void Cancel(View view) {
        animationView.cancelAnimation();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                Start(v);
                break;
            case R.id.btn_pause:
                Pause(v);
                break;
            case R.id.btn_resume:
                Resume(v);
                break;
            case R.id.btn_cancel:
                Cancel(v);
                break;
        }
    }
}
