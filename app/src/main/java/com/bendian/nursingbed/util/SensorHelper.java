package com.bendian.nursingbed.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;

/**
 * 重力感应帮助类
 * Created by Administrator on 2016/9/5.
 */
public class SensorHelper {

    private volatile static SensorHelper mInstance;

    // 是否是竖屏
    private Direction direction;

    private SensorManager sm;
    private OrientationSensorListener listener;
    private Sensor sensor;

    private OnDirectionChangedListener onDirectionChangedListener;

    public void setOnDirectionChangedListener(OnDirectionChangedListener onDirectionChangedListener) {
        this.onDirectionChangedListener = onDirectionChangedListener;
    }

    public enum Direction{
        Left,Right,Up,Down
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 888:
                    int orientation = msg.arg1;
                    if (orientation > 45 && orientation < 135) {
                        if (direction!=Direction.Right) {
                            onDirectionChanged(direction,Direction.Right);
                            direction = Direction.Right;
                        }

                    } else if (orientation > 135 && orientation < 225) {
                        if (direction!=Direction.Up) {
                            onDirectionChanged(direction,Direction.Up);
                            direction = Direction.Up;
                        }

                    } else if (orientation > 225 && orientation < 315) {
                        if (direction!=Direction.Left) {
                            onDirectionChanged(direction,Direction.Left);
                            direction = Direction.Left;
                        }
                    } else if ((orientation > 315 && orientation < 360) || (orientation > 0 && orientation < 45)) {
                        if (direction!=Direction.Down) {
                            onDirectionChanged(direction,Direction.Down);
                            direction = Direction.Down;
                        }
                    }
                    break;
                default:
                    break;
            }

        }

        ;
    };

    private void onDirectionChanged(Direction from, Direction to) {
        if (onDirectionChangedListener!=null){
            onDirectionChangedListener.onChanged(from,to);
        }
    }

    /**
     * 返回ScreenSwitchUtils单例
     **/
    public static SensorHelper init(Context context) {
        if (mInstance == null) {
            synchronized (SensorHelper.class) {
                if (mInstance == null) {
                    mInstance = new SensorHelper(context);
                }
            }
        }
        return mInstance;
    }

    private SensorHelper(Context context) {
        // 注册重力感应器,监听屏幕旋转
        sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        listener = new OrientationSensorListener(mHandler);
    }

    /**
     * 开始监听
     */
    public void start() {
        sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * 停止监听
     */
    public void stop() {
        sm.unregisterListener(listener);
    }

    /**
     * 手动横竖屏切换方向
     */
    public void toggleScreen(Direction direction) {
        sm.unregisterListener(listener);
        if (this.direction!=direction) {
            this.direction = direction;
        }
    }

    public Direction getDirection() {
        return direction;
    }

    public interface OnDirectionChangedListener {
        void onChanged(Direction from,Direction to);
    }

    /**
     * 重力感应监听者
     */
    public class OrientationSensorListener implements SensorEventListener {
        private static final int _DATA_X = 0;
        private static final int _DATA_Y = 1;
        private static final int _DATA_Z = 2;

        public static final int ORIENTATION_UNKNOWN = -1;

        private Handler rotateHandler;

        public OrientationSensorListener(Handler handler) {
            rotateHandler = handler;
        }

        public void onAccuracyChanged(Sensor arg0, int arg1) {
        }

        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            int orientation = ORIENTATION_UNKNOWN;
            float X = -values[_DATA_X];
            float Y = -values[_DATA_Y];
            float Z = -values[_DATA_Z];
            float magnitude = X * X + Y * Y;
            // Don't trust the angle if the magnitude is small compared to the y
            // value
            if (magnitude * 4 >= Z * Z) {
                // 屏幕旋转时
                float OneEightyOverPi = 57.29577957855f;
                float angle = (float) Math.atan2(-Y, X) * OneEightyOverPi;
                orientation = 90 - (int) Math.round(angle);
                // normalize to 0 - 359 range
                while (orientation >= 360) {
                    orientation -= 360;
                }
                while (orientation < 0) {
                    orientation += 360;
                }
            }
            if (rotateHandler != null) {
                rotateHandler.obtainMessage(888, orientation, 0).sendToTarget();
            }
        }
    }

}
