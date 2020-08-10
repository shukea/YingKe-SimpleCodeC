package com.yingke.encode.camera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yingke.core.base.BaseActivity;
import com.yingke.core.base.BasePermissionActivity;
import com.yingke.core.util.FileUtil;
import com.yingke.encode.camera.camera.CameraV1;

import java.io.File;

public class EncodeMp4JpegActivity extends BasePermissionActivity {

    private ViewGroup mRootLayer;

    private Button mBtnEncodeMP4;
    private TextView mTvMp4Path;

    private Button mBtnEncodeJpeg;
    private TextView mTvJpegPath;

    private CameraV1 mCameraV1;
    private SurfaceView mSurfaceView;

    private boolean mIsEncodingMp4 = false;
    private String mEncodedMp4;
    private String mEncodedJpeg;


    public static void start(Context context){
        Intent intent = new Intent(context, EncodeMp4JpegActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encode_mp4_jpeg);

        requestCameraPermission(new IPermissionResult() {
            @Override
            public void granted() {
                setupView();
            }

            @Override
            public void unGranted() {

            }
        });
    }

    private void setupView() {
        mRootLayer = (ViewGroup) findViewById(R.id.camera_root_layer);

        mBtnEncodeMP4 = (Button) findViewById(R.id.btn_encode_mp4_start);
        mTvMp4Path = (TextView) findViewById(R.id.tv_mp4_path);
        mBtnEncodeJpeg = (Button) findViewById(R.id.btn_encode_jpeg);
        mTvJpegPath = (TextView) findViewById(R.id.tv_jpeg_path);

        mSurfaceView = new SurfaceView(this);
        mRootLayer.addView(mSurfaceView);
        mCameraV1 = new CameraV1();
        mCameraV1.setPreviewView(mSurfaceView);
    }

    /**
     * 编码的 mp4
     * @param view
     */
    public void onEncodeMp4(View view) {
        if (!mIsEncodingMp4) {
            mIsEncodingMp4 = true;
            mBtnEncodeMP4.setEnabled(true);
            mBtnEncodeMP4.setText("停止编码MP4");
            mEncodedMp4 = "";
            mTvMp4Path.setText("");

            mEncodedMp4 = FileUtil.getsExternalFiles() + "/" + "camera_" +System.currentTimeMillis() + ".mp4";
            mCameraV1.encodeStart(mEncodedMp4);
        } else {
            mIsEncodingMp4 = false;
            mBtnEncodeMP4.setEnabled(true);
            mBtnEncodeMP4.setText("Camera编码MP4");

            mCameraV1.encodeStop();
            mTvMp4Path.setText("Mp4路径：" + mEncodedMp4);
        }
    }

    /**
     * 编码 Jpeg
     * @param view
     */
    public void onEncodeJPEG(View view) {
        if (mCameraV1 != null) {
            mEncodedJpeg = FileUtil.getsExternalFiles() + "/" + "camera_" +System.currentTimeMillis() + ".jpeg";
            mCameraV1.encodeJPEG(mEncodedJpeg);
            mTvJpegPath.setText("Jpeg路径：" + mEncodedJpeg);
        }
    }

}
