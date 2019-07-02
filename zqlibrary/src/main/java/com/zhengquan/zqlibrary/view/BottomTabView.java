package com.zhengquan.zqlibrary.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * 创 建 人：zheng Quan
 * 创建日期：2019/6/26
 * 修改时间：
 * 修改备注：
 */
public class BottomTabView extends LinearLayout {
    /**
     * 方法说明
     * <p>
     * bottomTabView.setTextColors(Color.BLACK)//设置未选中标题颜色
     * .setOpen(false)//是否显示中间大按钮，当标题个数为奇数是，自动不显示按钮
     * .setTextCheckColor(Color.BLUE)//设置选中标题颜色
     * .setTextSize(20)
     * .setSquare(true)//设置按钮是否居中还是往上突起，当setOpen(false)此设置无效
     * .setImageCheck(imagesList)//设置选中时图片
     * .setMiddleImage(R.mipmap.ic_launcher_round)//设置中间大按钮的图片
     * .setImagesWidthHeight(50, 50)//设置中间大按钮的高宽度
     * .setImageBottom(40)//设置大按钮的下边距
     * .setNameOrImage(nameList, imageList)//设置每个item的标题和图片
     * .setImageWidthHeight(30, 30)//设置item图片的高宽度
     * .init();//初始化
     * bottomTabView.setOnClick(this);//点击回调
     * bottomTabView.setPosition(0);//设置选中的item
     */
    private TextView textView;
    private List<String> name;
    private List<Object> image;
    private List<Object> images;
    private Context context;
    private LinearLayout linearLayout;
    private ImageView imageView;
    private int color;
    private int bgColor;
    private int size = 10;
    private int checkColor;
    private int width = 80;
    private int height = 80;
    private List<LinearLayout> linearLayouts;
    private List<ImageView> imageViews;
    private List<TextView> textViews;
    private boolean open = true;
    private boolean square = true;
    private Object imageContet;
    private int imageWidth = 100;
    private int imageHeight = 100;
    private OnClick onClick;
    private int imageBottom = 0;

    public void setOnClick(BottomTabView.OnClick onClick) {
        this.onClick = onClick;
    }

    public BottomTabView(Context context) {
        super(context);
        this.context = context;
    }

    public BottomTabView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }


    public BottomTabView init() {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1;
        layoutParams.gravity = Gravity.CENTER;
        LayoutParams imageLayoutParams = new LayoutParams(width, height);
        imageLayoutParams.gravity = Gravity.CENTER;

        setBackgroundColor(bgColor);

        imageViews = new ArrayList<>();
        linearLayouts = new ArrayList<>();
        textViews = new ArrayList<>();
        this.setClipChildren(false);
        //判断是否要打开中间按钮
        if (open) {
            for (int i = 0; i < name.size(); i++) {
                final int finalI = i;
                //判断view的个数，是奇数即使打开了中间按钮也不起作用
                if ((name.size() % 2) != 0) {
                    textView = new TextView(context);
                    textView.setText(name.get(i));
                    textView.setGravity(Gravity.CENTER_HORIZONTAL);
                    textView.setTextColor(color);
                    textView.setTextSize(size);
                    textViews.add(textView);

                    imageView = new ImageView(context);
                    Glide.with(context).load(image.get(i)).into(imageView);
                    imageView.setLayoutParams(imageLayoutParams);
                    imageViews.add(imageView);

                    linearLayout = new LinearLayout(context);
                    linearLayout.setOrientation(VERTICAL);
                    linearLayout.setLayoutParams(layoutParams);
                    linearLayout.addView(imageView);
                    linearLayout.addView(textView);
                    linearLayouts.add(linearLayout);
                } else {
                    textView = new TextView(context);
                    textView.setText(name.get(i));
                    textView.setGravity(Gravity.CENTER_HORIZONTAL);
                    textView.setTextColor(color);
                    textView.setTextSize(size);
                    textViews.add(textView);

                    imageView = new ImageView(context);
                    Glide.with(context).load(image.get(i)).into(imageView);
                    imageView.setLayoutParams(imageLayoutParams);
                    imageViews.add(imageView);

                    linearLayout = new LinearLayout(context);
                    linearLayout.setOrientation(VERTICAL);
                    linearLayout.setLayoutParams(layoutParams);
                    if (i == name.size() / 2) {
                        this.setClipChildren(true);
                        LayoutParams layoutParamss = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                        layoutParamss.gravity = Gravity.CENTER;
                        LinearLayout linearLayout = new LinearLayout(context);
                        linearLayout.setLayoutParams(layoutParamss);
                        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
                        LayoutParams imageLayoutParamss = new LayoutParams(imageWidth, imageHeight);
                        imageLayoutParamss.gravity = Gravity.CENTER;
                        if (!square) {
                            this.setClipChildren(false);
                            imageLayoutParamss.setMargins(0, 0, 0, imageBottom);
                        }
                        ImageView imageViews = new ImageView(context);
                        Glide.with(context).load(imageContet).into(imageViews);
                        imageViews.setLayoutParams(imageLayoutParamss);
                        linearLayout.addView(imageViews);
                        linearLayouts.add(linearLayout);
                        linearLayout.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setChecked();
                                onClick.onClick(finalI);
                            }
                        });
                        this.addView(linearLayout);
                    }
                    linearLayout.addView(imageView);
                    linearLayout.addView(textView);
                    linearLayouts.add(linearLayout);
                }

                linearLayout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setChecked();
                        textViews.get(finalI).setTextColor(checkColor);
                        Glide.with(context).load(images.get(finalI)).into(imageViews.get(finalI));
                        if ((name.size() % 2) != 0) {
                            onClick.onClick(finalI);
                        } else {
                            if (finalI >= name.size() / 2) {
                                onClick.onClick(finalI + 1);
                            } else {
                                onClick.onClick(finalI);
                            }
                        }
                    }
                });

                this.addView(linearLayout);
            }

        } else {
            for (int i = 0; i < name.size(); i++) {
                textView = new TextView(context);
                textView.setText(name.get(i));
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setTextColor(color);
                textView.setTextSize(size);
                textViews.add(textView);

                imageView = new ImageView(context);
                Glide.with(context).load(image.get(i)).into(imageView);
                imageView.setLayoutParams(imageLayoutParams);
                imageViews.add(imageView);

                linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(VERTICAL);
                linearLayout.setLayoutParams(layoutParams);
                linearLayout.addView(imageView);
                linearLayout.addView(textView);
                linearLayouts.add(linearLayout);

                final int finalI = i;
                linearLayout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setChecked();
                        textViews.get(finalI).setTextColor(checkColor);
                        Glide.with(context).load(images.get(finalI)).into(imageViews.get(finalI));
                        onClick.onClick(finalI);
                    }
                });

                this.addView(linearLayout);
            }
        }
        return this;
    }

    /**
     * 设置item的文字和图片
     *
     * @param name
     * @param image
     * @return
     */
    public BottomTabView setNameOrImage(List<String> name, List<Object> image) {
        this.name = name;
        this.image = image;
        return this;
    }

    /**
     * 设置文字颜色
     *
     * @param color
     * @return
     */
    public BottomTabView setTextColors(int color) {
        this.color = color;
        return this;
    }

    /**
     * 设置背景颜色
     *
     * @param bgColor
     * @return
     */
    public BottomTabView setBgColor(int bgColor) {
        this.bgColor = bgColor;
        return this;
    }

    /**
     * 设置文字的大小
     *
     * @param size
     * @return
     */
    public BottomTabView setTextSize(int size) {
        this.size = size;
        return this;
    }

    /**
     * 设置选中的item的文字颜色
     *
     * @param checkColor
     * @return
     */
    public BottomTabView setTextCheckColor(int checkColor) {
        this.checkColor = checkColor;
        return this;
    }

    /**
     * 设置item选中的图片
     *
     * @param images
     * @return
     */
    public BottomTabView setImageCheck(List<Object> images) {
        this.images = images;
        return this;
    }

    /**
     * 设置中间图片的下边距
     *
     * @param bottom
     * @return
     */
    public BottomTabView setImageBottom(int bottom) {
        this.imageBottom = bottom;
        return this;
    }

    /**
     * 设置item图片的高度和宽度
     *
     * @param width
     * @param height
     * @return
     */
    public BottomTabView setImageWidthHeight(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     * 设置中间大按钮的宽度和高度
     *
     * @param width
     * @param height
     * @return
     */
    public BottomTabView setImagesWidthHeight(int width, int height) {
        this.imageWidth = width;
        this.imageHeight = height;
        return this;
    }

    /**
     * 设置图片全部未选中
     */
    public void setChecked() {
        for (int i = 0; i < textViews.size(); i++) {
            textViews.get(i).setTextColor(color);
            Glide.with(context).load(image.get(i)).into(imageViews.get(i));
        }
    }

    /**
     * 设置是否显示中间大按钮
     *
     * @param open
     * @return
     */
    public BottomTabView setOpen(boolean open) {
        this.open = open;
        return this;
    }

    /**
     * 设置是否让大按钮居中
     *
     * @param square
     * @return
     */
    public BottomTabView setSquare(boolean square) {
        this.square = square;
        return this;
    }

    /**
     * 设置中间大按钮的图片
     *
     * @param image
     * @return
     */
    public BottomTabView setMiddleImage(Object image) {
        this.imageContet = image;
        return this;
    }

    /**
     * 设置选中的item
     *
     * @param position
     */
    public void setPosition(int position) {
        setChecked();
        textViews.get(position).setTextColor(checkColor);
        Glide.with(context).load(images.get(position)).into(imageViews.get(position));
    }

    public interface OnClick {
        void onClick(int position);
    }
}
