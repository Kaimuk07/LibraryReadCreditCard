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
	        compile 'com.github.Kaimuk07:LibraryReadCreditCard:v2.2'
	}
```

Step 3
--------

```
implements Camera.CallBack
```


Step 4
--------

```
camera = new Camera(Context, Listener).openCamera();
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
