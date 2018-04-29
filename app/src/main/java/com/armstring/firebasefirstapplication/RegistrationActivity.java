package com.armstring.firebasefirstapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;

public class RegistrationActivity extends AppCompatActivity {

    EditText etUserName;
    EditText etEmail;
    EditText etPassword;
    Button btnRegister;

    FirebaseAuth mAuth;
    DatabaseReference database;
    StorageReference storageReference;

    Bitmap bitmapCroppedImage;
    ByteArrayOutputStream byteArrayOutputStream;
    byte[] byteArray;
    String userImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etUserName = (EditText)findViewById(R.id.etNameRegistration);
        etEmail = (EditText)findViewById(R.id.etEmailRegistration);
        etPassword = (EditText)findViewById(R.id.etPasswordRegistration);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

    }

    public void btnRegister(View view) {

        final String name = etUserName.getText().toString();
        final String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if(!email.equals("")  && !password.equals("") && !name.equals("")) {
            //register our user
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        insertUserDataIntoFirebase();
                        User user = new User(name, email, userImageUrl);
                        database.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(user);
                        Toast.makeText(RegistrationActivity.this,
                                "registered successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                        startActivity(intent);
                    }else {
                        //when some error happens
                        Toast.makeText(RegistrationActivity.this,
                                "unexpected error happened", Toast.LENGTH_SHORT).show();
                    }
                }
            });



        }else {
            Toast.makeText(RegistrationActivity.this,
                    "please enter email and password", Toast.LENGTH_SHORT).show();

        }
    }

    public void goToGallery() {
        Intent galleryImageIntent = new Intent();
        galleryImageIntent.setType("image/*");
        galleryImageIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryImageIntent, "SELECT IMAGE"), 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1000 && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setAspectRatio(16, 9)
                    .start(this);
        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK) {
                Uri croppedImage = result.getUri();
                File imageFile = new File(croppedImage.getPath());
                try {
                    bitmapCroppedImage = new Compressor(this)
                            .setQuality(60)
                            .setMaxHeight(250)
                            .setMaxWidth(250)
                            .compressToBitmap(imageFile);
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmapCroppedImage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byteArray = byteArrayOutputStream.toByteArray();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void insertUserDataIntoFirebase() {

        storageReference = FirebaseStorage.getInstance().getReference()
                .child("user_pic").child(mAuth.getCurrentUser().getUid() + ".jpg");

        UploadTask uploadTask = storageReference.putBytes(byteArray);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()) {
                    userImageUrl = task.getResult().getDownloadUrl().toString();
                    Toast.makeText(RegistrationActivity.this,
                            "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void btnAddImage(View view) {
        goToGallery();
    }
}
