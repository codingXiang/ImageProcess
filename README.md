## ImageHandler
### 初始化 - new ImageHandler("影像路徑")
````java
ImageHandler image_handler = new ImageHandler("影像路徑");
````
### 讀取影像 - loadImage()
````java
BufferedImage origin_image = image_handler.loadImage();
````
### 儲存影像 - outputImage(影像檔, 影像名稱)
````java
String image_path = "影像路徑";
ImageHandler image_handler = new ImageHandler(image_path);
image_handler.outputImage(影像檔, "測試輸出");

````
## Filter
### Gray : 灰階化
````java
Filter.toGray(影像檔);
````
### toNegative : 負片效果
````java
Filter.toNegative(影像檔);
````

### setGamma : 設定 gamma
````java
Filter.setGamma(影像檔, gamma 值);
````

### saltpepper : 胡椒鹽雜訊
````java
Filter.saltpepper(影像檔);
````

### median_filter : 3 * 3 中值濾波器
````java
Filter.median_filter(影像檔);
````

### mean_filter : 3 * 3 平均濾波器
````java
Filter.mean_filter(影像檔);
````

### sobel_filter : Sobel 取邊緣
````java
Filter.sobel_filter(影像檔)
````
### scale_2_binary : 二值化（取平均做門檻值）
````java
Filter.scale_2_binary(影像檔);
````
