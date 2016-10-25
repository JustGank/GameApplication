package game.xjl.shake;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * 项目名称：GameApplication
 * 类描述：
 * 创建人：xjl
 * 创建时间：2016/10/24 16:38
 * 修改人：Administrator
 * 修改时间：2016/10/24 16:38
 * 修改备注：
 */

public class ShakeListener implements SensorEventListener {
    //速度阀值，当摇晃的速度达到这个值后产生作用
    private static final int SPEED_SHRESHOLD = 2000;
    //两次检测的时间间隔
    private static final int UPDATA_INTERVAL_TIME = 70;
    //传感器管理器
    private SensorManager sensorManager;
    //传感器
    private Sensor sensor;
    //重力传感器
    private OnShakeListener onShakeListener;
    private Context mContext;

    //手机上一个位置时重力感应坐标
    private float lastX;
    private float lastY;
    private float lastZ;

    //上次检测时间
    private long lastUpdateTime;

    public ShakeListener(Context context) {
        this.mContext = context;
        start();
    }

    public void start() {
        //获得系统传感器服务
        sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager != null) {
            //获得重力传感器
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        //注册
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
        }

    }

    //停止检测
    public void stop() {
        sensorManager.unregisterListener(this);
    }

    //设置重力感应监听器
    public void setOnShakeListener(OnShakeListener onShakeListener) {
        this.onShakeListener = onShakeListener;
    }

    //重力感应器获得变化数据
    @Override
    public void onSensorChanged(SensorEvent event) {
        //现在检测时间
        long currentUpdateTime = System.currentTimeMillis();
        //两次检测的时间间隔
        long timeInterval = currentUpdateTime - lastUpdateTime;
        if (timeInterval < UPDATA_INTERVAL_TIME) {
            return;
        }
        //现在的时间变成last时间
        lastUpdateTime = currentUpdateTime;
        //获得XYZ坐标
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        //获得xyz的变化值
        float deltaX = x - lastX;
        float deltaY = y - lastY;
        float deltaZ = z - lastZ;

        //将现在的坐标变成last坐标
        lastX = x;
        lastY = y;
        lastZ = z;

        double speed=Math.abs(deltaX*deltaX+deltaY*deltaY+deltaZ*deltaZ)/timeInterval*10000;
        Log.e("speed",speed+"");
        if(speed>=SPEED_SHRESHOLD)
        {
            onShakeListener.onShake();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public interface OnShakeListener {
        public void onShake();
    }
}
