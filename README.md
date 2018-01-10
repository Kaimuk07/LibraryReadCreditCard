How to LibrarReadCreditCard
========
Step 1
--------

Add the JitPack repository to your build file:
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Step 2
--------

Add the dependency:
```
dependencies {
	compile 'com.github.Kaimuk07:LibraryReadCreditCard:v4'
	}
```

Step 3
--------
Premission in AndroidManifest.xml
```
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.CAMERA" />
```


Step 4
--------

```
camera = new Camera(MainActivity.this, new Camera.CallBack() {
    @Override
    public void checkResultCameraSuccess(Bitmap bitmap) {
                  
    }
	
    @Override
    public void checkResultCameraFailed(int requestCode, int resultCode, Intent data) {
	
    }
	
    @Override
    public void loading() {
	
    }
	
    @Override
    public void failed(String message) {
	
    }
	
    @Override
    public void success(Card card) {
	
    }
}).openCamera();
```

Step 5
--------
```
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        camera.openCamera(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        camera.onActivityResultCamera(requestCode, resultCode, data);
    }
```
