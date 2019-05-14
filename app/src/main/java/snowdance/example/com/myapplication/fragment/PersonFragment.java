package snowdance.example.com.myapplication.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;
import snowdance.example.com.myapplication.R;
import snowdance.example.com.myapplication.activities.CourierAct;
import snowdance.example.com.myapplication.activities.ForgetPasswordAct;
import snowdance.example.com.myapplication.activities.LoginAct;
import snowdance.example.com.myapplication.entity.MyUser;
import snowdance.example.com.myapplication.utils.MLog;
import snowdance.example.com.myapplication.utils.SharedUtils;
import snowdance.example.com.myapplication.utils.StaticClass;
//import snowdance.example.com.myapplication.utils.UriToPathUtil;
//import snowdance.example.com.myapplication.utils.UtilFile;
import snowdance.example.com.myapplication.utils.UriToPathUtil;
import snowdance.example.com.myapplication.utils.UtilFile;
import snowdance.example.com.myapplication.utils.UtilTools;
import snowdance.example.com.myapplication.view.CustomDialog;

/**
 * Project Name : EnjoyLife
 * Package Name : snowdance.example.com.myapplication.fragment
 * FILE    Name : PersonFragment
 * Creator Name : Snow_Dance
 * Creator Time : 2018/9/23/023 14:15
 * Description  : NULL
 */

public class PersonFragment extends Fragment implements View.OnClickListener {
    //  编辑属性
    private TextView et_info;
    private EditText et_username;
    private EditText et_gender;
    private EditText et_age;
    private EditText et_desc;

    //  文字、按钮
    private boolean canModify = true;
    private Button bt_confirm_modify;
    private Button bt_exit_login;
    private TextView tv_forget;
    private TextView tv_courier;

    //  圆形头像
    private CircleImageView circleImageView;
    //  自定义Dialog，拍照、选择图片、取消
    private CustomDialog dialog;
    private Button bt_take_photo;
    private Button bt_select_photo;
    private Button bt_cancel;
    //  用户
    private MyUser userInfo;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater
            , @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        //  编辑按钮
        et_info = view.findViewById(R.id.et_info);
        et_info.setOnClickListener(this);

        //  圆形头像
        circleImageView = view.findViewById(R.id.profile_image);
        circleImageView.setOnClickListener(this);
        dialog = new CustomDialog(getActivity(), 1050, 650,
                R.layout.dialog_photo, R.style.pop_anim_style,
                Gravity.BOTTOM);
        //  设置屏幕外点击无效
        dialog.setCancelable(false);
        //  三个按钮：拍照、选择图片、取消，同时设置点击事件
        (bt_take_photo = dialog.findViewById(R.id.bt_take_photo)).setOnClickListener(this);
        (bt_select_photo = dialog.findViewById(R.id.bt_select_photo)).setOnClickListener(this);
        (bt_cancel = dialog.findViewById(R.id.bt_cancel)).setOnClickListener(this);

        //  编辑区域，默认不可点击，需要先点击"编辑"键
        et_username = view.findViewById(R.id.et_username);
        et_gender   = view.findViewById(R.id.et_gender);
        et_age = view.findViewById(R.id.et_age);
        et_desc = view.findViewById(R.id.et_desc);
        setEnabled(false);

        //  获取当前用户属性
        userInfo = BmobUser.getCurrentUser(MyUser.class);
        et_username.setText(userInfo.getNickname());
        et_gender.setText(userInfo.getSex() ?
                this.getString(R.string.boy):this.getString(R.string.girl));
        et_age.setText(userInfo.getAge() + "");
        et_desc.setText(userInfo.getDescription());

        //  忘记密码
        tv_forget = view.findViewById(R.id.tv_forget);
        tv_forget.setOnClickListener(this);
//        UtilTools.setFont(getContext(), tv_forget, "fonts/simkai.ttf");

        //  确认修改按钮
        bt_confirm_modify = view.findViewById(R.id.bt_confirm_modify);
        bt_confirm_modify.setOnClickListener(this);
        bt_confirm_modify.setVisibility(View.GONE);

        //  快递查询、归属地查询
        tv_courier = view.findViewById(R.id.tv_courier);
        tv_courier.setOnClickListener(this);

        //  退出登录按钮
        bt_exit_login = view.findViewById(R.id.bt_exit_login);
        bt_exit_login.setOnClickListener(this);

        //  设置头像
        //  原来从ShareUtils中获取
        //String tel = userInfo.getTelephone();
        //UtilTools.setImageFromShare(getActivity(), circleImageView, tel);
        downAndSetImage();
    }

    private void downAndSetImage(){
        BmobQuery<MyUser> query = new BmobQuery<>();
        query.getObject(userInfo.getObjectId(), new QueryListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if( e == null )
                    download(myUser.getImg());
            }

        });
    }

    private void download(final BmobFile img) {
        img.download(new DownloadFileListener() {
            @Override
            public void done(String path, BmobException e) {
                if( e == null ) {
                    MLog.d(path);
                    try {
                        FileInputStream inputStream = new FileInputStream(path);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        circleImageView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            @Override
            public void onProgress(Integer integer, long l) {

            }
        });
    }


    //  控制是否能够修改资料
    private void setEnabled(boolean tag){
        et_username.setEnabled(tag);
        et_gender.setEnabled(tag);
        et_age.setEnabled(tag);
        et_desc.setEnabled(tag);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //  头像区域
            case R.id.profile_image:
                //  编辑头像
                dialog.show();
                break;
            case R.id.bt_cancel:
                //  取消
                dialog.dismiss();
                break;
            case R.id.bt_take_photo:
                //  拍照选择
                toCamera();
                break;
            case R.id.bt_select_photo:
                //  相册选择
                if( ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){
                    MLog.d("相册if路线");
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1);
                }else{
                    toPicture();
                }
                break;
            case R.id.tv_courier:
                //  快递查询
                startActivity(new Intent(getActivity(), CourierAct.class));
                break;
            /**************************************/
            /**************************************/
            case R.id.et_info:
                //  编辑资料按钮
                if( canModify ){
                    setEnabled(true);
                    bt_confirm_modify.setVisibility(View.VISIBLE);
                }else{
                    setEnabled(false);
                    bt_confirm_modify.setVisibility(View.GONE);
                }
                canModify = !canModify;
                break;
            case R.id.bt_confirm_modify:
                //  确认修改资料
                String username = et_username.getText().toString().trim();
                if( UtilTools.isEmpty(username) ){
                    username = this.getString(R.string.nameless);
                }

                String age = et_age.getText().toString().trim();
                if( UtilTools.isEmpty(age) ) {
                    //  用户不设置年龄，则默认为18
                    //  否则字符串为空Integer.parseInt()方法会出错
                    age = this.getString(R.string.age_18);
                }
                if( !UtilTools.isNormalAge(Integer.parseInt(age)) ){
                    showSth(this.getString(R.string.abnormal_age));
                    break;
                }

                String gender = et_gender.getText().toString().trim();
                if( !UtilTools.isEquals(gender, this.getString(R.string.boy) ) &&
                    !UtilTools.isEquals(gender, this.getString(R.string.girl)) ){
                    showSth(this.getString(R.string.error_gender));
                    et_gender.setText(R.string.ladyboy);
                    break;
                }

                String desc = et_desc.getText().toString().trim();
                if( UtilTools.isEmpty(desc) ){
                    //  默认个人简介
                    desc = this.getString(R.string.default_Desc);
                }
                //  设置信息
                MyUser newUser = new MyUser();
                newUser.setNickname(username);
                newUser.setAge(Integer.parseInt(age));
                newUser.setSex(UtilTools.isEquals(gender,
                        this.getString(R.string.boy)) ? true:false);
                newUser.setDescription(desc);

                //  更新信息
                BmobUser thisUser = BmobUser.getCurrentUser();
                newUser.update(thisUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if( e == null ){
                            showSth(getActivity().getString(R.string.success_modify));
                        }else{
                            showSth(getActivity().getString(R.string.error_modify) + e.getMessage());
                        }
                        //  隐藏编辑区域、确认修改按钮
                        setEnabled(false);
                        bt_confirm_modify.setVisibility(View.GONE);
                    }
                });

                break;
            case R.id.bt_exit_login:
                //  退出登录
                //  清除缓冲对象
                MyUser.logOut();
                //  现在currentUser为null
                BmobUser currentUser = MyUser.getCurrentUser();
                startActivity(new Intent(getActivity(), LoginAct.class));
                getActivity().finish();
                break;
            case R.id.tv_forget:
                startActivity(new Intent(getActivity(), ForgetPasswordAct.class));
                break;
        }
    }

    private Uri image_Uri;
    private Uri cropImageUri;
    private Bitmap bitmap;

    //跳转相册
    private void toPicture() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType(StaticClass.SET_TYPE);
        MLog.d("准备从相册返回！");
        startActivityForResult(intent, StaticClass.IMAGE_REQUEST_CODE);
    }

    //跳转相机
    private void toCamera() {
        MLog.d("从相机中拍照");
        String status = Environment.getExternalStorageState();
        //  判断有无SD卡
        if(status.equals(Environment.MEDIA_MOUNTED)){
            //  创建文件
            File outputImage = new File(getActivity().getExternalCacheDir(),
                    "out_image.jpg");
            try {
                //  若文件已存在，删除
                if (outputImage.exists()) {
                    outputImage.delete();
                }
                outputImage.createNewFile();
                //  根据SDK版本不同调用不同方法获得URI
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    image_Uri = FileProvider.getUriForFile(getActivity(),
                            StaticClass.AUTHORITIES, outputImage);
                }else{
                    image_Uri = Uri.fromFile(outputImage);
                }
                //  进入相机活动
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, image_Uri);
                MLog.d("准备从相机返回！");
                //  回调方法
                startActivityForResult(intent, StaticClass.CAMERA_REQUEST_CODE);
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            MLog.d("无SD卡！");
        }
        dialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MLog.d("准备剪切图片！");
        if (resultCode != 0) {
            switch (requestCode) {
                //相册数据
                case StaticClass.IMAGE_REQUEST_CODE:
                    MLog.d("准备解析相册Uri！");
//                    handlerImageBeforeKitKat(data);
                    MLog.d("准备从相册剪裁头像！");
                    cutImage(data.getData());
                    break;
                //相机数据
                case StaticClass.CAMERA_REQUEST_CODE:
//                        tmpFile = new File(Environment.getExternalStorageDirectory(),
//                                StaticClass.PHOTO_IMAGE_FILE_NAME);
                        MLog.d("准备从相机剪裁头像！");
                        cutImage(image_Uri);
                    break;
                case StaticClass.RESULT_REQUEST_CODE:
                    try {
                        MLog.d("准备获取bitmap!");
                        bitmap = BitmapFactory.decodeStream(
                                getActivity().getContentResolver()
                                        .openInputStream(cropImageUri));
                        MLog.d("获取成功!准备设置头像");
                        setImageToView(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
            }
            dialog.dismiss();
        }
    }

//    private void handlerImageBeforeKitKat(Intent data) {
//        String image_Path = null;
//        Uri uri = data.getData();
//        if( DocumentsContract.isDocumentUri(getActivity(), uri) ){
//            //如果是document类型的Uri,则通过document id处理
//            String docId = DocumentsContract.getDocumentId(uri);
//            if("com.android.providers.media.documents".equals(uri.getAuthority())){
//                String id = docId.split(":")[1];//解析出数字格式的id
//                String selection = MediaStore.Images.Media._ID + "=" + id;
//                image_Path = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
//            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
//                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
//                        Long.valueOf(docId));
//                image_Path = getImagePath(contentUri, null);
//            }
//        }else if("content".equalsIgnoreCase(uri.getScheme())){
//            //如果是content类型的URI，则使用普通方式处理
//            image_Path = getImagePath(uri, null);
//        }else if("file".equalsIgnoreCase(uri.getScheme())){
//            //如果是file类型的Uri,直接获取图片路径即可
//            image_Path = uri.getPath();
//        }
//    }

//    private String getImagePath(Uri uri, String selection) {
//        String path = null;
//        //通过Uri和selection来获取真实的图片路径
//        Cursor cursor = getActivity().getContentResolver().query(uri, null,
//                selection, null, null);
//        if (cursor != null) {
//            if (cursor.moveToFirst()) {
//                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//            }
//            cursor.close();
//        }
//        return path;
//    }

    private void cutImage(Uri uri){
        if( uri == null ){
            MLog.d("uri == null !");
            return;
        }
        String imgPath = UriToPathUtil.getImageAbsolutePath(getContext(), uri);
        final BmobFile file = new BmobFile(new File(imgPath));
        file.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if( e == null ){
                    MyUser user = new MyUser(userInfo.getObjectId());
                    user.setImg(file);
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if( e == null )
                                MLog.d("头像文件上传成功！");
                            else
                                MLog.d("头像文件上传失败！");
                        }
                    });
                }
            }
        });

        File CropPhoto = new File(getActivity().getExternalCacheDir(),"crop_image.jpg");
        try{
            if(CropPhoto.exists()){
                CropPhoto.delete();
            }
            CropPhoto.createNewFile();
        }catch(IOException e){
            e.printStackTrace();
        }
        cropImageUri = Uri.fromFile(CropPhoto);
        //  开始裁剪
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, StaticClass.SET_TYPE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        //  长宽比
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //  分辨率
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        //  true返回bitmap，若图片太大可能崩溃，且小米系统必然崩溃
        //  我的手机就是红米5 PLUS，坑死了
        //  false返回uri
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection

        MLog.d("剪裁头像结束！");
        startActivityForResult(intent, StaticClass.RESULT_REQUEST_CODE);
    }

    //设置图片
    private void setImageToView(Bitmap bitmap) {
        MLog.d("进入设置头像函数！");
        MLog.d("bundle != null !");
        circleImageView.setImageBitmap(bitmap);
        MLog.d("设置成功!");
    }

    private void showSth(String text){
        Toast.makeText(getActivity(), text,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        //  存储至SharedPreferences
        //String tel = userInfo.getTelephone();
        //UtilTools.putImageToShare(getActivity(), bitmap, tel);
        super.onDestroy();
    }
}

